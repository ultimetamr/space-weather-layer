package com.spatialapps.spaceweather.data

import android.content.Context
import com.spatialapps.spaceweather.domain.City
import com.spatialapps.spaceweather.domain.DataOrigin
import com.spatialapps.spaceweather.domain.SpaceWeatherRules
import com.spatialapps.spaceweather.domain.WeatherSnapshot
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class OpenMeteoWeatherRepository(context: Context) : WeatherRepository {
    private val preferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun cached(city: City): WeatherSnapshot? =
        preferences.getString(cacheKey(city), null)?.let { encoded ->
            runCatching { decodeSnapshot(encoded).copy(origin = DataOrigin.CACHE) }.getOrNull()
        }

    override suspend fun load(city: City, forceNetwork: Boolean): WeatherLoadResult =
        withContext(Dispatchers.IO) {
            val cached = cached(city)
            val now = System.currentTimeMillis()
            if (!forceNetwork &&
                cached != null &&
                SpaceWeatherRules.isCacheFresh(cached.fetchedAtEpochMillis, now)
            ) {
                return@withContext WeatherLoadResult.Success(cached)
            }

            try {
                val forecastUrl =
                    "https://api.open-meteo.com/v1/forecast" +
                        "?latitude=${city.latitude}&longitude=${city.longitude}" +
                        "&current=temperature_2m,relative_humidity_2m,weather_code" +
                        "&timezone=auto"
                val airUrl =
                    "https://air-quality-api.open-meteo.com/v1/air-quality" +
                        "?latitude=${city.latitude}&longitude=${city.longitude}" +
                        "&current=us_aqi&timezone=auto"

                val forecastCurrent = fetchJson(forecastUrl).getJSONObject("current")
                val airCurrent = fetchJson(airUrl).getJSONObject("current")
                val snapshot =
                    WeatherSnapshot(
                        cityId = city.id,
                        temperatureC = forecastCurrent.getDouble("temperature_2m"),
                        humidityPercent = forecastCurrent.getInt("relative_humidity_2m"),
                        weatherCode = forecastCurrent.getInt("weather_code"),
                        aqi = airCurrent.optDouble("us_aqi", 0.0).toInt().coerceAtLeast(0),
                        fetchedAtEpochMillis = now,
                        origin = DataOrigin.NETWORK,
                    )
                preferences.edit().putString(cacheKey(city), encodeSnapshot(snapshot)).apply()
                WeatherLoadResult.Success(snapshot)
            } catch (error: Exception) {
                WeatherLoadResult.Failure(
                    fallback = cached,
                    message = error.message ?: "天气服务暂时不可用",
                )
            }
        }

    private fun fetchJson(address: String): JSONObject {
        val connection =
            (URL(address).openConnection() as HttpsURLConnection).apply {
                connectTimeout = NETWORK_TIMEOUT_MILLIS
                readTimeout = NETWORK_TIMEOUT_MILLIS
                requestMethod = "GET"
                setRequestProperty("Accept", "application/json")
            }
        return try {
            val status = connection.responseCode
            if (status !in 200..299) throw IOException("天气服务返回 $status")
            connection.inputStream.bufferedReader(Charsets.UTF_8).use { reader ->
                JSONObject(reader.readText())
            }
        } finally {
            connection.disconnect()
        }
    }

    private fun cacheKey(city: City): String = "weather_${city.id}"

    private fun encodeSnapshot(snapshot: WeatherSnapshot): String =
        JSONObject()
            .put("cityId", snapshot.cityId)
            .put("temperatureC", snapshot.temperatureC)
            .put("humidityPercent", snapshot.humidityPercent)
            .put("weatherCode", snapshot.weatherCode)
            .put("aqi", snapshot.aqi)
            .put("fetchedAtEpochMillis", snapshot.fetchedAtEpochMillis)
            .toString()

    private fun decodeSnapshot(encoded: String): WeatherSnapshot {
        val json = JSONObject(encoded)
        return WeatherSnapshot(
            cityId = json.getString("cityId"),
            temperatureC = json.getDouble("temperatureC"),
            humidityPercent = json.getInt("humidityPercent"),
            weatherCode = json.getInt("weatherCode"),
            aqi = json.getInt("aqi"),
            fetchedAtEpochMillis = json.getLong("fetchedAtEpochMillis"),
            origin = DataOrigin.CACHE,
        )
    }

    companion object {
        private const val PREFERENCES_NAME = "space_weather_cache"
        private const val NETWORK_TIMEOUT_MILLIS = 10_000
    }
}
