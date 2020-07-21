package com.app.movies.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movie: List<Movie>)

    @Query("SELECT * FROM movie_table WHERE track_id = :trackId")
    fun getMovie(trackId : Int) : Movie

    @Query("SELECT * FROM movie_table")
    fun getAllMovies() : List<Movie>

    @Query("SELECT * FROM movie_table ORDER BY position ASC")
    fun getTopMovies() : List<Movie>

    @Query("SELECT * FROM movie_table WHERE track_name LIKE '%' || :term || '%'")
    fun searchMovies(term : String) : List<Movie>

    @Update
    suspend fun updateMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Query("DELETE FROM movie_table")
    suspend fun deleteAll()

}