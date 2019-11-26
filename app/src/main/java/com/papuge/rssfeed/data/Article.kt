package com.papuge.rssfeed.data

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Article (
    val title: String,
    val pubDate: String,
    val description: String,
    val mainImage: String,
    val id: Int = 0
): Parcelable