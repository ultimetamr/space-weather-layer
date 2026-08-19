package com.spatialapps.spaceweather.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pico.spatial.ui.design.Button
import com.pico.spatial.ui.design.PicoTheme
import com.pico.spatial.ui.design.Text
import com.pico.spatial.ui.foundation.hover.spatialHoverEffect
import com.pico.spatial.ui.platform.containers.LocalSpatialNavigator
import com.pico.spatial.ui.platform.containers.WindowContainerScope
import com.spatialapps.spaceweather.AQI_WINDOW_ID
import com.spatialapps.spaceweather.CONTROL_WINDOW_ID
import com.spatialapps.spaceweather.SINGLETON_WINDOW_TAG
import com.spatialapps.spaceweather.WEATHER_WINDOW_ID
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun WindowContainerScope.TimeWindow(store: SpaceWeatherStore) {
    val state by store.state.collectAsStateWithLifecycle()
    val navigator = LocalSpatialNavigator.current
    var isHovered by remember { mutableStateOf(false) }
    var now by remember { mutableStateOf(Instant.now()) }

    LaunchedEffect(Unit) {
        while (isActive) {
            now = Instant.now()
            delay((1_000L - System.currentTimeMillis() % 1_000L).coerceAtLeast(100L))
        }
    }
    LaunchedEffect(state.minimalMode) {
        if (state.minimalMode) {
            navigator.closeWindowContainer(WEATHER_WINDOW_ID, SINGLETON_WINDOW_TAG)
            navigator.closeWindowContainer(AQI_WINDOW_ID, SINGLETON_WINDOW_TAG)
        } else {
            navigator.openWindowContainer(WEATHER_WINDOW_ID, SINGLETON_WINDOW_TAG)
            navigator.openWindowContainer(AQI_WINDOW_ID, SINGLETON_WINDOW_TAG)
        }
    }
    LaunchedEffect(state.controlVisible) {
        if (state.controlVisible) {
            navigator.openWindowContainer(CONTROL_WINDOW_ID, SINGLETON_WINDOW_TAG)
        } else {
            navigator.closeWindowContainer(CONTROL_WINDOW_ID, SINGLETON_WINDOW_TAG)
        }
    }

    AmbientPanel(
        modifier =
            Modifier
                .windowConstraints(
                    minWidth = 520.dp,
                    maxWidth = 860.dp,
                    minHeight = 200.dp,
                    maxHeight = 300.dp,
                )
                .fillMaxSize()
                .padding(16.dp)
                .alpha(state.opacity),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .spatialHoverEffect()
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    when (awaitPointerEvent().type) {
                                        PointerEventType.Enter -> isHovered = true
                                        PointerEventType.Exit -> isHovered = false
                                        else -> Unit
                                    }
                                }
                            }
                        }
                        .animateContentSize()
                        .semantics {
                            contentDescription =
                                if (isHovered) "当前时间，包含秒数" else "当前时间，凝视显示秒数"
                        },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text =
                        DateTimeFormatter.ofPattern(
                                if (isHovered) "HH:mm:ss" else "HH:mm",
                                Locale.SIMPLIFIED_CHINESE,
                            )
                            .withZone(ZoneId.systemDefault())
                            .format(now),
                    color = PicoTheme.colorScheme.labelPrimaryLight,
                    style =
                        PicoTheme.typography.titleLarge.copy(
                            fontSize = if (isHovered) 54.sp else 50.sp,
                            fontWeight = FontWeight.Thin,
                            letterSpacing = 2.sp,
                        ),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text =
                        DateTimeFormatter.ofPattern("M月d日  EEEE", Locale.SIMPLIFIED_CHINESE)
                            .withZone(ZoneId.systemDefault())
                            .format(now),
                    color = PicoTheme.colorScheme.labelSecondary,
                    style =
                        PicoTheme.typography.bodyLarge.copy(
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Light,
                            letterSpacing = 1.sp,
                        ),
                )
            }
            Button(
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 12.dp),
                onClick = { store.setControlVisible(true) },
            ) {
                Text("控制")
            }
        }
    }
}
