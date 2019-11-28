package com.papuge.rssfeed.data

import android.util.Log
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository {

    private val pageCount: Int = 10
    var url = "https://medium.com/feed/the-story"

    private val TAG = "Repository"

    suspend fun fetchFeed(): List<ArticleEntity> {
        Log.d(TAG, "Start fetching")
        return withContext(Dispatchers.IO) {
            val parser = Parser()
            val list: List<Article> = parser.getArticles(url)
            list.take(pageCount).map { article ->
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

}