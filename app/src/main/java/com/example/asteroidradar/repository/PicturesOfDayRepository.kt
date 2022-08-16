package com.example.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.asteroidradar.Constants
import com.example.asteroidradar.database.NasaDatabase
import com.example.asteroidradar.database.asDomainModel
import com.example.asteroidradar.domain.PictureOfDay
import com.example.asteroidradar.network.Network
import com.example.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class PicturesOfDayRepository(private val database: NasaDatabase) {
    val pictureOfDay: LiveData<PictureOfDay> =
        Transformations.map(database.pictureOfDayDao.getPictureOfDay()) {
            it?.asDomainModel()
        }

    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                val pictureOfDay =
                    Network.radarApi.getPictureOfDay(Constants.API_KEY).await()
                database.pictureOfDayDao.insertPictureOfDay(pictureOfDay.asDatabaseModel())
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}