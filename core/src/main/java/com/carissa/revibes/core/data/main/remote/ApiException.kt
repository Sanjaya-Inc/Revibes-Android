package com.carissa.revibes.core.data.main.remote

import com.carissa.revibes.core.data.main.model.ErrorModel

class ApiException(val errorModel: ErrorModel) : Exception(errorModel.error)
