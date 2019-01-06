package com.jay.bmsassignment.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jay.bmsassignment.R
import com.jay.bmsassignment.model.Status
import java.text.SimpleDateFormat
import java.util.*


class SearchAdapter(private val context: Context, private var tweets: ArrayList<Status>) :
    RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
        var name: TextView = view.findViewById(R.id.name)
        var screenName: TextView = view.findViewById(R.id.screen_name)
        var tweetText: TextView = view.findViewById(R.id.tweet_text)
        var likesCount: TextView = view.findViewById(R.id.likes_count)
        var retweetsCount: TextView = view.findViewById(R.id.retweets_count)
        var timeAgo: TextView = view.findViewById(R.id.time_ago)
        var isVerifiedUser: ImageView = view.findViewById(R.id.verified_account_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.tweet_row_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tweet = tweets[position]
        if (tweet.retweetedStatus != null) {
            holder.name.text = tweet.retweetedStatus.user?.name
            holder.screenName.text = "@" + tweet.retweetedStatus.user?.screen_name
            holder.isVerifiedUser.visibility = if (tweet.retweetedStatus.user?.verified!!) View.VISIBLE else View.GONE
            holder.likesCount.text = tweet.retweetedStatus.favoriteCount.toString()
            holder.retweetsCount.text = tweet.retweetedStatus.retweetCount.toString()
            val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy")
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            val ago = DateUtils.getRelativeTimeSpanString(
                sdf.parse(tweet.retweetedStatus.createdAt).time,
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS
            )
            holder.timeAgo.text = ago
            var tweetText: String? = null
            try {
                tweetText = tweet.retweetedStatus.fullText?.substring(
                    tweet.retweetedStatus.displayTextRange?.get(0)!!, tweet.retweetedStatus.displayTextRange.get(1)
                )
            } catch (e: Exception) {
                holder.tweetText.text = tweet.retweetedStatus.fullText
            }
            tweet.retweetedStatus.entities?.urls?.forEach {
                tweetText = tweetText?.replace(it.url, it.displayUrl)
            }
            holder.tweetText.text = tweetText
            Glide.with(context)
                .load(tweet.retweetedStatus.user.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail)
        } else {
            holder.name.text = tweet.user?.name
            holder.screenName.text = "@" + tweet.user?.screen_name
            holder.isVerifiedUser.visibility = if (tweet.user?.verified!!) View.VISIBLE else View.GONE
            holder.likesCount.text = tweet.favoriteCount.toString()
            holder.retweetsCount.text = tweet.retweetCount.toString()
            val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy")
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            val ago = DateUtils.getRelativeTimeSpanString(
                sdf.parse(tweet.createdAt).time,
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS
            )
            holder.timeAgo.text = ago
            var tweetText: String? = null
            try {
                tweetText = tweet.fullText?.substring(
                    tweet.displayTextRange?.get(0)!!, tweet.displayTextRange.get(1)
                )
            } catch (e: Exception) {
                holder.tweetText.text = tweet.fullText
            }
            tweet.entities?.urls?.forEach {
                tweetText = tweetText?.replace(it.url, it.displayUrl)
            }
            holder.tweetText.text = tweetText
            Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail)
        }
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    fun addTweets(newTweets: ArrayList<Status>) {
        this.tweets = newTweets
        notifyDataSetChanged()
    }

    fun getTweets(): List<Status> {
        return this.tweets
    }
}