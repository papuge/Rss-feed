package com.papuge.rssfeed.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository {

    private val pageCount: Int = 10
    var defaultUrl = "https://medium.com/feed/the-story"

    private val TAG = "Repository"

    suspend fun fetchFeed(): List<ArticleEntity> {
        Log.d(TAG, "Start fetching")
        return withContext(Dispatchers.IO) {
            val parser = Parser()
            val list: List<Article> = parser.getArticles(defaultUrl)
            list.map { article ->
                ArticleEntity(
                    title = article.title ?: "",
                    pubDate = article.pubDate ?: "",
                    description = article.description ?: "",
                    mainImage = article.image ?: "",
                    content = article.content ?: ""
                )
            }
        }
    }

    fun saveCacheData(appContext: Context, articles: List<ArticleEntity>) {
        val sharedPrefs = appContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(
                List::class.java, ArticleEntity::class.java
            )
        val articlesAdapter: JsonAdapter<List<ArticleEntity>> = moshi.adapter(type)
        val jsonData = articlesAdapter.toJson(articles)

        val editor = sharedPrefs.edit()
        editor.putString(CACHE_DATA_KEY, jsonData)
        editor.apply()
    }

    fun restoreCacheData(appContext: Context): List<ArticleEntity> {
        val sharedPrefs = appContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE)

        val jsonData = sharedPrefs.getString(CACHE_DATA_KEY, null)
        Log.d(TAG, "Restoring Json: $jsonData")
        if (jsonData != null) {
            val moshi = Moshi.Builder().build()
            val type = Types.newParameterizedType(
                List::class.java, ArticleEntity::class.java
            )
            val articlesAdapter: JsonAdapter<List<ArticleEntity>> = moshi.adapter(type)
            val articles = articlesAdapter.fromJson(jsonData)
            Log.d(TAG, "Returned articles: $articles")
            return requireNotNull(articles)
        }
        return listOf()
    }

    companion object {
        private const val CACHE_DATA_KEY = "CACHE_DATA"
        private const val PREF_NAME = "CachedData"
    }

}