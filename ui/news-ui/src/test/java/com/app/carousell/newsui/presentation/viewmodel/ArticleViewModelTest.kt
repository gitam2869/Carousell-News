package com.app.carousell.newsui.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.carousell.common.NetworkResult
import com.app.carousell.newsdata.model.Article
import com.app.carousell.newsdata.model.Articles
import com.app.carousell.newsdata.repository.ArticleRepository
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq

private const val TAG = "ArticleViewModelTest"

class ArticleViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var articleRepository: ArticleRepository
    lateinit var articleViewModel: ArticleViewModel
    private val articles = mock<Articles>()
    private val comparator = mock<Comparator<Article>>()
    private val callback = argumentCaptor<(NetworkResult<Articles>) -> Unit>()


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        articleViewModel = ArticleViewModel(articleRepository)
    }

    @Test
    fun getArticles_loading() {
        val expectedResult = "Loading"
        `when`(articleRepository.getArticles(callback.capture())).thenAnswer {
            callback.firstValue.invoke(NetworkResult.Loading(expectedResult))
        }
        articleViewModel.getArticles()
        val result = articleViewModel.articleLiveData.getOrAwaitValue()
        Assert.assertEquals(true, result is NetworkResult.Loading)
    }

    @Test
    fun getArticles_success() {
        val expectedResult = listOf(Article())
        `when`(articleRepository.getArticles(callback.capture())).thenAnswer {
            callback.firstValue.invoke(NetworkResult.Success(expectedResult))
        }
        articleViewModel.getArticles()
        val result = articleViewModel.articleLiveData.getOrAwaitValue()
        Assert.assertEquals(expectedResult, result.data)
    }

    @Test
    fun getArticles_success_emptyList() {
        val expectedResult = listOf<Article>()
        `when`(articleRepository.getArticles(callback.capture())).thenAnswer {
            callback.firstValue.invoke(NetworkResult.Success(expectedResult))
        }
        articleViewModel.getArticles()
        val result = articleViewModel.articleLiveData.getOrAwaitValue()
        Assert.assertEquals(0, expectedResult.size)
    }

    @Test
    fun getArticles_error() {
        val expectedResult = "Error"
        `when`(articleRepository.getArticles(callback.capture())).thenAnswer {
            callback.firstValue.invoke(NetworkResult.Error(expectedResult))
        }
        articleViewModel.getArticles()
        val result = articleViewModel.articleLiveData.getOrAwaitValue()
        Assert.assertEquals(expectedResult, result.message)
    }

    @Test
    fun getArticles_count() {
        val list = listOf(Article(), Article())
        val expectedResult = list.size
        `when`(articleRepository.getArticles(callback.capture())).thenAnswer {
            callback.firstValue.invoke(NetworkResult.Success(list))
        }
        articleViewModel.getArticles()
        val result = articleViewModel.articleLiveData.getOrAwaitValue()
        Assert.assertEquals(expectedResult, result.data?.size)
    }


    /**
     * Sort Articles
     */
    @Test
    fun sortArticles_sortBasedOnTime() {
        val list = emptyList<Article>()
        `when`(articleRepository.sortArticles(eq(articles), eq(comparator), callback.capture())
        ).thenAnswer {
            callback.firstValue.invoke(NetworkResult.Success(list))
        }
        articleViewModel.sortArticles(articles, comparator)
        val result = articleViewModel.articleSortedLiveData.getOrAwaitValue()
        Assert.assertEquals(list, result.data)
    }

    @After
    fun tearDown() {
    }
}