package com.jay.bmsassignment.model

import com.google.gson.annotations.SerializedName

data class UserMentions(
    @SerializedName("screen_name") val screenName: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("id") val id: Long?,
    @SerializedName("id_str") val idStr: String?,
    @SerializedName("indices") val indices: ArrayList<Int>?
)