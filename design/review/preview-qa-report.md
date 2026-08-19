# Preview / QA Test Report · 空间时间气候层

> Report revision: `r8` | Stage 14 independent Preview Review rerun; reviewed prior report revision: `r7`

> Role: `prototype_frontend_engineer` (generates the preview) + `prototype_qa_reviewer` (independent review) | Workflow stage(s): `preview_build` → `preview_review` | Upstream inputs: design-system facts (layout / components / interaction / motion / data), approved visual references | Downstream recipients: engineering implementation team, Design Lead, PM
>
> This document carries these two roles' **LLM reasoning information** and **direct description of outputs**. It is not bound to any JSON Schema or validator error codes; mandatory gates are expressed through this document's Coverage Manifest, item-by-item mapping tables, independent QA evidence, and the `block` status.

## 0. Reasoning Guidance (how this role reasons)

- **The preview is generated only from the design-system fact documents (`interaction-spatial-spec.md` / `visual-system-spec.md`)**: do not select a domain template, do not invent missing design facts, do not modify the design-fact documents, do not generate Android/PICO runtime, do not fabricate device evidence, and do not draw cross-platform parity conclusions.
- **The preview must cover** the declared states, components, data bindings, and visual tokens, as well as Large / Compact / Constrained and Reduce Motion, and must be labeled with the scope `web_design_validation_only`.
- **The coverage unit is the design-fact item, not the name**: verify item by item `renderSpec.elements[]`, `dataBindings[]`, variants, component-specific states, transitions, and fallbacks; a component name or state button appearing does not count as implementation fidelity.
- **The Coverage Manifest precedes preview generation**: before generating `preview.html`, first declare the design-fact denominator in this report; the Manifest is a Markdown declarative denominator, not a script, schema, or validator.
- **Declarative checks replace script checks**: preview fidelity is declared and back-checked item by item through this report's Markdown checklist; script output can only serve as auxiliary observation, and cannot replace the evidence chain of source fact → selector → trigger → result.
- **Validation boundary**: the preview only verifies Web logical relationships and declared token references; it does not do screenshot-level visual diff, and does not interpret CSS pixels as PICO physical sizes or device color differences.
- **QA review independence**: `prototype_qa_reviewer` differs from the generator, only emits findings, coverage records, and patch goals, does not modify the prototype or design-fact documents, does not do device validation, and does not substitute for human approval. **A Preview PASS must not be described as PICO runtime validation**.
- **Device-validation status is fixed as `not_performed`**: physical viewing distance, occlusion, fatigue, hit precision, runtime performance, and safety must be handed off to device validation.

## 1. Direct Description of Outputs

This role delivers: **preview coverage verification → requirements traceability → sample data → Web logic tolerance → device-validation boundary → defect list**. The sections below are the structured descriptions of these outputs.

## 2. Test Scope and Verdict

- **Object under test**: a single-file Web validation prototype driven by the design-fact documents
- **Validation scope**: `web_design_validation_only`
- **Source design-fact documents**: `interaction-spatial-spec.md` r7; `visual-system-spec.md` r3; `design-critique-report.md` r6 (`DESIGN-SYS-REV-20260814-02=pass`).
- **Overall verdict**: `pass` — `PREVIEW-REV-20260816-04` independently reviewed Preview r3 against Interaction r7 + Visual r3 + design-system Critique r6 with harness r2. Preview r3 differs from the reviewed Preview r2 only by the revision comment marker (`preview-revision: 2` → `3`); the independent `pnpm test:preview` rerun passed every observable denominator: states 8/8, transitions 14/14, render elements 37/37, normal/fallback bindings 28/28, variants 17/17, component states 31/31, semantic groups 15/15, visual-token groups 12/12, and responsive/Reduce Motion scenarios 4/4. This is a Web-logic verdict only; device validation remains `not_performed`.

### 2.0 Reviewer Invocation Evidence

| Review Gate | reviewerRole | invocationId | contextPolicy | reviewedRevision | evidenceRebuilt | Verdict |
|---|---|---|---|---|---|---|
| Preview implementation | prototype_qa_reviewer | PREVIEW-REV-20260814-01 | isolated_subagent | Preview r1 (source Interaction r7 + Visual r3) | yes | block |
| Preview implementation | prototype_qa_reviewer | PREVIEW-REV-20260814-02 | isolated_subagent | Preview r1 + preview-dom-test.mjs r1 + Preview QA report r3 (source Interaction r7 + Visual r3) | yes | block |
| Preview implementation | prototype_qa_reviewer | PREVIEW-REV-20260815-03 | isolated_subagent | Preview r2 + preview-dom-test.mjs r2 + Preview QA report r5 + Interaction r7 + Visual r3 + Critique r6 | yes | pass |
| Preview implementation | prototype_qa_reviewer | PREVIEW-REV-20260816-04 | isolated_subagent | Preview r3 + preview-dom-test.mjs r2 + Preview QA report r7 + Interaction r7 + Visual r3 + Critique r6 | yes | pass |

> When `invocationId` is empty, `contextPolicy=unavailable`, or "independently rebuilt evidence=no", the Preview Review can only be `block`.

### 2.1 Input Readiness Table (required before preview_build)

> If any item is not `pass`, a preview must not be generated or reused from an old one; the verdict must be `block`.

| Input Fact | Source Section / Version | Completeness Assertion | Verdict |
|---|---|---|---|
| Design-system review | Critique r6 §2.4 | `DESIGN-SYS-REV-20260814-02=pass` for Interaction r7 + Visual r3 | pass |
| States and transitions | Interaction r7 §§10–11 | S0–S7 each define entry/exit/exception/return; TR01–TR14 define trigger/action/confirmation | pass |
| Core component 8-section structure | Visual r3 §§5–5.1 | 5 components × 8 sections = 40/40, independent review confirmed | pass |
| renderSpec.elements[] | Visual r3 §5 | 37 elements, each with stable id/label/type/bind/role | pass |
| dataBindings[] | Visual r3 §5 | 28 bindings, each with target/fallback/display-or-semantic | pass |
| variants / component-specific states | Visual r3 §5 | 17 variants + 31 states with trigger/change/motion/accessibility/precedence | pass |
| Responsive window tiers / Reduce Motion | Interaction r7 §§9,13–14 + Visual r3 §§5.0/5 | Large/Compact/Constrained map to max/default/min content boxes; Reduce Motion explicit | pass |
| tokens / colorSemantics / materials | Visual r3 §§3–4,6 | exact tokens, dual-channel semantics, legal Regular/Thick glass and matte fallback | pass |

### 2.2 Preview Coverage Manifest (declarative denominator before generation)

> This section is filled in before `preview.html` is generated. It is the sole coverage denominator for subsequent implementation mapping and QA back-checking. Do not use "all components covered" or "see preview" as a merged replacement for an item-by-item Manifest.

#### 2.2.1 State / transition denominator

| Type | ID | Source Fact Anchor | Trigger event / entry | Target / visible result | High-risk confirmation requirement | Verdict |
|---|---|---|---|---|---|---|
| state | S0_BOOTSTRAP | Interaction r7 §10 | launch | placeholders then restored cache/live/demo decision | N/A | included |
| state | S1_AMBIENT_LIVE | Interaction r7 §10 | live sample/success | three-window constellation + 实时 | N/A | included |
| state | S2_AMBIENT_CACHED | Interaction r7 §10 | fallback sample/cache failure | values retained + 缓存/age | N/A | included |
| state | S3_MINIMAL_TIME | Interaction r7 §10 | minimal toggle | TimeWindow only + controls affordance | N/A | included |
| state | S4_SECONDS_FOCUSED | Interaction r7 §10 | focus time | seconds, veil, outline, ≥0.85 alpha | N/A | included |
| state | S5_CONTROLS_OPEN | Interaction r7 §10 | controls request | Thick control plane; ambient dimmed | N/A | included |
| state | S6_REFRESHING | Interaction r7 §10 | due/manual/city refresh | retained data + static 更新中 | N/A | included |
| state | S7_DEMO_OR_ERROR | Interaction r7 §10 | no cache/error sample | 演示数据 or 更新失败, never blank | N/A | included |
| transition | TR01 | Interaction r7 §10 | `system.appLaunched` | S0; restore/open/cache/schedule | no | included |
| transition | TR02 | Interaction r7 §10 | due/refresh button | S6 single-flight | no | included |
| transition | TR03 | Interaction r7 §10 | refresh success | S1 + persisted live | no | included |
| transition | TR04 | Interaction r7 §10 | failure with cache | S2 + cached/error age | no | included |
| transition | TR05 | Interaction r7 §10 | failure without cache | S7 demo/error | no | included |
| transition | TR06 | Interaction r7 §10 | time focus enter | S4 seconds visible | no | included |
| transition | TR07 | Interaction r7 §10 | time focus exit | remembered prior ambient | no | included |
| transition | TR08 | Interaction r7 §10 | controls/palm-up simulation | S5 open/reuse + dim ambient | no | included |
| transition | TR09 | Interaction r7 §10 | done/close/back | prior ambient + restored opacity | no | included |
| transition | TR10 | Interaction r7 §10 | previous/next city | cached/demo immediately then S6 | no | included |
| transition | TR11 | Interaction r7 §10 | slider/−/+ | S5, clamped persisted preview | no | included |
| transition | TR12 | Interaction r7 §10 | minimal enabled | S3, side windows hidden | no | included |
| transition | TR13 | Interaction r7 §10 | full mode enabled | S0, side windows reopened/reused | no | included |
| transition | TR14 | Interaction r7 §10 | QA `simulateSystemClose` | system-owned close boundary/cleanup evidence | yes only for explicit app close; no lifecycle stop | included |

