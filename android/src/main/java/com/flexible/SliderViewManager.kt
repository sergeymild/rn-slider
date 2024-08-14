package com.flexible

import android.content.res.ColorStateList
import com.facebook.react.bridge.ColorPropConverter
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.PixelUtil
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class SliderViewManager : SimpleViewManager<SliderHostView>() {
  override fun getName() = "SliderView"

  private fun ReadableMap.double(key: String): Double? {
    if (hasKey(key)) return getDouble(key)
    return null
  }

  override fun createViewInstance(reactContext: ThemedReactContext): SliderHostView {
    return SliderHostView(reactContext)
  }

  override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any>? {
    return MapBuilder.builder<String, Any>()
      .put("onValueChange", MapBuilder.of("registrationName", "onValueChange"))
      .put("onPremiumValue", MapBuilder.of("registrationName", "onPremiumValue"))
      .build()
  }

  @ReactProp(name = "params")
  fun setParams(view: SliderHostView, params: ReadableMap) {
    view.slider.setValues(
      values = arrayListOf(params.getDouble("minimumValue").toFloat(), params.getDouble("maximumValue").toFloat()),
      valueTo = params.getDouble("to").toFloat()
    )

    val padding = PixelUtil.toPixelFromDIP(0f)
    view.slider.setPadding(padding.toInt(), padding.toInt(), padding.toInt(), padding.toInt())

    val thumbStroke = PixelUtil.toPixelFromDIP(params.getDouble("thumbStrokeWidth"))
    view.slider.setThumbStrokeWidthActive(thumbStroke)
    view.slider.setThumbStrokeWidthInactive(thumbStroke)

    val strokeColor = ColorPropConverter.getColor(params.getDouble("thumbStrokeColor"), view.context)
    view.slider.setThumbStrokeColorActive(ColorStateList.valueOf(strokeColor))
    view.slider.setThumbStrokeColorInactive(ColorStateList.valueOf(strokeColor))

    val fillColor = ColorPropConverter.getColor(params.getDouble("thumbFillColor"), view.context)
    view.slider.setThumbFillColorActive(ColorStateList.valueOf(fillColor))
    view.slider.setThumbFillColorInactive(ColorStateList.valueOf(fillColor))

    val elevation = PixelUtil.toPixelFromDIP(params.getDouble("thumbElevation"))
    view.slider.setThumbElevationActive(elevation)

    view.slider.setThumbElevationInactive(elevation)
    view.slider.setPremiumValuee(params.double("premiumValue")?.toFloat() ?: -1f)

    val radius =  PixelUtil.toPixelFromDIP(params.getDouble("thumbRadius")).toInt()
    view.slider.setThumbRadiusActive(radius)
    view.slider.setThumbRadiusInactive(radius)

    if (params.hasKey("tickColor")) {
      val tickColor = ColorPropConverter.getColor(params.getDouble("tickColor"), view.context)
      view.slider.setTickColorActive(ColorStateList.valueOf(tickColor))
      view.slider.setTickColorInactive(ColorStateList.valueOf(tickColor))
      view.slider.isTickVisible = false
    }

    if (params.hasKey("premiumColor")) {
      val tickColor = ColorPropConverter.getColor(params.getDouble("premiumColor"), view.context)
      view.slider.setPremiumColor(ColorStateList.valueOf(tickColor))
    }

    val trackColorActive = ColorPropConverter.getColor(params.getDouble("trackColorActive"), view.context)
    view.slider.setTrackColorActive(ColorStateList.valueOf(trackColorActive))

    val trackColorInactive = ColorPropConverter.getColor(params.getDouble("trackColorInactive"), view.context)
    view.slider.setTrackColorInactive(ColorStateList.valueOf(trackColorInactive))

    val trackHeight = PixelUtil.toPixelFromDIP(params.getDouble("trackHeight")).toInt()
    view.slider.setTrackHeight(trackHeight)

    view.slider.update()
  }
}
