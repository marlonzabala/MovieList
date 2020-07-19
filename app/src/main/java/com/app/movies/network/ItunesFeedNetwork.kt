package com.app.movies.network

import com.app.movies.network.model.ItunesItemFeed
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Handling api calls using retrofit library
 */
const val BASE_URL_FEED = "https://rss.itunes.apple.com/"

interface ItunesFeedNetwork {

    @GET("api/v1/{country}/{media_type}/{feed_type}/{genre}/{result_limit}/{explicit}.json")
    fun getTopMovies(
        @Path(value = "country", encoded = true) country : String,
        @Path(value = "media_type", encoded = true) mediaType : String,
        @Path(value = "feed_type", encoded = true) feedType : String,
        @Path(value = "genre", encoded = true) genre : String,
        @Path(value = "result_limit", encoded = true) resultLimit : String,
        @Path(value = "explicit", encoded = true) explicit : String
    ) : Call<ItunesItemFeed>

//    // removed "&all" because default value is all
//    @GET("search?")
//    fun getMovies(
//        @Query("term") term : String,
//        @Query("media") media : String,
//        @Query("country") country : String
//    ) : Call<ItunesResult>
//
//    @GET("lookup?")
//    fun lookUp(@Query("id") id : String) : Call<ItunesResult>
}