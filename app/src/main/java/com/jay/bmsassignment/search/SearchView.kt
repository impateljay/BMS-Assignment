package com.jay.bmsassignment.search

import com.jay.bmsassignment.base.BaseView
import com.jay.bmsassignment.model.Status

interface SearchView : BaseView {
    fun showToast(message: String?, length: Int)
    fun changeProgressBarVisibility(visibility: Int)
    fun changeRecyclerViewVisibility(visibility: Int)
    fun changeNoRecordsVisibility(visibility: Int)
    fun searchViewPadding(paddingLeft: Int, paddingTop: Int, paddingRight: Int, paddingBottom: Int)
    fun sortImageViewPadding(paddingLeft: Int, paddingTop: Int, paddingRight: Int, paddingBottom: Int)
    fun addTweets(newTweets: List<Status>?)
}