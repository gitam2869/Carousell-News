package com.app.carousell.util

sealed class ArticleSort {
    object Recent: ArticleSort()
    object Popular: ArticleSort()
}