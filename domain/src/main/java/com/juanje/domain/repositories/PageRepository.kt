package com.juanje.domain.repositories

import com.juanje.domain.dataclasses.Page

interface PageRepository {
    suspend fun getPage(userName: String, category: String): Page?
    suspend fun deletePage(userName: String, category: String)
    suspend fun insertPage(page: Page)
}