package com.app.carousell.newsui.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.carousell.common.NetworkResult
import com.app.carousell.newsdata.model.Article
import com.app.carousell.newsdata.model.Articles
import com.app.carousell.newsdata.repository.ArticleRepository
import javax.inject.Inject

class ArticleViewModel(private val articleRepository: ArticleRepository) : ViewModel() {

    private val _articleLiveData = MutableLiveData<NetworkResult<Articles>>()
    val articleLiveData: LiveData<NetworkResult<Articles>>
        get() = _articleLiveData

    fun getArticles(comparator: Comparator<Article>) {
        if (_articleLiveData.value == null) {
            articleRepository.getArticles {
                if (it is NetworkResult.Success) {
                    it.data?.let { it1 -> sortArticles(it1, comparator) }
                } else {
                    _articleLiveData.value = it
                }
            }
        } else {
            _articleLiveData.value = _articleLiveData.value
        }
    }

    fun sortArticles(articles: Articles, comparator: Comparator<Article>) {
        articleRepository.sortArticles(articles, comparator) {
            _articleLiveData.value = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        articleRepository.dispose()
    }
}

class ArticleViewModelFactory @Inject constructor(private val articleRepository: ArticleRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ArticleViewModel(articleRepository) as T
    }
}