#### 2.2.2 `renderSpec.elements[]` denominator

| Component | element id | Source Fact Anchor | Visible label | bind | Conditional hide / show rule | Verdict |
|---|---|---|---|---|---|---|
| TimeBeacon | `time_text` | Visual r3 §5 TimeBeacon | current HH:mm | `clock.localTime` | always in app-visible states | included |
| TimeBeacon | `seconds_text` | Visual r3 §5 TimeBeacon | :ss | `clock.seconds` | only S4/focused | included |
| TimeBeacon | `date_text` | Visual r3 §5 TimeBeacon | localized date | `clock.localDate` | always | included |
| TimeBeacon | `weekday_text` | Visual r3 §5 TimeBeacon | localized weekday | `clock.weekday` | hidden only if formatter fallback | included |
| TimeBeacon | `controls_action` | Visual r3 §5 TimeBeacon | 控制 | `actions.openControls` | always; sole visible control entry | included |
| TimeBeacon | `focus_outline` | Visual r3 §5 TimeBeacon | 时间已聚焦 | `ui.timeFocused` | S4 only | included |
| WeatherGlyphReadout | `condition_glyph` | Visual r3 §5 Weather | weather shape | `weather.conditionSemantic` | full mode; not S3 | included |
| WeatherGlyphReadout | `condition_label` | Visual r3 §5 Weather | 晴/阴/雨/雪 | condition label | full mode; not S3 | included |
| WeatherGlyphReadout | `temperature_text` | Visual r3 §5 Weather | 26° | `weather.temperatureC` | full mode; fallback `--°` | included |
| WeatherGlyphReadout | `humidity_text` | Visual r3 §5 Weather | 湿度 48% | relativeHumidity | full mode; fallback `湿度 --%` | included |
| WeatherGlyphReadout | `weather_placeholder` | Visual r3 §5 Weather | 天气读取中/暂不可用 | freshness | loading/error only | included |
| AqiRingReadout | `aqi_track` | Visual r3 §5 AQI | AQI范围 | static | full mode and data-usable/loading | included |
| AqiRingReadout | `aqi_arc` | Visual r3 §5 AQI | AQI进度 | normalizedAqi | valid AQI only | included |
| AqiRingReadout | `aqi_shape` | Visual r3 §5 AQI | semantic shape | band.shape | valid band/error shape | included |
| AqiRingReadout | `aqi_value` | Visual r3 §5 AQI | 42 | usAqi | full mode; `--` fallback | included |
| AqiRingReadout | `aqi_label` | Visual r3 §5 AQI | 优/良/污染等级 | band.label | valid band; error becomes 暂不可用 | included |
| AqiRingReadout | `aqi_placeholder` | Visual r3 §5 AQI | 空气质量读取中 | freshness | loading/error only | included |
| FreshnessBadge | `trust_shape` | Visual r3 §5 Freshness | status shape | freshness.semantic.shape | weather/AQI/control trust regions | included |
| FreshnessBadge | `city_label` | Visual r3 §5 Freshness | selected city | city.displayName | all environment values | included |
| FreshnessBadge | `trust_label` | Visual r3 §5 Freshness | 实时/缓存/演示数据/更新失败 | freshness semantic label | always when environment shown | included |
| FreshnessBadge | `age_label` | Visual r3 §5 Freshness | 2分钟前 | freshness.age | non-demo; 时间未知 fallback | included |
| FreshnessBadge | `source_label` | Visual r3 §5 Freshness | Open-Meteo/本地演示 | freshness.source | abbreviated in Constrained but description retains | included |
| FreshnessBadge | `refresh_mark` | Visual r3 §5 Freshness | 更新中 | freshness.refreshing | S6 only | included |
| FreshnessBadge | `retry_action` | Visual r3 §5 Freshness | 重试 | actions.refresh | control/error focus only | included |
| AmbientControlPanel | `panel_title` | Visual r3 §5 Controls | 城市与显示 | static | S5 only | included |
| AmbientControlPanel | `control_freshness` | Visual r3 §5 Controls | city/trust/update/source | environment.freshness | S5 only | included |
| AmbientControlPanel | `close_action` | Visual r3 §5 Controls | 关闭 | dismissControls | S5 only | included |
| AmbientControlPanel | `previous_city_action` | Visual r3 §5 Controls | 上一个城市 | previousCity | S5; disabled if catalog <2 | included |
| AmbientControlPanel | `current_city_label` | Visual r3 §5 Controls | current city | selectedCity | S5 | included |
| AmbientControlPanel | `next_city_action` | Visual r3 §5 Controls | 下一个城市 | nextCity | S5; disabled if catalog <2 | included |
| AmbientControlPanel | `opacity_decrease` | Visual r3 §5 Controls | 降低透明度 | decreaseOpacity | S5; disabled at 25% | included |
| AmbientControlPanel | `opacity_slider` | Visual r3 §5 Controls | 信息层透明度 | preferences.opacity | S5 | included |
| AmbientControlPanel | `opacity_increase` | Visual r3 §5 Controls | 提高透明度 | increaseOpacity | S5; disabled at 100% | included |
| AmbientControlPanel | `opacity_value` | Visual r3 §5 Controls | 60% | preferences.opacity | S5 | included |
| AmbientControlPanel | `minimal_toggle` | Visual r3 §5 Controls | 仅显示时间 | minimalMode | S5 | included |
| AmbientControlPanel | `refresh_action` | Visual r3 §5 Controls | 刷新天气/更新中 | refresh | S5; disabled while refreshing | included |
| AmbientControlPanel | `done_action` | Visual r3 §5 Controls | 完成 | dismissControls | S5 | included |

#### 2.2.3 `dataBindings[]` denominator

| Component | Source path | Target element / attribute | normal sample | fallback / error sample | display-only / semantic | Verdict |
|---|---|---|---|---|---|---|
| TimeBeacon | `ClockSnapshot.localTime` | `time_text.textContent` | 09:41 | --:-- | display-only | included |
| TimeBeacon | `ClockSnapshot.seconds` | `seconds_text.textContent/display` | :26 | hidden | display-only | included |
| TimeBeacon | `ClockSnapshot.localDate` | `date_text.textContent` | 8月14日 | localized numeric date | display-only | included |
| TimeBeacon | `ClockSnapshot.weekday` | `weekday_text.textContent` | 星期五 | hidden, date retained | display-only | included |
| TimeBeacon | `UiState.timeFocused` | seconds/veil/outline/alpha | true focus sample | controller focus equivalent | semantic | included |
| TimeBeacon | `UiState.opacity` | TimeWindow content alpha | 0.60 | clamp/reset 0.60 | semantic | included |
| WeatherGlyphReadout | `WeatherSnapshot.temperatureC` | `temperature_text` | 26° | --° | display-only | included |
| WeatherGlyphReadout | `WeatherSnapshot.relativeHumidity` | `humidity_text` | 湿度 48% | 湿度 --% | display-only | included |
| WeatherGlyphReadout | `WeatherSnapshot.weatherCode` | glyph/label/color/shape | 0→晴 circle | unknown→阴 neutral square | semantic | included |
| WeatherGlyphReadout | `DataFreshness.state` | placeholder/value alpha | live | error retains cache/demo or unavailable | semantic | included |
| WeatherGlyphReadout | `UiState.opacity` | WeatherWindow alpha | 0.60 | clamped 0.25…1.00 | semantic | included |
| AqiRingReadout | `AirQualitySnapshot.usAqi` | value/arc sweep | 42 | --, no arc | semantic quantitative | included |
| AqiRingReadout | `AqiBand.key` | color/shape/label | good→优 circle | error→暂不可用 dashed | semantic | included |
| AqiRingReadout | `DataFreshness.state` | availability | live | cache/demo/error labeled | semantic | included |
| AqiRingReadout | `UiState.opacity` | AqiWindow alpha | 0.60 | clamped 0.25…1.00 | semantic | included |
| FreshnessBadge | `City.displayName` | city label | 北京 | preset name, never ID | display-only | included |
| FreshnessBadge | `DataFreshness.state` | shape/color/label/retry | 实时 circle | 更新失败 triangle | semantic | included |
| FreshnessBadge | `DataFreshness.age` | age label | 2分钟前 | 时间未知 | display-only | included |
| FreshnessBadge | `DataFreshness.source` | source label | Open-Meteo | 本地演示/来源未知 | display-only | included |
| FreshnessBadge | `DataFreshness.refreshing` | mark/text | false hidden | true 更新中 | semantic | included |
| FreshnessBadge | `NetworkState` | trust state mapping | online live | offline→缓存 | semantic | included |
| AmbientControlPanel | `CityCatalog.items` | neighbor labels/enabled | four cities | bundled four-city fallback | semantic navigation | included |
| AmbientControlPanel | `selectedCityIndex` | city/actions | 北京 index0 | clamp/wrap | semantic | included |
| AmbientControlPanel | `preferences.opacity` | slider/value/preview | 60% | invalid→60% | semantic | included |
| AmbientControlPanel | `preferences.minimalMode` | toggle/window visibility | false/full | missing→false | semantic | included |
| AmbientControlPanel | `DataFreshness` | header/refresh | live age/source | cache/demo/error retained | semantic | included |
| AmbientControlPanel | `UiState.refreshing` | refresh enabled/text | false/刷新天气 | true disabled/更新中 | semantic | included |
| AmbientControlPanel | `UiState.controlVisible` | panel lifecycle | true/S5 | open failure leaves time affordance | semantic | included |

