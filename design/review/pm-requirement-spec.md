# Spatial App Requirement Spec · 空间时间气候层

> Role: `product_strategist` | Workflow stage(s): `intent_draft` → `quality_contract_freeze` | Upstream inputs: user's raw requirements, `research_analyst` evidence/domain model | Downstream recipients: UXR, Interaction / Spatial Designer, Design Lead
>
> This document carries this role's **LLM reasoning information** and **direct description of outputs**. It is not bound to any JSON Schema or validator error codes; mandatory gates are expressed through this document's structured Markdown required tables, evidence anchors, and the `block` status.

## 0. Reasoning Guidance (how this role reasons)

- **Make decisions only at the product-outcome layer**: the outcomes the user must achieve, success criteria, and risk boundaries. Do not overstep into deciding layout, components, or visual direction, or substitute for human approval.
- **Requirement → intent extraction**: from the user's natural language, identify domain, sub-domain, target users, use scenarios, posture, frequency, duration, core tasks, key decisions, risks, data, AI, sensors, permissions, and collaboration.
- **A gap is an assumption, no implicit guessing**: write any unknown information into the "assumptions list" with `confidence / impact / validation plan`; do not present it as a factual statement.
- **The quality contract is derived from this requirement**, not copied from historical projects or templates. The contract must be usable by downstream roles as an acceptance anchor and starting point for traceability.
- **Prohibitions**: bypassing upstream stages; presenting the project's own derivations as PICO official hard rules; manufacturing a "sense of space" by adding floating windows; hiding assumptions, error states, or failure paths.

## 1. Direct Description of Outputs

This role delivers two facts: the **intent definition** (draft → frozen) and the **quality contract**. The sections below are the structured descriptions of these two outputs; filling in every item completely constitutes a complete delivery.

## 2. Background and Problem (intent definition · foundation)

- **One-sentence requirement description**: 在 PICO OS 6 Shared Space 中，将时间、日期、天气、湿度与 AQI 以低干扰、可分散放置的透明信息层长期呈现在用户视野上方和两侧，让用户无需离开当前活动即可抬眼获取环境信息，并能快速切换城市、调节透明度或进入仅时间模式。
- **Target users**: 长时间佩戴 PICO 头显在家中工作、娱乐或处理家务，希望持续掌握时间与环境信息、但不希望被传统仪表盘遮挡的普通用户；次要用户是演示和无网络环境下需要稳定样例数据的开发与验收人员。
- **Use scenarios**: 家庭 Shared Space 内的常驻信息查看；抬眼快速确认时间或天气；短暂唤出控制面板切换城市、调节透明度、刷新数据和切换极简模式；网络失败时查看缓存及数据新鲜度。
- **Wearing posture**: 以坐姿和站姿为主，允许缓慢走动；不要求用户移动头部追随动画，也不移动相机。
- **Frequency and duration**: 信息层在应用存续期间常驻，时间每秒更新；典型查看为 1–3 秒，控制面板操作为 5–15 秒，天气后台每 30 分钟刷新一次。
- **Preliminary judgment of spatial necessity**: 核心价值来自“环境信息与当前空间活动并存”以及按视野方向分布的可扫视信息，而不是把二维天气页搬入 XR；Shared Space 让用户保留真实环境和其他窗口，同时以多个 Planar 信息窗形成低干扰的环绕式 glance layer。

## 3. Key Moment (the linchpin of spatial value)

- **The moment a screen cannot achieve**: 用户正在看房间中的人、物或另一应用时，只需抬眼便看到正上方时间，轻扫两侧即可读到天气与 AQI；无需切换应用、遮挡中心视野或转入沉浸场景。
- **Placement on the immersion spectrum**: 全部核心功能停留在 Shared Space 的 Planar WindowContainer 层；不需要 Volumetric 内容或 Full Space Stage。控制面板是临时 Planar 辅助窗，信息窗是常驻低透明度 Planar 窗。
- **Entry path**: 从 Shared Space Planar 信息层启动，默认显示时间、天气和 AQI；用户通过手掌向上语义入口或等价按钮唤出控制面板，可选择极简模式，但不会自动进入沉浸状态。

## 4. Product Research (baseline anchors)

| Dimension | Content | Source |
|---|---|---|
| Competitor feature matrix | Apple Weather widgets cover at-a-glance conditions and location changes; Windows Widgets cover glance/hover expansion; Apple Weather app covers multi-location depth. All are 2D and do not provide spatial distribution. | `uxr-research-report.md` §3; official Apple/Microsoft links therein |
| Decision duration baseline | Product targets: 1–3 s glance and ≤15 s adjustment. No measured XR sample exists, so these remain validation targets rather than research facts. | `uxr-research-report.md` §9 |
| Industry safety · comfort conventions | No camera motion or high-motion content; preserve central sight line; color is never the only AQI signal; device comfort and CPU claims remain device-measurement items. | `uxr-research-report.md` §§8–10; PICO workflow validation boundary |

