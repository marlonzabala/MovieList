package com.app.movies.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.app.movies.db.Movie
import com.app.movies.network.model.ItunesItem
import com.app.movies.network.model.ItunesItemFeed
import java.util.*


class Converter {
    companion object {
        fun convertFeedToItunesItemList(itemFeed: ItunesItemFeed): List<ItunesItem> {
            val list: MutableList<ItunesItem> = mutableListOf()
            val feedItems = itemFeed.feed.results

            for (feedItem in feedItems) {
                if(feedItem.artworkUrl100 == "")
                    continue

                val itunesItem = ItunesItem(
                    feedItem.id.toInt(),
                    feedItem.name,
                    feedItem.artworkUrl100,
                    "",
                    feedItem.genres.get(0).name,
                    feedItem.artistName
                )
                list.add(itunesItem)
            }

            return list
        }

        fun convertItunesItemToMovie(itunesItem: List<ItunesItem>): List<Movie>  {
            val movieList: MutableList<Movie> = mutableListOf()

            for (feedItem in itunesItem) {
                if(feedItem.artworkUrl100 == "")
                    continue

                val movie = Movie(
                    feedItem.trackId,
                    feedItem.trackName,
                    feedItem.artworkUrl100,
                    feedItem.longDescription,
                    feedItem.primaryGenreName,
                    feedItem.artistName,
                    99
                )
                movieList.add(movie)
            }

            return movieList
        }

        fun convertItunesItemToTopMovie(itunesItem: List<ItunesItem>): List<Movie>  {
            val movieList: MutableList<Movie> = mutableListOf()
            var positionCounter = 0
            for (feedItem in itunesItem) {
                positionCounter++

                if(feedItem.artworkUrl100 == "")
                    continue

                val movie = Movie(
                    feedItem.trackId,
                    feedItem.trackName,
                    feedItem.artworkUrl100,
                    feedItem.longDescription,
                    feedItem.primaryGenreName,
                    feedItem.artistName,
                    positionCounter
                )
                movieList.add(movie)
            }

            return movieList
        }

        fun convertMovieToItunesItem(itunesItem: List<Movie>): List<ItunesItem>  {
            val movieList: MutableList<ItunesItem> = mutableListOf()

            for (feedItem in itunesItem) {
                if(feedItem.artworkUrl100 == "")
                    continue

                val movie = ItunesItem(
                    feedItem.trackId,
                    feedItem.trackName,
                    feedItem.artworkUrl100,
                    feedItem.longDescription,
                    feedItem.primaryGenreName,
                    feedItem.artistName
                )
                movieList.add(movie)
            }

            return movieList
        }

        fun convertMovieToItunesItem(movie: Movie): ItunesItem  {
            val itunesItem = ItunesItem(
                movie.trackId,
                movie.trackName,
                movie.artworkUrl100,
                movie.longDescription,
                movie.primaryGenreName,
                movie.artistName
            )

            return itunesItem
        }
    }
}