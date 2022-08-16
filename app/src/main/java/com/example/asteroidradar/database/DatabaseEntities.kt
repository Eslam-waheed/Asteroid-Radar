package com.example.asteroidradar.database

import androidx.room.*
import com.example.asteroidradar.domain.Asteroid
import com.example.asteroidradar.domain.PictureOfDay

@Entity
data class DatabasePictureOfDay constructor(
    @PrimaryKey
    val url: String,
    val date: String,
    val mediaType: String,
    val title: String
)

fun DatabasePictureOfDay.asDomainModel(): PictureOfDay {
    return PictureOfDay(url, mediaType, title)
}

@Entity
data class DatabaseAsteroid constructor(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            it.id, it.codename, it.closeApproachDate, it.absoluteMagnitude,
            it.estimatedDiameter, it.relativeVelocity, it.distanceFromEarth,
            it.isPotentiallyHazardous)
    }
}