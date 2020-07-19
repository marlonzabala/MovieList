package com.app.movies.view

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.app.movies.R
import com.app.movies.adapter.ItunesItemSearchAdapter
import com.app.movies.viewModel.SearchActivityViewModel
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel : SearchActivityViewModel
    private lateinit var searchAdapter : ItunesItemSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(this).get(SearchActivityViewModel::class.java)

        imageViewSearch.setOnClickListener {
            if(editTextSearch.text!!.isNotEmpty())
                viewModel.searchMovies(editTextSearch.text.toString())
        }

        editTextSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if(editTextSearch.text!!.isNotEmpty())
                    viewModel.searchMovies(editTextSearch.text.toString())
                return@OnEditorActionListener true
            }
            false
        })

        viewModel.showProgress.observe(this, Observer {
            if(it) {
                search_progress.visibility = VISIBLE
            } else {
                search_progress.visibility = GONE
            }
        })

        viewModel.itunesItemList.observe(this, Observer {
            searchAdapter.setItunesItemList(it)
        })

        searchAdapter = ItunesItemSearchAdapter(this)
        recycleViewSearch.adapter = searchAdapter
        val columns = resources.getInteger(R.integer.gallery_columns)
        recycleViewSearch.layoutManager = GridLayoutManager(this, columns)

        viewModel.searchTerm.observe(this, Observer {
            editTextSearch.setText(it)
            viewModel.searchMovies(it)
        })

        initDefaultValues()
    }

    private fun initDefaultValues() {
        viewModel.init()
        viewModel.setViewingSearch()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setViewingSearch()
    }
}