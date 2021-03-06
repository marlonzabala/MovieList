package com.app.movies.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.app.movies.network.model.ItunesItem
import com.app.movies.repository.DetailsActivityRepository
import com.app.movies.repository.PreferenceRepository

/**
 * Handles logic of details activity
 */
class DetailsActivityViewModel (application: Application) : AndroidViewModel(application) {
    private val repository = DetailsActivityRepository(application)
    private val preferenceRepository = PreferenceRepository(application)
    val showProgress : LiveData<Boolean>
    val itunesItem : LiveData<ItunesItem>

    init {
        this.showProgress = repository.showProgress
        this.itunesItem = repository.movieInfo
    }

    fun loadMovie() {
        if(preferenceRepository.getIsTopMovies()) {
            repository.lookupId(preferenceRepository.getMovieId())
        } else {
            repository.searchMovies(
                preferenceRepository.getLastSearchTerm(),
                preferenceRepository.getLastPosition()
            )
        }

        preferenceRepository.setIsViewingDetails(true)
    }
}