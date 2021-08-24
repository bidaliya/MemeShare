package com.gmail.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {

    var URL:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadmeme()
    }

   private fun loadmeme(){
       val progressbar = findViewById<ProgressBar>(R.id.progressBar)

       progressbar.visibility = View.VISIBLE

      // val queue = Volley.newRequestQueue(this)
      // val url = "20e67d2f2ea84909afffdeb53a35dbce"

      val url = "https://meme-api.herokuapp.com/gimme"

       // Request a string response from the provided URL.

       val jsonObjectRequest = JsonObjectRequest(

           //we don't have to send anything, that's why we had put null over the JSON request
           Request.Method.GET, url,null,
          Response.Listener { response ->   // here we are getting a json object into the "response" and we have to extract the url from it
             URL = response.getString("url")

              val M = findViewById<ImageView>(R.id.meme)

              Glide.with(this).load(URL).listener(
                   //we have to pass a requestListener object
                  //So we have created an anonymous object of class RequestListener ( or we can say that the object had inherited the
                  // RequestListener class ) and override its inbuilt functions for our use

              object :RequestListener<Drawable>{

                   override fun onLoadFailed(
                      e: GlideException?,
                      model: Any?,
                      target: Target<Drawable>?,
                      isFirstResource: Boolean
                   ): Boolean {
                      progressbar.visibility = View.GONE
                       return false
                    }

                  override fun onResourceReady(
                      resource: Drawable?,
                      model: Any?,
                      target: Target<Drawable>?,
                      dataSource: DataSource?,
                      isFirstResource: Boolean
                  ): Boolean {
                      progressbar.visibility = View.GONE
                      return false
                  }


              }).into(M)
           },
           {
               Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
           })

// Add the request to the RequestQueue.
    //   queue.add(jsonObjectRequest)
       MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

   }

    fun sharememe(view: View) {

        // again here we will use INTENT, because intent is use for interprocess communication.
        // like when we move from one activity to another activity or to share data across apps, we use intent to pass some data
        // like we have used in birthday greeting card
        // https://developer.android.com/training/sharing/send?hl=ru

    val intent = Intent(Intent.ACTION_SEND) // this creates an intent which is used to send something
        intent.type = "text/plain"  // this will share my text with the relevant apps having the type specified.

        // this will share the URL of the image we are getting from the response
        intent.putExtra(Intent.EXTRA_TEXT,"Hey check out my memes having URL: $URL")

        val chooser = Intent.createChooser(intent,"Share this using...")
        startActivity(intent)
    }

    fun nextmeme(view: View) {
        loadmeme()
        MediaPlayer.create(this,R.raw.click).start()
    }

}