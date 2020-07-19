package com.app.movies.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.app.movies.R
import com.app.movies.network.model.ItunesItemFeed
import com.app.movies.network.BASE_URL
import com.app.movies.network.BASE_URL_FEED
import com.app.movies.network.ItunesFeedNetwork
import com.app.movies.network.ItunesNetwork
import com.app.movies.network.model.ItunesItem
import com.app.movies.network.model.ItunesResult
import com.app.movies.utils.Converter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Handles movie search values
 */
class SearchActivityRepository(val application: Application){
    val showProgress = MutableLiveData<Boolean>()
    val itunesItemList = MutableLiveData<List<ItunesItem>>()

    fun searchMovies(term : String) {
        showProgress.value = true

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ItunesNetwork::class.java)

        service.getMovies(term,"movie","au").enqueue(object : Callback<ItunesResult>{
            override fun onFailure(call: Call<ItunesResult>, t: Throwable) {
                showProgress.value = false
                Toast.makeText(application, application.getString(R.string.error_result), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ItunesResult>, response: Response<ItunesResult>) {
                itunesItemList.value = response.body()?.results
                showProgress.value = false
            }
        })
    }

    fun getTopMovies() {
        showProgress.value = true

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_FEED)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ItunesFeedNetwork::class.java)

        //https://rss.itunes.apple.com/api/v1/au/movies/top-movies/all/50/explicit.json
        service.getTopMovies(
            "au",
            "movies",
            "top-movies",
            "all",
            "100",
            "explicit")
            .enqueue(object : Callback<ItunesItemFeed>{
                override fun onFailure(call: Call<ItunesItemFeed>, t: Throwable) {
                    showProgress.value = false
                    Toast.makeText(application, application.getString(R.string.error_result), Toast.LENGTH_LONG).show()
                    Log.e("error",t.toString())
                    Log.e("error",t.localizedMessage)
                }

                override fun onResponse(call: Call<ItunesItemFeed>, response: Response<ItunesItemFeed>) {
                    itunesItemList.value = response.body()?.let { Converter.convertToItemList(it) }
                    showProgress.value = false
                }
            })
    }
}