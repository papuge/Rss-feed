package com.papuge.rssfeed.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.papuge.rssfeed.R
import com.papuge.rssfeed.data.ArticleEntity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class FeedAdapter(
    private val clickListener: (position: Int) -> Unit
): RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    private var articles: MutableList<ArticleEntity> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(R.layout.article_row, parent, false),
            clickListener
        )
    }

    override fun getItemCount(): Int = articles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    fun getArticle(position: Int) = articles[position]

    fun setArticles(someArticles: List<ArticleEntity>) {
        articles.clear()
        articles.plusAssign(someArticles)
        notifyDataSetChanged()
    }

    fun clearArticles() {
        articles.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(
        view: View,
        listener: (position: Int) -> Unit): RecyclerView.ViewHolder(view) {

        private var titleTV: TextView
        private var pubDateTV: TextView
        private var descriptionTV: TextView
        private var imageView: ImageView

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                listener(position)
            }
            titleTV = view.findViewById(R.id.article_title)
            pubDateTV = view.findViewById(R.id.article_pub_date)
            descriptionTV = view.findViewById(R.id.article_description)
            imageView = view.findViewById(R.id.article_image)
        }

        fun bind(article: ArticleEntity) {
            titleTV.text = article.title
            pubDateTV.text = try {
                val sourceDateString = article.pubDate

                val sourceSdf = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
                val date = sourceSdf.parse(sourceDateString)

                val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                sdf.format(date)

            } catch (e: ParseException) {
                e.printStackTrace()
                article.pubDate
            }
            val indexOfPreview = article.content.indexOf("<p>")
            if (indexOfPreview != -1) {
                val re = Regex("""<[^>]+>*""")
                var answer = article.content.substring(indexOfPreview, indexOfPreview + 100)
                answer = re.replace(answer, "")
                descriptionTV.text = answer
            } else {
                descriptionTV.text = article.description
            }
            if (article.mainImage.isEmpty()) {
                imageView.visibility = GONE
            } else {
                imageView.load(article.mainImage)
            }
        }
    }
}