#### 2.2.4 variants / component-specific states denominator

| Component | variant / state / stacking combination | Source Fact Anchor | Trigger method | Expected observable change | Verdict |
|---|---|---|---|---|---|
| TimeBeacon | variant ambient | Visual r3 §5 TimeBeacon | state S1/S2/S3/S7 | HH:mm + date/week, seconds hidden | included |
| TimeBeacon | variant precise | Visual r3 §5 TimeBeacon | focus time | seconds + veil + outline | included |
| TimeBeacon | variant minimal | Visual r3 §5 TimeBeacon | minimal toggle | only TimeWindow remains | included |
| TimeBeacon | variant constrainedPrecise | Visual r3 §5 TimeBeacon | Constrained + focus | seconds wrap below hero | included |
| TimeBeacon | state boot | Visual r3 §5 TimeBeacon | S0 | --:-- shell + reading status | included |
| TimeBeacon | state ambient | Visual r3 §5 TimeBeacon | S1/S2/S3/S7 | user opacity/no veil | included |
| TimeBeacon | state focused over ambient | Visual r3 §5 TimeBeacon | focus | ≥0.85, seconds/outline, precedence above ambient | included |
| TimeBeacon | state editing N/A | Visual r3 §5 TimeBeacon | QA state list | no editing primitive; control active | included |
| TimeBeacon | state error boundary | Visual r3 §5 TimeBeacon | error data mode | --:-- + 时间暂不可用, control retained | included |
| WeatherGlyphReadout | variant clear/cloudy/rain/snow | Visual r3 §5 Weather | condition selector | glyph/label/color/shape change | included |
| WeatherGlyphReadout | variant partial | Visual r3 §5 Weather | error/partial sample | known metric remains, missing `--` | included |
| WeatherGlyphReadout | variant constrained | Visual r3 §5 Weather | Constrained | three-row metric reflow | included |
| WeatherGlyphReadout | state loading | Visual r3 §5 Weather | S0/loading control | placeholder + reserved space | included |
| WeatherGlyphReadout | state fresh/aging | Visual r3 §5 Weather | normal/aging sample | metrics + condition semantic | included |
| WeatherGlyphReadout | state cached/demo | Visual r3 §5 Weather | fallback/demo | values retained + trust footer | included |
| WeatherGlyphReadout | state partial | Visual r3 §5 Weather | error partial | `--` only missing field | included |
| WeatherGlyphReadout | state editing N/A | Visual r3 §5 Weather | QA state list | wholly read-only, no action | included |
| WeatherGlyphReadout | state error/empty | Visual r3 §5 Weather | error/no usable snapshot | neutral square + 暂不可用 | included |
| AqiRingReadout | variant six bands | Visual r3 §5 AQI | AQI sample selector | arc/shape/label updates | included |
| AqiRingReadout | variant boundary | Visual r3 §5 AQI | set 51/101/151/201/301 | classifier changes at exact boundary | included |
| AqiRingReadout | variant constrained | Visual r3 §5 AQI | Constrained | 64dp ring/value left, label right | included |
| AqiRingReadout | state loading | Visual r3 §5 AQI | S0/loading | static track + placeholder | included |
| AqiRingReadout | state fresh/aging | Visual r3 §5 AQI | normal/aging | arc/marker/number/label | included |
| AqiRingReadout | state cached/demo | Visual r3 §5 AQI | fallback/demo | metric retained + trust label | included |
| AqiRingReadout | state boundary disabled | Visual r3 §5 AQI | out-of-range sample | clamped graphic + 数据异常 | included |
| AqiRingReadout | state editing N/A | Visual r3 §5 AQI | QA state list | wholly read-only | included |
| AqiRingReadout | state error/empty | Visual r3 §5 AQI | error mode | dashed track + 暂不可用 | included |
| FreshnessBadge | variant ambientFooter | Visual r3 §5 Freshness | ambient states | compact read-only city/state/age/source | included |
| FreshnessBadge | variant controlHeader | Visual r3 §5 Freshness | S5 | exact update + retry | included |
| FreshnessBadge | variant refreshing | Visual r3 §5 Freshness | S6 | static 更新中, no spinner | included |
| FreshnessBadge | state loading | Visual r3 §5 Freshness | S0 | dashed shape + 正在获取 | included |
| FreshnessBadge | state fresh | Visual r3 §5 Freshness | normal sample | circle + 实时 + age/source | included |
| FreshnessBadge | state aging | Visual r3 §5 Freshness | aging sample | square + 待更新 | included |
| FreshnessBadge | state cached/offline/stale | Visual r3 §5 Freshness | fallback/offline | dashed + 缓存 + exact age | included |
| FreshnessBadge | state demo | Visual r3 §5 Freshness | demo sample | diamond + 演示数据 + 本地演示 | included |
| FreshnessBadge | state partial/conflicting/permission/error | Visual r3 §5 Freshness | error sample | triangle + 更新失败 + retry in controls | included |
| AmbientControlPanel | variant fullMode | Visual r3 §5 Controls | toggle off | side windows visible | included |
| AmbientControlPanel | variant minimalMode | Visual r3 §5 Controls | toggle on | side windows hidden | included |
| AmbientControlPanel | variant refreshing | Visual r3 §5 Controls | refresh | action disabled + 更新中 | included |
| AmbientControlPanel | variant constrained | Visual r3 §5 Controls | Constrained | two-column reflow + scroll | included |
| AmbientControlPanel | state opening | Visual r3 §5 Controls | TR08 | Thick panel + focus entry + fade/slide | included |
| AmbientControlPanel | state ready | Visual r3 §5 Controls | S5 | all valid targets enabled | included |
| AmbientControlPanel | state dragging opacity | Visual r3 §5 Controls | range input | live percent/preview; precedence over ready | included |
| AmbientControlPanel | state city editing | Visual r3 §5 Controls | city previous/next | immediate city + refresh state | included |
| AmbientControlPanel | state refreshing | Visual r3 §5 Controls | TR10/TR02 | refresh disabled; settings usable | included |
| AmbientControlPanel | state boundary disabled | Visual r3 §5 Controls | opacity 25/100 or one city | corresponding actions disabled + reason | included |
| AmbientControlPanel | state error/empty | Visual r3 §5 Controls | error sample | error summary + retry, settings remain | included |
| AmbientControlPanel | state closing | Visual r3 §5 Controls | done/close/back | fade then focus returns to Time control | included |

#### 2.2.5 Responsive window tiers / Reduce Motion denominator

