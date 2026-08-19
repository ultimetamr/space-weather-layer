package com.spatialapps.spaceweather.ui

import android.content.Context
import com.spatialapps.spaceweather.data.WeatherLoadResult
import com.spatialapps.spaceweather.data.WeatherRepository
import com.spatialapps.spaceweather.domain.Cities
import com.spatialapps.spaceweather.domain.City
import com.spatialapps.spaceweather.domain.DataOrigin
import com.spatialapps.spaceweather.domain.SpaceWeatherRules
import com.spatialapps.spaceweather.domain.WeatherSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class SpaceWeatherState(
    val cityIndex: Int,
    val opacity: Float,
    val minimalMode: Boolean,
    val controlVisible: Boolean,
    val weather: WeatherSnapshot,
    val isRefreshing: Boolean,
    val message: String?,
) {
    val city: City
        get() = Cities.all[cityIndex]
}

class SpaceWeatherStore(
    context: Context,
    private val repository: WeatherRepository,
) {
    private val preferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val refreshMutex = Mutex()

    private val initialCityIndex = Cities.indexOf(preferences.getString(KEY_CITY, null) ?: "")
    private val initialCity = Cities.all[initialCityIndex]
    private val initialSnapshot = repository.cached(initialCity) ?: demoSnapshot(initialCity)

    private val mutableState =
        MutableStateFlow(
            SpaceWeatherState(
                cityIndex = initialCityIndex,
                opacity =
                    SpaceWeatherRules.clampOpacity(
                        preferences.getFloat(KEY_OPACITY, DEFAULT_OPACITY)
                    ),
                minimalMode = preferences.getBoolean(KEY_MINIMAL, false),
                controlVisible = false,
                weather = initialSnapshot,
                isRefreshing = false,
                message = null,
            )
        )
    val state: StateFlow<SpaceWeatherState> = mutableState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        scope.launch { refreshNow() }
    }

    suspend fun refreshNow(): Boolean =
        refreshMutex.withLock {
            mutableState.update { it.copy(isRefreshing = true, message = null) }
            val city = mutableState.value.city
            when (val result = repository.load(city, forceNetwork = true)) {
                is WeatherLoadResult.Success -> {
                    if (result.snapshot.cityId == mutableState.value.city.id) {
                        mutableState.update {
                            it.copy(
                                weather = result.snapshot,
                                isRefreshing = false,
                                message = null,
                            )
                        }
                    }
                    true
                }

                is WeatherLoadResult.Failure -> {
                    val fallback = result.fallback ?: demoSnapshot(city)
                    if (city.id == mutableState.value.city.id) {
                        mutableState.update {
                            it.copy(
                                weather = fallback,
                                isRefreshing = false,
                                message =
                                    if (fallback.origin == DataOrigin.DEMO) {
                                        "网络不可用，显示演示数据"
                                    } else {
                                        "网络不可用，显示本地缓存"
                                    },
                            )
                        }
                    }
                    false
                }
            }
        }

    fun shiftCity(delta: Int) {
        val nextIndex =
            SpaceWeatherRules.shiftedCityIndex(
                current = mutableState.value.cityIndex,
                delta = delta,
                count = Cities.all.size,
            )
        selectCity(nextIndex)
    }

    fun selectCity(index: Int) {
        val safeIndex = index.coerceIn(Cities.all.indices)
        val city = Cities.all[safeIndex]
        val snapshot = repository.cached(city) ?: demoSnapshot(city)
        preferences.edit().putString(KEY_CITY, city.id).apply()
        mutableState.update {
            it.copy(
                cityIndex = safeIndex,
                weather = snapshot,
                message = null,
            )
        }
        refresh()
    }

    fun setOpacity(value: Float) {
        val safe = SpaceWeatherRules.clampOpacity(value)
        preferences.edit().putFloat(KEY_OPACITY, safe).apply()
        mutableState.update { it.copy(opacity = safe) }
    }

    fun setMinimalMode(enabled: Boolean) {
        preferences.edit().putBoolean(KEY_MINIMAL, enabled).apply()
        mutableState.update { it.copy(minimalMode = enabled) }
    }

    fun setControlVisible(visible: Boolean) {
        mutableState.update { it.copy(controlVisible = visible) }
    }

    companion object {
        private const val PREFERENCES_NAME = "space_weather_settings"
        private const val KEY_CITY = "city"
        private const val KEY_OPACITY = "opacity"
        private const val KEY_MINIMAL = "minimal"
        private const val DEFAULT_OPACITY = 0.60f

        fun demoSnapshot(city: City): WeatherSnapshot {
            val seed = Cities.indexOf(city.id)
            return WeatherSnapshot(
                cityId = city.id,
                temperatureC = 22.0 + seed,
                humidityPercent = 48 + seed * 3,
                weatherCode = if (seed % 2 == 0) 1 else 3,
                aqi = 42 + seed * 12,
                fetchedAtEpochMillis = System.currentTimeMillis(),
                origin = DataOrigin.DEMO,
            )
        }
    }
}
