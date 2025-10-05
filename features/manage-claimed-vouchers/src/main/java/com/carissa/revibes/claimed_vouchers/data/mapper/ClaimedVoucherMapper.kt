package com.carissa.revibes.claimed_vouchers.data.mapper

import com.carissa.revibes.claimed_vouchers.data.model.ClaimedVoucherItemResponse
import com.carissa.revibes.claimed_vouchers.data.model.VoucherConditionsResponse
import com.carissa.revibes.claimed_vouchers.data.model.VoucherItemMetadataResponse
import com.carissa.revibes.claimed_vouchers.data.model.VoucherItemResponse
import com.carissa.revibes.claimed_vouchers.data.model.VoucherMetadataResponse
import com.carissa.revibes.claimed_vouchers.data.model.VoucherValueResponse
import com.carissa.revibes.claimed_vouchers.domain.model.ClaimedVoucherDomain
import com.carissa.revibes.claimed_vouchers.domain.model.ClaimedVoucherItemDomain
import com.carissa.revibes.claimed_vouchers.domain.model.VoucherConditionsDomain
import com.carissa.revibes.claimed_vouchers.domain.model.VoucherItemMetadataDomain
import com.carissa.revibes.claimed_vouchers.domain.model.VoucherMetadataDomain
import com.carissa.revibes.claimed_vouchers.domain.model.VoucherValueDomain

fun ClaimedVoucherItemResponse.toDomain(): ClaimedVoucherDomain {
    return ClaimedVoucherDomain(
        id = id,
        items = items.map { it.toDomain() },
        voucherCode = voucherCode,
        amount = amount,
        discount = discount,
        total = total,
        paymentMethod = paymentMethod,
        currency = currency,
        status = status,
        createdAt = createdAt,
        voucherMetadata = voucherMetadata?.toDomain()
    )
}

fun VoucherItemResponse.toDomain(): ClaimedVoucherItemDomain {
    return ClaimedVoucherItemDomain(
        id = id,
        type = type,
        qty = qty,
        metadata = metadata.toDomain()
    )
}

fun VoucherItemMetadataResponse.toDomain(): VoucherItemMetadataDomain {
    return VoucherItemMetadataDomain(
        id = id,
        code = code,
        name = name,
        description = description,
        value = value.toDomain(),
        conditions = conditions.toDomain(),
        imageUri = imageUri
    )
}

fun VoucherValueResponse.toDomain(): VoucherValueDomain {
    return VoucherValueDomain(
        type = type,
        amount = amount
    )
}

fun VoucherConditionsResponse.toDomain(): VoucherConditionsDomain {
    return VoucherConditionsDomain(
        maxClaim = maxClaim,
        maxDiscountAmount = maxDiscountAmount
    )
}

fun VoucherMetadataResponse.toDomain(): VoucherMetadataDomain {
    return VoucherMetadataDomain(
        id = id,
        code = code,
        name = name,
        description = description,
        value = value.toDomain(),
        conditions = conditions.toDomain(),
        imageUri = imageUri
    )
}
