package com.spatialapps.spaceweather.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SpaceWeatherRulesTest {
    @Test
    fun aqi_boundaries_map_to_expected_levels() {
        val samples =
            mapOf(
                0 to "优",
                50 to "优",
                51 to "良",
                100 to "良",
                101 to "轻度污染",
                150 to "轻度污染",
                151 to "中度污染",
                200 to "中度污染",
                201 to "重度污染",
                300 to "重度污染",
                301 to "严重污染",
            )
        samples.forEach { (aqi, expected) ->
            assertEquals(expected, SpaceWeatherRules.aqiBand(aqi).label)
        }
    }

    @Test
    fun weather_codes_cover_clear_cloud_rain_and_snow() {
        assertEquals(WeatherCondition.CLEAR, SpaceWeatherRules.conditionFor(0))
        assertEquals(WeatherCondition.CLOUDY, SpaceWeatherRules.conditionFor(3))
        assertEquals(WeatherCondition.RAIN, SpaceWeatherRules.conditionFor(61))
        assertEquals(WeatherCondition.SNOW, SpaceWeatherRules.conditionFor(75))
        assertEquals(WeatherCondition.RAIN, SpaceWeatherRules.conditionFor(95))
    }

    @Test
    fun city_shifting_wraps_in_both_directions() {
        assertEquals(0, SpaceWeatherRules.shiftedCityIndex(3, 1, 4))
        assertEquals(3, SpaceWeatherRules.shiftedCityIndex(0, -1, 4))
        assertEquals(1, SpaceWeatherRules.shiftedCityIndex(3, 2, 4))
    }

    @Test
    fun opacity_is_clamped_to_safe_ambient_range() {
        assertEquals(0.25f, SpaceWeatherRules.clampOpacity(0f))
        assertEquals(0.60f, SpaceWeatherRules.clampOpacity(0.60f))
        assertEquals(1f, SpaceWeatherRules.clampOpacity(1.5f))
    }

    @Test
    fun cache_freshness_uses_thirty_minute_boundary() {
        val fetched = 1_000L
        assertTrue(
            SpaceWeatherRules.isCacheFresh(
                fetchedAtEpochMillis = fetched,
                nowEpochMillis = fetched + SpaceWeatherRules.CACHE_TTL_MILLIS,
            )
        )
        assertFalse(
            SpaceWeatherRules.isCacheFresh(
                fetchedAtEpochMillis = fetched,
                nowEpochMillis = fetched + SpaceWeatherRules.CACHE_TTL_MILLIS + 1,
            )
        )
    }
}