| Scenario | Source Fact Anchor | Corresponding window tier / content area | Trigger method | Expected structural change / motion fallback | Verdict |
|---|---|---|---|---|---|
| Large | Visual r3 §§5.0/5; Interaction r7 §9 | max content: Time 828×268, Weather 688×308, AQI 548×288, Control 912×572 | QA tier selector | more negative space/ring size; type/targets do not scale globally | included |
| Compact | Visual r3 §§5.0/5; Interaction r7 §9 | default content: 608×208, 488×228, 388×228, 712×472 | QA tier selector | regular two-row readings/four-row controls | included |
| Constrained | Visual r3 §§5.0/5; Interaction r7 §9 | min content: 488×168, 388×188, 328×188, 592×372 | QA tier selector | seconds/weather/AQI/control structural reflow; 56dp targets preserved | included |
| Reduce Motion | Interaction r7 §13 | N/A | QA motion toggle | scale/translation removed; 100–120ms fade/instant semantic state | included |

### 2.3 Markdown Declarative Checklist (replaces scripts)

> This section is filled in after `prototype_frontend_engineer` generates, and independently re-reviewed by `prototype_qa_reviewer`. Each row must state the source fact, selector, trigger steps, expected result, actual result, and verdict; if any item is empty, preview implementation fidelity is `block`. Do not use script results, a component name appearing, or a state button existing as a replacement for this table.

| Check Item | Source Fact Denominator | Preview selector / structure | Trigger steps | Expected result | Actual result | Verdict |
|---|---|---|---|---|---|---|
| Coverage Manifest complete | §2.2: states 8, transitions 14, elements 37, bindings 28, variants/states 48, responsive 4 | §2.2 item rows | compare source facts item by item | no merges/dangling/missing | Independently rebuilt from Interaction r7 / Visual r3 as 8 / 14 / 37 / 28 / 48 / 4; generation-side totals match. | pass |
| State machine exists | §2.2.1 | `body[data-state]`, JS `states`, `transitions`, `renderScene` | use 8 state buttons | distinct visible result per S0–S7 | Harness r2 `stateChecks` contains 8 executable closures; each clicks the state trigger and asserts the declared text, visibility, focus, placeholder, control, or trust result in addition to `body.dataset.state`. Independent run: 8/8 observed. | pass |
| Transitions can be triggered | 14 transition rows | `[data-transition]`, product `[data-action]`, `triggerTransition` | TR01–TR14 buttons/actions | target/action/confirmation match source | Harness r2 `transitionChecks` executes TR01–TR13 with target plus transition-specific visible side effects; TR14 separately asserts blocking, cancel, and confirm/PROCESS_EXIT. Independent run: 14/14 observed. | pass |
| renderSpec DOM back-check | 37 element rows | unique `[data-preview-id="…"]` | inspect and trigger conditional states | unique selector + visible/hide result | Harness r2 first proves 37/37 selectors unique, then `normalElementChecks`, `controlElementChecks`, state and binding closures assert their declared text/graphic/conditional visibility behavior. | pass |
| dataBindings normal/fallback/error | 28 binding rows | `[data-binding]`, `#dataMode`, component state selectors | switch normal/fallback/error/demo | bound copy/shape/label/fallback changes | Harness r2 `bindingChecks` has 28 named executable closures, each asserting its normal and fallback/error target behavior; the independent run observed 28/28. | pass |
| variants / component states | 17 variants + 31 states | five component-state selectors plus condition/AQI/tier/mode controls | trigger all 48 rows | value/structure/disabled/focus/trust change | Harness r2 has 17 named `variantChecks` and 31 named `componentStateChecks`; every closure dispatches its trigger and asserts a structural, semantic, disabled, focus, trust, or precedence result. Independent run: 48/48. | pass |
| High-risk confirmation Dialog | TR14 | `#exit_dialog[data-preview-id="exit_confirmation"]` | click TR14; cancel; repeat and confirm | Dialog blocks; cancel stays; confirm hides windows and shows PROCESS_EXIT | Harness r2 asserts `open` after TR14, no `open` after cancel, and `closed-simulation` plus PROCESS_EXIT after confirm. | pass |
| Responsive window tiers / Reduce Motion | 4 rows | `body[data-responsive]`, `body[data-reduce-motion]` | select Large/Compact/Constrained; toggle Reduce Motion | content boxes/reflow change; no whole-scene scale; translation/scale removed | Harness r2 `responsiveChecks` asserts tier-specific computed window/ring geometry for Large, Compact, and Constrained; Reduce Motion asserts computed `transform:none` and 110ms duration. Independent run: 4/4. | pass |

### 2.4 Preview Denominator Reconciliation (required for preview_review)

| Denominator Type | Design-fact total | Generation-side Manifest total | QA-rebuilt total | Difference | Verdict |
|---|---:|---:|---:|---:|---|
| States | 8 | 8 | 8 | 0 | pass |
| transition | 14 | 14 | 14 | 0 | pass |
| renderSpec.elements[] | 37 | 37 | 37 | 0 | pass |
| dataBindings[] | 28 | 28 | 28 | 0 | pass |
| variants / component states | 48 | 48 | 48 | 0 | pass |
| responsive / Reduce Motion | 4 | 4 | 4 | 0 | pass |

### 2.5 Preview Hard Gate

> `prototype_qa_reviewer` must recount from the active design facts; do not copy the generation-side totals.
> A missing Coverage Manifest or inconsistent denominators, a design-fact total filled with 0 without basis, any Manifest
> row missing a source fact anchor, any generation-side/QA total being empty, or any difference not being 0 all make
> `previewImplementationFidelity=block` and the overall `designStatus=invalid`.

| hard gate | pass condition | Evidence | Verdict |
|---|---|---|---|
| HG-PREVIEW-INPUT | each row of §2.1 is pass and references the active revision | §2.1 | pass |
| HG-PREVIEW-MANIFEST | the five categories of denominators in §2.2 are listed item by item, with no merges, no empty rows, no dangling references | §2.2; independent recount 8 / 14 / 37 / 28 / 48 / 4 | pass |
| HG-PREVIEW-ACTUAL-TRIGGER-EVIDENCE | each row of §2.3 and each independent validation field in §3.1–§3.5 has accepted actual trigger and observable-result evidence | Harness r2 source inspection plus independent run; named closures reconcile every row, with details recorded in §2.3 and §3.1–§3.5 | pass |
| HG-PREVIEW-DENOMINATOR | design-fact total = generation-side Manifest total = QA-rebuilt total, differences all 0 | §2.4 | pass |
| HG-PREVIEW-MAPS | each denominator item in §3.1–§3.5 has exactly one implementation and one piece of independent validation evidence | §3.1–§3.5 are itemized and now carry the corresponding r2 assertion-family actual result plus independent verdict | pass |

| Field | Value |
|---|---|
| previewImplementationFidelity | pass |
| minimumCompletenessGate | pass |
| designStatusImpact | none |

## 3. Preview Coverage

> The denominator comes from §2.2 Preview Coverage Manifest. Do not use "number of components" or "number of pages" to replace the number of elements, bindings, states, or scenarios; do not let §3 coverage be higher than the §2.3 declarative-check verdict.

| Coverage Item | Design-fact total | Verified item by item | Coverage rate | Missing / extra | Verdict |
|---|---:|---:|---:|---|---|
| Top-level states + transitions | 22 | 8 state-result closures + 14 transition-result closures | 100% | none | pass |
| renderSpec.elements[] | 37 | 37 unique selector and text/graphic/visibility checks | 100% | none | pass |
| dataBindings[] normal value | 28 | 28 named normal-target checks | 100% | none | pass |
| dataBindings[] fallback | 28 | 28 named fallback/error-target checks | 100% | none | pass |
| variants | 17 | 17 named variant closures | 100% | none | pass |
| Component-specific states + stacking combinations | 31 | 31 named component-state closures | 100% | none | pass |
| visualTokens actually consumed | 12 core token groups | 12 definition + consumption-site assertions | 100% | none | pass |
| colorSemantics color+shape+human-readable label | 15 semantic groups | 15 label + shape + explicit-color assertions | 100% | none | pass |
| Responsive + Reduce Motion | 4 | 3 tier geometry closures + 1 computed motion-fallback closure | 100% | none | pass |

- **Responsive modes (Large / Compact / Constrained)**: `body[data-responsive=large|compact|constrained]`; Large=max content boxes, Compact=default, Constrained=min with explicit component reflow.
- **Reduce Motion**: `true`; `body[data-reduce-motion=true]` removes scale/translation and limits state feedback to ≤110ms fade/instant change.

### 3.1 State / transition → scenario implementation mapping

