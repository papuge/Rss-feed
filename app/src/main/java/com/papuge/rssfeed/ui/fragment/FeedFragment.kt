package com.papuge.rssfeed.ui.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.papuge.rssfeed.R
import com.papuge.rssfeed.adapter.FeedAdapter
import com.papuge.rssfeed.ui.viewModel.FeedViewModel


class FeedFragment : Fragment() {

    private lateinit var viewModel: FeedViewModel
    private lateinit var articlesList: RecyclerView
    private lateinit var adapter: FeedAdapter
    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProvider(this)[FeedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeLayout = view.findViewById(R.id.swipe_layout)
        progressBar = view.findViewById(R.id.progress_bar)

        adapter = FeedAdapter { position ->
            showDetailsFragment(position)
        }
        articlesList = view.findViewById(R.id.articles_list)
        articlesList.adapter = adapter
        viewModel.articlesList.observe(viewLifecycleOwner) { articles ->
            adapter.setArticles(articles)
            progressBar.visibility = View.GONE
            swipeLayout.isRefreshing = false
        }
        articlesList.layoutManager = LinearLayoutManager(requireContext())

        swipeLayout.setOnRefreshListener {
            adapter.clearArticles()
            adapter.notifyDataSetChanged()
            swipeLayout.isRefreshing = true
            viewModel.getArticles()
        }
    }

    private fun showDetailsFragment(position: Int) {
        val article = adapter.getArticle(position)
        val direction = FeedFragmentDirections
            .actionFeedFragmentToArticleFragment(article)
        findNavController().navigate(direction)
    }
}
