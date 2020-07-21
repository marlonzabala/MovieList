package com.app.movies.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Movie::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract val movieDAO : MovieDAO
    companion object {
        @Volatile
        private var INSTANCE : MovieDatabase? = null
            fun getInstance(context : Context) : MovieDatabase{
                var instance : MovieDatabase? = INSTANCE
                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java,
                        "movie_data_database"
                    ).build()
                }
                return instance
            }
    }
}