package com.spatialapps.spaceweather.platform

import android.app.Application
import com.pico.spatial.ui.foundation.dsl.launch
import com.spatialapps.spaceweather.data.OpenMeteoWeatherRepository
import com.spatialapps.spaceweather.data.WeatherRepository
import com.spatialapps.spaceweather.mainApp
import com.spatialapps.spaceweather.ui.SpaceWeatherStore
import com.spatialapps.spaceweather.work.WeatherRefreshWorker

class SpatialApplication : Application() {
    lateinit var repository: WeatherRepository
        private set

    lateinit var store: SpaceWeatherStore
        private set

    override fun onCreate() {
        super.onCreate()
        repository = OpenMeteoWeatherRepository(this)
        store = SpaceWeatherStore(this, repository)
        WeatherRefreshWorker.schedule(this)
        launch(::mainApp)
    }
}
