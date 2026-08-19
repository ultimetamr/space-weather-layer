# User Research Report · 空间时间气候层

> Role: `research_analyst` | Workflow stages: `research` → `domain_model` | Revision: 2

## 1. Research goals and questions

- Validate whether weather/time information is useful as a glance layer rather than a full dashboard.
- Identify the minimum trusted data, freshness and offline states.
- Bound PICO Shared Space, Planar WindowContainer, gaze/hover, gesture and emulator validation claims.
- Compare adjacent “at-a-glance” products without copying their layouts or visual systems.

Methods used for this design pass: user-supplied PRD analysis; official product documentation review; PICO OS 6 local knowledge-graph retrieval; Open-Meteo official API documentation review. No user interview, eye-tracking study or PICO device usability sample was supplied, so those are explicit evidence gaps.

## 2. Five categories of research evidence

| Category | Evidence / gap | Source | Type | Scope | Confidence | Observed | Validation plan |
|---|---|---|---|---|---|---|---|
| market | Current mainstream systems use compact, dynamic weather/time surfaces for “at a glance” access; Apple Weather widgets support location switching and multiple locations. | https://support.apple.com/en-euro/guide/iphone/iph8bf15cb61/ios | official | iPhone widgets; adjacent 2D task | high | 2026-08-14 | Use only as requirement coverage, not layout evidence. |
| market | Windows Widgets expose current weather and concise information through a taskbar/board, with hover or selection entry. | https://support.microsoft.com/en-us/windows/experience/personalization/customize-the-taskbar-in-windows | official | Windows 11; adjacent 2D task | high | 2026-08-14 | Compare glance/expand behavior during concept selection. |
| user | The requested user outcome is persistent, low-opacity time/weather/AQI visible by looking up, with city, opacity and minimal controls. | User PRD in this task | user_supplied | Requested PICO app | high | 2026-08-14 | Trace every mandatory item to a state/component/test. |
| user | Evidence gap: no measured target-user sample exists for preferred opacity, dwell time, text contrast, palm-up discoverability or fatigue. | none | assumption | Home Shared Space users | low | 2026-08-14 | Run 5-user formative study on device; test 25/60/100% opacity and 1–3 s glance tasks. |
| domain | Open-Meteo Forecast API exposes current temperature, relative humidity and WMO weather code; its Air Quality API exposes current variables including US AQI. | https://open-meteo.com/en/docs and https://open-meteo.com/en/docs/air-quality-api | official | Public weather/AQI data | high | 2026-08-14 | Contract-test the exact response keys and units; retain visible source/freshness. |
| platform | PICO OS 6 supports a default and non-default WindowContainer declared through the static DSL/manifest; Planar windows and lifecycle APIs are documented. | PICO knowledge graph: `spatial-sdk_spatial-container_manage-windowcontainers_declare-a-windowcontainer.md`; `manage-the-lifecycle-and-state-of-a-windowcontainer.md` | official | PICO Spatial SDK 6.0 | high | 2026-08-14 | Confirm exact generated template symbols and build against installed SDK. |
| platform | SpatialUI supplies `PicoTheme`; window constraints and resize APIs are documented. Gaze/palm-up exact public symbols were not established by this research query. | PICO knowledge graph: `spatial-ui_theme.md`; API `com.pico.spatial.ui.design.md`, `platform.resize.md` | official + gap | PICO SpatialUI 6.0 | medium | 2026-08-14 | Use public hover/focus and an injectable HandInput boundary; compile and emulator-test equivalent paths; mark palm-up true-device-only. |
| safety | This is non-safety-critical environmental information; stale or estimated data must not look live, and AQI must use label + number rather than color alone. | User PRD + Open-Meteo model/update documentation | user_supplied + official | Weather/AQI display | high | 2026-08-14 | Show source, last-updated time and cached/demo badge; test color-independent AQI semantics. |
| safety | Evidence gap: comfort, occlusion, physical readability, palm gesture reach and CPU <5% cannot be concluded from a Web preview or emulator. | PICO workflow validation boundary | official workflow rule | Target Swan/PICO OS device | high | 2026-08-14 | Perform device usability and `pico-cli perf`/Perfetto measurement before production acceptance. |