| Source state / transition | Source Fact Anchor | Trigger steps | Stable selector | Expected visible result | Actual result | Verdict |
|---|---|---|---|---|---|---|
| S0_BOOTSTRAP | Interaction r7 §10 | S0 button / TR01 | `body[data-state="S0_BOOTSTRAP"]` | placeholders + restored shell | `stateChecks[S0]`: `--:--` and both placeholders visible. | pass |
| S1_AMBIENT_LIVE | Interaction r7 §10 | S1 button / TR03 | `body[data-state="S1_AMBIENT_LIVE"]` | full constellation + 实时 | `stateChecks[S1]`: live trust plus weather/AQI readings visible. | pass |
| S2_AMBIENT_CACHED | Interaction r7 §10 | S2 button / TR04 | `body[data-state="S2_AMBIENT_CACHED"]` | retained values + 缓存 | `stateChecks[S2]`: cached label and age observed. | pass |
| S3_MINIMAL_TIME | Interaction r7 §10 | S3 button / minimal toggle / TR12 | `body[data-state="S3_MINIMAL_TIME"]` | side windows hidden | `stateChecks[S3]`: weather/AQI hidden; control entry remains visible. | pass |
| S4_SECONDS_FOCUSED | Interaction r7 §10 | focus time / S4 / TR06 | `body[data-state="S4_SECONDS_FOCUSED"]` | seconds/veil/outline | `stateChecks[S4]`: seconds and focus outline visible. | pass |
| S5_CONTROLS_OPEN | Interaction r7 §10 | 控制 / S5 / TR08 | `body[data-state="S5_CONTROLS_OPEN"]` | Thick panel + dim ambient | `stateChecks[S5]`: control window and panel title visible. | pass |
| S6_REFRESHING | Interaction r7 §10 | refresh/city/S6/TR02 | `body[data-state="S6_REFRESHING"]` | retained data + static 更新中 | `stateChecks[S6]`: static refresh mark and label visible. | pass |
| S7_DEMO_OR_ERROR | Interaction r7 §10 | S7/TR05 + data mode | `body[data-state="S7_DEMO_OR_ERROR"]` | demo/error truth label | `stateChecks[S7]`: visible demo/error trust label observed. | pass |
| TR01 | Interaction r7 §10 | click TR01 | `[data-transition="TR01"]` | process→S0 | `transitionChecks[TR01]`: S0 plus `--:--` observed. | pass |
| TR02 | Interaction r7 §10 | click refresh/TR02 | `[data-transition="TR02"], [data-action="TR02"]` | S6 | `transitionChecks[TR02]`: S6 plus visible progress mark. | pass |
| TR03 | Interaction r7 §10 | click TR03 after S6 | `[data-transition="TR03"]` | S1 normal | `transitionChecks[TR03]`: S1 plus 实时. | pass |
| TR04 | Interaction r7 §10 | click TR04 after S6 | `[data-transition="TR04"]` | S2 fallback | `transitionChecks[TR04]`: S2 plus 缓存. | pass |
| TR05 | Interaction r7 §10 | click TR05 | `[data-transition="TR05"]` | S7 demo/error | `transitionChecks[TR05]`: S7 plus 演示数据. | pass |
| TR06 | Interaction r7 §10 | hover/focus time or click TR06 | `[data-transition="TR06"], .time-hero` | S4 | `transitionChecks[TR06]`: S4 plus seconds visible. | pass |
| TR07 | Interaction r7 §10 | blur/leave time or click TR07 | `[data-transition="TR07"]` | previous ambient | `transitionChecks[TR07]`: exits S4 and hides seconds. | pass |
| TR08 | Interaction r7 §10 | 控制 or TR08 | `[data-action="TR08"], [data-transition="TR08"]` | S5 | `transitionChecks[TR08]`: S5 control window visible. | pass |
| TR09 | Interaction r7 §10 | 关闭/完成/TR09 | `[data-action="TR09"], [data-transition="TR09"]` | prior ambient | `transitionChecks[TR09]`: exits S5 and hides control window. | pass |
| TR10 | Interaction r7 §10 | previous/next/TR10 | `[data-action^="TR10"], [data-transition="TR10"]` | city changes then S6 | `transitionChecks[TR10]`: city changes and S6 progress appears. | pass |
| TR11 | Interaction r7 §10 | slider/−/+ or TR11 | `[data-transition="TR11"], [data-binding="preferences.opacity"]` | clamped S5 preview | `transitionChecks[TR11]` plus opacity binding closure: S5 slider visible and clamped preview values observed. | pass |
| TR12 | Interaction r7 §10 | minimal toggle/TR12 | `[data-transition="TR12"]` | S3 | `transitionChecks[TR12]`: S3 and side window hidden. | pass |
| TR13 | Interaction r7 §10 | toggle full/TR13 | `[data-transition="TR13"]` | S0 | `transitionChecks[TR13]`: S0 and side window restored. | pass |
| TR14 | Interaction r7 §10 | TR14 then cancel/confirm | `[data-transition="TR14"], #exit_dialog` | blocking Dialog; cancel/exit paths | TR14 assertions: Dialog opens, cancel closes without exit, confirm produces `closed-simulation` and PROCESS_EXIT. | pass |

### 3.2 Component / renderSpec.elements[] → DOM implementation mapping

> One row per `renderSpec.elements[]` element; do not merge by component.

| Component.element id | Source Fact Anchor | Visible label / conditional hide | DOM selector | Visual / semantic role | Actual result | Verdict |
|---|---|---|---|---|---|---|
| TimeBeacon.time_text | Visual r3 §5 | HH:mm always | `[data-preview-id="time_text"]` | primary time | r2 render/state checks observe unique selector and `09:41`/bootstrap fallback. | pass |
| TimeBeacon.seconds_text | Visual r3 §5 | :ss in S4/focus | `[data-preview-id="seconds_text"]` | precise time | r2 render/state checks observe hidden ambient and visible focused seconds. | pass |
| TimeBeacon.date_text | Visual r3 §5 | localized date | `[data-preview-id="date_text"]` | context | r2 render/binding checks observe localized `8月14日`. | pass |
| TimeBeacon.weekday_text | Visual r3 §5 | weekday; fallback hidden | `[data-preview-id="weekday_text"]` | context | r2 render/binding checks observe `星期五` and hidden fallback. | pass |
| TimeBeacon.controls_action | Visual r3 §5 | 控制 always | `[data-preview-id="controls_action"]` | sole control entry | r2 render/state checks observe unique visible 控制 entry, including recovery states. | pass |
| TimeBeacon.focus_outline | Visual r3 §5 | S4 only | `[data-preview-id="focus_outline"]` | non-color focus | r2 render/state checks observe hidden ambient and visible S4 outline. | pass |
| Weather.condition_glyph | Visual r3 §5 | condition vector | `[data-preview-id="condition_glyph"]` | shape+color | r2 render/semantic checks observe glyph shape and explicit color. | pass |
| Weather.condition_label | Visual r3 §5 | 晴/阴/雨/雪 | `[data-preview-id="condition_label"]` | human semantic | r2 render/semantic checks observe all four labels. | pass |
| Weather.temperature_text | Visual r3 §5 | 26°/--° | `[data-preview-id="temperature_text"]` | primary metric | r2 render/binding checks observe normal and error text. | pass |
| Weather.humidity_text | Visual r3 §5 | 湿度 48%/-- | `[data-preview-id="humidity_text"]` | metric | r2 render/binding checks observe normal and partial/error text. | pass |
| Weather.weather_placeholder | Visual r3 §5 | loading/error only | `[data-preview-id="weather_placeholder"]` | fallback | r2 render/component-state checks observe hidden normal and visible loading/error placeholder. | pass |
| AQI.aqi_track | Visual r3 §5 | range track | `[data-preview-id="aqi_track"]` | scale | r2 render/component-state checks observe labeled track in usable state and hidden error track. | pass |
| AQI.aqi_arc | Visual r3 §5 | valid AQI | `[data-preview-id="aqi_arc"]` | quantitative | r2 render/binding/component-state checks observe labeled arc/value in valid state and error fallback path. | pass |
| AQI.aqi_shape | Visual r3 §5 | band marker | `[data-preview-id="aqi_shape"]` | redundant shape | r2 render/semantic checks observe band/error shapes. | pass |
| AQI.aqi_value | Visual r3 §5 | visible number inside ring | `[data-preview-id="aqi_value"]` | numeric metric | r2 render/binding checks observe `42` and `--` fallback. | pass |
| AQI.aqi_label | Visual r3 §5 | AQI level | `[data-preview-id="aqi_label"]` | human semantic | r2 render/semantic checks observe all human-readable bands and error label. | pass |
| AQI.aqi_placeholder | Visual r3 §5 | loading/error only | `[data-preview-id="aqi_placeholder"]` | fallback | r2 render/component-state checks observe hidden normal and visible loading/error placeholder. | pass |
| Freshness.trust_shape | Visual r3 §5 | always with trust | `[data-preview-id="trust_shape"]` | redundant trust cue | r2 render/semantic checks observe shape plus explicit color for trust states. | pass |
| Freshness.city_label | Visual r3 §5 | city | `[data-preview-id="city_label"]` | location truth | r2 render/binding checks observe human city label and city change. | pass |
| Freshness.trust_label | Visual r3 §5 | trust label | `[data-preview-id="trust_label"]` | data truth | r2 render/semantic checks observe live/cache/demo/error labels. | pass |
| Freshness.age_label | Visual r3 §5 | age/unknown | `[data-preview-id="age_label"]` | timeliness | r2 render/binding checks observe relative age and unknown fallback. | pass |
| Freshness.source_label | Visual r3 §5 | source | `[data-preview-id="source_label"]` | provenance | r2 render/binding checks observe Open-Meteo, demo, and unknown source text. | pass |
| Freshness.refresh_mark | Visual r3 §5 | S6 only | `[data-preview-id="refresh_mark"]` | non-loop progress | r2 render/state checks observe hidden ambient and visible static S6 mark. | pass |
| Freshness.retry_action | Visual r3 §5 | error/control focus | `[data-preview-id="retry_action"]` | recovery | r2 render/component-state checks observe hidden live and visible error recovery action. | pass |
| Controls.panel_title | Visual r3 §5 | 城市与显示 in S5 | `[data-preview-id="panel_title"]` | context | r2 control-element/state checks observe unique visible title in S5. | pass |
| Controls.control_freshness | Visual r3 §5 | trust summary S5 | `[data-preview-id="control_freshness"]` | trust | r2 control-element/binding checks observe live and error summaries. | pass |
| Controls.close_action | Visual r3 §5 | 关闭 S5 | `[data-preview-id="close_action"]` | exit | r2 control-element/transition checks observe visible action and dismiss result. | pass |
| Controls.previous_city_action | Visual r3 §5 | previous S5 | `[data-preview-id="previous_city_action"]` | navigation | r2 control-element/binding checks observe visible navigation and boundary disable. | pass |
| Controls.current_city_label | Visual r3 §5 | current city S5 | `[data-preview-id="current_city_label"]` | output | r2 control-element/binding checks observe visible current city and changed output. | pass |
| Controls.next_city_action | Visual r3 §5 | next S5 | `[data-preview-id="next_city_action"]` | navigation | r2 control-element/binding checks observe visible navigation and boundary disable. | pass |
| Controls.opacity_decrease | Visual r3 §5 | −; boundary disabled | `[data-preview-id="opacity_decrease"]` | adjustment | r2 control-element/component-state checks observe visible control and disabled boundary. | pass |
| Controls.opacity_slider | Visual r3 §5 | 25–100% | `[data-preview-id="opacity_slider"]` | adjustment | r2 control-element/binding checks observe visible slider and clamped values. | pass |
| Controls.opacity_increase | Visual r3 §5 | +; boundary disabled | `[data-preview-id="opacity_increase"]` | adjustment | r2 control-element/component-state checks observe visible control and disabled boundary. | pass |
| Controls.opacity_value | Visual r3 §5 | exact percent | `[data-preview-id="opacity_value"]` | output | r2 control-element/binding checks observe exact `60%`/`100%`. | pass |
| Controls.minimal_toggle | Visual r3 §5 | 仅显示时间 | `[data-preview-id="minimal_toggle"]` | mode | r2 control-element/binding checks observe label and S3 toggle result. | pass |
| Controls.refresh_action | Visual r3 §5 | refresh/更新中 | `[data-preview-id="refresh_action"]` | data action | r2 control-element/state checks observe enabled label and disabled 更新中 result. | pass |
| Controls.done_action | Visual r3 §5 | 完成 | `[data-preview-id="done_action"]` | return | r2 control-element/transition checks observe visible label and return path. | pass |

