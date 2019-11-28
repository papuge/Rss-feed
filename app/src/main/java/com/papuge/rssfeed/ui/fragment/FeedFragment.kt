package com.papuge.rssfeed.ui.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.papuge.rssfeed.R
import com.papuge.rssfeed.adapter.FeedAdapter
import com.papuge.rssfeed.ui.viewModel.FeedViewModel


class FeedFragment : Fragment() {

    private val viewModel: FeedViewModel by viewModels()
    private lateinit var articlesList: RecyclerView
    private lateinit var adapter: FeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FeedAdapter { position ->
            showDetailsFragment(position)
        }
        articlesList = view.findViewById(R.id.articles_list)
        articlesList.adapter = adapter
        viewModel.articlesList.observe(viewLifecycleOwner) { articles ->
            adapter.setArticles(articles)
        }
        articlesList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showDetailsFragment(position: Int) {
        val article = adapter.getArticle(position)
        val direction = FeedFragmentDirections
            .actionFeedFragmentToArticleFragment(article)
        findNavController().navigate(direction)
    }
}
