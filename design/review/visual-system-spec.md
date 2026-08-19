# Visual System Spec · 空间时间气候层

> Role: `visual_designer` (visual direction) and `spatial_design_system_designer` (component synthesis, data trust reconciliation) | Workflow stage(s): `visual_direction` → `composition_synthesis` → `design_system` (layout / component / visual / data-trust facts) | Upstream inputs: selected concept, experience architecture, research evidence, quality contract, state graph, approved visual reference | Downstream recipients: Prototype / Frontend Engineer, QA, Design Lead
>
> This document carries this role's **LLM reasoning information** and **direct description of outputs**. It is not bound to any JSON Schema or validator error codes; mandatory gates are expressed through this document's structured Markdown required tables, evidence anchors, and the `block` status.

## 0. Reasoning Guidance (how this role reasons)

- **Only make visual and design-system decisions**: visual hierarchy, typography, color semantics, materials, component anatomy, responsive behavior, motion fallback. Do not define task priorities or state flows, and do not substitute for human approval.
- **Visual direction comes first**: before freezing design-system facts, generate and compare 2–3 spatial visual directions; the selected direction becomes the "approved visual reference." Subsequent design-system sections record that direction and do not reinvent the aesthetic. A direction that only swaps color / theme / icons / copy is not substantially different.
- **The visual language is derived from project semantics**: brand personality, environment, mood, risk, content density, physical metaphor, domain symbols; different domains cannot simply swap colors, and at least two rejected visual directions must be recorded.
- **Visuals and components are the source of truth for the implementation handoff**: express them with structured data (dimensions, ratios, Grid, state tables), **not prose**. Prose like "magenta + square border = critical" cannot be consumed stably by engineering. Any design change must be reflected in the delivery facts (swapping a hex / family must show up in the structured fields).
- **Components are derived from tasks, data, and interactions**: domain knowledge only provides terminology and rules, not a catalog that must be reused. Each core component declares its source task, source data, purpose, anatomy (including layout and sizing), data bindings, variants, states, layout role, and accessibility.
- **Data is runtime fidelity**: display-only fields carry human-readable copy; status/enum fields are translated via the color-semantic `label` and never echo the machine enum.
- **Prohibitions**: presenting project-derived rules as official PICO rules; scoring by visual similarity to a reference case; disguising review metadata such as design theses / layer names / component classifications / skeleton region names as end-user copy.

## 1. Direct Description of Outputs

This role delivers: **visual direction candidates and the selection (approved visual reference) → visual language (tokens / typography / color semantics / materials) → component specs (structured anatomy) → data display and semantic contract**. Each section below is the structured description of these outputs.

## 2. Spatial Visual Direction Candidates (2–3)

> Each direction defines a spatial thesis, first-view composition, container relationships, depth plan, information hierarchy, interaction cues, spatial value, Dashboard risk, and preview/render instructions. Directions that only swap color are rejected.

| Direction | Spatial Thesis | First-View Composition | Container Relationships | Depth Plan | Information Hierarchy | Interaction Cues | Spatial Value | Dashboard Risk |
|---|---|---|---|---|---|---|---|---|
| D1 — Airline Constellation | Environmental facts behave like fine instrument markings printed on air: almost no surface, precise type, one restrained symbol per reading. | 50sp thin time floats above center; weather appears left as outline condition glyph + temperature/humidity; AQI appears right as an incomplete hairline ring + number/label. Center remains empty. | Three independent readings share typography/opacity rhythm but no enclosing master card; control surface uses a distinct temporary Regular glass plane. | Persistent readings sit at one calm visual plane with no simulated extrusion; focus adds only ≤1.04 scale and a faint local backing. Controls are the only foreground layer. | time hero → weather metric → AQI metric → city/source/freshness captions; one primary reading per window. | Gaze/focus brightens text, reveals seconds, and grows only the focused time group; a small “controls” affordance appears at the lower edge on focus, with controller equivalent. | Direction and negative space make the constellation readable as one system without a dashboard frame. | Low; risk is insufficient contrast on complex passthrough, mitigated by focused backing/Vibrant and a 25% opacity floor. |
| D2 — Instrument Nebula | Each fact is a compact translucent instrument: time plaque, weather lozenge, AQI circular dial. | Three small glass forms visibly punctuate upper/side space, each with centered metric and icon; focus increases blur/thickness. | Strongly separated surfaces with a repeated card grammar; control panel visually extends the same family. | Persistent Thin glass objects; control panel Thick glass; visible depth hierarchy through blur. | metrics lead, labels subordinate, freshness grouped beneath weather/AQI. | Whole-card hover, pinch depression, dial progress and slider affordance. | Spatial separation remains clear and contrast is robust. | Medium–high; repeated glass cards resemble a spatial widget grid and increase visual mass. |
| D3 — Horizon Trace | A near-invisible horizontal coordinate trace links the three windows, making time/weather/AQI feel like one ambient horizon. | A thin ruled line visually traverses upper view; time sits at the apex, weather/AQI hang from endpoints; controls unfold below the time anchor. | Windows are coordinated by alignment and a visual trace, though the trace would need separate ownership or carefully matched edges. | Flat ambient trace; controls descend as a temporary nearer layer. | time apex, symmetric side facts, tiny trust annotations. | Gaze causes the local trace segment to pulse and exposes contextual controls. | Strong directional coherence and glance scanning. | Medium; connector can imply false precision, complicate multi-window placement, and become decorative motion/aliasing. |

- **Selected direction (approved visual reference; structured review pass)**: **D1 — Airline Constellation**. It best expresses “lightweight, transparent, technological and non-interfering,” preserves the selected Ambient Constellation's open center and stable directional anchors, avoids a repeated widget-card grammar, and keeps city/source/freshness captions continuously discoverable without giving them card-level visual weight. This direction is frozen as the visual source for subsequent design-system and implementation facts by review `VISUAL-EFFECT-20260814-01`.
- **Preview/render instruction**: render a Shared Space passthrough-like neutral room with no authored scenery; place the time window 12–18° above forward center, weather 25–35° left and AQI 25–35° right, all on one stable distance plane. Use ultra-light neutral sans, white at 60% ambient opacity, desaturated cyan only for focus/clear state, no gradients/images, no glowing bloom, and no opaque background. Show the focused-time state with seconds and a faint local contrast veil; show the control panel below center as the only explicit Regular glass surface.
- **Rejected visual directions (final)**: **D2 — Instrument Nebula** is rejected because its three repeated glass instruments equalize the surfaces, add persistent visual mass, and reproduce the widget-grid/dashboard grammar explicitly excluded by PM r3 §7 and UXR r2 §3, despite its robust contrast and clear whole-card affordances. **D3 — Horizon Trace** is rejected because its connector is decorative rather than domain-bearing, can imply false measurement precision, has unresolved ownership/alignment across independent Planar windows, and makes contextual controls less legible while introducing pulse/aliasing behavior that conflicts with Interaction r3 P3–P4 and UXR r2 §§8–10.

### Structured Design-Effect Review Record

| Field | Value |
|---|---|
| reviewerRole | `design_effect_critic` |
| invocationId | `VISUAL-EFFECT-20260814-01` |
| contextPolicy | `isolated_subagent` |
| reviewedRevision | `Interaction r3 + Visual direction draft r1` |
| evidenceRebuilt | `yes` |
| recommendation | `pass` |
| blockingFindings | `0` |

| Review dimension | D1 — Airline Constellation | D2 — Instrument Nebula | D3 — Horizon Trace | Comparative judgment |
|---|---|---|---|---|
| Spatial composition | Three stable upper/side anchors share one distance plane while leaving forward center empty; the temporary control plane alone advances. | Separation is clear, but three repeated glass objects increase persistent mass and visually refill the center-adjacent field. | The horizon creates a strong scan line, but cross-window continuity depends on fragile ownership and alignment. | D1 most directly realizes Interaction r3 P1 and §6's stable, open-center Ambient Constellation without fabricating 3D extrusion. |
| Visual hierarchy | Time is the hero; weather and AQI remain parallel secondary metrics; city/source/freshness form a persistent trust tier. | Repeated card grammar gives all three surfaces similar visual weight and makes trust metadata compete with instrument chrome. | Time has a clear apex, but “tiny” trust annotations are too easy to subordinate below truth needs. | D1 best preserves Interaction r3 P2/P5 and PM r3 §7: truth remains discoverable without becoming another primary card. |
| Domain expression | Clock type, outline weather glyph, temperature/humidity pair, incomplete AQI ring plus number/label, and freshness captions create distinct time/weather/air-quality semantics. | Plaque/lozenge/dial distinguish readings, but the repeated translucent-instrument family is closer to a generic spatial widget kit. | The coordinate trace supplies atmosphere but no weather/time/AQI meaning and may imply measurement precision the data does not have. | D1 has the strongest domain-bearing marks with the least generic chrome, matching UXR r2 §§3–4. |
| Interaction legibility | Focus brightening, seconds reveal, restrained scale, local contrast veil, focusable controls affordance, and controller equivalent produce visible state change and a reachable target. | Whole-card hover/depression and slider/dial cues are highly visible, but imply that every ambient reading is a large actionable card. | Pulsing trace/contextual controls make the target less stable and risk hiding the entry until a decorative response occurs. | D1 passes provided downstream facts retain a visible/focusable controls fallback and never make the palm-up gesture the sole entry, per Interaction r3 P3. |
| PICO nativeness | Uses Planar surfaces, system-visible transparency, restrained gaze/focus feedback, a Regular glass control plane, and equivalent controller activation without Stage or camera motion. | Glass hierarchy and hover are compatible, but persistent Thin/Thick card styling overstates shell-like chrome and weakens low-interference ambience. | Independent Planar windows do not naturally own one uninterrupted trace; matched edges and pulse behavior create brittle platform coupling. | D1 feels native through PICO interaction/material restraint rather than imitation of system chrome; it honors PM r3 §7 and UXR r2 §§8–10. |
| Aesthetic maturity | Typography, negative space, one restrained symbol per reading, limited cyan, and no gradients/glow create a coherent quiet instrument language. | Polished and legible, but familiar glass-card repetition feels less authored and more dashboard-like. | Poetic and distinctive, but the ruled connector is visually mannered and vulnerable to aliasing/misalignment. | D1 is the most resolved balance of ambience, precision, restraint, and product-specific character. |
| Implementation-handoff clarity | Gives first-view roles, angular anchors, stable distance plane, 50sp hero scale, ≤1.04 focus scale, 60% ambient opacity, 25% floor, focus material, color restraint, and explicit no-go effects. | Material tiers and card interactions are clear, but a generic repeated grammar would require later differentiation rules. | Explicitly admits unresolved connector ownership/alignment, so it is not a reliable source for independent-window implementation. | D1 is sufficiently concrete to seed subsequent tokens, component anatomy, placement, responsive, focus, and fallback facts without inventing a new direction. |

