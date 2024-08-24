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

    private val _articleSortedLiveData = MutableLiveData<NetworkResult<Articles>>()
    val articleSortedLiveData: LiveData<NetworkResult<Articles>>
        get() = _articleSortedLiveData

    fun getArticles() {
        if (_articleLiveData.value == null) {
            //we must all loading state from viewmodel only, because it is part of ui not repository
            //and it helps while doing unit testing
            _articleLiveData.value = NetworkResult.Loading("Fetching Data")
            articleRepository.getArticles {
                _articleLiveData.value = it
            }
        } else {
            _articleLiveData.value = _articleLiveData.value
        }
    }

    fun sortArticles(articles: Articles?, comparator: Comparator<Article>) {
        if (articles == null) {
            _articleSortedLiveData.value = NetworkResult.Error("Can not sort because list is empty")
        } else {
            _articleSortedLiveData.value = NetworkResult.Loading("Filtering Data")
            articleRepository.sortArticles(articles, comparator) {
                _articleSortedLiveData.value = it
            }
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

