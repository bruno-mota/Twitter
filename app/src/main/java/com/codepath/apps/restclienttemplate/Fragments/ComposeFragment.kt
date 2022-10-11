package com.codepath.apps.restclienttemplate.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.codepath.apps.restclienttemplate.R
import com.codepath.apps.restclienttemplate.TwitterApplication
import com.codepath.apps.restclienttemplate.TwitterClient
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers


class ComposeFragment: DialogFragment() {
    interface OnBackListener {
        fun sendInput(tweet: Tweet)
    }


    interface EditNameDialogListener {
        fun onFinishEditDialog(inputText: String?)
    }


    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var client: TwitterClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //val listener = activity as EditNameDialogListener?
        //listener!!.onFinishEditDialog(mEditText.getText().toString())



        val rootView: View = inflater.inflate(R.layout.activity_compose, container,false)
        etCompose = rootView.findViewById(R.id.editTextTweet)
        btnTweet = rootView.findViewById(R.id.send_tweet)
        client = TwitterApplication.getRestClient(rootView.context)
        btnTweet.setOnClickListener{
            val tweetContent = etCompose.text.toString()

            if (tweetContent.isEmpty()){
                Toast.makeText(rootView.context,"Empty Tweet!", Toast.LENGTH_LONG).show()

            }else
                if(tweetContent.length > 140){
                    Toast.makeText(rootView.context,"Tweet is too long!", Toast.LENGTH_LONG).show()
                }
                else {
                    //Api Call to publish tweet
                    client.publishTweet(tweetContent,object : JsonHttpResponseHandler(){
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
                            val listener = activity as OnBackListener?
                            val tweet = Tweet.fromJson(json.jsonObject)
                            listener!!.sendInput(tweet)
                            //val intent = Intent()
                            //intent.putExtra("tweet",tweet)
                            //setResult(AppCompatActivity.RESULT_OK,intent)
                            dismiss()

                        }

                    })
                }
        }
        return rootView
    }
}