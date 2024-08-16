package com.app.carousell.util

import com.app.carousell.data.model.Article

object SortUtil {

    fun getArticlesSortComparatorByType(articleSort: ArticleSort): Comparator<Article> =
        when (articleSort) {
            ArticleSort.Recent -> {
                Comparator { o1, o2 ->
                    val obj1TimeCreated = o1?.timeCreated ?: 0L
                    val obj2TimeCreated = o2.timeCreated ?: 0L
                    return@Comparator if (obj1TimeCreated < obj2TimeCreated) -1
                    else if (obj1TimeCreated > obj2TimeCreated) 1
                    else 0
                }
            }

            ArticleSort.Popular -> {
                Comparator { o1, o2 ->
                    val obj1Rank = o1?.rank ?: 0
                    val obj2Rank = o2.rank ?: 0
                    val obj1TimeCreated = o1?.timeCreated ?: 0L
                    val obj2TimeCreated = o2.timeCreated ?: 0L

                    return@Comparator if (obj1Rank < obj2Rank) -1
                    else if (obj1Rank > obj2Rank) 1
                    else if (obj1TimeCreated < obj2TimeCreated) -1
                    else if (obj1TimeCreated > obj2TimeCreated) 1
                    else 0
                }
            }
        }
}