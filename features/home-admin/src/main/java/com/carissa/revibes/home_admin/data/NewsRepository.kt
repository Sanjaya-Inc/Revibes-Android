package com.carissa.revibes.home_admin.data

import com.carissa.revibes.core.data.utils.BaseRepository
import com.carissa.revibes.home_admin.data.model.CreateNewsRequest
import com.carissa.revibes.home_admin.data.model.NewsData
import com.carissa.revibes.home_admin.data.remote.NewsRemoteApi
import org.koin.core.annotation.Single

@Single
class NewsRepository(
    private val remoteApi: NewsRemoteApi
) : BaseRepository() {

    suspend fun getDailyNews(): NewsData? {
        return execute { remoteApi.getDailyNews().data }
    }

    suspend fun createNews(title: String, content: String) {
        execute { remoteApi.createNews(CreateNewsRequest(title, content)) }
    }
}
