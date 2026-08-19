package com.spatialapps.spaceweather.data

import com.spatialapps.spaceweather.domain.City
import com.spatialapps.spaceweather.domain.WeatherSnapshot

sealed interface WeatherLoadResult {
    data class Success(val snapshot: WeatherSnapshot) : WeatherLoadResult

    data class Failure(
        val fallback: WeatherSnapshot?,
        val message: String,
    ) : WeatherLoadResult
}

interface WeatherRepository {
    fun cached(city: City): WeatherSnapshot?

    suspend fun load(city: City, forceNetwork: Boolean = false): WeatherLoadResult
}