## 5. Intent Definition (frozen items)

- **Domain / sub-domain**: 环境信息与时间感知 / 空间化 glanceable ambient display。
- **Risk level**: 低；天气与 AQI 为一般信息，不用于医疗、灾害预警或安全关键决策。
- **Default space**: Shared Space。
- **Core scenario list**: 常驻扫视时间；扫视天气、湿度与 AQI；凝视时间查看秒数；唤出控制面板；切换城市；调节整体透明度；切换极简模式；手动刷新；断网读取缓存/演示回退。
- **Data / AI / sensors / permissions**: 使用公开天气与空气质量 HTTPS API、网络权限、本地缓存与 WorkManager；使用平台可用的 gaze/hover 和手势输入语义，不使用相机画面、定位、麦克风或 AI 推理，也不请求精确位置权限。
- **Collaboration**: 不包含多用户同步；Shared Space 仅指与现实环境及其他空间窗口共存。

## 6. Assumptions List (missing information, must not be treated as fact)

| # | Assumption | Confidence | Impact | Validation Plan |
|---|---|---|---|---|
| 1 | 首发城市预置为北京、上海、深圳、成都，用户不输入任意城市。 | medium | 决定城市模型、控制面板与 API 坐标表。 | 单元测试城市轮换并在验收说明中列出预置城市；后续可替换为搜索模块。 |
| 2 | “手掌向上”使用可注入的 `HandInput` 边界表达；当前模拟器若不提供稳定 palm-up 事件，则以控制面板按钮/键盘或控制器等价入口完成运行验收。 | high | 真机手势能力与模拟器演示路径不同。 | 记录真机待验项；模拟器验证所有等价状态转换。 |
| 3 | “凝视放大秒数”以 SpatialUI hover/focus 状态作为 gaze 等价信号。 | high | 避免依赖未确认的私有眼动 API。 | 模拟器指针/控制器 hover 验证秒数显隐和放大；真机 gaze 再验。 |
| 4 | 默认整体透明度为 0.60，可调范围 0.25–1.00，极简模式只保留时间窗。 | high | 影响可读性、无障碍与视觉干扰。 | 预览和模拟器分别检查最小、默认、最大三个状态。 |
| 5 | 公开数据源采用无需密钥的 Open-Meteo 天气与空气质量接口；无网或接口失败时显示持久缓存，首次离线使用明确标记的演示数据。 | medium | 决定数据字段、缓存与错误状态。 | 仓库测试覆盖成功、缓存、过期与首次离线；实现前核对官方 API 文档。 |
| 6 | CPU < 5% 只能在目标 PICO 设备和指定测量窗口上成立；模拟器结果不作为该指标的最终证明。 | high | 影响验收口径。 | 代码侧限制每秒一次时间更新、30 分钟网络任务且无逐帧循环；真机用 perf/Perfetto 复验。 |

## 7. Quality Contract (acceptance criteria)

> Derived from this requirement, serving as the anchor for the Design Critic and the traceability table.

- **Required business outcomes**: (1) current time/date/week visible; (2) current temperature/humidity/condition visible; (3) AQI number/level visible; (4) the three readings occupy coordinated but separated Planar surfaces; (5) overall opacity is adjustable and persisted; (6) city can switch among at least four presets; (7) weather/AQI auto-refresh every 30 minutes and can refresh manually; (8) time-only minimal mode works; (9) gaze/hover reveals seconds; (10) hand/pointer/controller paths reach the same control actions; (11) live/cached/demo freshness is explicit.
- **Success / efficiency criteria**: first meaningful ambient content ≤2 s from cached/demo data; typical time/weather/AQI glance 1–3 s target; city/mode/opacity adjustment ≤15 s target; clock updates once per second; scheduled refresh interval is 30 minutes; no continuous animation or per-frame polling.
- **Risks and must-not-fail items**: never imply sensor-grade/local measurement; never hide stale/demo state; never rely on color alone; never block the forward central sight line with an opaque root; networking/cache errors must not crash; gesture-only interaction is forbidden; seconds focus must not cause the entire multi-window layer to recompose at frame rate.
- **Preference for default number of visible primary windows**: three small primary Planar WindowContainers visible by default (time, weather, AQI), spatially separated but lifecycle-coordinated; one temporary control WindowContainer appears only on request. Minimal mode keeps only the time window.
- **Preference for domain-specialized components**: TimeBeacon, WeatherGlyphReadout, AqiRingReadout, FreshnessBadge and AmbientControlPanel must expose domain states (loading/live/cached/demo/error, condition and AQI bands), not generic cards with swapped labels.
- **Preference for real-time data trust**: every weather/AQI reading carries selected city, source and last update; cached/demo states are textually labeled; failed refresh preserves the last successful data; refresh activity is visible but non-blocking.
- **PICO platform and spatial-design hard constraints**: Shared Space; Planar WindowContainer only; SpatialUI and `PicoTheme` for all 2D UI; no Material/Material3; system glass preserved; public SDK APIs only; gaze + pinch preferred with controller/touch equivalent; no Stage, anchors, env mesh or camera motion; exact physical comfort and hand/gaze behavior require device evidence.
- **Originality requirement**: realize the UXR differentiation opportunity as a directional ambient constellation—not a widget grid or dashboard—while absorbing only glanceability, city configuration and data-trust needs. The center stays visually open, with single-purpose readings at upper/side anchors and controls temporary.
- **Design / readability / downstream-implementation acceptance plan**: complete and independently review all 17 design stages; require exact component anatomy/sizing/states/data bindings and a stateful Web preview; bridge the accepted design into a layered Android project; pass design-style checks, unit tests and debug assembly; launch in the PICO emulator with screenshot/crash-log evidence; reserve palm-up comfort and CPU <5% for Swan/device validation.