- **Failure-mode audit**: D1 is not a flat dashboard because it has no master frame or repeated enclosing cards; it has visible interaction feedback and a focusable controls target; its spatial value comes from stable direction, negative space, and a foreground-only temporary control plane rather than gratuitous depth; its weather/AQI/time marks and trust tier are domain-bearing. D2 triggers the dashboard-risk failure mode and D3 leaves interaction/implementation ownership unresolved, so neither can be approved.
- **Approval guardrails (non-blocking)**: downstream design facts must (1) keep the controls fallback visible/focusable and controller-equivalent, not hover- or gesture-exclusive; (2) preserve city/source/age/live-cached-demo labels and allow a focused contrast veil at low opacity; and (3) avoid converting D1 into three identical translucent cards or adding connector/pulse decoration. These guardrails clarify the approved reference and do not request a new visual direction.
- **Recommendation**: `pass` — approve **D1 — Airline Constellation** as the visual reference for subsequent design facts and implementation handoff. Evidence anchors: Interaction r3 §§2–6 (P1–P5, T1–T7, Ambient Constellation selection and 2D challenge); PM r3 §7 (open sight line, trust, Planar/SpatialUI constraints, originality); UXR r2 §§3–4, 6, 8–10 (dashboard avoidance, domain model, journey risks, eye-hand/comfort bounds); Visual direction draft r1 §2 (candidate rows and render instruction).

## 3. Design Tokens (the single contract between design and code)

> tokens, typography, color semantics, and materials are the source of truth for styling that downstream implementers consume verbatim; values must be precise (colors in hex).

| Token | Value | Semantics / Usage |
|---|---|---|
| accent | `#FF8FBCC8` | desaturated cyan focus, active controls and clear-weather line icon |
| surface | `#1F132029` | focused local contrast veil only; never a persistent root fill |
| brandPrimary | `#FFDCEFF3` | high-emphasis foreground and focus outline |
| textAmbient | `#99FFFFFF` | default 60% ambient content; multiplied by user opacity |
| textFocused | `#E6FFFFFF` | focused reading and active control text |
| textMuted | `#B38FA9B5` | city/source/freshness captions |
| danger | `#FFE38C95` | stale/error semantic only, paired with label + shape |
| radius | `r0 0 / rS 12 / rM 20 / rL 32dp` | component and control radii |
| spacing | `s1 4 / s2 8 / s3 16 / s4 24 / s5 32 / s6 48dp` | one shared spacing scale |
| opacityRange | `0.25…1.00; default 0.60` | persisted whole-layer alpha; focus may temporarily raise to 0.85 |

### 3.1 Typography hierarchy

> Each level: `family (grotesk/sans/mono/serif) · size · line · weight`. Implementers infer the display/title/metric/body/caption roles by descending size + mono for domain neutrality, and do not rely on specific key names (domain-custom key names such as asset/decision can also be consumed).

| Role / Key | family | size | line | weight |
|---|---|---|---|---|
| display | sans | 50sp | 60sp | Thin (100) |
| metric | mono | 32sp | 40sp | Light (300) |
| title | sans | 24sp | 32sp | Light (300) |
| body | sans | 17sp | 24sp | Regular (400) |
| caption | sans | 14sp | 20sp | Medium (500) |
| micro | mono | 12sp | 16sp | Medium (500) |

### 3.2 Color semantics colorSemantics (dual-channel: color + shape)

> Each item: `color(#hex) · shape · label · desc · aliases[]`. `shape` takes `circle/square/triangle/dashed/diamond` (a color-independent redundant encoding, required for accessibility); `aliases[]` lists all aliases of that semantic in the data (including localized copy, such as "Out of Stock" or "Pending Lock") for machine matching; `label` is the human-readable copy shown in the runtime UI (such as "Critical"), which replaces the visible text when a data value is matched rather than echoing the machine enum.

