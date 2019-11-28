package com.papuge.rssfeed.ui.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.lifecycle.*
import com.papuge.rssfeed.data.ArticleEntity
import com.papuge.rssfeed.data.Repository
import kotlinx.coroutines.launch

class FeedViewModel(private val app: Application): AndroidViewModel(app) {

    private val repository = Repository()
    private val TAG = "FeedViewModel"

    private var _articlesList = MutableLiveData<List<ArticleEntity>>()
    val articlesList: LiveData<List<ArticleEntity>>
        get() = _articlesList

    init {
        Log.d(TAG, "View model is initiated")
        getArticles()
    }


    private fun getArticles() {
        val cm = app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        viewModelScope.launch {
            val articles = when {
                isConnected -> repository.fetchFeed()
                else -> repository.restoreCacheData(app)
            }
            _articlesList.postValue(articles)
            Log.d(TAG, "Try to save data")
            repository.saveCacheData(app, articles.takeLast(10))
        }
    }
}