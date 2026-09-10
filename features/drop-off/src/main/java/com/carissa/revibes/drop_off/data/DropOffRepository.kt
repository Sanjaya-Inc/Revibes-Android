package com.carissa.revibes.drop_off.data

import android.content.Context
import android.net.Uri
import com.carissa.revibes.core.data.model.ErrorResponse
import com.carissa.revibes.core.data.utils.ApiException
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
import java.io.File
import java.util.concurrent.TimeUnit

data class EstimatePointItemData(
    val name: String,
    val type: String,
    val weight: Int,
    val unit: String
)

data class SubmitOrderItemData(
    val id: String,
    val name: String,
    val type: String,
    val weight: Int,
    val unit: String
)

@Single
class DropOffRepository(
    private val remoteApi: DropOffRemoteApi,
) : BaseRepository() {
    private val uploadClient: OkHttpClient = OkHttpClient.Builder()
        .retryOnConnectionFailure(false)
        .connectTimeout(UPLOAD_CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(UPLOAD_IO_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(UPLOAD_IO_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .callTimeout(UPLOAD_CALL_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

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
            val imageBytes = readMediaBytes(context, imageUri)
            val requestBody = imageBytes.toRequestBody(contentType = contentType.toMediaType())
            val request = Request.Builder()
                .url(uploadUrl)
                .put(requestBody)
                .addHeader("Content-Type", contentType)
                .build()

            uploadClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw ApiException(
                        statusCode = response.code,
                        errorResponse = ErrorResponse(
                            status = "failed",
                            code = response.code,
                            message = "Upload failed (${response.code})"
                        )
                    )
                }
                true
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
                        weight = item.weight,
                        unit = item.unit
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
    ) {
        execute {
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
                        weight = item.weight,
                        unit = item.unit
                    )
                }
            )
            val response = remoteApi.submitOrder(orderId, request)
            if (response.code != HTTP_SUCCESS_CODE) {
                throw ApiException(
                    statusCode = response.code,
                    errorResponse = ErrorResponse(
                        status = response.status,
                        code = response.code,
                        message = response.message
                    )
                )
            }
        }
    }

    private fun readMediaBytes(context: Context, imageUri: Uri): ByteArray {
        if (imageUri.scheme == "file") {
            return File(requireNotNull(imageUri.path)).readBytes()
        }
        return context.contentResolver.openInputStream(imageUri)?.use { it.readBytes() }
            ?: error("Unable to read image")
    }

    private companion object {
        const val HTTP_SUCCESS_CODE = 200
        const val UPLOAD_CONNECT_TIMEOUT_SECONDS = 15L
        const val UPLOAD_IO_TIMEOUT_SECONDS = 30L
        const val UPLOAD_CALL_TIMEOUT_SECONDS = 45L
    }
}
