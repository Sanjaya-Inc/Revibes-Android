package com.carissa.revibes.point.domain.model

data class Mission(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val imageUri: String,
    val reward: Int,
    val type: MissionType
)
