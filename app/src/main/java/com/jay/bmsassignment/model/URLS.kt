package com.jay.bmsassignment.model

import com.google.gson.annotations.SerializedName

data class URLS(
    @SerializedName("url") val url: String,
    @SerializedName("expanded_url") val expandedUrl: String,
    @SerializedName("display_url") val displayUrl: String,
    @SerializedName("indices") val indices: ArrayList<Int>
)