package com.app.carousell.newsui.utils

sealed class ArticleSort {
    object Recent: ArticleSort()
    object Popular: ArticleSort()
}