### 3.3 dataBindings[] → data and fallback implementation mapping

> One row per binding; the normal value and fallback must each be demonstrable.

| Source path | Target element / attribute | Display-type / semantic-type | Normal sample and trigger | fallback and trigger | DOM / JS evidence | Independent actual result | Verdict |
|---|---|---|---|---|---|---|---|
| ClockSnapshot.localTime | time text | display-only | normal/S1→09:41 | Time state error→--:-- | `data-binding="ClockSnapshot.localTime"`; `renderComponentOverrides` | Named `bindingChecks` closure observed both target texts. | pass |
| ClockSnapshot.seconds | seconds text/display | display-only | focus/TR06→:26 | ambient/TR07→hidden | `data-binding="ClockSnapshot.seconds"`; state CSS | Named closure observed focused visible and ambient hidden. | pass |
| ClockSnapshot.localDate | date text | display-only | normal→8月14日 | fallback retains numeric date | `data-binding="ClockSnapshot.localDate"` | Named closure observed localized date retained through fallback. | pass |
| ClockSnapshot.weekday | weekday text | display-only | normal→星期五 | formatter fallback conditional hide evidence | `data-binding="ClockSnapshot.weekday"` | Named closure observed normal weekday and hidden fallback. | pass |
| UiState.timeFocused | seconds/veil/outline | semantic | TR06/focus | controller-equivalent S4 button | `data-binding="UiState.timeFocused"`; `data-focused` | Named closure observed focused true and ambient false; state checks cover visible cues. | pass |
| UiState.opacity (time) | TimeWindow alpha | semantic | slider 60% | 25/100 clamp | CSS `--layer-alpha`; slider handler | Named opacity closures observed shared alpha and boundary values. | pass |
| WeatherSnapshot.temperatureC | temperature | display-only | normal→26° | error→--° | `data-binding="WeatherSnapshot.temperatureC"`; dataMode | Named closure observed normal and error text. | pass |
| WeatherSnapshot.relativeHumidity | humidity | display-only | normal→湿度48% | partial/error→湿度--% | corresponding data-binding + weatherState | Named closure observed normal and partial/error text. | pass |
| WeatherSnapshot.weatherCode | glyph/label/shape | semantic | condition select clear | error/unknown→cloudy square | two weatherCode selectors + `applyCondition` | Named closure plus semantic checks observed label/shape/color conversion. | pass |
| DataFreshness.state (weather) | placeholder/value state | semantic | normal/fresh | loading/error controls | placeholder selector + state override | Named closure observed hidden normal and visible error placeholder. | pass |
| UiState.opacity (weather) | WeatherWindow alpha | semantic | slider 60% | clamp 25–100% | shared CSS variable | Named opacity closure observed shared alpha consumption on visible window. | pass |
| AirQualitySnapshot.usAqi | value/arc | semantic quantitative | normal→42 | error→--/no valid band | arc/value data-binding + `applyAqi` | Named closure observed numeric normal and `--` error; AQI error state hides track/arc. | pass |
| AqiBand.key | color/shape/label | semantic | AQI band select | 999→数据异常 dashed | shape/label selectors + `aqiBand` | Named closure plus semantic checks observed normal/error label, shape, color. | pass |
| DataFreshness.state (AQI) | availability | semantic | normal | loading/cache/demo/error | `aqi_placeholder`; override | Named closure observed normal availability and error placeholder. | pass |
| UiState.opacity (AQI) | AqiWindow alpha | semantic | slider 60% | clamp 25–100% | shared CSS variable | Named opacity closure observed shared alpha consumption on visible window. | pass |
| City.displayName | city label | display-only | normal→北京 | city action cycles presets | `data-binding="City.displayName"` | Named closure observed human label and changed city. | pass |
| DataFreshness.state (badge) | trust shape/color/label/retry | semantic | normal→实时 circle | error→更新失败 triangle + retry | trust selectors + `applyTrust` | Named closure observed normal/error label, shape, and retry. | pass |
| DataFreshness.age | age label | display-only | normal→2分钟前 | error→时间未知 | age data-binding + dataMode | Named closure observed both age texts. | pass |
| DataFreshness.source | source label | display-only | Open-Meteo | demo→本地演示/error→来源未知 | source data-binding + dataMode | Named closure observed normal and demo source; render checks cover error source. | pass |
| DataFreshness.refreshing | refresh mark | semantic | S1 hidden | S6 shown 更新中 | refresh_mark + S6 CSS | Named closure observed hidden S1 and visible S6 mark. | pass |
| NetworkState | trust mapping | semantic | normal→live | fallback→缓存 | dataMode fallback + `applyTrust` | Named closure observed 实时 and 缓存 mappings. | pass |
| CityCatalog.items | neighbor actions | semantic navigation | four city cycle | control boundary disables | previous/next selectors + controlState | Named closure observed enabled normal and disabled boundary actions. | pass |
| selectedCityIndex | current city/actions | semantic | 北京 | previous/next wraps | current_city + `cityIndex` | Named closure observed current-city output change through wrapped index handler. | pass |
| preferences.opacity | slider/value/preview | semantic | 60% | invalid contract reset represented by default 60; boundary clamp | four opacity selectors + handler | Named closure observed `60%` and clamped boundary output. | pass |
| preferences.minimalMode | toggle/window visibility | semantic | unchecked/full | checked→S3 | minimal_toggle + handler | Named closure observed checked toggle entering S3. | pass |
| DataFreshness (control) | header/refresh | semantic | live summary | cache/demo/error samples | control_freshness + `applyTrust` | Named closure observed live and error control summaries. | pass |
| UiState.refreshing | refresh enabled/text | semantic | false/刷新天气 | control refreshing→disabled 更新中 | refresh_action + override | Named closure observed enabled normal and disabled 更新中. | pass |
| UiState.controlVisible | panel lifecycle | semantic | TR08/S5 visible | TR09 hidden; failed-open fallback embodied by persistent controls_action | ControlWindow state CSS | Named closure observed open, dismiss, and persistent control recovery entry. | pass |

