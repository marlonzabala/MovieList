package com.app.movies.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.movies.network.model.ItunesItem
import com.app.movies.repository.PreferenceRepository
import com.app.movies.repository.SearchActivityRepository

/**
 * Handles logic of search activity
 */
class SearchActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SearchActivityRepository(application)
    private val preferenceRepository = PreferenceRepository(application)
    val showProgress : LiveData<Boolean>
    val itunesItemList : LiveData<List<ItunesItem>>
    val searchTerm = MutableLiveData<String>()

    init {
        this.showProgress = repository.showProgress
        this.itunesItemList = repository.itunesItemList
    }

    fun searchMovies(term : String) {
        repository.searchMovies(term)
        preferenceRepository.setLastSearchTerm(term)
    }

    fun setViewingSearch(){
        preferenceRepository.setIsViewingDetails(false)
    }

    fun init() {
        repository.getTopMovies()


//        val lastSearchTerm = preferenceRepository.getLastSearchTerm()
//
//        if(lastSearchTerm.isNotEmpty()) {
//            searchTerm.value = lastSearchTerm
//        } else {
//            repository.getTopMovies()
//            //searchTerm.value = "Star"
//        }
    }
}