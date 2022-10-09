package com.codepath.apps.restclienttemplate

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.restclienttemplate.Helper.EndlessRecyclerViewScrollListener
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException


class TimelineActivity : AppCompatActivity() {

    lateinit var client: TwitterClient

    lateinit var rvTweets:RecyclerView
    lateinit var adapter: TweetsAdapter
    val tweets = ArrayList<Tweet>()
    lateinit var swipeContainer: SwipeRefreshLayout
    lateinit var scrollListener: EndlessRecyclerViewScrollListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        client = TwitterApplication.getRestClient(this)
        swipeContainer = findViewById(R.id.swipeContainer)

        swipeContainer.setOnRefreshListener {
            scrollListener.resetState()
            TwitterClient.since_id = (1).toString()
            Log.i(TAG,"Refreshing Timeline")
            populateHomeTimeline()
        }
        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)

        rvTweets=findViewById(R.id.rvTweets)
        adapter= TweetsAdapter(tweets)
        val linearLayoutManager = LinearLayoutManager(this)
        rvTweets.layoutManager=linearLayoutManager
        rvTweets.adapter = adapter
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                Log.i("TimeLine","onLoadMore")
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.i("TimeLine",page.toString())
                TwitterClient.since_id = (page+1).toString()
                populateHomeTimeline()
                //loadNextDataFromApi(page)
            }
        }
        rvTweets.addOnScrollListener(scrollListener)



        populateHomeTimeline()
    }
    fun populateHomeTimeline(){
        client.getHomeTimeline(object: JsonHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG,"onSuccess")
                val jsonArray = json.jsonArray
                try{
                    adapter.clear()
                    val listOfNewTweets = Tweet.fromJsonArray(jsonArray)
                    tweets.addAll(listOfNewTweets)
                    adapter.notifyDataSetChanged()
                    swipeContainer.isRefreshing=false
                }catch(e:JSONException){
                    Log.e(TAG,"json exception  $e")
                }

            }
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i(TAG,"onFailure")

            }
        })
    }
    companion object{
        val TAG = "TimelineActivity"
    }
}