package com.app.movies.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
data class Movie (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="track_id")
    var trackId: Int,

    @ColumnInfo(name ="track_name")
    var trackName: String,

    @ColumnInfo(name ="artwork_url")
    var artworkUrl100: String,

    @ColumnInfo(name ="long_description")
    var longDescription: String,

    @ColumnInfo(name ="primary_genre_name")
    var primaryGenreName: String,

    @ColumnInfo(name ="artist_name")
    var artistName: String,

    @ColumnInfo(name ="position")
    var position: Int
)