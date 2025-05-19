package com.carissa.revibes.core.data.remote

import com.carissa.revibes.core.data.model.ErrorModel

class ApiException(val errorModel: ErrorModel) : Exception(errorModel.error)
