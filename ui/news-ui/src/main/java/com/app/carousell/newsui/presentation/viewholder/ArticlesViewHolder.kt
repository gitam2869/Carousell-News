package com.app.carousell.newsui.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.carousell.newsdata.model.Article
import com.app.carousell.newsui.R
import com.app.carousell.newsui.databinding.ItemArticleBinding
import com.bumptech.glide.Glide

class ArticlesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val itemArticleBinding = ItemArticleBinding.bind(itemView)

    fun bind(
        article: Article,
        position: Int
    ) {
        itemArticleBinding.run {
            Glide.with(ivBanner)
                .load(article.bannerUrl)
                .placeholder(com.app.carousell.resource.R.color.gray)
                .error(R.drawable.error_placeholder)
                .into(ivBanner)

            tvTitle.text = article.title
            tvDescription.text = article.description
            tvTimeCreation.text = article.creationDate
        }
    }

    companion object {
        fun createViewHolder(parent: ViewGroup): View {
            return LayoutInflater.from(parent.context)
                .inflate(R.layout.item_article, parent, false)
        }
    }
}