### 3.4 variants / component-specific states → behavior implementation mapping

> One row per variant, component-specific state, and declared stacking combination; a top-level page state cannot replace this table.

| Component | variant / state / stacking combination | Source Fact Anchor | Trigger steps | Expected visual / behavior / accessibility result | DOM / JS evidence | Verdict |
|---|---|---|---|---|---|---|
| TimeBeacon | variant ambient | Visual r3 §5 | S1/S2/S3/S7 | seconds hidden, user alpha | body state CSS | pass |
| TimeBeacon | variant precise | Visual r3 §5 | TR06/timeState focused | seconds/veil/outline | data-state/data-focused | pass |
| TimeBeacon | variant minimal | Visual r3 §5 | TR12/toggle | side windows hidden | S3 CSS | pass |
| TimeBeacon | variant constrainedPrecise | Visual r3 §5 | Constrained + TR06 | seconds wraps under hero | responsive CSS + S4 | pass |
| TimeBeacon | boot | Visual r3 §5 | S0/timeState boot | --:-- shell | renderScene/override | pass |
| TimeBeacon | ambient | Visual r3 §5 | timeState ambient | no veil, seconds hidden | data-focused=false | pass |
| TimeBeacon | focused over ambient | Visual r3 §5 | focus | ≥.85 + precedence | focused CSS | pass |
| TimeBeacon | editing N/A | Visual r3 §5 | timeState editing-na | read-only note, control remains | state note + no edit target | pass |
| TimeBeacon | error boundary | Visual r3 §5 | timeState error | --:--; control active | override + controls_action | pass |
| Weather | four condition variants | Visual r3 §5 | condition select | glyph/shape/label changes | `applyCondition` | pass |
| Weather | partial variant | Visual r3 §5 | weatherState partial | missing humidity only | override | pass |
| Weather | constrained variant | Visual r3 §5 | Constrained | 3-row reflow | responsive CSS | pass |
| Weather | loading | Visual r3 §5 | weatherState loading/S0 | placeholder | override/S0 CSS | pass |
| Weather | fresh/aging | Visual r3 §5 | normal/freshState aging | metrics + semantic/trust | sample + trust override | pass |
| Weather | cached/demo | Visual r3 §5 | fallback/demo | values retained + trust | dataMode | pass |
| Weather | partial | Visual r3 §5 | partial | `湿度 --%` only | override | pass |
| Weather | editing N/A | Visual r3 §5 | editing-na | read-only evidence/no action | state note | pass |
| Weather | error/empty | Visual r3 §5 | error | placeholder `天气暂不可用` | override | pass |
| AQI | six-band variant | Visual r3 §5 | AQI select | number/arc/shape/label | `aqiBand/applyAqi` | pass |
| AQI | boundary variant | Visual r3 §5 | exact 51/101/151/201/301 | exact band transition | AQI select | pass |
| AQI | constrained variant | Visual r3 §5 | Constrained | 64dp two-column | responsive CSS | pass |
| AQI | loading | Visual r3 §5 | aqiState loading/S0 | track + placeholder | override/S0 CSS | pass |
| AQI | fresh/aging | Visual r3 §5 | normal/aging | full metric + trust | sample + trust state | pass |
| AQI | cached/demo | Visual r3 §5 | fallback/demo | metric retained + trust | dataMode | pass |
| AQI | boundary disabled | Visual r3 §5 | 999/boundary | 数据异常 dashed | applyAqi | pass |
| AQI | editing N/A | Visual r3 §5 | editing-na | read-only evidence | state note | pass |
| AQI | error/empty | Visual r3 §5 | error | -- + 暂不可用 | override | pass |
| Freshness | ambientFooter variant | Visual r3 §5 | ambient state | compact footer | Freshness DOM | pass |
| Freshness | controlHeader variant | Visual r3 §5 | S5 | exact summary + retry | control_freshness | pass |
| Freshness | refreshing variant | Visual r3 §5 | S6 | static 更新中 | S6 CSS | pass |
| Freshness | loading | Visual r3 §5 | freshState loading | dashed 正在获取 | applyTrust | pass |
| Freshness | fresh | Visual r3 §5 | freshState fresh | circle 实时 | applyTrust | pass |
| Freshness | aging | Visual r3 §5 | freshState aging | square 待更新 | applyTrust | pass |
| Freshness | cached/offline/stale | Visual r3 §5 | freshState cached | dashed 缓存 age | applyTrust | pass |
| Freshness | demo | Visual r3 §5 | freshState demo | diamond 演示数据 | applyTrust | pass |
| Freshness | error family | Visual r3 §5 | freshState error | triangle 更新失败 + retry | applyTrust | pass |
| Controls | fullMode variant | Visual r3 §5 | toggle off/TR13 | side windows return | TR13/S0 | pass |
| Controls | minimalMode variant | Visual r3 §5 | toggle on/TR12 | side windows hidden | S3 | pass |
| Controls | refreshing variant | Visual r3 §5 | controlState refreshing | refresh disabled 更新中 | override | pass |
| Controls | constrained variant | Visual r3 §5 | Constrained | 2-column scrollable | responsive CSS | pass |
| Controls | opening | Visual r3 §5 | TR08/controlState opening | panel visible/fade focus layer | S5 CSS/state dataset | pass |
| Controls | ready | Visual r3 §5 | S5/ready | controls enabled | base control DOM | pass |
| Controls | dragging opacity | Visual r3 §5 | slider/controlState dragging | percent/alpha update, over ready | input handler | pass |
| Controls | city editing | Visual r3 §5 | prev/next/controlState city | city immediate + S6 | TR10 handler | pass |
| Controls | refreshing | Visual r3 §5 | refresh/state | disabled action, other controls enabled | override | pass |
| Controls | boundary disabled | Visual r3 §5 | controlState boundary | −/+ disabled | override | pass |
| Controls | error/empty | Visual r3 §5 | controlState error | error summary/retry remains | control status + retry | pass |
| Controls | closing | Visual r3 §5 | close/done/back | panel hidden, Time control returns | TR09 | pass |

### 3.5 Responsive window tiers / Reduce Motion → reflow implementation mapping

| Scenario | Source Fact Anchor | Corresponding window tier / content area | Trigger method | Must preserve | Structural change / motion fallback | Actual result | Verdict |
|---|---|---|---|---|---|---|---|
| Large | Visual r3 §§5.0/5 | max: 828×268 / 688×308 / 548×288 / 912×572 | select `#responsive=large` | focus, tasks, ≥56 targets | larger ring/negative space; no global scale | Named closure observed time/weather/AQI/control/ring widths 860/720/580/960/112px. | pass |
| Compact | Visual r3 §§5.0/5 | default: 608×208 / 488×228 / 388×228 / 712×472 | select compact | same | regular 2-row/4-row structures | Named closure observed time/weather/AQI/control/ring widths 640/520/420/760/96px. | pass |
| Constrained | Visual r3 §§5.0/5 | min: 488×168 / 388×188 / 328×188 / 592×372 | select constrained | same | seconds/weather/AQI/control reflow; targets remain | Named closure observed time/weather/AQI/control/ring widths 520/420/360/640/64px. | pass |
| Reduce Motion | Interaction r7 §13 | N/A | check `#reduceMotion` | semantics/functional feedback | 110ms fade, no time scale/translation | Named closure observed reduceMotion=true, transform none, and 110ms transition. | pass |

## 4. Requirements Traceability Table requirementsTraceability

> Map each task item by item to state + component, ensuring full requirement coverage and traceability.

