package com.carissa.revibes.drop_off.data

import com.carissa.revibes.drop_off.data.mapper.toStoreDataList
import com.carissa.revibes.drop_off.data.model.PresignedUrlRequest
import com.carissa.revibes.drop_off.data.model.SubmitOrderRequest
import com.carissa.revibes.drop_off.data.remote.DropOffRemoteApi
import com.carissa.revibes.drop_off.domain.model.StoreData
import org.koin.core.annotation.Single

interface DropOffRepository {
    suspend fun getStores(longitude: Double, latitude: Double): List<StoreData>
    suspend fun createLogisticOrder(): String
    suspend fun createLogisticOrderItem(orderId: String): String
    suspend fun getPresignedUrl(
        orderId: String,
        itemId: String,
        contentType: String
    ): Pair<String, Long>

    suspend fun submitOrder(
        orderId: String,
        type: String,
        name: String,
        country: String,
        storeId: String,
        items: List<SubmitOrderItemData>
    ): Boolean
}

data class SubmitOrderItemData(
    val id: String,
    val name: String,
    val type: String,
    val weight: Int
)

@Single
internal class DropOffRepositoryImpl(
    private val remoteApi: DropOffRemoteApi
) : DropOffRepository {

    override suspend fun getStores(longitude: Double, latitude: Double): List<StoreData> {
        return remoteApi.getStores(longitude, latitude).data.toStoreDataList()
    }

    override suspend fun createLogisticOrder(): String {
        return remoteApi.createLogisticOrder().data
    }

    override suspend fun createLogisticOrderItem(orderId: String): String {
        return remoteApi.createLogisticOrderItem(orderId).data
    }

    override suspend fun getPresignedUrl(
        orderId: String,
        itemId: String,
        contentType: String
    ): Pair<String, Long> {
        val request = PresignedUrlRequest(contentType = contentType)
        val response = remoteApi.getPresignedUrl(orderId, itemId, request)
        return Pair(response.data.uploadUrl, response.data.expiredAt)
    }

    override suspend fun submitOrder(
        orderId: String,
        type: String,
        name: String,
        country: String,
        storeId: String,
        items: List<SubmitOrderItemData>
    ): Boolean {
        val request = SubmitOrderRequest(
            type = type,
            name = name,
            country = country,
            storeId = storeId,
            items = items.map { item ->
                com.carissa.revibes.drop_off.data.model.SubmitOrderItem(
                    id = item.id,
                    name = item.name,
                    type = item.type,
                    weight = item.weight
                )
            }
        )
        val response = remoteApi.submitOrder(orderId, request)
        return response.code == 200
    }
}
