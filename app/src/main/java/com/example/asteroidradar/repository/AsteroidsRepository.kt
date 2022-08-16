package com.example.asteroidradar.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.asteroidradar.domain.Asteroid;
import com.example.asteroidradar.Constants;
import com.example.asteroidradar.Utils
import com.example.asteroidradar.API.parseAsteroidsJsonResult
import com.example.asteroidradar.database.NasaDatabase;
import com.example.asteroidradar.database.asDomainModel
import com.example.asteroidradar.network.Network;
import com.example.asteroidradar.network.asDatabaseModel

import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber;
import java.util.*

class AsteroidsRepository(private val database: NasaDatabase) {
    val today = Utils.convertDateStringToFormattedString(
        Calendar.getInstance().time,
        Constants.API_QUERY_DATE_FORMAT
    )
    val week = Utils.convertDateStringToFormattedString(
        Utils.addDaysToDate(Calendar.getInstance().time, 7),
        Constants.API_QUERY_DATE_FORMAT
    )

    val asteroidsSaved: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAllAsteroids()) {
            it.asDomainModel()
        }
    val asteroidsWeek: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getWeeklyAsteroids(today, week)) {
            it.asDomainModel()
        }
    val asteroidsToday: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getTodayAsteroids(today)) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroids = Network.radarApi.getAsteroids(Constants.API_KEY).await()
                database.asteroidDao.insertAll(*parseAsteroidsJsonResult(JSONObject(asteroids)).asDatabaseModel())
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}