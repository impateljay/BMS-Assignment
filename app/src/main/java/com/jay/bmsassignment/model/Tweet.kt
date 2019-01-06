package com.jay.bmsassignment.model

import com.google.gson.annotations.SerializedName

data class Tweet(
    @SerializedName("statuses") val statuses: List<Status>?
)