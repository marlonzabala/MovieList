package com.app.movies.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.app.movies.R
import com.app.movies.db.MovieDatabase
import com.app.movies.network.model.ItunesItemFeed
import com.app.movies.network.BASE_URL
import com.app.movies.network.BASE_URL_FEED
import com.app.movies.network.ItunesFeedNetwork
import com.app.movies.network.ItunesNetwork
import com.app.movies.network.model.ItunesItem
import com.app.movies.network.model.ItunesResult
import com.app.movies.utils.Converter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Handles movie search values
 */
class SearchActivityRepository(val application: Application){
    val movieDatabase = MovieDatabase.getInstance(application)
    val showProgress = MutableLiveData<Boolean>()
    val itunesItemList = MutableLiveData<List<ItunesItem>>()

    fun searchMovies(term : String) {
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
                searchMoviesOffline(term)
            }

            override fun onResponse(call: Call<ItunesResult>, response: Response<ItunesResult>) {
                itunesItemList.value = response.body()?.results
                saveMoviesOffline(response.body()?.results)
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

        service.getTopMovies(
            "us",
            "movies",
            "top-movies",
            "all",
            "100",
            "explicit")
            .enqueue(object : Callback<ItunesItemFeed>{
                override fun onFailure(call: Call<ItunesItemFeed>, t: Throwable) {
                    showProgress.value = false
                    Toast.makeText(application, application.getString(R.string.error_result), Toast.LENGTH_LONG).show()
                    showTopMoviesOffline()
                }

                override fun onResponse(call: Call<ItunesItemFeed>, response: Response<ItunesItemFeed>) {
                    response.body()?.let {
                        val itunesItems = Converter.convertFeedToItunesItemList(it)
                        itunesItemList.value = itunesItems
                        saveTopMoviesOffline(itunesItems)
                    }

                    showProgress.value = false
                }
            })
    }

    fun searchMoviesOffline(term : String) {
        CoroutineScope(IO).launch {
            val movies = movieDatabase.movieDAO.searchMovies(term)
            val itunesItems = Converter.convertMovieToItunesItem(movies)
            updateMovieList(itunesItems)
        }
    }

    fun showTopMoviesOffline() {
        CoroutineScope(IO).launch {
            val movies = movieDatabase.movieDAO.getTopMovies()
            val itunesItems = Converter.convertMovieToItunesItem(movies)
            updateMovieList(itunesItems)
        }
    }

    suspend fun updateMovieList(itunesItems :List<ItunesItem>) {
        withContext(Main) {
            itunesItemList.value = itunesItems
        }
    }

    fun saveMoviesOffline(results: List<ItunesItem>?) {
        results?.let {
            CoroutineScope(IO).launch {
                movieDatabase.movieDAO.insertMovies(Converter.convertItunesItemToMovie(it))
            }
        }
    }

    fun saveTopMoviesOffline(results: List<ItunesItem>?) {
        results?.let {
            CoroutineScope(IO).launch {
                movieDatabase.movieDAO.insertMovies(Converter.convertItunesItemToTopMovie(it))
            }
        }
    }
}