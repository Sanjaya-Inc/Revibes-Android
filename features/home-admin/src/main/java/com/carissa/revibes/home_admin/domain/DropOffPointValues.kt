package com.carissa.revibes.home_admin.domain

internal data class DropOffPointValues(
    val organic: Int,
    val nonOrganic: Int,
    val b3: Int
)

internal fun parseDropOffPointValues(
    organic: String,
    nonOrganic: String,
    b3: String
): DropOffPointValues? {
    val organicPoints = organic.toIntOrNull() ?: return null
    val nonOrganicPoints = nonOrganic.toIntOrNull() ?: return null
    val b3Points = b3.toIntOrNull() ?: return null
    return DropOffPointValues(organicPoints, nonOrganicPoints, b3Points)
}