Source conflicts: “full-room/global layer” is a product intent, while a third-party app remains bounded by PICO-managed containers. The design therefore interprets global as a coordinated set of app-owned Planar windows in Shared Space, not system-shell overlay privileges.

## 3. Competitive benchmark

| # | Competitor / platform | Feature needs | Interaction experience | Visual experience (observation only) | Spatial-capability usage | Source / observed |
|---|---|---|---|---|---|---|
| 1 | Apple Weather widgets / iOS | At-a-glance current conditions; selectable city; multiple widgets can show multiple locations; AQI availability varies by region. | Touch/long-press configuration; direct glance, app opens for depth. Low learning cost; risk is needing several widgets for several places. | Compact system typography and density tiers; readable but bound to a flat home/lock screen. | 2D; no direction, depth or body input. Porting risk: a literal widget grid would waste Shared Space value. | Apple Support Weather widgets and widget guide, 2026-08-14. |
| 2 | Windows Widgets / Windows 11 | Dynamic weather plus other information; hover/select opens a larger board; cards can be customized. | Mouse hover, click, keyboard shortcut and edge swipe. Quick entry, but board expansion can interrupt the current task. | Concise taskbar cue expands to a denser board; more dashboard-like than ambient. | 2D desktop; no spatial distribution. Porting risk: board-style density would block the central view. | Microsoft Support Widgets/taskbar, 2026-08-14. |
| 3 | Google At a Glance / Android & Pixel | Home/lock-screen date, weather and severe-weather updates; broader task/event information on supported devices. | Touch/long-press configuration and direct glance; some features require activity/location permissions. Low interaction cost, but contextual rotation can reduce predictability. | Compact date-plus-context line intended for lock/home/AOD glance; very low density and little user-controlled spatial grouping. | 2D; no direction/depth/body interaction. Porting risk: copying one fixed top-left line would not satisfy distributed weather/AQI semantics. | Google Android Help, https://support.google.com/android/answer/14883154, observed 2026-08-14. |

### Per-product absorb / avoid distillation

| # | Strengths worth absorbing at needs/opportunity level | Weaknesses / anti-patterns to avoid |
|---|---|---|
| 1 | Fast city configuration; compact current-state summary; graceful scale choices. | Copying widget rectangles or showing multiple cities simultaneously without need. |
| 2 | Glance-first plus optional expansion; equivalent pointer/touch/keyboard entry. | Expanding into a central news/dashboard board; hover-only critical controls. |
| 3 | Persistent low-density date/weather availability and feature-level configuration. | Opaque personalization dependencies, contextual replacement of stable readings, and a single fixed 2D anchor. |

Our differentiation opportunity is a distributed “ambient constellation”: a time anchor above the central view, weather at one side, AQI at the other, plus a temporary control surface. It preserves real-world sight lines, uses direction and body/gaze access, and collapses to time-only. It absorbs glanceability, city selection, stable low-density availability and data-trust expectations while rejecting a copied widget grid, contextual carousel or dashboard. The benchmark contains three independent adjacent product families (Apple Weather widgets, Windows Widgets, Google At a Glance); there is no verified same-category PICO/Quest weather overlay sample, so XR-market maturity remains an evidence gap. Absorption is strictly limited to needs and risks.

## 4. Domain model

- **Workflow**: launch → restore last city/mode/opacity/cache → render glance layer → update clock each second → refresh weather/AQI when due or requested → persist successful data → expose source/freshness → temporarily open controls → change city/opacity/mode → return to ambient layer.
- **Decision variables**: local time zone; selected city coordinates; temperature; relative humidity; WMO weather code; US AQI; observation/update time; cache age; network state; opacity; minimal/full mode; focus/hover state.
- **Data entities and timeliness**: `City` is static; `ClockSnapshot` is current to one second; `WeatherSnapshot` and `AirQualitySnapshot` refresh every 30 minutes; `CachedEnvironment` persists the last success; `DataFreshness` distinguishes live, cached and demo fallback.
- **Specialized risks**: confusing forecast model output with a local sensor; silent staleness; AQI color-only interpretation; weather code mismatch; network churn; excessive one-second recomposition across every window; gesture without fallback.
- **User mental model**: time is continuously exact; weather/AQI are periodically refreshed estimates tied to a city and source; a small control layer changes the whole constellation.
- **Mature patterns**: glance summary with on-demand detail; visible city and freshness; cached fallback; one primary reading per surface. Anti-patterns: dashboard density, center-view occlusion, forced location permission, unlabeled stale values, rapid animation, per-frame polling and hidden gesture-only controls.

