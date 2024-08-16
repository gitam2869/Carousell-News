package com.app.carousell.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.carousell.data.model.Article
import com.app.carousell.data.model.Articles
import com.app.carousell.data.repository.ArticleRepository
import com.app.carousell.util.Response
import javax.inject.Inject

class ArticleViewModel(private val articleRepository: ArticleRepository) : ViewModel() {

    private val _articleLiveData = MutableLiveData<Response<Articles>>()
    val articleLiveData: LiveData<Response<Articles>>
        get() = _articleLiveData

    fun getArticles(comparator: Comparator<Article>) {
        articleRepository.getArticles {
            if (it is Response.Success) {
                it.data?.let { it1 -> sortArticles(it1, comparator) }
            } else {
                _articleLiveData.value = it
            }
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

