package com.app.movies.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.movies.R
import com.app.movies.extensions.load
import com.app.movies.network.model.ItunesItem
import com.app.movies.repository.PreferenceRepository
import com.app.movies.utils.Converter
import com.app.movies.view.DetailsActivity
import kotlinx.android.synthetic.main.rv_itunes_item_child.view.*

class ItunesItemSearchAdapter(private val context: Context) :
    RecyclerView.Adapter<ItunesItemSearchAdapter.ViewHolder>() {
    private var list: List<ItunesItem> = ArrayList()

    fun setItunesItemList(list: List<ItunesItem>){
        this.list = list
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.trackName.text = list[position].trackName
        holder.artworkUrl.load(list[position].artworkUrl100.replace("100x100","400x400"))
        holder.director.text = list[position].primaryGenreName
        holder.rootView.setOnClickListener {
            if(list[position].trackId.toString().isNotEmpty()) {
                PreferenceRepository(context).setLastPosition(position)
                val intent = Intent(context, DetailsActivity::class.java)
                context.startActivity(intent)
            }
        }
        holder.rootView.background = Converter.getRandomDrawableColor()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_itunes_item_child,
                parent,
                false
            )
        )
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val trackName = v.textViewTrackName!!
        val artworkUrl = v.imageViewArtwork!!
        val director = v.textViewDirector!!
        val rootView = v.rootView!!
    }
}