package com.carissa.revibes.core.data.main.remote

import com.carissa.revibes.core.data.main.model.ErrorModel

class ApiException(
    val errorModel: ErrorModel,
    val statusCode: Int = -1,
) : Exception(errorModel.error)
