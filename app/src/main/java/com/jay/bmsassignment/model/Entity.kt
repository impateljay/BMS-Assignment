package com.jay.bmsassignment.model

import com.google.gson.annotations.SerializedName

class Entity(
    @SerializedName("urls") val urls: List<URLS>
)