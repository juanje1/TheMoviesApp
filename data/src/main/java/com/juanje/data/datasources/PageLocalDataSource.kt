package com.juanje.data.datasources

import com.juanje.domain.dataclasses.Page

interface PageLocalDataSource {
    suspend fun getPage(userName: String, category: String): Page?
    suspend fun deletePage(userName: String, category: String)
    suspend fun insertPage(page: Page)
}