package com.app.carousell.newsui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.carousell.common.NetworkResult
import com.app.carousell.common.extension.ViewExtension.gone
import com.app.carousell.common.extension.ViewExtension.setOnSingleClickListener
import com.app.carousell.common.extension.ViewExtension.visible
import com.app.carousell.core.coreComponent
import com.app.carousell.newsdata.model.Articles
import com.app.carousell.newsui.databinding.FragmentCarousellNewsBinding
import com.app.carousell.newsui.di.DaggerNewsApplicationComponent
import com.app.carousell.newsui.utils.ArticleSort
import com.app.carousell.newsui.utils.ArticlesSortUtil
import javax.inject.Inject

private const val TAG = "CarousellNewsFragment"

class CarousellNewsFragment : Fragment() {

    private var _binding: FragmentCarousellNewsBinding? = null
    private val binding: FragmentCarousellNewsBinding
        get() = _binding!!

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var articleViewModel: ArticleViewModel
    @Inject
    lateinit var articleViewModelFactory: ArticleViewModelFactory
    private lateinit var articlesAdapter: ArticlesAdapter
    private var isFiltering: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerNewsApplicationComponent
            .factory()
            .create(coreComponent())
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCarousellNewsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        articleViewModel =
            ViewModelProvider(this, articleViewModelFactory)[ArticleViewModel::class.java]
        recyclerViewSetup()
        observeArticles()
        getArticles()
        registerClickListeners()
        sharedPreferences.edit().putInt("dd", 1).apply()
    }

    private fun recyclerViewSetup() {
        binding.rvArticles.run {
            articlesAdapter = ArticlesAdapter()
            itemAnimator = null
            layoutManager = LinearLayoutManager(this@CarousellNewsFragment.context)
            addItemDecoration(
                com.app.carousell.common.SpacingItemDecoration(
                    resources.getDimensionPixelSize(
                        com.app.carousell.resource.R.dimen.spacing_medium
                    )
                )
            )
            adapter = articlesAdapter
        }
    }

    private fun observeArticles() {
        articleViewModel.articleLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> {
                    showLoading()
                    it.message?.let { it1 -> showMessage(it1) }
                }

                is NetworkResult.Success -> {
                    hideLoading()
                    hideMessage()
                    it.data?.let { it1 -> setArticlesData(it1) }
                }

                is NetworkResult.Error -> {
                    hideLoading()
                    showMessage(it.message ?: "")
                }
            }
        }
    }

    private fun getArticles() {
        articleViewModel.getArticles(ArticlesSortUtil.getArticlesSortComparatorByType(ArticleSort.Recent))
    }

    private fun registerClickListeners() {
        binding.run {
            ivMenu.setOnSingleClickListener {
                openPopupMenu(it)
            }
        }
    }

    private fun showLoading() {
        binding.pbLoading.visible()
    }

    private fun hideLoading() {
        binding.pbLoading.gone()
    }

    private fun hideMessage() {
        binding.tvMessage.gone()
    }

    private fun showMessage(message: String) {
        binding.tvMessage.run {
            text = message
            visible()
        }
    }

    private fun setArticlesData(articles: Articles) {
        articlesAdapter.submitList(articles)
        if (isFiltering) {
            isFiltering = false
            binding.rvArticles.scrollToPosition(0)
        }
    }

    private fun openPopupMenu(view: View) {
        val popupMenu = PopupMenu(this@CarousellNewsFragment.context, view)
        popupMenu.menuInflater.inflate(R.menu.menu_article_filter, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.action_recent -> {
                        isFiltering = true
                        val itemList = articlesAdapter.getIteList()
                        articlesAdapter.submitList(emptyList())
                        articleViewModel.sortArticles(
                            itemList,
                            ArticlesSortUtil.getArticlesSortComparatorByType(ArticleSort.Recent)
                        )
                        true
                    }

                    R.id.action_popular -> {
                        isFiltering = true
                        val itemList = articlesAdapter.getIteList()
                        articlesAdapter.submitList(emptyList())
                        articleViewModel.sortArticles(
                            itemList,
                            ArticlesSortUtil.getArticlesSortComparatorByType(ArticleSort.Popular)
                        )
                        true
                    }

                    else -> false
                }
            }
        })
        popupMenu.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

