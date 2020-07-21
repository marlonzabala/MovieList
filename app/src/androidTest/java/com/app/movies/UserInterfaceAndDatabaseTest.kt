package com.app.movies

import android.util.Log
import android.view.View
import androidx.core.view.get
import androidx.core.view.size
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.app.movies.db.Movie
import com.app.movies.db.MovieDatabase
import com.app.movies.network.model.ItunesItem
import com.app.movies.utils.Converter
import com.app.movies.view.SearchActivity
import com.app.movies.viewModel.SearchActivityViewModel
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.rv_itunes_item_child.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UserInterfaceAndDatabaseTest {
    @Rule
    @JvmField
    val rule : ActivityTestRule<SearchActivity> = ActivityTestRule(SearchActivity::class.java)

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.app.movies", appContext.packageName)
    }

    @Test
    fun testSearch1() {
        onView(withId(R.id.editTextSearch)).perform(typeText("car"))
        onView(withId(R.id.imageViewSearch)).perform(click())
        onView(withId(R.id.editTextSearch)).check(matches(withText("car")))
        assertEquals("car",rule.activity.editTextSearch.text.toString())
    }

    @Test
    fun testSearch2() {
        onView(withId(R.id.editTextSearch)).perform(typeText("star"))
        onView(withId(R.id.imageViewSearch)).perform(click())
        assertEquals("star",rule.activity.editTextSearch.text.toString())
    }

    @Test
    fun testSearchResultContainsTerm1() {
        onView(withId(R.id.editTextSearch)).perform(typeText("star"))
        onView(withId(R.id.imageViewSearch)).perform(click())
        val searchResult1 = rule.activity.recycleViewSearch.get(0).textViewTrackName.text.toString().toLowerCase()
        assertEquals(true , searchResult1.contains("star"))
    }

    @Test
    fun testSearchResultContainsTerm2() {
        onView(withId(R.id.editTextSearch)).perform(typeText("the"))
        onView(withId(R.id.imageViewSearch)).perform(click())
        val searchResult1 = rule.activity.recycleViewSearch.get(0).textViewTrackName.text.toString().toLowerCase()
        assertEquals(true , searchResult1.contains("the"))
    }

    @Test
    fun testSearchResultContainsTerm3() {
        onView(withId(R.id.editTextSearch)).perform(typeText("Avengers"))
        onView(withId(R.id.imageViewSearch)).perform(click())
        val searchResult1 = rule.activity.recycleViewSearch.get(0).textViewTrackName.text.toString().toLowerCase()
        assertEquals(true , searchResult1.contains("avengers"))
    }

    @Test
    fun testSearchResultContainsTerm4() {
        onView(withId(R.id.editTextSearch)).perform(typeText("World"))
        onView(withId(R.id.imageViewSearch)).perform(click())
        val searchResult1 = rule.activity.recycleViewSearch.get(0).textViewTrackName.text.toString().toLowerCase()
        assertEquals(true , searchResult1.contains("orld"))
    }

    @Test
    fun testSearchResultContainsTopMovies() {
        onView(withId(R.id.editTextSearch)).perform(typeText(""))
        onView(withId(R.id.imageViewSearch)).perform(click())
        val searchResult1 = rule.activity.recycleViewSearch.adapter
        assertEquals(100 , searchResult1?.itemCount)
    }

    @Test
    fun testDatabase() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val movieDatabase = MovieDatabase.getInstance(appContext)

        val items : MutableList<Movie> = mutableListOf()
        for(x in 1..100) {
            items.add(
                Movie(
                    x,
                    "name",
                    "pic.jpg",
                    "movie",
                    "action",
                    "director",
                    0)
            )
        }

        CoroutineScope(Dispatchers.IO).launch {
            movieDatabase.movieDAO.deleteAll()
            movieDatabase.movieDAO.insertMovies(items)
        }

        // wait for coroutines
        Thread.sleep(1000)
        assertEquals(100 , movieDatabase.movieDAO.getAllMovies().size)
    }

    @Test
    fun testDatabase2() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val movieDatabase = MovieDatabase.getInstance(appContext)

        val items : MutableList<Movie> = mutableListOf()
        for(x in 1..100) {
            items.add(
                Movie(
                    x,
                    "name",
                    "pic.jpg",
                    "movie",
                    "action",
                    "director",
                    0)
            )
        }

        CoroutineScope(Dispatchers.IO).launch {
            movieDatabase.movieDAO.deleteAll()
            movieDatabase.movieDAO.insertMovies(items)
            movieDatabase.movieDAO.deleteMovie(Movie(
                4,
                "name",
                "pic.jpg",
                "movie",
                "action",
                "director",
                0))
        }

        // wait for coroutines
        Thread.sleep(1000)
        assertEquals(99 , movieDatabase.movieDAO.getAllMovies().size)
    }
}