| Semantic Key | color (#hex) | shape | label (human-readable copy) | desc | aliases[] (machine matching, including Chinese aliases) |
|---|---|---|---|---|---|
| weather_clear | `#FF9AC5C9` | circle | 晴 | clear/mostly clear weather | `[clear, sunny, mostly_clear, 晴, 晴朗]` |
| weather_cloudy | `#FFAAB4BF` | square | 阴 | cloudy/overcast weather | `[cloudy, overcast, fog, 阴, 多云, 雾]` |
| weather_rain | `#FF81A9BD` | dashed | 雨 | drizzle/rain/thunderstorm | `[rain, drizzle, thunderstorm, 雨, 小雨, 大雨, 雷雨]` |
| weather_snow | `#FFC2D3DC` | diamond | 雪 | snow/snow showers | `[snow, snowfall, 雪, 小雪, 大雪]` |
| aqi_good | `#FF78B6A0` | circle | 优 | US AQI 0–50 |
| aqi_moderate | `#FFC6B56A` | square | 良 | US AQI 51–100 |
| aqi_sensitive | `#FFD49A68` | triangle | 轻度污染 | US AQI 101–150; sensitive groups may be affected |
| aqi_unhealthy | `#FFD17878` | diamond | 中度污染 | US AQI 151–200 |
| aqi_very_unhealthy | `#FFA984B1` | dashed | 重度污染 | US AQI 201–300 |
| aqi_hazardous | `#FF9F6670` | triangle | 严重污染 | US AQI 301–500 |
| trust_live | `#FF8FBCC8` | circle | 实时 | successful public-API result within 30 minutes | `[live, fresh, 实时, 最新]` |
| trust_aging | `#FFC6B56A` | square | 待更新 | successful result aged 30–60 minutes | `[aging, 待更新]` |
| trust_cached | `#FFD49A68` | dashed | 缓存 | last successful result over 60 minutes or shown offline | `[cached, stale, offline, 缓存, 离线]` |
| trust_demo | `#FFA984B1` | diamond | 演示数据 | bundled non-live fallback | `[demo, sample, 演示, 示例]` |
| trust_error | `#FFE38C95` | triangle | 更新失败 | latest refresh failed; prior values may remain | `[error, failed, partial, conflicting, permission_denied, 错误, 更新失败]` |

### 3.3 Materials

> Each item: `desc · treatment(matte/glass/opaque) · glassStyle(Thin/Regular/Thick/Thickest/none) · opacity`. The glass look is a **system capability of the PICO spatial platform**: the PICO Spatial SDK provides four glass background material tiers `Thin/Regular/Thick/Thickest` (increasing degree of blur behind the content, applied via `Modifier.backgroundMaterial(...)`; for a WindowContainer it is controlled by `enableMaterialBackground` and enabled by default). When `treatment=glass`, the `glassStyle` tier must be specified, and the implementer calls the system `Material.<tier>` directly at handoff. `matte`→ a solid card. The Web preview using `backdrop-filter: blur+semi-transparency` is only a preview approximation of the four-tier system glass, and is not equal to the real material on a PICO device.
>
> **Component-level backgrounds are optional, and a custom color and glass are mutually exclusive**: a component inside a window can have no background (none, falling directly onto the parent container), a custom color background (customColor, with the color set by the component, not limited to a solid color), **or** a glass background material (the four glassStyle tiers), but **the same component must not stack a custom color + glass at the same time**—pick one. The glass background material is **only available inside a WindowContainer**. Which one a component uses is declared in the "background" row of §5 "Anatomy · Internal Metrics".

| Material Name | desc | treatment | glassStyle | opacity |
|---|---|---|---|---|
| ambientWindow | system window glass retained; component roots paint no custom background | glass | Regular | system-managed; content token 0.60 |
| focusedVeil | local focused group uses system glass only, never custom color simultaneously | glass | Regular | system-managed; foreground 0.90 |
| controlWindow | temporary controls need stronger passthrough separation | glass | Thick | system-managed; foreground 0.96 |
| errorBacking | solid fallback used only when Vibrant/material contrast is unavailable | matte | none | 0.88 |

### 3.4 Scale (spacing / corner radius / icons, unified baseline)

> Component metrics must reference a unified scale and must not each write their own set of magic numbers. Spacing is based on 4/8dp. All padding / gap / radius / iconSize in the §5 component metrics table and the §5.0 in-window layout should reference the tier names here or their dp values.

| Scale | Tier → Value (dp) | Usage |
|---|---|---|
| spacing | xs 4 / s 8 / m 16 / l 24 / xl 32 | Component padding, gap between components, margin |
| radius | r0 0 / rS 12 / rM 20 / rL 32 | outline focus group, controls, control shell |
| iconSize | iS 20 / iM 28 / iL 40 / iXL 56 | status, weather, AQI and focus affordance icons |

## 4. Environment Adaptation Spec (hard spatial constraints)

- **No large blocks of high-saturation color in dark environments.**
- **Color does not carry semantics on its own**: color + shape/text dual-channel is mandatory.
- **Minimum font size and contrast at wearing distance**: 12sp micro, 14sp caption, 17sp body; CJK caption/body use Medium/Regular. A focused item rises to ≥0.85 foreground alpha and gains outline/material backing rather than merely increasing saturation.
- **Readability on glass / semi-transparent backgrounds**: persistent roots retain `Material.Regular` window glass and paint no opaque root. Focused groups choose `focusedVeil` glass without a custom-color background. The control window uses `Material.Thick`; if platform material/Vibrant is unavailable, it switches to `errorBacking`, never stacking matte and glass.
- **Vibrant Style**: app-level `com.pico.spatial.ui.isVibrant=true`. Monochrome time, weather and caption foregrounds use `Color.Vibrant` with `vibrantEffect(ultralight-equivalent)`; the control panel uses a lighter foreground over Thick glass. AQI semantic strokes keep explicit token colors plus labels/shapes and do not rely on Vibrant. No images or gradients occur.
- **Spatial state and background controllability**: Shared Space passthrough is uncontrolled; therefore focused data and all controls receive system material/Vibrant assistance. There is no Full Space or Stage branch.
- **Environment adaptation**: bright room → focus alpha/outline and Thick controls guarantee hierarchy; dark room → ambient alpha remains user-set and high-saturation blocks are prohibited; high-detail background → local system material strengthens only focused content. Automatic opacity changes are prohibited because they would override the user setting; adaptation affects backing/foreground contrast, not persisted opacity.

## 5. Component Definition Spec (structured anatomy, no prose)

> Each core component declares: source task, source data, purpose, layout role, priority, anatomy (layout + sizing), data bindings, variants, states. The component description must contain explicit dimensions (ratio or fixed value) and internal structure.
>
> **Structure is incompressible**: the "Component" block below must be fully copied for each core component. Do not merge multiple base fields into one row, do not compress `anatomy.layout` / `sizing` / `metrics` into a field value, and do not rewrite `renderSpec` / `dataBindings` / `variants` / `states` as untitled path strings or state enums. The shared state table can only supplement, not replace, a component's dedicated state table. Stage / 3D components only swap Grid for world anchors, local coordinates, orientation, and metric ranges, but the 8-section structure must still be preserved.

### 5.0 Window structure and in-window layout (structure diagram + dashed boxes, required)

The geometry authority is Interaction r6 §§9/14. All four shells are Planar with system depth 640dp, preserve the default `Material.Regular` background unless stated, use no TabBar/Toolbar/Subwindow/Augment, and never paint an opaque root.

| Window | Constrained shell → content box | Default shell → content box | Large shell → content box | Placed-component envelope check |
|---|---|---|---|---|
| TimeWindow, inset 16dp | 520×200 → 488×168 | 640×240 → 608×208 | 860×300 → 828×268 | TimeBeacon 456×152 / 576×192 / 796×252; default rows 144+8+56=208; all fit |
| WeatherWindow, inset 16dp | 420×220 → 388×188 | 520×260 → 488×228 | 720×340 → 688×308 | reading+gap+badge heights 136+8+44=188 / 152+8+52=212 / 192+8+56=256; max widths 372/472/672; all fit |
| AqiWindow, inset 16dp | 360×220 → 328×188 | 420×260 → 388×228 | 580×320 → 548×288 | reading+gap+badge heights 136+8+44=188 / 152+8+52=212 / 176+8+56=240; max widths 312/372/532; all fit |
| ControlWindow, inset 24dp | 640×420 → 592×372 | 760×520 → 712×472 | 960×620 → 912×572 | panel 576×356 / 696×456 / 896×556; all fit, with internal scroll only when Constrained text scaling overflows |

#### TimeWindow shell and grid

| Field | Content |
|---|---|
| Window / container name | `time_window` / default `TimeWindow` |
| form | Planar; system depth 640dp |
| Logical dimensions | 640×240dp default |
| min / max | 520×200dp Constrained / 860×300dp Large |
| Content safe inset contentInset | 16dp all sides (`s3`), matching Interaction r6 §9 |
| Docked attachment | none; 56dp “控制” target is in content |

```
┌──────────────────────── TimeWindow 640×240 ────────────────────────┐
│ inset 16dp   ┌╌╌╌ TimeBeacon · hero/date/seconds ╌╌╌┐             │
│              ┊             09:41                    ┊  row1 144dp  │
│              ┊       8月14日 · 星期五               ┊              │
│              └╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘             │
│                         ↕ s2 8dp                                  │
│                                     ┌╌ 控制 56×56 ╌┐ row2 56dp   │
│                                     └╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘               │
└───────────────────────────────────────────────────────────────────┘
```

- **Grid definition**: 1 column; rows 144dp + 8dp gap + 56dp, hero centered and control aligned end/bottom.
- **Region → component mapping**: hero + date + seconds + controls affordance → `TimeBeacon` (single component, no orphan region).
- **Region spacing**: `s2` between reading and affordance; internal gaps live in component metrics.
- **Reflow**: Large adds negative space only. Compact remains inline. Constrained moves focused seconds below `HH:mm`, merges date/week on one line, and retains 56dp control target; text never scales below tokens.

#### WeatherWindow shell and grid

| Field | Content |
|---|---|
| Window / container name | `weather_window` / `WeatherWindow` |
| form | Planar; system depth 640dp |
| Logical dimensions | 520×260dp default |
| min / max | 420×220dp Constrained / 720×340dp Large |
| Content safe inset contentInset | 16dp all sides (`s3`), matching Interaction r6 §9 |
| Docked attachment | none |

```
┌────────────────────── WeatherWindow 520×260 ──────────────────────┐
│ inset 16dp  ┌╌ WeatherGlyphReadout · icon + metrics ╌┐ row1 152  │
│             ┊  ○ 晴        26°    湿度 48%            ┊           │
│             └╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘           │
│                       ↕ s2 8dp                                   │
│             ┌╌ FreshnessBadge · city/source/age ╌┐ row2 52      │
│             ┊ 北京 · 实时 · 2分钟前 · Open-Meteo  ┊              │
│             └╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘              │
└───────────────────────────────────────────────────────────────────┘
```

- **Grid definition**: 1 column, 152dp reading + `s2` + 52dp trust footer; start aligned.
- **Region → component mapping**: main → `WeatherGlyphReadout`; footer → shared-instance `FreshnessBadge` with owner=`weather`.
- **Region spacing**: `s2`; component internal alignment uses `s3`/`s4`.
- **Reflow**: Large preserves two-column icon/metric grouping and adds air. Compact shortens source to “Open-Meteo”. Constrained stacks icon/condition above temperature/humidity and condenses footer to `北京 · 缓存 · 42分钟前`; source remains available on focus description.

#### AqiWindow shell and grid

| Field | Content |
|---|---|
| Window / container name | `aqi_window` / `AqiWindow` |
| form | Planar; system depth 640dp |
| Logical dimensions | 420×260dp default |
| min / max | 360×220dp Constrained / 580×320dp Large |
| Content safe inset contentInset | 16dp all sides (`s3`), matching Interaction r6 §9 |
| Docked attachment | none |

```
┌──────────────── AqiWindow 420×260 ─────────────────┐
│ inset 16dp ┌╌ AqiRingReadout · ring/value/label ╌┐ │ row1 152
│            ┊       ◯  42       优                ┊ │
│            └╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘ │
│                       ↕ s2 8dp                    │
│            ┌╌ FreshnessBadge · source/age ╌┐     │ row2 52
│            ┊ 北京 · 实时 · 2分钟前           ┊     │
│            └╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘     │
└───────────────────────────────────────────────────┘
```

- **Grid definition**: 1 column, 152dp reading + `s2` + 52dp trust footer; reading content centered.
- **Region → component mapping**: main → `AqiRingReadout`; footer → shared-instance `FreshnessBadge` with owner=`airQuality`.
- **Region spacing**: `s2`; label has `s2` gap from value.
- **Reflow**: Large ring grows to its declared Large tier, not the type. Compact keeps ring/value inline. Constrained uses a 64dp partial ring with number beside it and the short trust footer; AQI label is never hidden.

#### ControlWindow shell and grid

| Field | Content |
|---|---|
| Window / container name | `control_window` / `ControlWindow` |
| form | Planar; system depth 640dp; `Material.Thick` |
| Logical dimensions | 760×520dp default |
| min / max | 640×420dp Constrained / 960×620dp Large |
| Content safe inset contentInset | 24dp all sides (`s4`), matching Interaction r6 §9 |
| Docked attachment | none; temporary non-default window below TimeWindow |

```
┌──────────────────── ControlWindow 760×520 ───────────────────────┐
│ inset24 ┌╌ AmbientControlPanel.header · 城市/状态/关闭 ╌┐ 64     │
│         └╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘        │
│                               ↕ s3 16                            │
│         ┌╌ city previous / current / next targets ╌┐ 80          │
│         └╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘              │
│                               ↕ s3 16                            │
│         ┌╌ opacity − / slider / + / percent ╌┐ 80                │
│         └╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘              │
│                               ↕ s3 16                            │
│         ┌╌ mode toggle / refresh / dismiss actions ╌┐ 80         │
│         └╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘              │
└─────────────────────────────────────────────────────────────────┘
```

- **Grid definition**: 1 column; 64/80/80/80dp regions with `s3` gaps; remaining space is negative space, not extra content.
- **Region → component mapping**: all four regions are explicit subregions of the single `AmbientControlPanel` component; `FreshnessBadge` semantics are rendered in its header substate rather than a separate component instance.
- **Region spacing**: `s3`; target-to-target gaps `s2` or greater.
- **Reflow**: Large increases gutters. Compact wraps action row. Constrained uses two columns for city/actions, keeps every target ≥56dp, and enables internal vertical scrolling if text scale causes overflow; no text or target is uniformly shrunk.

### Component: TimeBeacon

| Field | Content |
|---|---|
| Source task derivedFromTasks | `T1`, `T2`, `T5` |
| Source data derivedFromData | `ClockSnapshot`, `UiState.secondsVisible`, `UiState.controlsVisible` |
| Purpose | let the user decide whether to act now, reveal seconds on focus, and retain a reachable settings entry |
| Layout role layoutRole | `primary_hero` in TimeWindow |
| Priority | primary |
| Runtime role runtimeRole | `ambientTimeMetric` |

**Anatomy · Layout (anatomy.layout)**

```text
┌─────────────────────────────────────────────┐
│ [heroTime HH:mm] [seconds :ss when focused] │ centered baseline
│                 ↕ s2                        │
│ [date M月d日] · [weekday EEEE]              │ centered
│                              [控制 56×56]    │ end/bottom
└─────────────────────────────────────────────┘
```

- **Grid / spatial region definition**: one column with hero row, `s2`, date row, flexible spacer and bottom-end affordance; seconds share the hero baseline at Regular/Large and move under it at Constrained. Hero/date are centered; the affordance is end-aligned and never overlaps text.

**Anatomy · Sizing (sizing)**

| Component tier | Owning WindowContainer tier | Outer size | Internal layout change | Fits content area? |
|---|---|---|---|---|
| Regular | TimeWindow default | 576×192dp | seconds inline; date/week inline | yes |
| Compact | near default | 520×184dp | tighter negative space only | yes |
| Constrained | TimeWindow min 520×200 | 456×152dp | seconds below hero; merged date/week; affordance uses unused bottom-end area | yes |
| Large | TimeWindow max 860×300 | 796×252dp | no new data; more negative space | yes |

**Anatomy · Internal Metrics (metrics)**

| Metric | Token / precise value | Notes |
|---|---|---|
| background | `none`; focused hero alone uses `glassStyle(Regular)` | custom color is never combined with glass |
| radius | `rM 20dp` focused veil; `rL 32dp` control target | §3.4 |
| padding | outer `s3 16dp`; focused veil `s2 8dp × s3 16dp` | §3.4 |
| gap | hero/date `s2 8dp`; time/seconds `s1 4dp` | §3.4 |
| stroke | 1dp `brandPrimary` focus outline; ambient 0dp | non-color focus cue |
| icon / graphic | controls glyph `iM 28dp` | tintable |
| primary text | `display 50/60sp Thin`; seconds `metric 32/40sp Light` | §3.1 |
| secondary text | `body 17/24sp Regular` | §3.1 |
| hitTarget | controls 56×56dp; focused hero min 240×72dp | ≥§8 |

**Render Elements renderSpec.elements[]**

| id | visible label | type | bind | state / semantic role |
|---|---|---|---|---|
| `time_text` | current `HH:mm` | text | `clock.localTime` | primary exact-to-minute reading |
| `seconds_text` | `:ss` | text | `clock.seconds` | focus-only precision; polite live region |
| `date_text` | localized date | text | `clock.localDate` | display context |
| `weekday_text` | localized weekday | text | `clock.weekday` | display context |
| `controls_action` | 控制 | icon button + label | `actions.openControls` | focusable settings fallback |
| `focus_outline` | 时间已聚焦 | outline | `ui.timeFocused` | non-color focus cue |

**Data Bindings dataBindings[]**

| Source path | Target element / property | Fallback behavior | Display-only / semantic |
|---|---|---|---|
| `ClockSnapshot.localTime` | `time_text.text` | `--:--` only on formatter error | display-only |
| `ClockSnapshot.seconds` | `seconds_text.text` | hide seconds, keep minute time | display-only |
| `ClockSnapshot.localDate` | `date_text.text` | localized numeric date | display-only |
| `ClockSnapshot.weekday` | `weekday_text.text` | omit weekday but keep date | display-only |
| `UiState.timeFocused` | seconds, veil, outline, alpha | controller focus provides same state | semantic interaction |
| `UiState.opacity` | root content alpha | clamp 0.25…1.00; focus temporarily max 0.85 | semantic presentation |

**Variants variants**

| Variant | Structural / behavioral difference | Use condition |
|---|---|---|
| `ambient` | minute time + date/week; no seconds veil | default full/minimal state |
| `precise` | seconds join hero and focused veil appears | gaze/controller focus |
| `minimal` | same TimeBeacon; other windows hidden | minimal mode |
| `constrainedPrecise` | seconds on second line | min width + focused |

**States states**

| State | Trigger | Visual parameters | Size change | Motion | Accessibility | Stacking precedence |
|---|---|---|---|---|---|---|
| boot | clock flow not emitted | `--:--`, localized date shell | none | none | announces “正在读取时间” once | below focus |
| ambient | ticker active, no focus | user alpha, no veil | none | 180ms from focus | description includes date/week | base |
| focused | gaze/controller focus | alpha ≥0.85, outline, seconds, ≤1.04 scale | seconds inline/below by tier | 120/180ms; Reduce Motion no scale | outline + spoken seconds | above ambient; below controls |
| editing N/A | time is not user-editable | no primitive | none | none | system time is authoritative | N/A |
| empty/error boundary | formatter exception | `--:--`; control remains active | none | immediate | “时间暂不可用” | error overrides ambient, not controls |

### Component: WeatherGlyphReadout

| Field | Content |
|---|---|
| Source task derivedFromTasks | `T3`, `T6`, `T7` |
| Source data derivedFromData | `WeatherSnapshot`, `City`, `DataFreshness` |
| Purpose | support a 1–3 second decision about current outdoor temperature, humidity and condition for the selected city |
| Layout role layoutRole | `primary_metric` in WeatherWindow |
| Priority | primary |
| Runtime role runtimeRole | `weatherConditionMetric` |

**Anatomy · Layout (anatomy.layout)**

```text
┌──────────────────────────────────────────┐
│ [condition glyph 40] [condition label]   │ row A
│ [temperature 32sp]   [humidity label %]  │ row B, s4 between metrics
└──────────────────────────────────────────┘
```

- **Grid / spatial region definition**: two rows and two content columns; glyph/condition occupy row A, temperature/humidity row B. Start aligned, `s3` row gap, `s4` metric gap. Constrained reflows to three rows: glyph+condition, temperature, humidity.

**Anatomy · Sizing (sizing)**

| Component tier | Owning WindowContainer tier | Outer size | Internal layout change | Fits content area? |
|---|---|---|---|---|
| Regular | WeatherWindow default | 472×152dp | 2×2 grouping | yes |
| Compact | intermediate | 400×144dp | compact humidity label | yes |
| Constrained | WeatherWindow min | 372×136dp | three rows; trust remains footer | yes |
| Large | WeatherWindow max | 672×192dp | glyph iXL; no extra forecast | yes |

**Anatomy · Internal Metrics (metrics)**

| Metric | Token / precise value | Notes |
|---|---|---|
| background | `none` | inherits ambientWindow only |
| radius | `r0 0dp` | no card |
| padding | `s2 8dp` | §3.4 |
| gap | rows `s3 16dp`; metric columns `s4 24dp` | §3.4 |
| stroke | glyph 1.5dp semantic token; no border | condition shape remains visible |
| icon / graphic | `iL 40dp`; Large `iXL 56dp` | tintable vector |
| primary text | temperature `metric 32/40sp`; condition `title 24/32sp` | §3.1 |
| secondary text | humidity `body 17/24sp` | §3.1 |
| hitTarget | N/A: read-only; owning TimeWindow control target is 56dp | no hidden action |

**Render Elements renderSpec.elements[]**

| id | visible label | type | bind | state / semantic role |
|---|---|---|---|---|
| `condition_glyph` | weather shape | tintable vector | `weather.conditionSemantic` | color + shape encoding |
| `condition_label` | 晴/阴/雨/雪 | text | `weather.conditionSemantic.label` | human-readable semantic |
| `temperature_text` | e.g. `26°` | text | `weather.temperatureC` | primary metric |
| `humidity_text` | e.g. `湿度 48%` | text | `weather.relativeHumidity` | secondary metric |
| `weather_placeholder` | 天气读取中 | text | `freshness.state` | loading/empty primitive |

**Data Bindings dataBindings[]**

| Source path | Target element / property | Fallback behavior | Display-only / semantic |
|---|---|---|---|
| `WeatherSnapshot.temperatureC` | temperature text | `--°` when null/out of range | display-only |
| `WeatherSnapshot.relativeHumidity` | humidity text | `湿度 --%` | display-only |
| `WeatherSnapshot.weatherCode` | glyph, label, color/shape | unmapped code → `阴` neutral square; raw code hidden | semantic |
| `DataFreshness.state` | placeholder/value alpha | last cache/demo remains during refresh | semantic |
| `UiState.opacity` | component alpha | clamp; focus/readability contract applies | semantic presentation |

**Variants variants**

| Variant | Structural / behavioral difference | Use condition |
|---|---|---|
| `clear`, `cloudy`, `rain`, `snow` | glyph geometry, label and semantic token change; metric layout stable | WMO classifier |
| `partial` | known values remain, missing metric uses `--` | one invalid/missing field |
| `constrained` | vertical metric reflow | min width/text scale |

**States states**

| State | Trigger | Visual parameters | Size change | Motion | Accessibility | Stacking precedence |
|---|---|---|---|---|---|---|
| loading | no snapshot yet | static placeholder + reserved metric spaces | none | none | “正在读取天气” | base until data |
| fresh/aging | live snapshot | semantic glyph + values | none | 180ms crossfade | combined condition/temp/humidity | above loading |
| cached/demo | fallback snapshot | values remain; footer owns trust label | none | 180ms crossfade | trust included in window description | above live styling when active |
| partial | one field missing | `--` only for missing field; error in footer | none | immediate | announces unavailable field | above normal value |
| editing/dragging N/A | read-only metric | no primitive | none | none | editing occurs in controls | N/A |
| error/empty | no usable snapshot | neutral square + `天气暂不可用`; no invented metric | text row replaces metrics | immediate | explicit text, no color-only cue | highest within reading |

### Component: AqiRingReadout

| Field | Content |
|---|---|
| Source task derivedFromTasks | `T3`, `T6`, `T7` |
| Source data derivedFromData | `AirQualitySnapshot.usAqi`, `AqiBand`, `DataFreshness` |
| Purpose | show AQI number and understandable level without relying on color alone |
| Layout role layoutRole | `critical_primary` in AqiWindow |
| Priority | primary |
| Runtime role runtimeRole | `airQualityMetric` |

**Anatomy · Layout (anatomy.layout)**

```text
┌──────────────────────────────────────┐
│ [partial ring 96×96 + shape marker]  │
│          [AQI value 42]              │ centered in ring
│          [level label 优]             │ below, s2
└──────────────────────────────────────┘
```

- **Grid / spatial region definition**: centered one-column overlay grid: ring and number share a 96dp square; label sits below at `s2`. Constrained becomes two columns—64dp ring+number left, label right—to preserve text floors.

**Anatomy · Sizing (sizing)**

| Component tier | Owning WindowContainer tier | Outer size | Internal layout change | Fits content area? |
|---|---|---|---|---|
| Regular | AqiWindow default | 372×152dp | 96dp centered ring | yes |
| Compact | intermediate | 336×144dp | 80dp ring | yes |
| Constrained | AqiWindow min | 312×136dp | 64dp ring + horizontal label | yes |
| Large | AqiWindow max | 532×176dp | 112dp ring; metric type unchanged | yes |

**Anatomy · Internal Metrics (metrics)**

| Metric | Token / precise value | Notes |
|---|---|---|
| background | `none` | no card or glass/custom stacking |
| radius | `r0 0dp` | ring is a data graphic |
| padding | `s2 8dp` | §3.4 |
| gap | ring-to-label `s2 8dp` | §3.4 |
| stroke | 3dp semantic arc + 1dp neutral track; marker uses shape token | dual encoding |
| icon / graphic | 96dp Regular / 80 Compact / 64 Constrained / 112 Large | partial ring |
| primary text | `metric 32/40sp Light` | §3.1 |
| secondary text | `body 17/24sp Medium` plus shape indicator | §3.1 |
| hitTarget | N/A: read-only; ring has no gesture | no hidden action |

**Render Elements renderSpec.elements[]**

| id | visible label | type | bind | state / semantic role |
|---|---|---|---|---|
| `aqi_track` | AQI范围 | circular track | static | scale background |
| `aqi_arc` | AQI进度 | partial arc | `airQuality.normalizedAqi` | quantitative graphic capped at 500 |
| `aqi_shape` | semantic shape marker | vector marker | `airQuality.band.shape` | color-independent band encoding |
| `aqi_value` | e.g. `42` | text | `airQuality.usAqi` | primary numeric metric |
| `aqi_label` | 优/良/轻度污染… | text | `airQuality.band.label` | human-readable level |
| `aqi_placeholder` | 空气质量读取中 | text | `freshness.state` | loading/empty primitive |

**Data Bindings dataBindings[]**

| Source path | Target element / property | Fallback behavior | Display-only / semantic |
|---|---|---|---|
| `AirQualitySnapshot.usAqi` | value and normalized arc | clamp graphic 0…500; invalid/null → no arc and `--` | semantic quantitative |
| `AqiBand.key` | arc color, marker shape, label | unclassified → `trust_error` + `暂不可用` | semantic |
| `DataFreshness.state` | placeholder/value availability | last cache/demo remains with badge | semantic |
| `UiState.opacity` | component alpha | clamp; label remains visible | semantic presentation |

**Variants variants**

| Variant | Structural / behavioral difference | Use condition |
|---|---|---|
| six AQI bands | arc token + marker shape + exact Chinese label change | classifier 0…500 |
| `boundary` | new band starts exactly at 51/101/151/201/301 | classifier boundaries |
| `constrained` | ring/value left, label right | min width/text scale |

**States states**

| State | Trigger | Visual parameters | Size change | Motion | Accessibility | Stacking precedence |
|---|---|---|---|---|---|---|
| loading | no snapshot | static track + placeholder; no fake value | none | none | “正在读取空气质量” | base |
| fresh/aging | valid AQI | arc + marker + number + label | none | 180ms arc interpolation | says number and label | above loading |
| cached/demo | valid fallback | same metric; footer labels trust | none | 180ms crossfade | trust included | above live styling when active |
| boundary disabled | value <0 or >500 | graphic clamps; label `数据异常`; value not asserted valid | none | immediate | explicit abnormal text | above bands |
| editing/dragging N/A | read-only metric | no primitive | none | none | no hidden adjustment | N/A |
| error/empty | null/unclassifiable | neutral dashed track + `空气质量暂不可用` | text may replace ring | immediate | shape + text | highest within reading |

### Component: FreshnessBadge

| Field | Content |
|---|---|
| Source task derivedFromTasks | `T3`, `T6`, `T7` |
| Source data derivedFromData | `City`, `DataFreshness`, `CachedEnvironment`, source/update timestamps/network state |
| Purpose | make city, source, data age and live/cache/demo/error trust visible without competing with metrics |
| Layout role layoutRole | `status_footer` in WeatherWindow/AqiWindow; summary in ControlWindow header |
| Priority | secondary |
| Runtime role runtimeRole | `dataTrustBadge` |

**Anatomy · Layout (anatomy.layout)**

```text
┌──────────────────────────────────────────────┐
│ [shape] [city] · [trust label] · [age]       │ row1
│         [source] [retry only on error]       │ row2 optional
└──────────────────────────────────────────────┘
```

- **Grid / spatial region definition**: two-column prefix shape (20dp) + flexible text, maximum two rows. Regular may show source on row two; Constrained keeps city/trust/age on one wrapped line and moves full source into its accessibility description/control header.

**Anatomy · Sizing (sizing)**

| Component tier | Owning WindowContainer tier | Outer size | Internal layout change | Fits content area? |
|---|---|---|---|---|
| Regular | Weather/AQI default | owner width × 52dp | two rows available | yes |
| Compact | intermediate | owner width × 48dp | source abbreviated | yes |
| Constrained | owner min | owner width × 44dp | one wrapped line; source in description | yes |
| Large | owner max/control header | owner width × 56dp | exact update time visible | yes |

**Anatomy · Internal Metrics (metrics)**

| Metric | Token / precise value | Notes |
|---|---|---|
| background | `none` | inherits owning window |
| radius | `rS 12dp` only around focused retry | no persistent badge pill |
| padding | vertical `s1 4dp`, horizontal `s2 8dp` | §3.4 |
| gap | shape/text `s2 8dp`; clauses `s1 4dp` | §3.4 |
| stroke | 1.5dp semantic shape; 1dp retry focus outline | dual encoding |
| icon / graphic | `iS 20dp` semantic shape/static refresh mark | §3.4 |
| primary text | `caption 14/20sp Medium` | §3.1 |
| secondary text | `micro 12/16sp Medium` | §3.1 |
| hitTarget | retry 56×56dp when present; read-only badge N/A | ≥§8 |

**Render Elements renderSpec.elements[]**

| id | visible label | type | bind | state / semantic role |
|---|---|---|---|---|
| `trust_shape` | status shape | vector | `freshness.semantic.shape` | redundant trust cue |
| `city_label` | selected city | text | `city.displayName` | location truth |
| `trust_label` | 实时/待更新/缓存/演示数据/更新失败 | text | `freshness.semantic.label` | trust state |
| `age_label` | e.g. `2分钟前` | text | `freshness.age` | timeliness |
| `source_label` | Open-Meteo / 本地演示 | text | `freshness.source` | provenance |
| `refresh_mark` | 更新中 | static icon + text | `freshness.refreshing` | non-blocking progress |
| `retry_action` | 重试 | text button | `actions.refresh` | recovery inside controls/focused footer |

**Data Bindings dataBindings[]**

| Source path | Target element / property | Fallback behavior | Display-only / semantic |
|---|---|---|---|
| `City.displayName` | city label | preset display name; never coordinates/internal ID | display-only |
| `DataFreshness.state` | shape/color/label/retry | unknown → `更新失败` triangle | semantic |
| `DataFreshness.age` | age label | `时间未知`; trust state remains | display-only |
| `DataFreshness.source` | source label | cache retains origin; demo → `本地演示` | display-only |
| `DataFreshness.refreshing` | static mark/text | retain current data | semantic |
| `NetworkState` | offline mapping | cache is visibly `缓存` while offline | semantic |

**Variants variants**

| Variant | Structural / behavioral difference | Use condition |
|---|---|---|
| `ambientFooter` | read-only city/state/age/source | weather/AQI windows |
| `controlHeader` | exact update time + actionable retry | control panel |
| `refreshing` | static mark and `更新中`; no spinner | single-flight refresh |

**States states**

| State | Trigger | Visual parameters | Size change | Motion | Accessibility | Stacking precedence |
|---|---|---|---|---|---|---|
| loading | initial request/no fallback decision | dashed shape + `正在获取` + source | none | none | polite status | base |
| fresh | age <30m, online success | cyan circle + `实时` + age/source | none | 180ms crossfade | full source/time | above loading |
| aging | 30–60m | yellow square + `待更新` | none | crossfade | label names state | above fresh |
| cached/offline/stale | cache or age ≥60m | orange dashed + `缓存` + exact age | optional row | crossfade | “缓存，更新于…” | above aging |
| demo | no usable success | purple diamond + `演示数据` + `本地演示` | none | crossfade | explicitly not live | above cached |
| partial/conflicting/permission_denied/error | failed/inconsistent | red triangle + `更新失败`; optional retry | retry target only in control/focus | immediate | error + retained age | highest; never replaces metric value with fake data |

### Component: AmbientControlPanel

| Field | Content |
|---|---|
| Source task derivedFromTasks | `T4`, `T5`, `T6`, `T7`, `T8` |
| Source data derivedFromData | `CityCatalog`, `AmbientPreferences`, `DataFreshness`, `UiState` |
| Purpose | provide the visible equivalent route for city, opacity, minimal mode, refresh and dismissal |
| Layout role layoutRole | `primary_control_surface` in ControlWindow |
| Priority | primary |
| Runtime role runtimeRole | `ambientSettingsControl` |

**Anatomy · Layout (anatomy.layout)**

```text
┌─────────────────────────────────────────────────────────┐
│ [城市与显示] [freshness summary]            [关闭 56]   │ header
│ [上一城市 56] [当前城市] [下一城市 56]                  │ city row
│ [− 56] [透明度 slider flexible] [+ 56] [60%]            │ opacity row
│ [仅显示时间 toggle] [刷新天气 56] [完成 56]             │ action row
└─────────────────────────────────────────────────────────┘
```

- **Grid / spatial region definition**: four rows, one flexible column. Header has title/status/end action; city and opacity rows have fixed 56dp edge targets plus a flexible center; action row wraps at Compact. Constrained uses a two-column grid and internal vertical scroll; stable focus order follows Interaction §12.

**Anatomy · Sizing (sizing)**

| Component tier | Owning WindowContainer tier | Outer size | Internal layout change | Fits content area? |
|---|---|---|---|---|
| Regular | ControlWindow default | 696×456dp | four rows, horizontal controls | yes |
| Compact | intermediate | 640×420dp | actions wrap; freshness one line | yes |
| Constrained | ControlWindow min | 576×356dp viewport | two-column reflow + vertical scroll | yes; targets stay ≥56dp |
| Large | ControlWindow max | 896×556dp | larger gutters, same data | yes |

**Anatomy · Internal Metrics (metrics)**

| Metric | Token / precise value | Notes |
|---|---|---|
| background | `none` inside `controlWindow` Material.Thick | no custom color + glass stack |
| radius | controls `rM 20dp`; selected toggle `rL 32dp` | §3.4 |
| padding | component `s4 24dp`; window safe inset is `s4 24dp` | §3.4 |
| gap | rows `s3 16dp`; targets `s2 8dp` | §3.4 |
| stroke | 1dp neutral; focused 2dp `brandPrimary` | non-color focus cue |
| icon / graphic | actions `iM 28dp`; status `iS 20dp` | §3.4 |
| primary text | title `title 24/32sp`; city/value `body 17/24sp` | §3.1 |
| secondary text | labels `caption 14/20sp`; percent `micro 12/16sp` | §3.1 |
| hitTarget | every action, slider thumb and toggle ≥56×56dp | ≥§8 |

**Render Elements renderSpec.elements[]**

| id | visible label | type | bind | state / semantic role |
|---|---|---|---|---|
| `panel_title` | 城市与显示 | text | static | context |
| `control_freshness` | city/trust/updated/source | status summary | `environment.freshness` | trust context |
| `close_action` | 关闭 | icon button | `actions.dismissControls` | stable exit |
| `previous_city_action` | 上一个城市 | button | `actions.previousCity` | ordered navigation |
| `current_city_label` | current city | text | `preferences.selectedCity` | selection output |
| `next_city_action` | 下一个城市 | button | `actions.nextCity` | ordered navigation |
| `opacity_decrease` | 降低透明度 | button | `actions.decreaseOpacity` | 5% adjustment |
| `opacity_slider` | 信息层透明度 | slider | `preferences.opacity` | 25–100% edit |
| `opacity_increase` | 提高透明度 | button | `actions.increaseOpacity` | 5% adjustment |
| `opacity_value` | e.g. `60%` | text | `preferences.opacity` | exact setting |
| `minimal_toggle` | 仅显示时间 | switch/toggle | `preferences.minimalMode` | full/minimal decision |
| `refresh_action` | 刷新天气 | button | `actions.refresh` | manual single-flight refresh |
| `done_action` | 完成 | button | `actions.dismissControls` | stable return |

**Data Bindings dataBindings[]**

| Source path | Target element / property | Fallback behavior | Display-only / semantic |
|---|---|---|---|
| `CityCatalog.items` | neighbor labels/enabled state | bundled four-city catalog | semantic navigation |
| `AmbientPreferences.selectedCityIndex` | city/actions | clamp; circular neighbors announce destination | semantic |
| `AmbientPreferences.opacity` | slider/value/preview | clamp; invalid persisted value →0.60 | semantic |
| `AmbientPreferences.minimalMode` | toggle/window visibility | missing →false | semantic |
| `DataFreshness` | header/refresh state | retain cache/demo labels | semantic |
| `UiState.refreshing` | refresh enabled/text | duplicate disabled with `更新中` | semantic |
| `UiState.controlVisible` | panel lifecycle | failed open leaves TimeWindow affordance | semantic |

**Variants variants**

| Variant | Structural / behavioral difference | Use condition |
|---|---|---|
| `fullMode` | toggle off; weather/AQI visible | default |
| `minimalMode` | toggle on; side windows hidden | user choice |
| `refreshing` | refresh disabled, static `更新中` | single-flight work |
| `constrained` | two-column reflow and scroll | min width/high text scale |

**States states**

| State | Trigger | Visual parameters | Size change | Motion | Accessibility | Stacking precedence |
|---|---|---|---|---|---|---|
| opening | open intent | Thick glass; focus enters close/title | none | 220ms local fade/slide; Reduce Motion fade | announces “控制面板” | above ambient readings |
| ready | preferences loaded | all valid targets enabled | none | none | ordered focus/labels | base within control |
| dragging opacity | slider active | live percent; thumb/track focused; ambient preview updates | none | no spatial motion | controller ±5%; cancel restores commit | above ready |
| city editing | previous/next/swipe | new city immediate; trust may become cache/demo | none | 180ms text crossfade | announces city + refresh | above ready |
| refreshing | network in flight | refresh disabled; static `更新中`; settings active | none | no spinner | polite status | above ready, below error |
| boundary disabled | catalog <2 or opacity at 25/100 | unavailable action disabled and described | none | immediate | disabled reason exposed | above ready |
| error/empty | refresh/catalog failure | settings remain; error + retry; bundled catalog restores cities | retry retained | immediate | failure + retained age | above refreshing |
| closing | 完成/关闭/back | focus returns to TimeWindow control | none | 160ms fade; Reduce Motion 100ms | stable return | top until closed |

### 5.1 Component structure completeness checklist (before coverage reconciliation)

> Verify the fixed structure component by component. If any column for any core component is "no" or the corresponding section anchor is missing, this stage's verdict can only be `block`; "the information already appears elsewhere," "shared states are already defined," or "limited space" must not be used as a reason to pass.

| Core Component | Base fields on separate rows | anatomy.layout | sizing | metrics | renderSpec | dataBindings | variants | states + stacking precedence | Verdict |
|---|---|---|---|---|---|---|---|---|---|
| TimeBeacon | yes | yes | yes | yes | yes | yes | yes | yes | pass |
| WeatherGlyphReadout | yes | yes | yes | yes | yes | yes | yes | yes | pass |
| AqiRingReadout | yes | yes | yes | yes | yes | yes | yes | yes | pass |
| FreshnessBadge | yes | yes | yes | yes | yes | yes | yes | yes | pass |
| AmbientControlPanel | yes | yes | yes | yes | yes | yes | yes | yes | pass |

### 5.2 Coverage reconciliation (performed after structure is complete)

#### Table A · Data entity → component binding

| Data entity / decision variable (referencing the UXR domain model) | Timeliness | Consuming component.dataBinding | Presentation / semantic method | Gap handling (add binding / intentionally not presented + rationale) |
|---|---|---|---|---|
| `City` display name | static catalog | FreshnessBadge `City.displayName`; ControlPanel `selectedCityIndex` | visible city + neighbor navigation | bound; raw coordinate/internal ID intentionally not displayed because it has no glance value |
| `City` coordinates/time zone | static per city | repository request and ClockTicker formatting | drives API/time formatting | intentionally not presented; display name communicates location without technical noise |
| `ClockSnapshot` time/date/weekday/seconds | 1 second | TimeBeacon clock paths | localized type hierarchy; seconds focus-only | bound |
| `WeatherSnapshot.temperatureC` | 30 min refresh | WeatherGlyphReadout | rounded Celsius value | bound |
| `WeatherSnapshot.relativeHumidity` | 30 min refresh | WeatherGlyphReadout | integer percent with `湿度` label | bound |
| `WeatherSnapshot.weatherCode` | 30 min refresh | WeatherGlyphReadout condition semantic | WMO classifier → 4 visible labels + color/shape | bound; raw code intentionally hidden |
| `AirQualitySnapshot.usAqi` | 30 min refresh | AqiRingReadout | number + partial arc + six-band label/shape/color | bound |
| observation/update time | response time/30 min | FreshnessBadge age/update binding; control summary | relative ambient age + exact control timestamp | bound |
| `CachedEnvironment` last success | persisted, age-derived | Weather/AQI through repository; FreshnessBadge state/source | last values retained with `缓存` and exact age | bound |
| `DataFreshness` live/aging/cached/demo/error | event + age | FreshnessBadge; all primary component availability | label + shape + semantic color; never silent | bound |
| network state | event | FreshnessBadge `NetworkState`; repository | offline maps retained data to `缓存` | bound; raw network enum hidden |
| opacity | persisted + interactive | TimeBeacon/Weather/AQI alpha; ControlPanel slider | 25–100%, exact percent | bound |
| full/minimal mode | persisted + interactive | ControlPanel toggle; window coordinator | labeled toggle + side-window visibility | bound |
| focus/hover state | event | TimeBeacon focus binding | seconds + outline + alpha/material | bound |
| refresh-in-flight/single-flight lock | event | FreshnessBadge and ControlPanel | static `更新中`; refresh disabled | bound |

#### Table B · Task decision output → component interaction

| Task ID · decision output | read-only / actionable | Consuming component + `renderSpec` element + interaction behavior | Gap handling |
|---|---|---|---|
| T1 · act now or continue based on time/date | read-only | TimeBeacon `time_text/date_text/weekday_text`; glance | covered |
| T2 · inspect second-level precision | actionable focus | TimeBeacon `seconds_text/focus_outline`; gaze/controller focus sets visibility | covered |
| T3 · assess current outdoor conditions | read-only | WeatherGlyphReadout metrics + AqiRingReadout value/label + FreshnessBadge | covered |
| T4 · choose opacity and full/minimal mode | actionable | ControlPanel `opacity_slider`, −/+, value and `minimal_toggle`; live preview then persistence | covered |
| T5 · open/dismiss controls | actionable | TimeBeacon `controls_action`; ControlPanel `close_action/done_action`; palm-up accelerator | covered |
| T6 · choose live/cache/demo result or retry | actionable app policy + retry | FreshnessBadge trust elements; ControlPanel `refresh_action/retry_action`; repository single-flight | covered |
| T7 · select previous/next city | actionable | ControlPanel previous/current/next elements; swipe/button commit one city | covered |
| T8 · dismiss controls or exit | actionable | ControlPanel close/done + system back ordering; default-window lifecycle | covered |

#### Table C · Exhaustive sub-states of primary components

| Primary component → sub-component | Runtime sub-states (loading / buffering / dragging or editing / empty / error / boundary-disabled and project-specific states) | Corresponding render primitive | Data binding |
|---|---|---|---|
| TimeBeacon → hero time | boot, ambient, focused-seconds, formatter error | `time_text`, `seconds_text`, `focus_outline` | ClockSnapshot + `UiState.timeFocused` |
| TimeBeacon → date context | ambient, formatter fallback, empty weekday | `date_text`, `weekday_text` | localDate/weekday |
| TimeBeacon → controls entry | ready, focused, open reuse, failed-window-open recovery | `controls_action` | `UiState.controlVisible` + open action |
| TimeBeacon → editing/dragging | intentionally N/A: system time is read-only | no editing primitive; control remains | N/A with explicit state row |
| WeatherGlyphReadout → condition | loading, fresh, cached/demo, unknown code, empty/error | `condition_glyph`, `condition_label`, placeholder | weatherCode + freshness |
| WeatherGlyphReadout → metrics | loading, valid, partial missing, empty/error | temperature/humidity text, `--` fallback | temperatureC/humidity |
| WeatherGlyphReadout → editing/dragging | intentionally N/A: reading is read-only | no editing primitive | city editing owned by ControlPanel |
| AqiRingReadout → ring/number | loading, valid, band boundary, out-of-range disabled, empty/error | track, arc, number, placeholder | usAqi/normalizedAqi |
| AqiRingReadout → semantic label | six bands, unclassified error | shape marker + human label | AqiBand.key |
| AqiRingReadout → editing/dragging | intentionally N/A: reading is read-only | no editing primitive | N/A |
| AmbientControlPanel → city row | ready, editing, catalog boundary-disabled, refresh error | previous/current/next elements | CityCatalog + selected index |
| AmbientControlPanel → opacity row | ready, dragging, 25%/100% boundary-disabled, invalid-cache reset | −/slider/+/percent | preferences.opacity |
| AmbientControlPanel → mode | full, minimal, preference fallback | `minimal_toggle` | preferences.minimalMode |
| AmbientControlPanel → refresh | idle, buffering/refreshing, failure, retry | freshness summary + refresh action | DataFreshness + refreshing |
| AmbientControlPanel → shell/exit | opening, ready, closing, failed-open recovery | panel title, close/done | controlVisible + navigator lifecycle |

## 6. Material and depth semantics

- **Material / glass tier / opacity per layer**: the three ambient WindowContainers retain the platform-enabled `Material.Regular` shell; their Compose roots are transparent and component backgrounds are `none`. The temporary ControlWindow uses `Material.Thick`. User opacity multiplies content only (0.25…1.00), never the truth label below legibility; focus temporarily raises foreground alpha to at least 0.85.
- **Depth cues**: all persistent readings share the same system-managed Planar distance plane and 640dp depth. Time leads by direction and type scale, not app-authored Z. The temporary controls become the foreground attention layer through open/focus order and stronger glass; 2D content stays at the back of each Planar box. No fake extrusion or camera motion exists.
- **Mapping of system glass tiers to depth layers**: Regular = ambient/focused reading shell and local focus veil; Thick = temporary control plane; matte `errorBacking` = runtime fallback only when system material/Vibrant contrast cannot be guaranteed. Thin/Thickest are intentionally unused: Thin is too vulnerable to passthrough detail and Thickest is too visually heavy for an ambient utility.

| Layer | Material treatment | glassStyle | opacity | Content carried | Meets contrast |
|---|---|---|---|---|---|
| Temporary ControlWindow | system glass | Thick | foreground 0.96 | settings, refresh, source/age | yes via Thick glass + Vibrant/focus outline |
| Focused TimeBeacon group | system glass | Regular | foreground ≥0.85 | seconds and focus outline | yes via local veil + outline |
| Ambient readings | system glass shell; no component background | Regular | user 0.25…1.00, default 0.60 | time/weather/AQI/trust | ambient by intent; focus recovery always available |
| Material unavailable fallback | matte customColor `#E6132029` | none | 0.90 | same critical text/controls | yes; mutually exclusive replacement for glass |
| Passthrough/environment | not app-owned | none | N/A | room/other apps | N/A |

- **passthrough / MR readability adjudication**: Time/Weather/AQI are intentionally ambient but focusable: complex backgrounds trigger the local Regular veil and ≥0.85 foreground. Trust labels use caption Medium and a redundant shape. Controls always use Thick glass or the exclusive matte fallback. No TabBar is used, so its glass known issue is outside this design.
- **Vibrant Style application list**: Vibrant is enabled centrally. Only monochrome text/line icons participate; AQI and trust semantic colors terminate propagation at their explicitly tokenized strokes/labels. No image or gradient region exists.

| Element / panel | Background controllability (Full/Shared/MR) | Vibrant tier (darkest→ultralight/none) | Propagation / termination | Fallback (solid backing / thicker glass) |
|---|---|---|---|---|
| Time/date/seconds text | Shared/MR uncontrolled | ultralight | propagates through monochrome TimeBeacon text; stops at focus outline | Regular focused veil |
| Weather metric text + outline glyph | Shared/MR uncontrolled | light | propagates through neutral metric; semantic condition tint remains explicit | Regular shell + local focused veil |
| AQI number/caption | Shared/MR uncontrolled | light | number/caption participate; arc/shape terminate Vibrant | Regular shell; explicit label/shape |
| Freshness text | Shared/MR uncontrolled | light | city/age/source participate; semantic shape/color terminate | Regular shell; caption Medium |
| Control text/icons | Shared/MR uncontrolled | ultralight | contained within ControlWindow; semantic status shape terminates | Thick glass, else exclusive matte backing |

## 7. Data display and semantic contract

> Declare how data is converted into user-visible UI.

- **Display-only paths displayOnlyPaths[]**: paths used only for display and not for coloring. Sample values must be end-user-readable copy (such as "Vibration Sensor Array" rather than `vibration_array`, "Bearing 3" rather than `bearing_3`).
- **Semantic enum paths semanticEnumPaths[]**: paths involved in coloring / state determination / alert level / trend, each mapped to the `aliases[]` and `label` of the §3.2 color semantics, guaranteeing that the visible UI shows human-readable labels.
- **Display-only paths displayOnlyPaths[]**: `clock.localTime`→`09:41`; `clock.localDate`→`8月14日`; `clock.weekday`→`星期五`; `city.displayName`→`北京`; `weather.temperatureC`→`26°`; `weather.relativeHumidity`→`湿度 48%`; `freshness.age`→`2分钟前`; `freshness.updatedAt`→`今天 09:39`; `freshness.source`→`Open-Meteo`; `preferences.opacity`→`60%`. Raw coordinates, WMO codes, cache keys and database enums never appear.

| Semantic enum path semanticEnumPaths[] | Match set / classifier | Runtime label source |
|---|---|---|
| `weather.conditionSemantic` | §3.2 `weather_clear/cloudy/rain/snow.aliases` after WMO classifier | matching semantic `label` |
| `airQuality.band` | numeric boundaries 0–50/51–100/101–150/151–200/201–300/301–500 | §3.2 `aqi_*.label` |
| `freshness.state` | live/aging/cached/demo/error plus aliases | §3.2 `trust_*.label` |

| Data state | Source / update time / trust treatment |
|---|---|
| loading | source `Open-Meteo`; no snapshot decision yet; static `正在获取`, no invented metric |
| fresh | successful public API result age <30m; show `实时`, city, relative age and source |
| aging | successful result 30–60m; show `待更新`; scheduled/manual refresh may run without blocking values |
| stale | last success ≥60m; show `缓存` and exact age; never `实时` |
| offline | network unavailable; retain last good result as `缓存`; source and age remain |
| partial | one forecast/AQI field absent; known fields remain, missing is `--`, trust becomes `更新失败` |
| conflicting | city/response identity or timestamps conflict; reject new response and retain labeled prior data |
| permission_denied | network permission/configuration unavailable; use labeled cache/demo and `更新失败`; no location permission is requested |
| error | timeout/HTTP/parse/storage error; last good result retained; otherwise bundled `演示数据` |
| demo | bundled deterministic sample; source `本地演示`; never claims observation freshness |

- **Trust policy trustPolicy**: city, trust label, age/update time and source remain visible or focus-accessible for every weather/AQI value; cache/demo is never styled as live; refresh never clears last good data; AQI always carries number + Chinese level + shape; public model output is described as city weather, never a room sensor; source conflicts reject the new response; manual and scheduled refresh share one single-flight path.

| Display format rules formattingRules | Input path | Output format | fallback | Applicable data states |
|---|---|---|---|---|
| clock minute | `clock.localTime` | locale `HH:mm` | `--:--` | all |
| focused seconds | `clock.seconds` | `:ss`, two digits | hidden | fresh system clock/focus |
| date/week | local date fields | `M月d日 · EEEE` | numeric date only | all |
| temperature | `temperatureC` | round to integer + `°` | `--°` | fresh…error |
| humidity | `relativeHumidity` | clamp 0–100, integer `湿度 N%` | `湿度 --%` | fresh…error |
| weather condition | `weatherCode` | WMO group → `晴/阴/雨/雪` | `天气暂不可用` | fresh…error |
| AQI | `usAqi` | integer 0–500 + band label | `-- · 暂不可用` | fresh…error |
| relative age | `updatedAt` | `<1分钟 / N分钟前 / N小时前` | `时间未知` | non-demo |
| exact update | `updatedAt` | `今天 HH:mm` or `M月d日 HH:mm` | `时间未知` | controls/non-demo |
| source | `source` | `Open-Meteo` or `本地演示` | `来源未知` + error | all |
| opacity | `preferences.opacity` | integer 25–100% | 60% | settings |

## 8. PICO platform numeric spec

> The target platform is fixed as PICO spatial; numbers are governed by the official PICO spec and the Design Tokens above.

- **Corner radius**: platform baseline 32dp for major spatial surfaces; project scale uses 0/12/20/32dp and never exceeds it for component controls.
- **Minimum font size**: 12sp; CJK caption/body ≤17sp use Medium/Regular rather than ultra-thin. Only the 50sp display role is Thin.
- **Interaction hit target**: 56×56dp minimum; slider thumb has a 56dp interaction envelope even when the visible mark is smaller.
- **Central field-of-view zones**: core content checked within approximately 65° horizontal × 40° vertical; separated secondary content stays within 85° × 55°. The center forward sight line remains empty.
- **Planar bounds/depth**: project logical sizes remain within 320×180dp…2700×1800dp; Planar depth is 640dp and is not repurposed as content space.

## 9. Asset Delivery

> Beyond sliced images/icons, there are also 3D models, materials, spatial audio, and environment assets. The core is "engineering can use it directly, and it does not blur or break at different distances."

### 9.1 2D bitmap / sliced image

| Item | Delivery spec |
|---|---|
| Format | N/A — no bitmap/sliced-image dependency; prevents blur and avoids unnecessary decode work |
| Multiplier | N/A |
| Naming | N/A |
| Slice inset | N/A |

### 9.2 Iconography

| Item | Delivery spec |
|---|---|
| Format | Android VectorDrawable/XML or SpatialUI built-in vector; no raster fallback |
| Grid | 28dp action grid; 40dp weather grid; consistent 1.5dp optical stroke |
| Naming classification | `ic_weather_clear/cloudy/rain/snow`, `ic_trust_circle/square/triangle/dashed/diamond`, `ic_chevron_left/right`, `ic_controls`, `ic_refresh`, `ic_close` |
| Adaptation | single-color tintable; tokens drive tint; shape remains distinguishable without color |

### 9.3 3D assets (specific to spatial apps)

| Item | Delivery spec |
|---|---|
| Polygon budget | N/A — Planar-only app, zero authored 3D models |
| Material / PBR | N/A |
| Scale / anchor | N/A; WindowContainer placement contract owns spatial position |
| LOD | N/A |

> The specific 3D file format and import flow are governed by the official PICO spatial-engine conventions; this design spec does not lock the engine implementation.

### 9.4 Spatial audio / motion / environment assets

| Item | Delivery spec |
|---|---|
| Spatial audio | N/A — silent ambient product; optional system haptic through boundary only |
| Motion assets | N/A — all short transitions are code-native tokenized fades/scales; no sprite/timeline asset |
| Environment assets | N/A — Shared Space passthrough is system-owned; no panorama |

### 9.5 Delivery method and engineering handoff

- **Single source**: assets follow the Design Tokens; colors/sizes are not hard-coded into the sliced images, and can be tinted at runtime.
- **Asset list**:

| asset name | format | visual box | usage | owning component |
|---|---|---|---|---|
| `ic_weather_clear/cloudy/rain/snow` | VectorDrawable | 40dp | condition shape | WeatherGlyphReadout |
| `ic_trust_circle/square/triangle/dashed/diamond` | VectorDrawable | 20dp | trust/AQI redundant marker | FreshnessBadge/AqiRingReadout |
| `ic_chevron_left/right` | VectorDrawable | 28dp | city navigation | AmbientControlPanel |
| `ic_controls`, `ic_refresh`, `ic_close`, `ic_minus`, `ic_plus` | VectorDrawable | 28dp | actions | TimeBeacon/ControlPanel |
| AQI arc | code-native Canvas path | 64–112dp | quantitative ring | AqiRingReadout |

## 10. Minimum Completeness Gate

> This table is self-checked by the visual/design-system generating role and independently re-reviewed by `design_coherence_reviewer`.
> Giving only style adjectives, a component list, or a shared state table does not constitute structural completeness. If any core component is missing a fixed structure block,
> any key token is still a placeholder, or the window layout and components cannot be mapped one-to-one, it is `block`. When any row is
> `block`, this document's `minimumCompletenessGate=block` and the overall `designStatus=invalid`.

| Check Item | Minimum Pass Condition | Evidence Anchor | Verdict |
|---|---|---|---|
| Visual direction | 2–3 substantially different directions, selection basis, ≥2 rejected directions and approval evidence complete | §2 + `VISUAL-EFFECT-20260814-01` | pass |
| Visual language | tokens, typography, colorSemantics, materials, scale are all consumable precise values with no mutually exclusive conflicts | §3–§4 | pass |
| Window structure | Each primary WindowContainer has a shell, ASCII/Grid, region→component mapping, spacing, and reflow | §5.0 | pass |
| Component structure | Each core component's base fields, anatomy.layout, sizing, metrics, renderSpec, dataBindings, variants, states all exist independently | §5 | pass |
| Coverage reconciliation | The structure-completeness checklist and the three reconciliation tables have no unhandled gaps | §5.1–§5.2 | pass |
| Semantics and trust | Materials/depth, data display, fallback, data states, and trust policy are implementable and traceable | §6–§8 | pass |

| Field | Value |
|---|---|
| minimumCompletenessGate | pass |

## 11. Delivery and Recipients

- **Deliverables**: visual direction and approved reference, visual language tokens, component specs, data-display semantic contract, asset list (this document is their human-readable source of truth)
- **Recipients**: Prototype / Frontend Engineer, QA, Design Lead

---

> Format convention: Tokens are the single contract between design and code, and values must be precise (colors #hex); components use structured anatomy (layout Grid + sizing tiers), no prose; colors must use the color+shape dual-channel with a human-readable label; data does not echo the machine enum; PICO platform numbers must not be missing; any design change must be reflected in the delivery facts.
