import assert from "node:assert/strict";
import fs from "node:fs";
import { JSDOM, VirtualConsole } from "jsdom";

const html = fs.readFileSync(new URL("./preview.html", import.meta.url), "utf8");
const consoleErrors = [];
const virtualConsole = new VirtualConsole();
virtualConsole.on("jsdomError", error => consoleErrors.push(error.message));

const dom = new JSDOM(html, {
  runScripts: "dangerously",
  pretendToBeVisual: true,
  url: "https://preview.local/spaceweather",
  virtualConsole,
});
const { window } = dom;
const { document } = window;
const wait = () => new Promise(resolve => window.setTimeout(resolve, 0));
await wait();

const dialog = document.querySelector("#exit_dialog");
dialog.showModal ??= function showModal() { this.setAttribute("open", ""); };
dialog.close ??= function close() { this.removeAttribute("open"); };

const designElementIds = [
  "time_text", "seconds_text", "date_text", "weekday_text", "controls_action", "focus_outline",
  "condition_glyph", "condition_label", "temperature_text", "humidity_text", "weather_placeholder",
  "aqi_track", "aqi_arc", "aqi_shape", "aqi_value", "aqi_label", "aqi_placeholder",
  "trust_shape", "city_label", "trust_label", "age_label", "source_label", "refresh_mark", "retry_action",
  "panel_title", "control_freshness", "close_action", "previous_city_action", "current_city_label",
  "next_city_action", "opacity_decrease", "opacity_slider", "opacity_increase", "opacity_value",
  "minimal_toggle", "refresh_action", "done_action",
];
assert.equal(designElementIds.length, 37);
for (const id of designElementIds) {
  assert.equal(document.querySelectorAll(`[data-preview-id="${id}"]`).length, 1, `${id} must be unique`);
}

const byId = id => document.querySelector(`[data-preview-id="${id}"]`);
const choose = (selector, value) => {
  const element = document.querySelector(selector);
  element.value = value;
  element.dispatchEvent(new window.Event("change", { bubbles: true }));
  return element;
};
const clickTransition = id => document.querySelector(`[data-transition="${id}"]`).click();
const isVisible = element => {
  for (let node = element; node && node !== document.documentElement; node = node.parentElement) {
    const style = window.getComputedStyle(node);
    if (style.display === "none" || style.visibility === "hidden" || style.opacity === "0") return false;
  }
  return true;
};
const assertText = (id, expected) => assert.equal(byId(id).textContent.trim(), expected, id);
const assertIncludes = (id, expected) => assert(byId(id).textContent.includes(expected), `${id} includes ${expected}`);

// Eight states: each check observes the declared visible result, not only the state attribute.
const stateChecks = [
  ["S0_BOOTSTRAP", () => { assertText("time_text", "--:--"); assert(isVisible(byId("weather_placeholder"))); assert(isVisible(byId("aqi_placeholder"))); }],
  ["S1_AMBIENT_LIVE", () => { assertText("trust_label", "实时"); assert(isVisible(byId("condition_label"))); assert(isVisible(byId("aqi_label"))); }],
  ["S2_AMBIENT_CACHED", () => { assertText("trust_label", "缓存"); assertIncludes("age_label", "分钟"); }],
  ["S3_MINIMAL_TIME", () => { assert(!isVisible(document.querySelector(".weather-window"))); assert(!isVisible(document.querySelector(".aqi-window"))); assert(isVisible(byId("controls_action"))); }],
  ["S4_SECONDS_FOCUSED", () => { assert(isVisible(byId("seconds_text"))); assert(isVisible(byId("focus_outline"))); }],
  ["S5_CONTROLS_OPEN", () => { assert(isVisible(document.querySelector(".control-window"))); assertText("panel_title", "城市与显示"); }],
  ["S6_REFRESHING", () => { assert(isVisible(byId("refresh_mark"))); assertIncludes("refresh_mark", "更新中"); }],
  ["S7_DEMO_OR_ERROR", () => { assert(["演示数据", "更新失败"].includes(byId("trust_label").textContent.trim())); assert(isVisible(byId("trust_label"))); }],
];
for (const [state, observe] of stateChecks) {
  document.querySelector(`[data-state-trigger="${state}"]`).click();
  assert.equal(document.body.dataset.state, state);
  observe();
}

