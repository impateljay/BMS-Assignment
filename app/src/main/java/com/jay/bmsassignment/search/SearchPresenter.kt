package com.jay.bmsassignment.search

import android.content.res.Resources
import android.view.View
import com.jay.bmsassignment.base.BasePresenter
import com.jay.bmsassignment.model.Status
import com.jay.bmsassignment.model.Tweet
import com.jay.bmsassignment.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class SearchPresenter : BasePresenter<SearchView> {

    private var searchView: SearchView? = null

    override fun onAttach(view: SearchView) {
        searchView = view
    }

    override fun onDetach() {
        searchView = null
    }

    fun getTweets(searchText: String, resources: Resources) {
        searchView?.changeProgressBarVisibility(View.VISIBLE)
        searchView?.changeNoRecordsVisibility(View.GONE)
        searchView?.changeRecyclerViewVisibility(View.GONE)
        val apiService = ApiInterface.create()
        val usersCall = apiService.getTweets(searchText, "mixed", 100, "extended")
        usersCall.enqueue(object : Callback<Tweet> {
            override fun onFailure(call: Call<Tweet>, t: Throwable) {
                searchView?.changeProgressBarVisibility(View.GONE)
            }

            override fun onResponse(call: Call<Tweet>, response: Response<Tweet>) {
                if (response.isSuccessful) {
                    searchView?.searchViewPadding(20, (24 * resources.displayMetrics.density + 0.5f).toInt(), 20, 8)
                    searchView?.sortImageViewPadding(240, (24 * resources.displayMetrics.density + 0.5f).toInt(), 20, 8)
                    searchView?.changeProgressBarVisibility(View.GONE)
                    searchView?.changeRecyclerViewVisibility(View.VISIBLE)
                    searchView?.addTweets(response.body()?.statuses)
                } else {
                    searchView?.changeProgressBarVisibility(View.GONE)
                }
            }

        })
    }

    fun sortTweets(sortBy: String, tweets: List<Status>, id: Int) {
        when (sortBy) {
            "Popular" -> {
                searchView?.changeProgressBarVisibility(View.VISIBLE)
                searchView?.changeRecyclerViewVisibility(View.GONE)
                Collections.sort(tweets, PopularSort())
                searchView?.addTweets(tweets)
                searchView?.changeProgressBarVisibility(View.GONE)
                searchView?.changeRecyclerViewVisibility(View.VISIBLE)
            }
            "Newest" -> {
                searchView?.changeProgressBarVisibility(View.VISIBLE)
                searchView?.changeRecyclerViewVisibility(View.GONE)
                Collections.sort(tweets, NewestSort())
                searchView?.addTweets(tweets)
                searchView?.changeProgressBarVisibility(View.GONE)
                searchView?.changeRecyclerViewVisibility(View.VISIBLE)
            }
        }
    }

    internal inner class NewestSort : Comparator<Status> {
        private var dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy")
        override fun compare(lhs: Status, rhs: Status): Int {
            if (lhs.retweetedStatus != null && rhs.retweetedStatus != null) {
                return dateFormat.parse(rhs.retweetedStatus.createdAt)
                    .compareTo(dateFormat.parse(lhs.retweetedStatus.createdAt))
            } else if (lhs.retweetedStatus == null && rhs.retweetedStatus != null) {
                return dateFormat.parse(rhs.retweetedStatus.createdAt).compareTo(dateFormat.parse(lhs.createdAt))
            } else if (lhs.retweetedStatus != null && rhs.retweetedStatus == null) {
                return dateFormat.parse(rhs.createdAt).compareTo(dateFormat.parse(lhs.retweetedStatus.createdAt))
            } else {
                return dateFormat.parse(rhs.createdAt).compareTo(dateFormat.parse(lhs.createdAt))
            }
        }
    }

    inner class PopularSort : Comparator<Status> {
        override fun compare(lhs: Status, rhs: Status): Int {

            if (lhs.retweetedStatus != null && rhs.retweetedStatus != null) {
                val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy")
                sdf.timeZone = TimeZone.getTimeZone("GMT")
                val lhsDiff = System.currentTimeMillis() - sdf.parse(lhs.retweetedStatus.createdAt).time
                val rhsDiff = System.currentTimeMillis() - sdf.parse(rhs.retweetedStatus.createdAt).time
                val lhsCount =
                    (lhs.retweetedStatus.retweetCount?.toDouble()?.times(1.5))?.plus((lhs.retweetedStatus.favoriteCount?.toDouble()!!))
                val rhsCount =
                    (rhs.retweetedStatus.retweetCount?.toDouble()?.times(1.5))?.plus((rhs.retweetedStatus.favoriteCount?.toDouble()!!))
                val lhsDiffMinutes = lhsDiff / (1000 % 60)
                val rhsDiffMinutes = rhsDiff / (1000 % 60)
                val lhsNormalized: Double = lhsCount?.div(lhsDiffMinutes)!!
                val rhsNormalized: Double = rhsCount?.div(rhsDiffMinutes)!!
                if (lhsNormalized > rhsNormalized) return -1
                return if (lhsNormalized < rhsNormalized) 1 else 0
            } else if (lhs.retweetedStatus == null && rhs.retweetedStatus != null) {
                val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy")
                sdf.timeZone = TimeZone.getTimeZone("GMT")
                val lhsDiff = System.currentTimeMillis() - sdf.parse(lhs.createdAt).time
                val rhsDiff = System.currentTimeMillis() - sdf.parse(rhs.retweetedStatus.createdAt).time
                val lhsCount = (lhs.retweetCount?.toDouble()?.times(1.5))?.plus((lhs.favoriteCount?.toDouble()!!))
                val rhsCount =
                    (rhs.retweetedStatus.retweetCount?.toDouble()?.times(1.5))?.plus((rhs.retweetedStatus.favoriteCount?.toDouble()!!))
                val lhsDiffMinutes = lhsDiff / (1000 % 60)
                val rhsDiffMinutes = rhsDiff / (1000 % 60)
                val lhsNormalized: Double = lhsCount?.div(lhsDiffMinutes)!!
                val rhsNormalized: Double = rhsCount?.div(rhsDiffMinutes)!!
                if (lhsNormalized > rhsNormalized) return -1
                return if (lhsNormalized < rhsNormalized) 1 else 0
            } else if (lhs.retweetedStatus != null && rhs.retweetedStatus == null) {
                val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy")
                sdf.timeZone = TimeZone.getTimeZone("GMT")
                val lhsDiff = System.currentTimeMillis() - sdf.parse(lhs.retweetedStatus.createdAt).time
                val rhsDiff = System.currentTimeMillis() - sdf.parse(rhs.createdAt).time
                val lhsCount =
                    (lhs.retweetedStatus.retweetCount?.toDouble()?.times(1.5))?.plus((lhs.retweetedStatus.favoriteCount?.toDouble()!!))
                val rhsCount = (rhs.retweetCount?.toDouble()?.times(1.5))?.plus((rhs.favoriteCount?.toDouble()!!))
                val lhsDiffMinutes = lhsDiff / (1000 % 60)
                val rhsDiffMinutes = rhsDiff / (1000 % 60)
                val lhsNormalized: Double = lhsCount?.div(lhsDiffMinutes)!!
                val rhsNormalized: Double = rhsCount?.div(rhsDiffMinutes)!!
                if (lhsNormalized > rhsNormalized) return -1
                return if (lhsNormalized < rhsNormalized) 1 else 0
            } else {
                val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy")
                sdf.timeZone = TimeZone.getTimeZone("GMT")
                val lhsDiff = System.currentTimeMillis() - sdf.parse(lhs.createdAt).time
                val rhsDiff = System.currentTimeMillis() - sdf.parse(rhs.createdAt).time
                val lhsCount = (lhs.retweetCount?.toDouble()?.times(1.5))?.plus((lhs.favoriteCount?.toDouble()!!))
                val rhsCount = (rhs.retweetCount?.toDouble()?.times(1.5))?.plus((rhs.favoriteCount?.toDouble()!!))
                val lhsDiffMinutes = lhsDiff / (1000 % 60)
                val rhsDiffMinutes = rhsDiff / (1000 % 60)
                val lhsNormalized: Double = lhsCount?.div(lhsDiffMinutes)!!
                val rhsNormalized: Double = rhsCount?.div(rhsDiffMinutes)!!
                if (lhsNormalized > rhsNormalized) return -1
                return if (lhsNormalized < rhsNormalized) 1 else 0
            }
        }
    }
}