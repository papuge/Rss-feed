package com.papuge.rssfeed.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository {
    var url = "https://www.androidauthority.com/feed"

    private val TAG = "Repository"

    suspend fun fetchFeed(): List<ArticleEntity> {
        Log.d(TAG, "Start fetching from $url")
        return withContext(Dispatchers.IO) {
            val parser = Parser()
            try {
                val list: List<Article> = parser.getArticles(url)
                list.map { article ->
                    ArticleEntity(
                        title = article.title ?: "",
                        pubDate = article.pubDate ?: "",
                        description = article.description ?: "",
                        mainImage = article.image ?: "",
                        content = article.content ?: ""
                    )
                }
            } catch (ex: Exception) {
                listOf<ArticleEntity>()
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
        editor.putString(URL_KEY, url)
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

    fun restoreUrl(appContext: Context) {
        val sharedPrefs = appContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        val restoredUrl = sharedPrefs.getString(URL_KEY, "https://www.androidauthority.com/feed")
        url = requireNotNull(restoredUrl)
    }

    companion object {
        private const val CACHE_DATA_KEY = "CACHE_DATA"
        private const val URL_KEY = "URL_KEY"
        private const val PREF_NAME = "CachedData"
    }

}