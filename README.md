# 空间时间气候层

<p align="center">
  <img src="design/assets/space-weather-app-icon-source.png" width="160" alt="空间时间气候层应用图标">
</p>

> 将时间、实时天气、湿度与空气质量轻盈铺展在共享空间中的常驻环境信息层。

“空间时间气候层”是一款面向 PICO OS 6 Shared Space 的空间信息应用。应用使用多个 Planar `WindowContainer`，将时间、天气和空气质量分散显示在视野上方与两侧，以低饱和、半透明的视觉方式保持信息随时可见，同时尽量减少对现实环境和主要任务的遮挡。

## 基本信息

| 项目 | 内容 |
| --- | --- |
| 应用名称 | 空间时间气候层 |
| 包名 | `com.spatialapps.spaceweather` |
| 当前版本 | `1.1`（`versionCode 2`） |
| 应用类型 | PICO Shared Space 共享空间应用 |
| 窗口模型 | 多个 Planar `WindowContainer` |
| 最低系统 API | 35 |
| 目标系统 API | 35 |

## 主要功能

- 时间信息：每秒更新当前时间，并显示日期和星期。
- 凝视反馈：时间区域获得空间悬停焦点时放大，并显示秒数。
- 实时天气：显示温度、湿度以及晴、阴、雨、雪等天气状态。
- 空气质量：显示 US AQI 数值、质量等级和彩色圆形进度环。
- 多城市切换：内置北京、上海、深圳、成都四座城市。
- 透明度调节：整体信息层支持 `25%`—`100%` 透明度，默认值为 `60%`。
- 极简模式：关闭天气和 AQI 窗口，仅保留时间窗口。
- 自动刷新：WorkManager 在联网条件下每 30 分钟刷新一次天气数据。
- 本地容错：优先使用 30 分钟有效期的本地缓存；网络不可用且无缓存时显示演示数据，应用不会因请求失败而崩溃。

## 空间布局与交互

应用将信息拆分为四个相互独立的窗口：

1. `SpaceWeatherTime`：时间、日期、星期和控制入口。
2. `SpaceWeatherWeather`：城市、天气图标、温度和湿度。
3. `SpaceWeatherAqi`：AQI 数值、等级和圆形进度环。
4. `SpaceWeatherControl`：城市、透明度、极简模式和立即刷新控制。

控制面板支持按钮、滑杆以及拖动手势：

- 左右滑动：切换城市。
- 上下滑动：调整信息层透明度。
- 凝视/空间悬停时间区域：显示秒数。
- 控制器、触摸或指针：可完成与手势等价的主要操作。

> 当前 PICO Shared Space 不向应用开放原始手部关节跟踪。本项目已通过 `HandInput` 保留“手掌向上唤出控制面板”的能力边界，但 `SharedSpaceHandInput` 当前标记为不可用；现阶段请使用时间窗口中的“控制”按钮打开面板。

## 数据来源

天气与空气质量数据来自无需 API Key 的 [Open-Meteo](https://open-meteo.com/) 公共接口：

- Forecast API：温度、相对湿度、天气代码。
- Air Quality API：US AQI 指数。

应用仅请求联网和网络状态权限。城市选择、透明度、极简模式及天气缓存存储在应用本地 `SharedPreferences` 中。

## 技术栈

- Kotlin `2.1.20`
- PICO Spatial SDK BOM `6.0.0`
- SpatialUI Compose + `PicoTheme`
- Android Gradle Plugin `8.13.2`
- WorkManager `2.9.1`
- Kotlin Coroutines
- Open-Meteo Forecast / Air Quality API

所有二维界面均使用 SpatialUI 组件并由 `PicoTheme` 包裹。项目未使用 Material 或 Material3，根界面保留 PICO 系统窗口玻璃材质。

## 项目结构

```text
app/src/main/java/com/spatialapps/spaceweather/
├── Main.kt                  # 窗口注册与空间布局
├── data/                    # Open-Meteo 请求与本地缓存
├── domain/                  # 城市、天气、AQI 与业务规则
├── input/                   # 手部输入能力边界
├── platform/                # Spatial Application 与启动 Activity
├── ui/                      # 时间、天气、AQI 和控制窗口
└── work/                    # 30 分钟周期刷新任务

design/
├── assets/                  # 应用图标源文件
├── preview.html             # 可交互 Web 视觉预览
└── review/                  # 产品、交互、视觉与验收文档
```

## 构建

### 环境要求

- Windows、macOS 或 Linux
- JDK 11
- Android SDK 35
- 可访问项目 Gradle 仓库
- 用于运行验证的 PICO OS 6 模拟器或兼容设备

### 执行测试并生成 Debug APK

Windows PowerShell：

```powershell
.\gradlew.bat :app:testDebugUnitTest
.\gradlew.bat :app:assembleDebug
```

macOS / Linux：

```bash
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
```

生成文件位于：

```text
app/build/outputs/apk/debug/app-debug.apk
```

安装前请确认本机 PICO Spatial 开发环境和目标模拟器/设备已就绪，再使用当前环境支持的 `pico-cli` 或 Android 调试工具完成安装与启动。

## 设计与验证资料

- [设计索引](design/review/index.md)
- [产品需求](design/review/pm-requirement-spec.md)
- [空间交互规范](design/review/interaction-spatial-spec.md)
- [视觉系统规范](design/review/visual-system-spec.md)
- [预览 QA 报告](design/review/preview-qa-report.md)
- [浏览器视觉预览](design/preview.html)

## 注意事项

- WorkManager 的 30 分钟周期是系统调度目标，实际执行时刻可能受系统节能策略影响。
- AQI 使用 Open-Meteo 返回的 US AQI 口径。
- 真机空间位置、凝视焦点和性能表现仍应在目标 PICO 设备上进行最终确认。

