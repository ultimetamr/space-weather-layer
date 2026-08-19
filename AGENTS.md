# SpaceWeatherLayer project guidance

This directory is the isolated workspace for the PICO OS 6 Shared Space app “空间时间气候层”.

- Application label: `空间时间气候层`; version `1.1` (`versionCode 2`).
- Launcher icon source: `design/assets/space-weather-app-icon-source.png`; packaged raster resources are `ic_spatial_launcher.png` and `ic_launcher_foreground.png`.
- Package/namespace: `com.spatialapps.spaceweather`.
- Use a planar `WindowContainer` architecture with SpatialUI Compose only.
- Wrap every 2D UI surface in `PicoTheme`; Material and Material3 are forbidden.
- Keep the system window glass visible; do not paint an opaque root background.
- Keep all source, design artifacts, build outputs, screenshots, and project-local caches inside this directory.
- Prefer gaze + pinch and provide equivalent controller/touch fallback paths.
- Weather networking must degrade to cached/demo data without crashing.
- Time updates once per second; weather refreshes every 30 minutes through WorkManager.
- Validate unit tests, debug assembly, emulator launch, screenshot evidence, and runtime crash logs before handoff.

