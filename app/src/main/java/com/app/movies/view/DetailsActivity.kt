package com.app.movies.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.movies.R
import com.app.movies.viewModel.DetailsActivityViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_search.*

class DetailsActivity : AppCompatActivity() {
    private lateinit var viewModel : DetailsActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        viewModel = ViewModelProvider(this).get(DetailsActivityViewModel::class.java)

        viewModel.showProgress.observe(this, Observer {
            if(it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })

        viewModel.itunesItem.observe(this, Observer {
            textViewTitle.text = it.trackName
            textViewGenre.text = it.primaryGenreName
            textViewDirector.text = String.format(getString(R.string.director_label), it.artistName)
            textViewDescription.text = it.longDescription

            val thumbnailRequest = Glide.with(this)
                .load(it.artworkUrl100)

            val requestOption = RequestOptions()
                .centerCrop()
                .dontTransform()

            Glide.with(this)
                .load(it.artworkUrl100)
                .thumbnail(thumbnailRequest)
                .apply(requestOption)
                .into(imageViewDetailsArtwork)

            // Get higher quality image
            Glide.with(this)
                .load(it.artworkUrl100.replace("100","600"))
                .thumbnail(thumbnailRequest)
                .apply(requestOption)
                .into(imageViewDetailsArtwork)
        })

        viewModel.loadMovie()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, SearchActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        finish()
    }
}