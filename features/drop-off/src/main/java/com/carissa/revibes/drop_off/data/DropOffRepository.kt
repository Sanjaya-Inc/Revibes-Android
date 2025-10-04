package com.carissa.revibes.drop_off.data

import android.content.Context
import android.net.Uri
import com.carissa.revibes.core.data.utils.BaseRepository
import com.carissa.revibes.drop_off.data.mapper.toStoreDataList
import com.carissa.revibes.drop_off.data.model.EstimatePointItem
import com.carissa.revibes.drop_off.data.model.EstimatePointRequest
import com.carissa.revibes.drop_off.data.model.PresignedUrlRequest
import com.carissa.revibes.drop_off.data.model.SubmitOrderRequest
import com.carissa.revibes.drop_off.data.remote.DropOffRemoteApi
import com.carissa.revibes.drop_off.domain.model.StoreData
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.core.annotation.Single
import java.io.IOException

data class EstimatePointItemData(
    val name: String,
    val type: String,
    val weight: Int
)

data class SubmitOrderItemData(
    val id: String,
    val name: String,
    val type: String,
    val weight: Int
)

@Single
class DropOffRepository(
    private val remoteApi: DropOffRemoteApi,
    private val httpClient: OkHttpClient
) : BaseRepository() {

    suspend fun getStores(longitude: Double, latitude: Double): List<StoreData> {
        return execute { remoteApi.getStores(longitude, latitude).data.toStoreDataList() }
    }

    suspend fun createLogisticOrder(): String {
        return execute { remoteApi.createLogisticOrder().data }
    }

    suspend fun createLogisticOrderItem(orderId: String): String {
        return execute { remoteApi.createLogisticOrderItem(orderId).data }
    }

    suspend fun getPresignedUrl(
        orderId: String,
        itemId: String,
        contentType: String
    ): Triple<String, String, Long> {
        return execute {
            val request = PresignedUrlRequest(contentType = contentType)
            val response = remoteApi.getPresignedUrl(orderId, itemId, request)
            Triple(response.data.uploadUrl, response.data.downloadUrl, response.data.expiredAt)
        }
    }

    suspend fun uploadImageToUrl(
        context: Context,
        uploadUrl: String,
        imageUri: Uri,
        contentType: String
    ): Boolean {
        return execute {
            try {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                val imageBytes = requireNotNull(inputStream?.readBytes())
                inputStream.close()

                val requestBody = imageBytes.toRequestBody(contentType = contentType.toMediaType())
                val request = Request.Builder()
                    .url(uploadUrl)
                    .put(requestBody)
                    .addHeader("Content-Type", contentType)
                    .addHeader("X-GOOG-ACL", "public-read")
                    .build()

                val response = httpClient.newCall(request).execute()
                response.isSuccessful
            } catch (e: IOException) {
                false
            }
        }
    }

    suspend fun estimatePoint(items: List<EstimatePointItemData>): Pair<Map<String, Int>, Int> {
        return execute {
            val request = EstimatePointRequest(
                items = items.map { item ->
                    EstimatePointItem(
                        name = item.name,
                        type = item.type,
                        weight = item.weight
                    )
                }
            )
            val response = remoteApi.estimatePoint(request)
            Pair(response.data.items, response.data.total)
        }
    }

    suspend fun submitOrder(
        orderId: String,
        type: String,
        name: String,
        country: String,
        storeId: String,
        items: List<SubmitOrderItemData>
    ): Boolean {
        return execute {
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
            response.code == 200
        }
    }
}
