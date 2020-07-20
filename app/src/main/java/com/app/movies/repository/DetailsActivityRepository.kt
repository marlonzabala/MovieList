package com.app.movies.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.app.movies.R
import com.app.movies.network.BASE_URL
import com.app.movies.network.ItunesNetwork
import com.app.movies.network.model.ItunesItem
import com.app.movies.network.model.ItunesResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Handling the data of Details Activity
 */
class DetailsActivityRepository(val application: Application) {
    val showProgress = MutableLiveData<Boolean>()
    val movieInfo = MutableLiveData<ItunesItem>()

    fun searchMovies(term : String, position : Int) {
        showProgress.value = true

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ItunesNetwork::class.java)

        service.getMovies(term,"movie","us").enqueue(object : Callback<ItunesResult>{
            override fun onFailure(call: Call<ItunesResult>, t: Throwable) {
                showProgress.value = false
                Toast.makeText(application, application.getString(R.string.error_result), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ItunesResult>, response: Response<ItunesResult>) {
                movieInfo.value = response.body()?.results?.get(position)
                Log.e("movieId",response.body()?.results?.get(position)?.trackId.toString())
                showProgress.value = false
            }
        })
    }

    fun lookupId(id : Int) {
        showProgress.value = true

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ItunesNetwork::class.java)

        service.lookUp(id).enqueue(object : Callback<ItunesResult>{
            override fun onFailure(call: Call<ItunesResult>, t: Throwable) {
                showProgress.value = false
                Toast.makeText(application, application.getString(R.string.error_result), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ItunesResult>, response: Response<ItunesResult>) {
                if(response.body()?.results?.size!! >= 1) {
                    movieInfo.value = response.body()?.results?.get(0)
                    Log.e("movieId", response.body()?.results?.get(0)?.trackId.toString())
                }
                showProgress.value = false
            }
        })
    }
}