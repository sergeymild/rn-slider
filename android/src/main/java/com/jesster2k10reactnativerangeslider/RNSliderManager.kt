package com.jesster2k10reactnativerangeslider

import android.content.Context
import androidx.core.app.NotificationCompat.StreamType
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class RNSliderManager: SimpleViewManager<RNSliderView>() {
  override fun getName(): String {
    return "RNSlider"
  }

  override fun createViewInstance(reactContext: ThemedReactContext): RNSliderView {
    return RNSliderView(reactContext as Context)
  }

  @ReactProp(name = "tintColorBetweenHandles")
  fun setTintColorBetweenHandles(view: RNSliderView, color: Double) {
    view.run { setTintColorBetweenHandles(color) }
  }

  @ReactProp(name = "tintColor")
  fun setTintColor(view: RNSliderView, color: Double) {
    view.run { setTintColor(color) }
  }

  @ReactProp(name = "handleColor")
  fun setHandleColor(view: RNSliderView, color: Double) {
    view.run { setHandleColor(color) }
  }

  @ReactProp(name = "handlePressedColor")
  fun setHandlePressedColor(view: RNSliderView, color: Double) {
    view.run { setHandlePressedColor(color) }
  }

  @ReactProp(name = "step")
  fun setStep(view: RNSliderView, step: Int?) {
    if (step == null) return
    view.run { setStep(step.toFloat()) }
  }

  @ReactProp(name = "leftHandlePressedColor")
  fun setLeftHandlePressedColor(view: RNSliderView, color: Double) {
    view.run { setLeftHandlePressedColor(color) }
  }

  @ReactProp(name = "leftHandleColor")
  fun setLeftHandleColor(view: RNSliderView, color: Double) {
    view.run { setLeftHandleColor(color) }
  }

  @ReactProp(name = "handleDiameter")
  fun setCornerRadius(view: RNSliderView, radius: Int?) {
    if (radius == null) return
    view.run { setCornerRadius(radius.toFloat()) }
  }

  @ReactProp(name = "min")
  fun setMin(view: RNSliderView, min: Int?) {
    if (min == null) return
    view.run { setMinValue(min.toFloat()) }
  }

  @ReactProp(name = "max")
  fun setMax(view: RNSliderView, max: Int?) {
    if (max == null) return
    view.run { setMaxValue(max.toFloat()) }
  }

  @ReactProp(name = "minStart")
  fun setMinStart(view: RNSliderView, min: Int?) {
    if (min == null) return
    view.run { setMinStartValue(min.toFloat()) }
  }

  @ReactProp(name = "maxStart")
  fun maxStart(view: RNSliderView, max: Int?) {
    if (max == null) return
    view.run { setMaxStartValue(max.toFloat()) }
  }

  @ReactProp(name = "suffix")
  fun setSuffix(view: RNSliderView, suffix: String?) {
    if (suffix == null) return
    view.suffix = suffix
  }

  @ReactProp(name = "prefix")
  fun setPrefix(view: RNSliderView, prefix: String?) {
    if (prefix == null) return
    view.prefix = prefix
  }

  override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any> {
    return MapBuilder.of(
      RangeSliderChangeEvent.EVENT_NAME, MapBuilder.of(
      "registrationName", "onChange"
      )
    )
  }
}
