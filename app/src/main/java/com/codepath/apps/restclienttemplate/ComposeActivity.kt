package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers


class ComposeActivity : AppCompatActivity() {
    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var client: TwitterClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)
        etCompose = findViewById(R.id.editTextTweet)
        btnTweet = findViewById(R.id.send_tweet)
        client = TwitterApplication.getRestClient(this)
        btnTweet.setOnClickListener{
            val tweetContent = etCompose.text.toString()

            if (tweetContent.isEmpty()){
                Toast.makeText(this,"Empty Tweet!",Toast.LENGTH_LONG).show()

            }else
            if(tweetContent.length > 140){
                Toast.makeText(this,"Tweet is too long!",Toast.LENGTH_LONG).show()
            }
            else {
                //Api Call to publish tweet
                client.publishTweet(tweetContent,object :JsonHttpResponseHandler(){
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.i("ComposeActivity","failed to publish tweet $statusCode",throwable)
                    }

                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                        Log.i("ComposeActivity","Published Tweet $statusCode")
                        val tweet = Tweet.fromJson(json.jsonObject)
                        val intent = Intent()
                        intent.putExtra("tweet",tweet)
                        setResult(RESULT_OK,intent)
                        finish()

                    }

                })
            }
        }
    }
}