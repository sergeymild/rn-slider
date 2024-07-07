package com.jesster2k10reactnativerangeslider

import android.content.Context
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class RNRangeSliderManager: SimpleViewManager<RNRangeSliderView>() {
  override fun getName(): String {
    return "RNRangeSlider"
  }

  override fun createViewInstance(reactContext: ThemedReactContext): RNRangeSliderView {
    return RNRangeSliderView(reactContext as Context)
  }

  @ReactProp(name = "tintColorBetweenHandles")
  fun setTintColorBetweenHandles(view: RNRangeSliderView, color: String?) {
    if (color == null) return
    view.run { setTintColorBetweenHandles(color) }
  }

  @ReactProp(name = "tintColor")
  fun setTintColor(view: RNRangeSliderView, color: String?) {
    if (color == null) return
    view.run { setTintColor(color) }
  }

  @ReactProp(name = "handleColor")
  fun setHandleColor(view: RNRangeSliderView, color: String?) {
    if (color == null) return
    view.run { setHandleColor(color) }
  }

  @ReactProp(name = "handlePressedColor")
  fun setHandlePressedColor(view: RNRangeSliderView, color: String?) {
    if (color == null) return
    view.run { setHandlePressedColor(color) }
  }

  @ReactProp(name = "step")
  fun setStep(view: RNRangeSliderView, step: Int?) {
    if (step == null) return
    view.run { setStep(step.toFloat()) }
  }

  @ReactProp(name = "leftHandlePressedColor")
  fun setLeftHandlePressedColor(view: RNRangeSliderView, color: String?) {
    if (color == null) return
    view.run { setLeftHandlePressedColor(color) }
  }

  @ReactProp(name = "rightHandlePressedColor")
  fun setRightHandlePressedColor(view: RNRangeSliderView, color: String?) {
    if (color == null) return
    view.run { setRightHandlePressedColor(color) }
  }

  @ReactProp(name = "leftHandleColor")
  fun setLeftHandleColor(view: RNRangeSliderView, color: String?) {
    if (color == null) return
    view.run { setLeftHandleColor(color) }
  }

  @ReactProp(name = "rightHandleColor")
  fun setRightHandleColor(view: RNRangeSliderView, color: String?) {
    if (color == null) return
    view.run { setRightHandleColor(color) }
  }

  @ReactProp(name = "handleDiameter")
  fun setCornerRadius(view: RNRangeSliderView, radius: Int?) {
    if (radius == null) return
    view.run { setCornerRadius(radius.toFloat()) }
  }

  @ReactProp(name = "min")
  fun setMin(view: RNRangeSliderView, min: Int?) {
    if (min == null) return
    view.run { setMinValue(min.toFloat()) }
  }

  @ReactProp(name = "max")
  fun setMax(view: RNRangeSliderView, max: Int?) {
    if (max == null) return
    view.run { setMaxValue(max.toFloat()) }
  }

  @ReactProp(name = "minStart")
  fun setMinStart(view: RNRangeSliderView, min: Int?) {
    if (min == null) return
    view.run { setMinStartValue(min.toFloat()) }
  }

  @ReactProp(name = "maxStart")
  fun maxStart(view: RNRangeSliderView, max: Int?) {
    if (max == null) return
    view.run { setMaxStartValue(max.toFloat()) }
  }

  @ReactProp(name = "fixGap")
  fun setFixGap(view: RNRangeSliderView, gap: Int?) {
    if (gap == null) return
    view.run { setFixGap(gap.toFloat()) }

  }

  @ReactProp(name = "suffix")
  fun setSuffix(view: RNRangeSliderView, suffix: String?) {
    if (suffix == null) return
    view.suffix = suffix
  }

  @ReactProp(name = "prefix")
  fun setPrefix(view: RNRangeSliderView, prefix: String?) {
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
