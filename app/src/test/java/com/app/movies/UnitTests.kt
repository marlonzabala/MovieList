package com.app.movies

import androidx.lifecycle.ViewModelProvider
import com.app.movies.network.model.ItunesItem
import com.app.movies.utils.Converter.Companion.convertItunesItemToMovie
import com.app.movies.utils.Converter.Companion.convertMovieToItunesItem
import com.app.movies.viewModel.SearchActivityViewModel
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Converter

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTests {
    @Test
    fun converterTest() {
        val items : MutableList<ItunesItem> = mutableListOf()
        for(x in 1..100) {
            items.add(ItunesItem(
                x,
                "name",
                "pic.jpg",
                "movie",
                "action",
                "director"))
        }

        val movie = convertItunesItemToMovie(items)
        assertEquals(100, movie.size)
    }

    @Test
    fun converterTest2() {
        val items : MutableList<ItunesItem> = mutableListOf()
        for(x in 1..1000) {
            items.add(ItunesItem(
                x,
                "name",
                "pic.jpg",
                "movie",
                "action",
                "director"))
        }

        val movie = convertItunesItemToMovie(items)
        assertEquals(1000, movie.size)
    }
}