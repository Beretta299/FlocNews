package com.karasm.flocnews.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.karasm.flocnews.models.CityModel
import com.karasm.flocnews.models.CountryModel
import com.karasm.flocnews.models.WeatherModel

@Database(entities = arrayOf(CityModel::class,CountryModel::class), version = 2)
abstract class LocalStorage : RoomDatabase() {

    abstract fun localStorageDataDao(): LocalStorageDao

    companion object {
        private var INSTANCE: LocalStorage? = null

        fun getInstance(context: Context): LocalStorage? {
            if (INSTANCE == null) {
                synchronized(LocalStorage::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        LocalStorage::class.java, "local_storage.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}