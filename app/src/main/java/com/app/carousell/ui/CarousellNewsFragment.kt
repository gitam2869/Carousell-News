package com.app.carousell.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.carousell.CarousellApplication
import com.app.carousell.R
import com.app.carousell.data.model.Articles
import com.app.carousell.databinding.FragmentCarousellNewsBinding
import com.app.carousell.util.ArticleSort
import com.app.carousell.util.Response
import com.app.carousell.util.SortUtil
import com.app.carousell.util.SpacingItemDecoration
import com.app.carousell.util.extension.ViewExtension.gone
import com.app.carousell.util.extension.ViewExtension.setOnSingleClickListener
import com.app.carousell.util.extension.ViewExtension.visible
import javax.inject.Inject


private const val TAG = "CarousellNewsFragment"

class CarousellNewsFragment : Fragment() {

    private var _binding: FragmentCarousellNewsBinding? = null
    private val binding: FragmentCarousellNewsBinding
        get() = _binding!!

    private lateinit var articleViewModel: ArticleViewModel

    @Inject
    lateinit var articleViewModelFactory: ArticleViewModelFactory
    private lateinit var carousellArticlesAdapter: CarousellArticlesAdapter
    private var isFiltering: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as CarousellApplication).applicationComponent.inject(this)
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
        articleViewModel = ViewModelProvider(this, articleViewModelFactory)[ArticleViewModel::class.java]
        recyclerViewSetup()
        observeArticles()
        getArticles()
        registerClickListeners()
    }

    private fun recyclerViewSetup() {
        binding.rvArticles.run {
            carousellArticlesAdapter = CarousellArticlesAdapter()
            itemAnimator = null
            layoutManager = LinearLayoutManager(this@CarousellNewsFragment.context)
            addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_medium)))
            adapter = carousellArticlesAdapter
        }
    }

    private fun observeArticles() {
        articleViewModel.articleLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Response.Loading -> {
                    showLoading()
                    it.message?.let { it1 -> showMessage(it1) }
                }

                is Response.Success -> {
                    hideLoading()
                    hideMessage()
                    it.data?.let { it1 -> setArticlesData(it1) }
                }

                is Response.Error -> {
                    hideLoading()
                    showMessage(it.message ?: "")
                }
            }
        }
    }

    private fun getArticles() {
        articleViewModel.getArticles(SortUtil.getArticlesSortComparatorByType(ArticleSort.Recent))
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
        carousellArticlesAdapter.submitList(articles)
        if(isFiltering) {
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
                        val itemList = carousellArticlesAdapter.getIteList()
                        carousellArticlesAdapter.submitList(emptyList())
                        articleViewModel.sortArticles(
                            itemList,
                            SortUtil.getArticlesSortComparatorByType(ArticleSort.Recent)
                        )
                        true
                    }

                    R.id.action_popular -> {
                        isFiltering = true
                        val itemList = carousellArticlesAdapter.getIteList()
                        carousellArticlesAdapter.submitList(emptyList())
                        articleViewModel.sortArticles(
                            itemList,
                            SortUtil.getArticlesSortComparatorByType(ArticleSort.Popular)
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