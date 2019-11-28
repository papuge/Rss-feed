package com.papuge.rssfeed.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.papuge.rssfeed.data.ArticleEntity
import com.papuge.rssfeed.data.Repository
import kotlinx.coroutines.launch

class FeedViewModel: ViewModel() {

    private val repository = Repository()
    private val TAG = "FeedViewModel"

    init {
        Log.d(TAG, "View model is initiated")
        getArticles()
    }

    private var _articlesList = MutableLiveData<List<ArticleEntity>>()
    val articlesList: LiveData<List<ArticleEntity>>
        get() = _articlesList


    fun getArticles() {
        viewModelScope.launch {
            Log.d(TAG, "Fetching started in viewModel")
            val articles = repository.fetchFeed()
            Log.d(TAG, "Articles: $articles")
            _articlesList.value = articles
        }
    }

}