## 5. Persona

### Persona 1: “需要抬眼确认、但不想离开当前任务的家庭 XR 用户”

| Dimension | Content |
|---|---|
| Basic information | Evidence-bounded archetype; adult home user, mixed XR experience. No demographic study supplied. |
| Use scenario and frequency | Sitting or standing in Shared Space for work/entertainment; several 1–3 s glances per session. Frequency is a design assumption pending study. |
| Goals / motivations | Know current time and environmental conditions without opening a weather app or blocking the room. |
| Pain points / frustrations | Flat dashboards steal focus; dense widgets clutter; stale data is hard to detect; gesture-only controls can be undiscoverable. |
| Spatial usage habits | Keeps head mostly forward, accepts a small upward glance and side scan; control actions should remain inside a comfortable gaze/pinch region. |
| Accessibility needs | AQI must not depend on color alone; low-opacity text needs a stronger focused state; controller/touch fallback for hand limitations. |
| Key quote (user-supplied requirement) | “视觉通透不干扰，随时抬眼可见环境信息。” |

## 6. Journey map

| Stage | Entry | First hands-on | Core use | Adjustment | Exit / return |
|---|---|---|---|---|---|
| User goal | See useful state immediately | Understand distribution and freshness | Glance without interruption | Change city/opacity/mode | Leave settings and keep ambient state |
| Behavior | Launch in Shared Space | Look up and scan sides | Gaze time, read weather/AQI | Palm-up semantic or visible fallback control; swipe/buttons/slider | Dismiss control panel or stop app |
| Touchpoint | Coordinated Planar windows | Time/weather/AQI windows | Ambient constellation | Temporary control window | Persistent settings/cache next launch |
| Thought | “Is this for my city and current?” | “Where are the three readings?” | “I got it without switching tasks.” | “Can I tune this without hunting?” | “Will my setup remain?” |
| Emotion | curious | cautious | calm | in control | confident |
| Pain point | Demo/cached data may be mistaken as live | Low opacity can reduce readability | Side windows may drift outside comfort | Hand gesture may be unavailable | Hidden stale state on resume |
| Opportunity | Clear city/source/freshness | Coherent but separated anchors | Focus boost only on demand | Equivalent controls and immediate preview | Persist and refresh safely |

Emotional low point: the first screen showing old or demo weather without an obvious freshness label. This must be addressed before visual polish. Key opportunities: distributed single-purpose windows, visible freshness, focus boost, minimal mode, and equivalent interaction paths.

## 7. Key findings

| # | Finding | Evidence | Confidence | Design implication |
|---|---|---|---|---|
| 1 | Inference bounded to the reviewed Apple/Microsoft/Google product families: each exposes weather/date or other current information on compact 2D surfaces. This supports a glance-oriented opportunity, not a population-wide need claim. | Apple/Microsoft/Google official support docs, observed 2026-08-14 | medium | Preserve glance value while deriving a new spatial distribution; validate with target-user study. |
| 2 | For this product, city switching is required by the user; Apple Weather demonstrates that selectable/multiple locations are a viable adjacent pattern, not proof of a universal expectation. | Apple Weather widget docs + user PRD | high for requirement, medium for market inference | Provide explicit city state and left/right switch; test discoverability with users. |
| 3 | Source, freshness and offline semantics are essential because API data is modeled and refreshed periodically. | Open-Meteo docs | high | Display update time and live/cached/demo state. |
| 4 | Exact palm-up and gaze APIs/behavior remain device-dependent. | PICO query results and absent confirmed symbols | medium | Compile against public SpatialUI; isolate HandInput; provide controller/touch fallback. |
| 5 | CPU and comfort targets require device evidence. | PICO validation rules | high | Avoid per-frame loops; measure on Swan before claiming acceptance. |

