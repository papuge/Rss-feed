package com.papuge.rssfeed.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.papuge.rssfeed.data.Article

class FeedAdapter(
    private val clickListener: (article: Article) -> Unit
): RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    private val articles: MutableList<Article> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("not implemented")
    }

    override fun getItemCount(): Int = articles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class ViewHolder(
        view: View,
        listener: (article: Article) -> Unit): RecyclerView.ViewHolder(view) {

    }
}