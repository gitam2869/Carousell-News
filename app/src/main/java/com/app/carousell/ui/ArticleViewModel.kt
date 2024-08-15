package com.app.carousell.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.carousell.data.model.Articles
import com.app.carousell.data.repository.ArticleRepository
import javax.inject.Inject

class ArticleViewModel(private val articleRepository: ArticleRepository) : ViewModel() {

    private val _articleLiveData = MutableLiveData<ApiResponse<Articles>>()
    val articleLiveData: LiveData<ApiResponse<Articles>>
        get() = _articleLiveData

    fun getArticles() {
        articleRepository.getArticles {
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

