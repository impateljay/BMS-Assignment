package com.jay.bmsassignment.model

import com.google.gson.annotations.SerializedName

data class Retweet(
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("full_text") val fullText: String?,
    @SerializedName("display_text_range") val displayTextRange: ArrayList<Int>?,
    @SerializedName("entities") val entities: Entity?,
    @SerializedName("user") val user: User?,
    @SerializedName("retweet_count") val retweetCount: Long?,
    @SerializedName("favorite_count") val favoriteCount: Long?
)
