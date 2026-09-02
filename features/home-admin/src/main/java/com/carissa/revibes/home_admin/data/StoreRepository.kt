package com.carissa.revibes.home_admin.data

import com.carissa.revibes.core.data.utils.BaseRepository
import com.carissa.revibes.home_admin.data.model.CreateStoreRequest
import com.carissa.revibes.home_admin.data.model.StoreData
import com.carissa.revibes.home_admin.data.model.UpdateStoreRequest
import com.carissa.revibes.home_admin.data.remote.StoreRemoteApi
import org.koin.core.annotation.Single

@Single
class StoreRepository(
    private val remoteApi: StoreRemoteApi
) : BaseRepository() {

    suspend fun getStores(): List<StoreData> {
        return execute { remoteApi.getStores().data }
    }

    suspend fun createStore(request: CreateStoreRequest): StoreData? {
        return execute { remoteApi.createStore(request).data }
    }

    suspend fun updateStore(id: String, request: UpdateStoreRequest) {
        execute { remoteApi.updateStore(id, request) }
    }
}
