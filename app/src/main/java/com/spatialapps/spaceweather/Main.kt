package com.spatialapps.spaceweather

import androidx.compose.ui.unit.dp
import com.pico.spatial.ui.design.PicoTheme
import com.pico.spatial.ui.foundation.dsl.DefaultWindowContainer
import com.pico.spatial.ui.foundation.dsl.Form
import com.pico.spatial.ui.foundation.dsl.Placement
import com.pico.spatial.ui.foundation.dsl.SpatialAppScope
import com.pico.spatial.ui.foundation.dsl.WindowContainer
import com.pico.spatial.ui.foundation.dsl.WindowContainerSize
import com.pico.spatial.ui.platform.resize.CaptionBarType
import com.pico.spatial.ui.platform.resize.ContainerResizeRestriction
import com.pico.spatial.ui.platform.resize.ContainerResizeType
import com.spatialapps.spaceweather.platform.SpatialApplication
import com.spatialapps.spaceweather.ui.AqiWindow
import com.spatialapps.spaceweather.ui.ControlWindow
import com.spatialapps.spaceweather.ui.TimeWindow
import com.spatialapps.spaceweather.ui.WeatherWindow

const val TIME_WINDOW_ID = "SpaceWeatherTime"
const val WEATHER_WINDOW_ID = "SpaceWeatherWeather"
const val AQI_WINDOW_ID = "SpaceWeatherAqi"
const val CONTROL_WINDOW_ID = "SpaceWeatherControl"
const val SINGLETON_WINDOW_TAG = "singleton"

fun mainApp(scope: SpatialAppScope) =
    with(scope) {
        val store = (context.applicationContext as SpatialApplication).store
        DefaultWindowContainer { PicoTheme { TimeWindow(store) } }

        WindowContainer(
            id = WEATHER_WINDOW_ID,
            form = Form.Planar,
            defaultSize = WindowContainerSize(width = 420.dp, height = 260.dp),
            resizeType = ContainerResizeType.ContentSize,
            defaultResizeRestriction = ContainerResizeRestriction.NonUniformResizable,
            defaultCaptionBarType = CaptionBarType.AutomaticHide,
            enableMaterialBackground = true,
            placement = {
                containers.firstOrNull { it.id == TIME_WINDOW_ID }
                    ?.let { Placement.left(it, 12.dp) } ?: Placement.none()
            },
        ) {
            PicoTheme { WeatherWindow(store) }
        }

        WindowContainer(
            id = AQI_WINDOW_ID,
            form = Form.Planar,
            defaultSize = WindowContainerSize(width = 360.dp, height = 260.dp),
            resizeType = ContainerResizeType.ContentSize,
            defaultResizeRestriction = ContainerResizeRestriction.NonUniformResizable,
            defaultCaptionBarType = CaptionBarType.AutomaticHide,
            enableMaterialBackground = true,
            placement = {
                containers.firstOrNull { it.id == TIME_WINDOW_ID }
                    ?.let { Placement.right(it, 12.dp) } ?: Placement.none()
            },
        ) {
            PicoTheme { AqiWindow(store) }
        }

        WindowContainer(
            id = CONTROL_WINDOW_ID,
            form = Form.Planar,
            defaultSize = WindowContainerSize(width = 760.dp, height = 520.dp),
            resizeType = ContainerResizeType.ContentSize,
            defaultResizeRestriction = ContainerResizeRestriction.NonUniformResizable,
            defaultCaptionBarType = CaptionBarType.AutomaticHide,
            enableMaterialBackground = true,
            placement = {
                containers.firstOrNull { it.id == TIME_WINDOW_ID }
                    ?.let { Placement.bottom(it, 40.dp) } ?: Placement.none()
            },
        ) {
            PicoTheme { ControlWindow(store) }
        }
    }
