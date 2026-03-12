package com.juanje.data.repositories

import com.juanje.data.common.safeCall
import com.juanje.data.datasources.PageLocalDataSource
import com.juanje.domain.dataclasses.Page
import com.juanje.domain.repositories.PageRepository
import javax.inject.Inject

class PageRepositoryImpl @Inject constructor(private val pageLocalDataSource: PageLocalDataSource): PageRepository {

    override suspend fun getPage(userName: String, category: String): Page? {
        return safeCall {
            pageLocalDataSource.getPage(userName, category)
        }
    }

    override suspend fun deletePage(userName: String, category: String) {
        return safeCall {
            pageLocalDataSource.deletePage(userName, category)
        }
    }

    override suspend fun insertPage(page: Page) {
        return safeCall {
            pageLocalDataSource.insertPage(page)
        }
    }
}