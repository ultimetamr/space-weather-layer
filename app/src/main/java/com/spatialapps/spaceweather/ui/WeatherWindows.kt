package com.spatialapps.spaceweather.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pico.spatial.ui.design.PicoTheme
import com.pico.spatial.ui.design.Text
import com.pico.spatial.ui.platform.containers.WindowContainerScope
import com.spatialapps.spaceweather.domain.SpaceWeatherRules
import java.util.Locale

@Composable
fun WindowContainerScope.WeatherWindow(store: SpaceWeatherStore) {
    val state by store.state.collectAsStateWithLifecycle()
    val snapshot = state.weather
    AmbientPanel(
        modifier =
            Modifier
                .windowConstraints(
                    minWidth = 420.dp,
                    maxWidth = 720.dp,
                    minHeight = 220.dp,
                    maxHeight = 340.dp,
                )
                .fillMaxSize()
                .padding(16.dp)
                .alpha(state.opacity),
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp, vertical = 22.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = state.city.name,
                    color = PicoTheme.colorScheme.labelTertiary,
                    style =
                        PicoTheme.typography.bodyLarge.copy(
                            fontSize = 15.sp,
                            letterSpacing = 2.sp,
                        ),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = snapshot.condition.symbol,
                        color = conditionColor(snapshot.weatherCode),
                        style = PicoTheme.typography.titleLarge.copy(fontSize = 45.sp),
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = String.format(Locale.US, "%.0f°", snapshot.temperatureC),
                            color = PicoTheme.colorScheme.labelPrimaryLight,
                            style =
                                PicoTheme.typography.titleLarge.copy(
                                    fontSize = 46.sp,
                                    fontWeight = FontWeight.Thin,
                                ),
                        )
                        Text(
                            text = snapshot.condition.label,
                            color = PicoTheme.colorScheme.labelSecondary,
                            style = PicoTheme.typography.bodyLarge.copy(fontSize = 17.sp),
                        )
                    }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "湿度",
                    color = PicoTheme.colorScheme.labelTertiary,
                    style = PicoTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                )
                Text(
                    text = "${snapshot.humidityPercent}%",
                    color = PicoTheme.colorScheme.labelPrimaryLight,
                    style =
                        PicoTheme.typography.titleLarge.copy(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Light,
                        ),
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = weatherSourceLabel(state),
                    color = PicoTheme.colorScheme.labelTertiary,
                    style = PicoTheme.typography.bodyLarge.copy(fontSize = 12.sp),
                )
            }
        }
    }
}

@Composable
fun WindowContainerScope.AqiWindow(store: SpaceWeatherStore) {
    val state by store.state.collectAsStateWithLifecycle()
    val aqi = state.weather.aqi
    val band = SpaceWeatherRules.aqiBand(aqi)
    val color = aqiColor(aqi)
    val trackColor = PicoTheme.colorScheme.dividerLine.copy(alpha = 0.35f)

    AmbientPanel(
        modifier =
            Modifier
                .windowConstraints(
                    minWidth = 360.dp,
                    maxWidth = 580.dp,
                    minHeight = 220.dp,
                    maxHeight = 320.dp,
                )
                .fillMaxSize()
                .padding(16.dp)
                .alpha(state.opacity),
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp, vertical = 22.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(modifier = Modifier.size(132.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = trackColor,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 9.dp.toPx(), cap = StrokeCap.Round),
                    )
                    drawArc(
                        color = color,
                        startAngle = -90f,
                        sweepAngle = 360f * band.fraction,
                        useCenter = false,
                        style = Stroke(width = 9.dp.toPx(), cap = StrokeCap.Round),
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = aqi.toString(),
                        color = PicoTheme.colorScheme.labelPrimaryLight,
                        style =
                            PicoTheme.typography.titleLarge.copy(
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Thin,
                            ),
                    )
                    Text(
                        text = "AQI",
                        color = PicoTheme.colorScheme.labelTertiary,
                        style = PicoTheme.typography.bodyLarge.copy(fontSize = 12.sp),
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "空气质量",
                    color = PicoTheme.colorScheme.labelTertiary,
                    style =
                        PicoTheme.typography.bodyLarge.copy(
                            fontSize = 14.sp,
                            letterSpacing = 1.sp,
                        ),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = band.label,
                    color = color,
                    style =
                        PicoTheme.typography.titleLarge.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Light,
                        ),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.city.name,
                    color = PicoTheme.colorScheme.labelSecondary,
                    style = PicoTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                )
            }
        }
    }
}
