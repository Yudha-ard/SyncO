package com.bangkit.synco.data.model

import com.google.gson.annotations.SerializedName

data class ArticleModel(
    @field:SerializedName("link")
    val link: String,
    @field:SerializedName("title")
    val title: String,
    @field:SerializedName("slug")
    val slug: String,
    @field:SerializedName("thumbnail")
    val thumbnail: String,
    @field:SerializedName("intro")
    val intro: String,
    @field:SerializedName("date")
    val date: String
)