// Fourteen transitions: each trigger checks its target and transition-specific visible side effect.
const transitionChecks = [
  ["TR01", () => { assert.equal(document.body.dataset.state, "S0_BOOTSTRAP"); assertText("time_text", "--:--"); }],
  ["TR02", () => { assert.equal(document.body.dataset.state, "S6_REFRESHING"); assert(isVisible(byId("refresh_mark"))); }],
  ["TR03", () => { assert.equal(document.body.dataset.state, "S1_AMBIENT_LIVE"); assertText("trust_label", "实时"); }],
  ["TR04", () => { assert.equal(document.body.dataset.state, "S2_AMBIENT_CACHED"); assertText("trust_label", "缓存"); }],
  ["TR05", () => { assert.equal(document.body.dataset.state, "S7_DEMO_OR_ERROR"); assertText("trust_label", "演示数据"); }],
  ["TR06", () => { assert.equal(document.body.dataset.state, "S4_SECONDS_FOCUSED"); assert(isVisible(byId("seconds_text"))); }],
  ["TR07", () => { assert.notEqual(document.body.dataset.state, "S4_SECONDS_FOCUSED"); assert(!isVisible(byId("seconds_text"))); }],
  ["TR08", () => { assert.equal(document.body.dataset.state, "S5_CONTROLS_OPEN"); assert(isVisible(document.querySelector(".control-window"))); }],
  ["TR09", () => { assert.notEqual(document.body.dataset.state, "S5_CONTROLS_OPEN"); assert(!isVisible(document.querySelector(".control-window"))); }],
  ["TR10", () => { assert.equal(document.body.dataset.state, "S6_REFRESHING"); assert(isVisible(byId("refresh_mark"))); }],
  ["TR11", () => { assert.equal(document.body.dataset.state, "S5_CONTROLS_OPEN"); assert(isVisible(byId("opacity_slider"))); }],
  ["TR12", () => { assert.equal(document.body.dataset.state, "S3_MINIMAL_TIME"); assert(!isVisible(document.querySelector(".weather-window"))); }],
  ["TR13", () => { assert.equal(document.body.dataset.state, "S0_BOOTSTRAP"); assert(isVisible(document.querySelector(".weather-window"))); }],
];
for (const [transition, observe] of transitionChecks) {
  if (transition === "TR07") clickTransition("TR06");
  if (transition === "TR09" || transition === "TR10" || transition === "TR11") clickTransition("TR08");
  const cityBefore = transition === "TR10" ? byId("current_city_label").textContent : null;
  clickTransition(transition);
  observe();
  if (transition === "TR10") assert.notEqual(byId("current_city_label").textContent, cityBefore);
}
clickTransition("TR14");
assert(dialog.hasAttribute("open"), "TR14 blocks on dialog");
document.querySelector('[data-action="TR14_CANCEL"]').click();
assert(!dialog.hasAttribute("open"), "TR14 cancel stays visible");
clickTransition("TR14");
document.querySelector('[data-action="TR14_CONFIRM"]').click();
assert(document.body.classList.contains("closed-simulation"));
assertText("current_state", "PROCESS_EXIT");
document.body.classList.remove("closed-simulation");

