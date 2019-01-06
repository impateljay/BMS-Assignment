package com.jay.bmsassignment.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("name") val name: String?,
    @SerializedName("screen_name") val screen_name: String?,
    @SerializedName("verified") val verified: Boolean?,
    @SerializedName("profile_image_url") val profileImageUrl: String?
)