package com.carissa.revibes.exchange_points.data.model

import com.carissa.revibes.core.data.user.model.Pagination
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserVoucherResponse(
    @SerialName("code")
    val code: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: UserVoucherData,
    @SerialName("status")
    val status: String
)

@Serializable
data class UserVoucherData(
    @SerialName("items")
    val items: List<UserVoucherItem>,
    @SerialName("pagination")
    val pagination: Pagination
)

@Serializable
data class UserVoucherItem(
    @SerialName("id")
    val id: String,
    @SerialName("voucherId")
    val voucherId: String,
    @SerialName("status")
    val status: String,
    @SerialName("claimedAt")
    val claimedAt: String?,
    @SerialName("expiredAt")
    val expiredAt: String?,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("metadata")
    val metadata: VoucherMetadata
)
