package com.app.carousell.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.carousell.CarousellApplication
import javax.inject.Inject

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    lateinit var articleViewModel: ArticleViewModel

    @Inject
    lateinit var articleViewModelFactory: ArticleViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as CarousellApplication).applicationComponent.inject(this)
        articleViewModel =
            ViewModelProvider(this, articleViewModelFactory)[ArticleViewModel::class.java]

        articleViewModel.articleLiveData.observe(this, Observer {
            when (it) {
                is ApiResponse.Loading -> {}
                is ApiResponse.Success -> {
                    Log.d(TAG, "onCreate: Result $it")
                }
                is ApiResponse.Error -> {}
            }
        })
        articleViewModel.getArticles()
    }
}