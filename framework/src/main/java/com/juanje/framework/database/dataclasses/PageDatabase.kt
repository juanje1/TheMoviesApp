package com.juanje.framework.database.dataclasses

import androidx.room.Entity
import com.juanje.domain.dataclasses.Page

@Entity(
    primaryKeys = ["userName", "category"]
)
data class PageDatabase (
    val userName: String,
    val category: String,
    val nextPage: Int?
)

fun PageDatabase.toPage() = Page(
    userName = userName,
    category = category,
    nextPage = nextPage
)

fun Page.toPageDatabase() = PageDatabase(
    userName = userName,
    category = category,
    nextPage = nextPage
)