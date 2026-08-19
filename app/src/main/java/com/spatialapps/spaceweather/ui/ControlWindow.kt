package com.spatialapps.spaceweather.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pico.spatial.ui.design.Button
import com.pico.spatial.ui.design.PicoTheme
import com.pico.spatial.ui.design.Slider
import com.pico.spatial.ui.design.Switch
import com.pico.spatial.ui.design.Text
import com.pico.spatial.ui.platform.containers.WindowContainerScope
import com.spatialapps.spaceweather.domain.SpaceWeatherRules
import kotlin.math.abs

@Composable
fun WindowContainerScope.ControlWindow(store: SpaceWeatherStore) {
    val state by store.state.collectAsStateWithLifecycle()
    ControlPanel(
        modifier =
            Modifier
                .windowConstraints(
                    minWidth = 640.dp,
                    maxWidth = 960.dp,
                    minHeight = 420.dp,
                    maxHeight = 620.dp,
                )
                .fillMaxSize()
                .padding(24.dp),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Header(state, onClose = { store.setControlVisible(false) })
            Spacer(modifier = Modifier.height(18.dp))
            SettingLabel("城市")
            CitySelector(
                cityName = state.city.name,
                onPrevious = { store.shiftCity(-1) },
                onNext = { store.shiftCity(1) },
            )
            Spacer(modifier = Modifier.height(16.dp))
            OpacitySetting(
                opacity = state.opacity,
                onOpacityChange = store::setOpacity,
            )
            Spacer(modifier = Modifier.height(12.dp))
            MinimalSetting(
                enabled = state.minimalMode,
                onEnabledChange = store::setMinimalMode,
            )
            Spacer(modifier = Modifier.height(14.dp))
            RefreshSetting(
                state = state,
                onRefresh = store::refresh,
            )
            Spacer(modifier = Modifier.height(14.dp))
            GestureStrip(
                onHorizontalSwipe = store::shiftCity,
                onVerticalSwipe = { delta ->
                    store.setOpacity(state.opacity + delta * 0.05f)
                },
            )
        }
    }
}

@Composable
private fun Header(
    state: SpaceWeatherState,
    onClose: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text = "空间时间气候层",
                color = PicoTheme.colorScheme.labelPrimaryLight,
                style =
                    PicoTheme.typography.titleLarge.copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Light,
                    ),
            )
            Text(
                text = state.message ?: "每 30 分钟自动更新",
                color = PicoTheme.colorScheme.labelTertiary,
                style = PicoTheme.typography.bodyLarge.copy(fontSize = 13.sp),
            )
        }
        Button(onClick = onClose) { Text("完成") }
    }
}

@Composable
private fun CitySelector(
    cityName: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Button(onClick = onPrevious) { Text("上一个") }
        Text(
            text = cityName,
            color = PicoTheme.colorScheme.labelPrimaryLight,
            style =
                PicoTheme.typography.titleLarge.copy(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Light,
                ),
        )
        Button(onClick = onNext) { Text("下一个") }
    }
}

@Composable
private fun OpacitySetting(
    opacity: Float,
    onOpacityChange: (Float) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        SettingLabel("信息层透明度")
        Text(
            text = "${(opacity * 100).toInt()}%",
            color = PicoTheme.colorScheme.labelSecondary,
            style = PicoTheme.typography.bodyLarge.copy(fontSize = 15.sp),
        )
    }
    Slider(
        value = opacity,
        onValueChange = onOpacityChange,
        valueRange = SpaceWeatherRules.MIN_OPACITY..SpaceWeatherRules.MAX_OPACITY,
    )
}

@Composable
private fun MinimalSetting(
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            SettingLabel("极简模式")
            Text(
                text = "仅保留时间窗口",
                color = PicoTheme.colorScheme.labelTertiary,
                style = PicoTheme.typography.bodyLarge.copy(fontSize = 12.sp),
            )
        }
        Switch(checked = enabled, onCheckedChange = onEnabledChange)
    }
}

@Composable
private fun RefreshSetting(
    state: SpaceWeatherState,
    onRefresh: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text =
                if (state.isRefreshing) {
                    "正在获取最新天气…"
                } else {
                    weatherSourceLabel(state)
                },
            color = PicoTheme.colorScheme.labelTertiary,
            style = PicoTheme.typography.bodyLarge.copy(fontSize = 13.sp),
        )
        Button(onClick = onRefresh, enabled = !state.isRefreshing) { Text("立即刷新") }
    }
}

@Composable
private fun GestureStrip(
    onHorizontalSwipe: (Int) -> Unit,
    onVerticalSwipe: (Int) -> Unit,
) {
    var dragDistance by remember { mutableStateOf(Offset.Zero) }
    val shape = RoundedCornerShape(18.dp)
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(68.dp)
                .clip(shape)
                .background(PicoTheme.colorScheme.fillTertiary.copy(alpha = 0.18f))
                .border(1.dp, PicoTheme.colorScheme.dividerLine, shape)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { dragDistance = Offset.Zero },
                        onDragEnd = {
                            if (abs(dragDistance.x) > abs(dragDistance.y) &&
                                abs(dragDistance.x) > 56f
                            ) {
                                onHorizontalSwipe(if (dragDistance.x < 0f) 1 else -1)
                            } else if (abs(dragDistance.y) > 56f) {
                                onVerticalSwipe(if (dragDistance.y < 0f) 1 else -1)
                            }
                            dragDistance = Offset.Zero
                        },
                        onDragCancel = { dragDistance = Offset.Zero },
                        onDrag = { change, amount ->
                            change.consume()
                            dragDistance += amount
                        },
                    )
                }
                .semantics {
                    contentDescription = "左右滑动切换城市，上下滑动调节透明度"
                },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "←  城市切换     ·     透明度调节  ↕",
            color = PicoTheme.colorScheme.labelTertiary,
            style =
                PicoTheme.typography.bodyLarge.copy(
                    fontSize = 14.sp,
                    letterSpacing = 1.sp,
                ),
        )
    }
}