| Requirement / task | Priority | Mapped state | Mapped component | Validation method | Coverage status |
|---|---|---|---|---|---|
| current time/date/week + focused seconds (T1/T2) | P0 | S1/S3/S4 | TimeBeacon | preview audit: TR06/TR07 + time states | covered |
| temperature/humidity/condition (T3) | P0 | S1/S2/S7 | WeatherGlyphReadout | data/condition/state selectors | covered |
| AQI number/level dual channel (T3) | P0 | S1/S2/S7 | AqiRingReadout | band/boundary/error selectors | covered |
| distributed three-window constellation | P0 | S1/S2/S7 | four WindowContainer facsimiles | responsive tier visual audit | covered |
| opacity 25–100% (T4) | P0 | S5 | AmbientControlPanel | slider/−/+ | covered |
| four-city switching (T7) | P0 | S5/S6 | AmbientControlPanel + FreshnessBadge | previous/next/TR10 | covered |
| 30-minute/manual refresh semantics (T6) | P0 | S6 | FreshnessBadge + Controls | TR02–TR05; static progress | covered (schedule timing is runtime boundary) |
| minimal time-only mode (T4) | P0 | S3 | TimeBeacon + coordinator state | toggle/TR12/TR13 | covered |
| palm-up/gaze/controller equivalents (T2/T5) | P0 | S4/S5 | TimeBeacon + Controls | gaze focus + QA palm semantic/visible action | covered logically; physical input is device boundary |
| persistent low-interference visual style | P0 | ambient states | all | token/material/opacity audit | covered |
| live/cache/demo/error data trust | P0 | S1/S2/S7 | FreshnessBadge | data/freshness selectors | covered |

- **Requirement coverage rate**: 11/11 logical preview requirements = 100% declared.
- **Uncovered requirements (gaps)**: no Web-logic gap; real palm/gaze tracking, physical comfort, WorkManager timing and performance are explicitly deferred to runtime/device validation.

## 5. Sample Data sampleData

> Sample data must look like the display data returned by a real app backend: pure display fields are filled with human-readable copy, status/enum fields are translated via color-semantic labels, and machine enums are not echoed back.

| Source path | Normal sample | fallback / exception sample | Mapped element | Human-readable conversion |
|---|---|---|---|---|
| clock.localTime | 09:41 | --:-- | time_text | locale `HH:mm` |
| clock.seconds | :26 | hidden | seconds_text | two digits, focus-only |
| clock.localDate | 8月14日 | numeric localized date | date_text | user-readable Chinese date |
| clock.weekday | 星期五 | hidden, date retained | weekday_text | localized weekday |
| city.displayName | 北京 | bundled preset | city/current city | no raw ID/coordinates |
| temperatureC | 26° | --° | temperature_text | rounded Celsius |
| relativeHumidity | 湿度 48% | 湿度 --% | humidity_text | labeled integer percent |
| weather condition | 晴 circle | 阴 neutral square | glyph/label | machine code translated |
| usAqi | 42 | -- / 999 abnormal test | value/arc | integer + classifier |
| AqiBand | 优 circle | 暂不可用 dashed | shape/label | human label + shape/color |
| freshness.state | 实时 circle | 缓存 dashed / 更新失败 triangle / 演示数据 diamond | badge/header | no machine enum visible |
| freshness.age | 2分钟前 | 时间未知/非实时 | age_label | relative user-readable time |
| freshness.source | Open-Meteo | 本地演示/来源未知 | source_label | human source |
| preferences.opacity | 60% | default/reset 60%, clamp 25–100% | slider/value | integer percent |
| preferences.minimalMode | full | false fallback | toggle/windows | labeled switch |

## 6. Web Logic Consistency Tolerance (not physical tolerance)

| Tolerance Item | Range / standard |
|---|---|
| Logical geometric relationship | exact_id_relationship_match |
| Visual token reference | declared_group_reference_presence |
| Exclusions | screenshot_visual_diff, css_pixel_to_pico_physical_size, device_color_delta, web_pico_parity |

## 7. Device-Validation Boundary List (must be handed off to device validation, not performed at this stage)

> Device-validation status `deviceValidation.status`: `not_performed`. The following items cannot be closed-loop by Web preview.

| Validation Item | Ownership | Status |
|---|---|---|
| Physical viewing distance and arc-resolution readability | requires device validation | not_performed |
| Occlusion and central comfort zone | requires device validation | not_performed |
| Fatigue and sustained posture | requires device validation | not_performed |
| Hand and controller hit precision | requires device validation | not_performed |
| PICO runtime performance and safety behavior | requires device validation | not_performed |
| State / component / binding / token logical coverage | Web preview independent QA | pass — Web logical coverage only (`PREVIEW-REV-20260816-04`) |

### 7.1 Repair evidence and independent rerun for `PV-01`

- `design/preview-dom-test.mjs` r2 loads Preview r2 into `jsdom` at the synthetic local origin `https://preview.local/spaceweather`, executes its real inline script, dispatches DOM click/change/input/focus-equivalent events, and observes computed visibility, exact text, shape/color, disabled controls, focus, content-box widths, motion fallback and Dialog state. It does not navigate a browser to a blocked `file:` URL and does not claim visual, PICO, physical or device equivalence.
- The repaired harness now has an explicit assertion closure for each denominator item: 8 state result closures; 14 transition closures including TR14 block/cancel/confirm; 37 unique render-element closures with label/graphic/visibility behavior; 28 binding closures with normal plus fallback/error result; 17 variant closures; 31 component-state closures; 15 semantic color/shape/label closures; and Large/Compact/Constrained computed widths plus Reduce Motion computed transform/duration.
- Preview r2 also repairs two defects exposed by the stronger harness: the default Freshness component override now inherits the active data sample (so cached/demo/error states are not overwritten as fresh), and leaving bootstrap restores the normal time value. The time hero consumes the declared `s3` token rather than a duplicate literal.
- Generator command `pnpm test:preview` observed `verdict=pass` with counts `8 / 14 / 37 / 28 bindings / 17 variants / 31 component states / 15 semantic groups / 12 token groups / 4 responsive-motion` and zero captured `jsdomError`. This remains auxiliary generation evidence and does not predetermine the independent verdict.
- Independent invocation `PREVIEW-REV-20260814-02` inspected harness r1 and reran the exact configured command via the bundled `pnpm.cmd`; it exited 0 and printed the same counts. Source review found that the printed totals overstate assertion depth: 17 variants is a constant, 31 states assert only the QA note, 37 elements assert only uniqueness, four data modes assert only one binding, and responsive/motion asserts only body flags. This evidence supports the retained `block`, not visual or PICO parity.
- Independent invocation `PREVIEW-REV-20260815-03` inspected every r2 assertion family and verified that its count is derived from executable item closures with observable assertions, then reran `pnpm test:preview`; the command exited 0 with `8 / 14 / 37 / 28 / 17 / 31 / 15 / 12 / 4` and zero captured `jsdomError`. This closes the Web-logical evidence gap only.

### 7.2 Ordered Stage 13 rerun generation evidence

- Rerun receipt: `SELF01-R13`; source facts remain Interaction r7 + Visual r3 + design-system Critique r6.
- Preview r3 differs from r2 only by the explicit preview revision marker; no state, layout, component, binding, token, interaction, or responsive fact changed during this rerun.
- Harness r2 remains the active generation-side executable evidence and must be rerun before the independent `SELF01-R14` Preview Review.
- Device validation remains `not_performed`; this rerun is Web-design generation evidence only.
- Independent invocation `PREVIEW-REV-20260816-04` inspected Preview r3 + harness r2 against Preview QA r7, Interaction r7, Visual r3, and Critique r6, reran `pnpm test:preview`, and observed the complete `8 / 14 / 37 / 28 / 17 / 31 / 15 / 12 / 4` pass counts. This closes ordered receipt `SELF01-R14`; it does not claim runtime, screenshot, visual-parity, or device validation.

## 8. Finding Resolution

| ID | Severity | Finding / evidence | Exact resolution target | Owning stage | Status |
|---|---|---|---|---|---|
| PV-01 | blocking | Historical cause: harness r1 provided shallow, partly constant evidence. Resolution evidence: Preview r2 remains sourced from Interaction r7 + Visual r3; harness r2 supplies named executable observable-result closures for every denominator row; `PREVIEW-REV-20260815-03` independently inspected them, reran the configured test successfully, and reconciled §3.1–§3.5. | Resolution target met; no further Preview Review patch target. | Preview review | closed |

## 9. Delivery and Recipients

- **Deliverables**: preview coverage verification, requirements traceability, sample data, Web logic tolerance, device-validation boundary, defect list (this document is their human-readable source of fact)
- **Recipients**: engineering implementation team, Design Lead, PM

---

> Format convention: the input readiness table precedes generation; the five implementation-fidelity tables are filled in item by item; a name appearing does not count as coverage; a missing core element, actionable binding, fallback, or exception/safety/stable-exit state is block; the device-validation boundary is labeled not_performed; a Preview PASS must not be described as PICO runtime validation.
