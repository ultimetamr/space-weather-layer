package com.spatialapps.spaceweather.domain

import kotlin.math.max
import kotlin.math.min

enum class WeatherCondition(val label: String, val symbol: String) {
    CLEAR("晴", "☀"),
    CLOUDY("阴", "☁"),
    RAIN("雨", "◔"),
    SNOW("雪", "✣"),
}

data class City(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
)

object Cities {
    val all =
        listOf(
            City("beijing", "北京", 39.9042, 116.4074),
            City("shanghai", "上海", 31.2304, 121.4737),
            City("shenzhen", "深圳", 22.5431, 114.0579),
            City("chengdu", "成都", 30.5728, 104.0668),
        )

    fun indexOf(id: String): Int = all.indexOfFirst { it.id == id }.takeIf { it >= 0 } ?: 0
}

enum class DataOrigin {
    NETWORK,
    CACHE,
    DEMO,
}

data class WeatherSnapshot(
    val cityId: String,
    val temperatureC: Double,
    val humidityPercent: Int,
    val weatherCode: Int,
    val aqi: Int,
    val fetchedAtEpochMillis: Long,
    val origin: DataOrigin,
) {
    val condition: WeatherCondition
        get() = SpaceWeatherRules.conditionFor(weatherCode)
}

data class AqiBand(val label: String, val fraction: Float)

object SpaceWeatherRules {
    const val MIN_OPACITY = 0.25f
    const val MAX_OPACITY = 1f
    const val CACHE_TTL_MILLIS = 30L * 60L * 1000L

    fun conditionFor(code: Int): WeatherCondition =
        when (code) {
            0 -> WeatherCondition.CLEAR
            in 1..48 -> WeatherCondition.CLOUDY
            in 51..67, in 80..82, in 95..99 -> WeatherCondition.RAIN
            in 71..77, in 85..86 -> WeatherCondition.SNOW
            else -> WeatherCondition.CLOUDY
        }

    fun aqiBand(aqi: Int): AqiBand {
        val safe = max(0, aqi)
        val label =
            when {
                safe <= 50 -> "优"
                safe <= 100 -> "良"
                safe <= 150 -> "轻度污染"
                safe <= 200 -> "中度污染"
                safe <= 300 -> "重度污染"
                else -> "严重污染"
            }
        return AqiBand(label, min(safe / 300f, 1f))
    }

    fun clampOpacity(value: Float): Float = value.coerceIn(MIN_OPACITY, MAX_OPACITY)

    fun shiftedCityIndex(current: Int, delta: Int, count: Int): Int {
        if (count <= 0) return 0
        return ((current + delta) % count + count) % count
    }

    fun isCacheFresh(
        fetchedAtEpochMillis: Long,
        nowEpochMillis: Long,
        ttlMillis: Long = CACHE_TTL_MILLIS,
    ): Boolean = fetchedAtEpochMillis > 0 && nowEpochMillis - fetchedAtEpochMillis <= ttlMillis
}
