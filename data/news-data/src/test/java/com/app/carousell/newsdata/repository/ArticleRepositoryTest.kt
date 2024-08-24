package com.app.carousell.newsdata.repository

import com.app.carousell.common.NetworkResult
import com.app.carousell.newsdata.model.Article
import com.app.carousell.newsdata.model.Articles
import com.app.carousell.newsdata.remote.NewsService
import io.reactivex.rxjava3.core.Single
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock

class ArticleRepositoryTest {

    @get:Rule
    val rxJavaSchedulersRule = RxJavaSchedulersRule()

    @Mock
    lateinit var newsService: NewsService
    private lateinit var articleRepository: ArticleRepository
    private val comparator = mock<Comparator<Article>>()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        articleRepository = ArticleRepository(newsService)
    }

    @Test
    fun getArticles_Success() {
        val expected = listOf(Article())
        Mockito.`when`(newsService.getArticles()).thenReturn(
            Single.just(expected)
        )

        var result: NetworkResult<Articles>? = null
        articleRepository.getArticles {
            result = it
        }

        Assert.assertEquals(expected, result?.data)
    }

    @Test
    fun getArticles_Error() {
        val expected = Throwable()
        Mockito.`when`(newsService.getArticles()).thenReturn(
            Single.error(expected)
        )

        var result: NetworkResult<Articles>? = null
        articleRepository.getArticles {
            result = it
        }

        Assert.assertEquals(true, result is NetworkResult.Error)
    }


    /***
     * Sort Articles
     */

    @Test
    fun sortArticles_List_Success() {
        val list = listOf(Article())
        var result: NetworkResult<Articles>? = null
        articleRepository.sortArticles(list, comparator) {
            result = it
        }

        Assert.assertEquals(true, result is NetworkResult.Success)
    }

    @Test
    fun sortArticles_emptyList_Success() {
        val list = emptyList<Article>()
        var result: NetworkResult<Articles>? = null
        articleRepository.sortArticles(list, comparator) {
            result = it
        }

        Assert.assertEquals(true, result is NetworkResult.Success)
    }

    @After
    fun tearDown() {
    }
}