package com.app.movies.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.app.movies.R
import com.app.movies.db.MovieDatabase
import com.app.movies.network.BASE_URL
import com.app.movies.network.ItunesNetwork
import com.app.movies.network.model.ItunesItem
import com.app.movies.network.model.ItunesResult
import com.app.movies.utils.Converter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Handling the data of Details Activity
 */
class DetailsActivityRepository(val application: Application) {
    val movieDatabase = MovieDatabase.getInstance(application)
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
                getMovieInfoOffline(PreferenceRepository(application).getMovieId())
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
                getMovieInfoOffline(id)
            }

            override fun onResponse(call: Call<ItunesResult>, response: Response<ItunesResult>) {
                if(response.body()?.results?.size!! >= 1) {
                    response.body()?.results?.get(0)?.let {
                        movieInfo.value = it
                        if(it.longDescription != null) {
                            saveMovieDescriptionOffline(it.trackId, it.longDescription)
                        }
                    }
                }
                showProgress.value = false
            }
        })
    }

    fun getMovieInfoOffline(id : Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val movies = movieDatabase.movieDAO.getMovie(id)
            val itunesItem = Converter.convertMovieToItunesItem(movies)
            updateMovieInfo(itunesItem)
        }
    }

    suspend fun updateMovieInfo(itunesItem : ItunesItem) {
        withContext(Dispatchers.Main) {
            movieInfo.value = itunesItem
        }
    }

    fun saveMovieDescriptionOffline(id: Int, description : String) {
        CoroutineScope(Dispatchers.IO).launch {
            val movie = movieDatabase.movieDAO.getMovie(id)
            movie.longDescription = description
            movieDatabase.movieDAO.insertMovie(movie)
        }
    }
}