package com.jesster2k10reactnativerangeslider

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar
import com.facebook.react.bridge.ColorPropConverter
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.UIManagerModule

class RNSliderView(context: Context): LinearLayout(context), OnSeekbarChangeListener {
  private var rangeSeekBar: CrystalSeekbar
  private var minTextView: TextView?

  private var minValue: Float = 0f
  private var maxValue: Float = 100f

  var suffix: String? = ""
    set(value) {
      field = value
      updateText()
    }

  var prefix: String? = ""
    set(value) {
      field = value
      updateText()
    }

  init {
    inflate(context, R.layout.slider, this)
    rangeSeekBar = findViewById(R.id.seek_bar)
    rangeSeekBar.setOnSeekbarChangeListener(this)
    rangeSeekBar.setMinStartValue(minValue)
    rangeSeekBar.setMaxValue(maxValue)

    minTextView = findViewById(R.id.range_seek_bar_min)
    updateText(minValue)
  }

  fun setTintColorBetweenHandles(color: Double) {
    rangeSeekBar.setBarHighlightColor(ColorPropConverter.getColor(color, context))
  }

  fun setTintColor(color: Double) {
    rangeSeekBar.setBarColor(ColorPropConverter.getColor(color, context))
  }

  fun setHandleColor(color: Double) {
    rangeSeekBar.setThumbColor(ColorPropConverter.getColor(color, context))
  }

  fun setHandlePressedColor(color: Double) {
    rangeSeekBar.setThumbHighlightColor(ColorPropConverter.getColor(color, context))
  }

  fun setLeftHandleColor(color: Double) {
    rangeSeekBar.setThumbColor(ColorPropConverter.getColor(color, context))
  }

  fun setLeftHandlePressedColor(color: Double) {
    rangeSeekBar.setThumbHighlightColor(ColorPropConverter.getColor(color, context))
  }

  fun setCornerRadius(diameter: Float) {
    rangeSeekBar.setCornerRadius(diameter / 2)
  }

  fun setMinValue(min: Float) {
    minValue = min
    rangeSeekBar.setMinValue(min)
    updateText(minValue)
  }

  fun setMaxValue(max: Float) {
    maxValue = max
    rangeSeekBar.setMaxValue(max)
    updateText(minValue)
  }

  fun setMinStartValue(minStartValue: Float) {
    rangeSeekBar.setMinStartValue(minStartValue)
  }

  fun setMaxStartValue(maxStartValue: Float) {
    rangeSeekBar.setMinStartValue(maxStartValue)
  }

  fun setStep(steps: Float) {
    rangeSeekBar.setSteps(steps)
  }

  private fun updateText(min: Number? = null) {
    var minText = minTextView?.text
    if (min !== null) {
      minText = min.toString()
    }

    minTextView?.text = StringBuilder()
      .append(prefix)
      .append(minText)
      .append(suffix)
    minTextView?.visibility = if (prefix == null || prefix?.isEmpty() == true) GONE else VISIBLE
  }

  override fun valueChanged(value: Number?) {
    if (value == null) return
    updateText(value)

    val event = RangeSliderChangeEvent(rangeSeekBar.id)
    event.max = value.toDouble()
    event.min = value.toDouble()

    val reactContext = context as ReactContext
    reactContext.getNativeModule(UIManagerModule::class.java)?.eventDispatcher?.dispatchEvent(
      event
    )
  }
}