## 8. Requirements Traceability

| Requirement | Implementation Node | Validation Method |
|---|---|---|
| Time/date/week + focused seconds | TimeBeacon + ClockTicker | Clock formatting/use-case tests; preview focus state; emulator screenshot |
| Temperature/humidity/condition | WeatherGlyphReadout + WeatherRepository | DTO mapping/cache tests; preview live/cached states; emulator screenshot |
| AQI number and level | AqiRingReadout + AqiClassifier | AQI boundary tests; color-independent label inspection |
| Spatially distributed information | three Planar WindowContainers + placement contract | manifest/DSL scan; emulator spatial screenshot |
| Overall opacity | AmbientPreferences + control slider + swipe semantic | reducer/persistence tests; min/default/max preview and emulator checks |
| City switching | CityCatalog + previous/next actions + horizontal swipe semantic | city wrap-around tests; preview/emulator action sequence |
| 30-minute automatic refresh | WorkManager periodic work + repository freshness policy | worker scheduling and freshness unit tests; work registration inspection |
| Minimal mode | UiState.minimalMode + visibility coordinator | reducer tests; preview/emulator full↔minimal evidence |
| Palm-up/gaze interaction | HandInput boundary + hover/focus + equivalent controls | compile/public-API scan; emulator fallback path; real-device palm-up/gaze pending |
| Persistent low-interference visuals | transparent roots + 0.60 default opacity + no continuous motion | design-style scan; screenshot inspection; frame/log observation |
| Stable 60fps / CPU <5% | once-per-second ticker, no per-frame loops, infrequent network work | code inspection + emulator frame observation; final `pico-cli perf` on target device |
| Live/cached/demo data trust | FreshnessBadge + selected city/source/lastUpdated fields in UiState | repository freshness tests; preview live/cached/demo states; emulator screenshot must show city, source and update time |

## 9. Minimum Completeness Gate

> This table is self-checked by `product_strategist` and independently re-reviewed by `evidence_integrity_reviewer`.
> A section that exists but still contains placeholders, a key table that has only an empty sample row, or acceptance criteria that are unverifiable or lack evidence anchors are all considered unmet.
> When any row is `block`, this document's `minimumCompletenessGate=block` and the overall
> `designStatus=invalid`, and it must not proceed to subsequent design stages.

| Check Item | Minimum Pass Condition | Evidence Anchor | Verdict |
|---|---|---|---|
| Background and intent | one-sentence requirement, users, scenario, posture, frequency/duration, and spatial necessity all have facts or explicit assumptions | §§2–5 | pass |
| Assumption governance | every unknown item has confidence, impact, and a validation plan; no implicit guessing | §6 | pass |
| Quality contract | all nine contract items are complete; outcomes/efficiency/risks are acceptance-testable and constraint sources are traceable | §7 | pass |
| Requirements traceability | every required business outcome maps to at least one implementation node and validation method | §8 | pass |

| Field | Value |
|---|---|
| minimumCompletenessGate | pass |

## 10. Delivery and Recipients

- **Deliverables**: intent definition + quality contract (this document is their human-readable source of fact)
- **Recipients**: UXR, Interaction / Spatial Designer, Design Lead

---

> Format convention: every "why" must have a source (traceable); missing information goes into the assumptions list with confidence/impact/validation plan; acceptance items must be quantifiable; do not dress up the project's derivations as PICO official rules.