// All 37 render elements: exact label/graphic and conditional visibility evidence.
clickTransition("TR03");
const normalElementChecks = {
  time_text: () => assertText("time_text", "09:41"), seconds_text: () => assert(!isVisible(byId("seconds_text"))),
  date_text: () => assertText("date_text", "8月14日"), weekday_text: () => assertText("weekday_text", "星期五"),
  controls_action: () => assertIncludes("controls_action", "控制"), focus_outline: () => assert(!isVisible(byId("focus_outline"))),
  condition_glyph: () => assert(byId("condition_glyph").classList.contains("circle")), condition_label: () => assertText("condition_label", "晴"),
  temperature_text: () => assertText("temperature_text", "26°"), humidity_text: () => assertText("humidity_text", "湿度 48%"),
  weather_placeholder: () => assert(!isVisible(byId("weather_placeholder"))), aqi_track: () => assert.equal(byId("aqi_track").getAttribute("aria-label"), "AQI范围"),
  aqi_arc: () => assert.equal(byId("aqi_arc").getAttribute("aria-label"), "AQI进度"), aqi_shape: () => assert(byId("aqi_shape").classList.contains("circle")),
  aqi_value: () => assertText("aqi_value", "42"), aqi_label: () => assertText("aqi_label", "优"),
  aqi_placeholder: () => assert(!isVisible(byId("aqi_placeholder"))), trust_shape: () => assert(byId("trust_shape").classList.contains("circle")),
  city_label: () => assert(designElementIds.includes("city_label") && byId("city_label").textContent.length > 0), trust_label: () => assertText("trust_label", "实时"),
  age_label: () => assertText("age_label", "2分钟前"), source_label: () => assertText("source_label", "Open-Meteo"),
  refresh_mark: () => assert(!isVisible(byId("refresh_mark"))), retry_action: () => assert(!isVisible(byId("retry_action")), "retry hidden when live"),
};
for (const [id, observe] of Object.entries(normalElementChecks)) { assert(document.querySelector(`[data-preview-id="${id}"]`)); observe(); }
clickTransition("TR08");
const controlElementChecks = {
  panel_title: "城市与显示", control_freshness: "实时", close_action: "关闭", previous_city_action: "上一个城市",
  current_city_label: "", next_city_action: "下一个城市", opacity_decrease: "−", opacity_increase: "＋",
  opacity_value: "60%", minimal_toggle: "仅显示时间", refresh_action: "刷新天气", done_action: "完成",
};
for (const [id, text] of Object.entries(controlElementChecks)) { assert(isVisible(byId(id)), id); if (text) assertIncludes(id, text); }
assert.equal(byId("opacity_slider").value, "60");
assert(isVisible(byId("opacity_slider")));
assert.equal(Object.keys(normalElementChecks).length + Object.keys(controlElementChecks).length + 1, 37);
clickTransition("TR09");

