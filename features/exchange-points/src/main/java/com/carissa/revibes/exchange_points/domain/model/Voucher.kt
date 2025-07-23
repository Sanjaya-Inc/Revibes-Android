package com.carissa.revibes.exchange_points.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Voucher(
    val id: String,
    val name: String,
    val description: String,
    val imageUri: String,
    val point: Int,
    val quota: Int,
    val validUntil: String = "Valid until: 31 Dec 2025",
    val terms: List<String> = DUMMY_TERMS,
)

private val DUMMY_TERMS = listOf(
    "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut " +
        "laoreet dolore magna aliquam erat volutpat.",
    "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut " +
        "aliquip ex ea commodo consequat.",
    "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum " +
        "dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit " +
        "praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.",
    "Lorem ipsum dolor sit amet, cons ectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut " +
        "laoreet dolore magna aliquam erat volutpat.",
    "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut " +
        "aliquip ex ea commodo consequat.",
    "Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut " +
        "laoreet dolore magna aliquam erat volutpat.",
    "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut " +
        "aliquip ex ea commodo consequat.",
    "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum " +
        "dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit " +
        "praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi."
)
