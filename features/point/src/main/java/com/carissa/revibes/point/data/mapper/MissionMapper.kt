package com.carissa.revibes.point.data.mapper

import com.carissa.revibes.point.data.remote.response.MissionItemResponse
import com.carissa.revibes.point.domain.model.Mission
import com.carissa.revibes.point.domain.model.MissionType

fun MissionItemResponse.toDomain(): Mission =
    Mission(
        id = id,
        title = mission.title,
        subtitle = mission.subtitle,
        description = mission.description,
        imageUri = mission.imageUri.orEmpty(),
        reward = mission.reward,
        type = when (type) {
            "logistic-order-complete" -> MissionType.LOGISTIC_ORDER_COMPLETE
            "user-profile-fulfill" -> MissionType.USER_PROFILE_FULLFILL
            else -> MissionType.UNKNOWN
        }
    )