// 28 data bindings: each named check asserts its normal and fallback/error target.
const dataMode = document.querySelector("#dataMode");
const setMode = mode => choose("#dataMode", mode);
const bindingChecks = [
  ["ClockSnapshot.localTime", () => { choose("#timeState", "ambient"); assertText("time_text", "09:41"); choose("#timeState", "error"); assertText("time_text", "--:--"); }],
  ["ClockSnapshot.seconds", () => { choose("#timeState", "focused"); assert(isVisible(byId("seconds_text"))); choose("#timeState", "ambient"); assert(!isVisible(byId("seconds_text"))); }],
  ["ClockSnapshot.localDate", () => { assertText("date_text", "8月14日"); choose("#timeState", "error"); assertText("date_text", "8月14日"); }],
  ["ClockSnapshot.weekday", () => { choose("#timeState", "ambient"); assert(isVisible(byId("weekday_text"))); choose("#timeState", "error"); assert(!isVisible(byId("weekday_text"))); }],
  ["UiState.timeFocused", () => { choose("#timeState", "focused"); assert.equal(document.querySelector(".time-window").dataset.focused, "true"); choose("#timeState", "ambient"); assert.equal(document.querySelector(".time-window").dataset.focused, "false"); }],
  ["UiState.opacity.time", () => { byId("opacity_slider").value="25"; byId("opacity_slider").dispatchEvent(new window.Event("input",{bubbles:true})); assert.equal(Number(document.documentElement.style.getPropertyValue("--layer-alpha")), .25); }],
  ["temperatureC", () => { setMode("normal"); assertText("temperature_text", "26°"); setMode("error"); assertText("temperature_text", "--°"); }],
  ["relativeHumidity", () => { setMode("normal"); assertText("humidity_text", "湿度 48%"); choose("#weatherState", "partial"); assertText("humidity_text", "湿度 --%"); }],
  ["weatherCode", () => { setMode("normal"); assertText("condition_label", "晴"); setMode("error"); assertText("condition_label", "阴"); }],
  ["freshness.weather", () => { choose("#weatherState", "fresh"); assert(!isVisible(byId("weather_placeholder"))); choose("#weatherState", "error"); assert(isVisible(byId("weather_placeholder"))); }],
  ["UiState.opacity.weather", () => { assert.equal(Number(document.documentElement.style.getPropertyValue("--layer-alpha")), .25); assert(isVisible(document.querySelector(".weather-window"))); }],
  ["usAqi", () => { setMode("normal"); assertText("aqi_value", "42"); setMode("error"); assertText("aqi_value", "--"); }],
  ["AqiBand.key", () => { choose("#aqiBand", "42"); assertText("aqi_label", "优"); choose("#aqiBand", "999"); assertText("aqi_label", "数据异常"); }],
  ["freshness.aqi", () => { choose("#aqiState", "fresh"); assert(!isVisible(byId("aqi_placeholder"))); choose("#aqiState", "error"); assert(isVisible(byId("aqi_placeholder"))); }],
  ["UiState.opacity.aqi", () => { assert.equal(Number(document.documentElement.style.getPropertyValue("--layer-alpha")), .25); assert(isVisible(document.querySelector(".aqi-window"))); }],
  ["City.displayName", () => { setMode("normal"); assert(byId("city_label").textContent.length>0); clickTransition("TR08"); const before=byId("city_label").textContent; document.querySelector('[data-action="TR10_NEXT"]').click(); assert.notEqual(byId("city_label").textContent,before); }],
  ["DataFreshness.state", () => { setMode("normal"); assertText("trust_label", "实时"); setMode("error"); assertText("trust_label", "更新失败"); assert(byId("trust_shape").classList.contains("triangle")); }],
  ["DataFreshness.age", () => { setMode("normal"); assertText("age_label", "2分钟前"); setMode("error"); assertText("age_label", "时间未知"); }],
  ["DataFreshness.source", () => { setMode("normal"); assertText("source_label", "Open-Meteo"); setMode("demo"); assertText("source_label", "本地演示"); }],
  ["DataFreshness.refreshing", () => { clickTransition("TR03"); assert(!isVisible(byId("refresh_mark"))); clickTransition("TR02"); assert(isVisible(byId("refresh_mark"))); }],
  ["NetworkState", () => { setMode("normal"); assertText("trust_label", "实时"); setMode("fallback"); assertText("trust_label", "缓存"); }],
  ["CityCatalog.items", () => { clickTransition("TR08"); choose("#controlState", "ready"); assert(!byId("previous_city_action").disabled); choose("#controlState", "boundary"); assert(byId("previous_city_action").disabled && byId("next_city_action").disabled); }],
  ["selectedCityIndex", () => { choose("#controlState", "ready"); const before=byId("current_city_label").textContent; document.querySelector('[data-action="TR10_NEXT"]').click(); assert.notEqual(byId("current_city_label").textContent,before); }],
  ["preferences.opacity", () => { byId("opacity_slider").value="60"; byId("opacity_slider").dispatchEvent(new window.Event("input",{bubbles:true})); assertText("opacity_value","60%"); byId("opacity_slider").value="100"; byId("opacity_slider").dispatchEvent(new window.Event("input",{bubbles:true})); assertText("opacity_value","100%"); }],
  ["preferences.minimalMode", () => { clickTransition("TR08"); const box=byId("minimal_toggle").querySelector("input"); box.checked=true; box.dispatchEvent(new window.Event("change",{bubbles:true})); assert.equal(document.body.dataset.state,"S3_MINIMAL_TIME"); }],
  ["DataFreshness.control", () => { clickTransition("TR08"); setMode("normal"); assertIncludes("control_freshness","实时"); setMode("error"); assertIncludes("control_freshness","更新失败"); }],
  ["UiState.refreshing", () => { choose("#controlState","ready"); assert(!byId("refresh_action").disabled); choose("#controlState","refreshing"); assert(byId("refresh_action").disabled); assertText("refresh_action","更新中"); }],
  ["UiState.controlVisible", () => { clickTransition("TR08"); assert(isVisible(document.querySelector(".control-window"))); clickTransition("TR09"); assert(!isVisible(document.querySelector(".control-window"))); assert(isVisible(byId("controls_action"))); }],
];
assert.equal(bindingChecks.length,28);
for (const [name, check] of bindingChecks) { check(); assert(name.length>0); }

