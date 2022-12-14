package com.codepath.apps.restclienttemplate


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.codepath.apps.restclienttemplate.models.Tweet

class TweetsAdapter(val tweets: ArrayList<Tweet>):RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_tweet,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TweetsAdapter.ViewHolder, position: Int) {
        val tweet: Tweet = tweets.get(position)
        holder.tvUser.text = tweet.user?.name
        holder.tvTweetBOdy.text = tweet.body
        holder.tvTimeStamp.text = tweet.createdAt
        Glide.with(holder.itemView.context).load(tweet.user?.publicImageUrl)
            .into(holder.ivProfileImage)

    }

    override fun getItemCount(): Int {
        return tweets.size
    }
    fun clear(){
        tweets.clear()
        notifyDataSetChanged()
    }
    fun addAll(tweetList:List<Tweet>){
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val ivProfileImage = itemView.findViewById<ImageView>(R.id.ivProfileImage)
        val tvTweetBOdy = itemView.findViewById<TextView>(R.id.tvTweetBody)
        val tvUser = itemView.findViewById<TextView>(R.id.tvUsername)
        val tvTimeStamp = itemView.findViewById<TextView>(R.id.tvTimeStamp)
    }
}