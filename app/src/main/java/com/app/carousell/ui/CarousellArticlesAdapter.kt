package com.app.carousell.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.carousell.data.model.Article

class CarousellArticlesAdapter : RecyclerView.Adapter<CarousellArticlesViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.timeCreated == newItem.timeCreated
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarousellArticlesViewHolder {
        return CarousellArticlesViewHolder(CarousellArticlesViewHolder.createViewHolder(parent))
    }

    override fun onBindViewHolder(holder: CarousellArticlesViewHolder, position: Int) {
        holder.bind(
            differ.currentList[position],
            position
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Article>) {
        differ.submitList(list)
    }

    fun getIteList(): List<Article> {
        return differ.currentList
    }
}