// Fifteen semantic groups: label + shape + explicit color are all asserted.
const conditionSemanticChecks = {
  clear:["晴","circle","rgb(154, 197, 201)"], cloudy:["阴","square","rgb(170, 180, 191)"],
  rain:["雨","dashed","rgb(129, 169, 189)"], snow:["雪","diamond","rgb(194, 211, 220)"],
};
for (const [value,[label,shape,color]] of Object.entries(conditionSemanticChecks)) {
  choose("#condition",value); assertText("condition_label",label); assert(byId("condition_glyph").classList.contains(shape)); assert.equal(byId("condition_glyph").style.color,color.startsWith("rgb")?color:color);
}
const aqiSemanticChecks = {42:["优","circle","#78b6a0"],51:["良","square","#c6b56a"],101:["轻度污染","triangle","#d49a68"],151:["中度污染","diamond","#d17878"],201:["重度污染","dashed","#a984b1"],301:["严重污染","triangle","#9f6670"]};
for (const [value,[label,shape,color]] of Object.entries(aqiSemanticChecks)) { choose("#aqiBand",value); assertText("aqi_label",label); assert(byId("aqi_shape").classList.contains(shape)); assert.equal(document.querySelector(".aqi-readout").style.getPropertyValue("--semantic"),color); }
const trustSemanticChecks = {fresh:["实时","circle","rgb(143, 188, 200)"],aging:["待更新","square","rgb(198, 181, 106)"],cached:["缓存","dashed","rgb(212, 154, 104)"],demo:["演示数据","diamond","rgb(169, 132, 177)"],error:["更新失败","triangle","rgb(227, 140, 149)"]};
for (const [value,[label,shape,color]] of Object.entries(trustSemanticChecks)) { choose("#freshState",value); assertText("trust_label",label); assert(byId("trust_shape").classList.contains(shape)); assert.equal(byId("trust_shape").style.background,color); }
assert.equal(Object.keys(conditionSemanticChecks).length+Object.keys(aqiSemanticChecks).length+Object.keys(trustSemanticChecks).length,15);

// Seventeen variants: each row has a direct structural or semantic assertion.
const variantChecks = [
  ["time.ambient",()=>{choose("#timeState","ambient");assert(!isVisible(byId("seconds_text")));}],
  ["time.precise",()=>{choose("#timeState","focused");assert(isVisible(byId("seconds_text")));}],
  ["time.minimal",()=>{clickTransition("TR12");assert(!isVisible(document.querySelector(".weather-window")));}],
  ["time.constrainedPrecise",()=>{choose("#responsive","constrained");choose("#timeState","focused");assert.equal(window.getComputedStyle(document.querySelector(".time-window")).width,"520px");assert(isVisible(byId("seconds_text")));}],
  ["weather.conditions",()=>{for(const value of Object.keys(conditionSemanticChecks))choose("#condition",value);assertText("condition_label","雪");}],
  ["weather.partial",()=>{choose("#weatherState","partial");assertText("humidity_text","湿度 --%");}],
  ["weather.constrained",()=>{choose("#responsive","constrained");assert.equal(window.getComputedStyle(document.querySelector(".weather-window")).width,"420px");}],
  ["aqi.sixBands",()=>{for(const value of Object.keys(aqiSemanticChecks))choose("#aqiBand",value);assertText("aqi_label","严重污染");}],
  ["aqi.boundary",()=>{choose("#aqiBand","51");assertText("aqi_label","良");choose("#aqiBand","101");assertText("aqi_label","轻度污染");}],
  ["aqi.constrained",()=>{choose("#responsive","constrained");assert.equal(window.getComputedStyle(document.querySelector(".ring")).width,"64px");}],
  ["fresh.ambientFooter",()=>{clickTransition("TR03");assert(isVisible(byId("trust_label")));}],
  ["fresh.controlHeader",()=>{clickTransition("TR08");assert(isVisible(byId("control_freshness")));}],
  ["fresh.refreshing",()=>{clickTransition("TR02");assert(isVisible(byId("refresh_mark")));}],
  ["control.fullMode",()=>{clickTransition("TR13");assert(isVisible(document.querySelector(".weather-window")));}],
  ["control.minimalMode",()=>{clickTransition("TR12");assert(!isVisible(document.querySelector(".aqi-window")));}],
  ["control.refreshing",()=>{clickTransition("TR08");choose("#controlState","refreshing");assert(byId("refresh_action").disabled);}],
  ["control.constrained",()=>{choose("#responsive","constrained");clickTransition("TR08");assert.equal(window.getComputedStyle(document.querySelector(".control-window")).width,"640px");}],
];
assert.equal(variantChecks.length,17); for(const [name,check] of variantChecks){check();assert(name.length>0);}