## 8. Posture, field of view and eye-hand usability

- Primary posture: sitting/standing; slow movement only, based on the user scenario rather than measured distribution.
- Comfortable reach: control panel must work with gaze + pinch/controller so hands can remain near torso; exact angular/reach limits are a device-study gap.
- Central field: keep the forward center mostly empty; time sits above it and side readings stay secondary. Exact clear-field fit is calculated in the window-sizing stage.
- Fatigue: no camera motion, no continuous spatial animation, no forced head sweep. Long-duration comfort remains device-unverified.
- Gaze/pinch hit rate: no measured sample exists. Validation target is ≥95% successful selection over 20 trials for the control fallback; palm-up recognition is tracked separately.
- Hover/focus feedback: interactive controls need visible scale/contrast feedback and controller-equivalent activation; ambient labels remain non-interactive.

## 9. Duration baselines

| Decision type | Duration anchor | Source |
|---|---|---|
| Glance decision | Design target 1–3 s; empirical baseline is an evidence gap. | User “抬眼可见” requirement; validate with timed device study. |
| Fine-tuning dwell | Design target ≤15 s for city/mode/opacity; empirical baseline is an evidence gap. | Product acceptance assumption; validate with 5-user formative test. |

## 10. Motion sickness, fatigue and safety

- Design inference: planned behavior removes common visual-motion triggers by using no camera/scene motion, parallax animation or forced immersion; actual symptom risk is unknown until device study.
- High Motion labeling is provisionally not planned because the design contains no high-motion behavior; this is not a medical/comfort conclusion and must be revisited if motion is added.
- No design-stage claim is made about safe continuous duration; encourage normal platform rest guidance and validate long-session readability on device.
- Data safety boundary: never present this as emergency, medical or sensor-grade information; show source and age; retain last success with a clear cached label.

## 11. Minimum Completeness Gate

| Check Item | Evidence | Verdict |
|---|---|---|
| Five evidence categories | §2 covers market/user/domain/platform/safety with sources or explicit gaps. | pass |
| Competitive benchmark | §3 covers three products across function, interaction, visual observation and spatial differences. | pass |
| Domain model | §4 covers workflow, variables, entities/timeliness, risks, mental model and anti-patterns. | pass |
| User evidence | §§5–7 distinguish the user-supplied quote from bounded assumptions and gaps. | pass |
| Quantitative and safety | §§8–10 provide targets or explicit device-study gaps and safety boundaries. | pass |

| Field | Value |
|---|---|
| minimumCompletenessGate | pass |

## 12. Platform retrieval receipt

- SDK knowledge workspace: PICO Spatial SDK 6.0, resolved by `pico-cli doctor --platform spatial` on 2026-08-14.
- Query invocation: `pico-dev-knowledge.query_graph`, BFS depth 2, queried 2026-08-14 for Shared Space/Planar WindowContainer, multiple windows/lifecycle, SpatialUI/PicoTheme/gaze-hover/window constraints, and performance/comfort guidance.
- Reproducible local source anchors:
  - `C:\Users\29852\AppData\Local\PICO\sdk\6.0\agent-vault\spatial\documentation\spatial-sdk_spatial-container_manage-windowcontainers_declare-a-windowcontainer.md`
  - `C:\Users\29852\AppData\Local\PICO\sdk\6.0\agent-vault\spatial\documentation\spatial-sdk_spatial-container_manage-windowcontainers_manage-the-lifecycle-and-state-of-a-windowcontainer.md`
  - `C:\Users\29852\AppData\Local\PICO\sdk\6.0\agent-vault\spatial\documentation\spatial-design_foundation_window.md`
  - `C:\Users\29852\AppData\Local\PICO\sdk\6.0\agent-vault\spatial\documentation\spatial-ui_theme.md`
  - `C:\Users\29852\AppData\Local\PICO\sdk\6.0\agent-vault\spatial\api-reference\com.pico.spatial.ui.platform.resize.md`
- Retrieval boundary: the query confirmed container/theme/constraints sources but did not confirm an exact palm-up or eye-tracking public API symbol; those remain compile/device validation items behind injected boundaries.
