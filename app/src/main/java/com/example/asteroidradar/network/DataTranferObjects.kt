package com.example.asteroidradar.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.example.asteroidradar.database.DatabaseAsteroid
import com.example.asteroidradar.domain.Asteroid
import com.example.asteroidradar.database.DatabasePictureOfDay

@JsonClass(generateAdapter = true)
data class NetworkPictureOfDay(
    val url : String,
    val date : String,
    @Json(name = "media_type") val mediaType : String,
    val title : String)

fun NetworkPictureOfDay.asDatabaseModel(): DatabasePictureOfDay {
    return DatabasePictureOfDay(
        url,
        date,
        mediaType,
        title
    )
}

fun List<Asteroid>.asDatabaseModel(): Array<DatabaseAsteroid> {
    return map {
        DatabaseAsteroid(
            it.id,
            it.codename,
            it.closeApproachDate,
            it.absoluteMagnitude,
            it.estimatedDiameter,
            it.relativeVelocity,
            it.distanceFromEarth,
            it.isPotentiallyHazardous)
    }.toTypedArray()
}