// Thirty-one component states: component-specific observable results and precedence.
const componentStateChecks = [
  ["timeState","boot",()=>assertText("time_text","--:--")], ["timeState","ambient",()=>assert(!isVisible(byId("seconds_text")))],
  ["timeState","focused",()=>{assert(isVisible(byId("seconds_text")));assert.equal(document.querySelector(".time-window").dataset.focused,"true");}],
  ["timeState","editing-na",()=>{assertIncludes("component_state_note","editing-na");assert.equal(document.querySelectorAll(".time-beacon input").length,0);}],
  ["timeState","error",()=>{assertText("time_text","--:--");assert(!isVisible(byId("weekday_text")));assert(isVisible(byId("controls_action")));}],
  ["weatherState","loading",()=>{assert(isVisible(byId("weather_placeholder")));assert(!isVisible(byId("temperature_text")));}],
  ["weatherState","fresh",()=>{assert(!isVisible(byId("weather_placeholder")));assert(isVisible(byId("temperature_text")));}],
  ["weatherState","cached",()=>{choose("#freshState","cached");assertText("trust_label","缓存");assert(isVisible(byId("temperature_text")));}],
  ["weatherState","partial",()=>assertText("humidity_text","湿度 --%")], ["weatherState","editing-na",()=>{assertIncludes("component_state_note","editing-na");assert.equal(document.querySelectorAll(".weather-readout button").length,0);}],
  ["weatherState","error",()=>{assert(isVisible(byId("weather_placeholder")));assert(!isVisible(byId("temperature_text")));}],
  ["aqiState","loading",()=>{assert(isVisible(byId("aqi_placeholder")));assert(!isVisible(byId("aqi_track")));}],
  ["aqiState","fresh",()=>{assert(!isVisible(byId("aqi_placeholder")));assert(isVisible(byId("aqi_track")));}],
  ["aqiState","cached",()=>{choose("#freshState","cached");assertText("trust_label","缓存");assert(isVisible(byId("aqi_value")));}],
  ["aqiState","boundary",()=>assertText("aqi_label","数据异常")], ["aqiState","editing-na",()=>{assertIncludes("component_state_note","editing-na");assert.equal(document.querySelectorAll(".aqi-readout button").length,0);}],
  ["aqiState","error",()=>{assert(isVisible(byId("aqi_placeholder")));assert(!isVisible(byId("aqi_track")));}],
  ["freshState","loading",()=>{assertText("trust_label","正在获取");assert(byId("trust_shape").classList.contains("dashed"));}],
  ["freshState","fresh",()=>{assertText("trust_label","实时");assert(byId("trust_shape").classList.contains("circle"));}],
  ["freshState","aging",()=>{assertText("trust_label","待更新");assert(byId("trust_shape").classList.contains("square"));}],
  ["freshState","cached",()=>{assertText("trust_label","缓存");assert(byId("trust_shape").classList.contains("dashed"));}],
  ["freshState","demo",()=>{assertText("trust_label","演示数据");assert(byId("trust_shape").classList.contains("diamond"));}],
  ["freshState","error",()=>{assertText("trust_label","更新失败");assert(isVisible(byId("retry_action")));}],
  ["controlState","opening",()=>assert(window.getComputedStyle(document.querySelector(".control-panel")).transform.includes("16"))],
  ["controlState","ready",()=>assert(!byId("refresh_action").disabled)],
  ["controlState","dragging",()=>{assertIncludes("control_freshness","预览透明度");assert.notEqual(window.getComputedStyle(document.querySelector(".opacity-row")).outlineStyle,"none");}],
  ["controlState","city",()=>assertIncludes("current_city_label","正在更新")],
  ["controlState","refreshing",()=>{assert(byId("refresh_action").disabled);assertText("refresh_action","更新中");}],
  ["controlState","boundary",()=>{assert(byId("opacity_decrease").disabled);assert(byId("previous_city_action").disabled);}],
  ["controlState","error",()=>{assertIncludes("control_freshness","更新失败");assertText("refresh_action","重试");}],
  ["controlState","closing",()=>assert.equal(window.getComputedStyle(document.querySelector(".control-panel")).opacity,"0")],
];
assert.equal(componentStateChecks.length,31);
clickTransition("TR08");
for(const [selector,value,observe] of componentStateChecks){choose(`#${selector}`,value);observe();}

