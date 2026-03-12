package com.juanje.framework.database.datasources

import com.juanje.data.datasources.PageLocalDataSource
import com.juanje.domain.IoDispatcher
import com.juanje.domain.dataclasses.Page
import com.juanje.framework.database.daos.PageDao
import com.juanje.framework.database.dataclasses.toPage
import com.juanje.framework.database.dataclasses.toPageDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PageDatabaseDataSource @Inject constructor(
    private val pageDao: PageDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): PageLocalDataSource {

    override suspend fun getPage(userName: String, category: String): Page? =
        withContext(ioDispatcher) {
            pageDao.getPage(userName, category)?.toPage()
        }

    override suspend fun deletePage(userName: String, category: String) =
        withContext(ioDispatcher) {
            pageDao.deletePage(userName, category)
        }

    override suspend fun insertPage(page: Page) =
        withContext(ioDispatcher) {
            pageDao.insertPage(page.toPageDatabase())
        }
}