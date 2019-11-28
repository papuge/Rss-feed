package com.papuge.rssfeed.data

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ArticleEntity (
    val title: String,
    val pubDate: String,
    val description: String,
    val mainImage: String,
    val content: String
): Parcelable