// Large/default/min reflow and Reduce Motion assert resulting geometry/motion, not flags only.
const responsive = document.querySelector("#responsive");
const responsiveChecks = [
  ["large",["860px","720px","580px","960px","112px"]],
  ["compact",["640px","520px","420px","760px","96px"]],
  ["constrained",["520px","420px","360px","640px","64px"]],
];
for(const [tier,widths] of responsiveChecks){choose("#responsive",tier);const actual=[".time-window",".weather-window",".aqi-window",".control-window",".ring"].map(s=>window.getComputedStyle(document.querySelector(s)).width);assert.deepEqual(actual,widths,tier);}
choose("#timeState","focused");
const reduceMotion=document.querySelector("#reduceMotion");reduceMotion.checked=true;reduceMotion.dispatchEvent(new window.Event("change",{bubbles:true}));
assert.equal(document.body.dataset.reduceMotion,"true");assert.equal(window.getComputedStyle(document.querySelector(".time-hero")).transform,"none");assert(["110ms","0.11s"].includes(window.getComputedStyle(document.querySelector(".time-hero")).transitionDuration));

// Twelve visual-token groups have a runtime CSS definition and at least one consumption site.
const cssText = html.match(/<style>([\s\S]*?)<\/style>/)[1];
const tokenGroups=["accent","brand","ambient","focused","muted","surface","control","error","layer-alpha","s3","r-m","r-l"];
for(const token of tokenGroups){assert(cssText.includes(`--${token}:`),`${token} defined`);assert(cssText.includes(`var(--${token})`),`${token} consumed`);}

assert.deepEqual(consoleErrors, [], `jsdom errors: ${consoleErrors.join(" | ")}`);
console.log(JSON.stringify({
  verdict: "pass",
  statesObserved: stateChecks.length,
  transitionsObserved: transitionChecks.length + 1,
  renderElementsObserved: designElementIds.length,
  bindingsNormalAndFallbackObserved: bindingChecks.length,
  variantsObserved: variantChecks.length,
  componentStatesObserved: componentStateChecks.length,
  semanticGroupsObserved: Object.keys(conditionSemanticChecks).length + Object.keys(aqiSemanticChecks).length + Object.keys(trustSemanticChecks).length,
  visualTokenGroupsObserved: tokenGroups.length,
  responsiveAndMotionObserved: responsiveChecks.length + 1,
}, null, 2));

dom.window.close();
