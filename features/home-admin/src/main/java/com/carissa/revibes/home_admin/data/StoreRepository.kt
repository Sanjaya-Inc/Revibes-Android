package com.carissa.revibes.home_admin.data

import com.carissa.revibes.core.data.utils.BaseRepository
import com.carissa.revibes.home_admin.data.model.CreateStoreRequest
import com.carissa.revibes.home_admin.data.model.StoreData
import com.carissa.revibes.home_admin.data.model.UpdateStoreRequest
import com.carissa.revibes.home_admin.data.remote.StoreRemoteApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.koin.core.annotation.Single
import java.util.concurrent.atomic.AtomicReference

@Single
class StoreRepository(
    private val remoteApi: StoreRemoteApi
) : BaseRepository() {

    private val cachedStores = AtomicReference<List<StoreData>?>(null)
    private val storesChanged = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val changes: SharedFlow<Unit> = storesChanged.asSharedFlow()

    fun peekStores(): List<StoreData>? = cachedStores.get()

    suspend fun getStores(forceRefresh: Boolean = false): List<StoreData> {
        if (!forceRefresh) {
            cachedStores.get()?.let { return it }
        }
        return execute {
            remoteApi.getStores().data.also { cachedStores.set(it) }
        }
    }

    suspend fun createStore(request: CreateStoreRequest): StoreData? {
        val created = execute { remoteApi.createStore(request).data }
        invalidate()
        return created
    }

    suspend fun updateStore(id: String, request: UpdateStoreRequest) {
        execute { remoteApi.updateStore(id, request) }
        invalidate()
    }

    private fun invalidate() {
        cachedStores.set(null)
        storesChanged.tryEmit(Unit)
    }
}
