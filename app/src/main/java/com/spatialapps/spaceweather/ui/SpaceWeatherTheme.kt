package com.spatialapps.spaceweather.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pico.spatial.ui.design.PicoTheme
import com.pico.spatial.ui.design.Text
import com.spatialapps.spaceweather.domain.DataOrigin
import com.spatialapps.spaceweather.domain.SpaceWeatherRules
import com.spatialapps.spaceweather.domain.WeatherCondition
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

internal val PanelShape = RoundedCornerShape(28.dp)

@Composable
internal fun AmbientPanel(
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .clip(PanelShape)
                    .background(PicoTheme.colorScheme.fillPrimary.copy(alpha = 0.22f))
                    .border(1.dp, PicoTheme.colorScheme.dividerLine, PanelShape),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}

@Composable
internal fun ControlPanel(
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .clip(PanelShape)
                    .background(PicoTheme.colorScheme.fillPrimary.copy(alpha = 0.54f))
                    .border(1.dp, PicoTheme.colorScheme.dividerLine, PanelShape)
                    .padding(24.dp),
        ) {
            content()
        }
    }
}

@Composable
internal fun SettingLabel(text: String) {
    Text(
        text = text,
        color = PicoTheme.colorScheme.labelSecondary,
        style =
            PicoTheme.typography.bodyLarge.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.Light,
            ),
    )
}

@Composable
internal fun conditionColor(code: Int): Color =
    when (SpaceWeatherRules.conditionFor(code)) {
        WeatherCondition.CLEAR -> PicoTheme.colorScheme.alert
        WeatherCondition.CLOUDY -> PicoTheme.colorScheme.labelTertiary
        WeatherCondition.RAIN -> PicoTheme.colorScheme.interaction
        WeatherCondition.SNOW -> PicoTheme.colorScheme.labelPrimaryLight
    }

@Composable
internal fun aqiColor(aqi: Int): Color =
    when {
        aqi <= 50 -> PicoTheme.colorScheme.passable
        aqi <= 100 -> PicoTheme.colorScheme.labelSecondary
        aqi <= 150 -> PicoTheme.colorScheme.alert
        aqi <= 200 -> PicoTheme.colorScheme.error.copy(alpha = 0.80f)
        aqi <= 300 -> PicoTheme.colorScheme.error.copy(alpha = 0.90f)
        else -> PicoTheme.colorScheme.error
    }

internal fun weatherSourceLabel(state: SpaceWeatherState): String {
    val updated =
        DateTimeFormatter.ofPattern("HH:mm", Locale.SIMPLIFIED_CHINESE)
            .withZone(ZoneId.systemDefault())
            .format(Instant.ofEpochMilli(state.weather.fetchedAtEpochMillis))
    val source =
        when (state.weather.origin) {
            DataOrigin.NETWORK -> "实时"
            DataOrigin.CACHE -> "缓存"
            DataOrigin.DEMO -> "演示"
        }
    return "$source · $updated 更新"
}
