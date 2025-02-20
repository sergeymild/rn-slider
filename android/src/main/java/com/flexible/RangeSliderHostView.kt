package com.flexible

import android.content.Context
import android.widget.FrameLayout
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter

fun RangeSliderHostView.onValueChange(
  from: Float,
  to: Float
) {
  val event = Arguments.createMap()
  event.putDouble("from", from.toDouble())
  event.putDouble("to", to.toDouble())
  val reactContext = context as ReactContext
  reactContext.getJSModule(RCTEventEmitter::class.java).receiveEvent(id, "onValueChange", event)
}

class RangeSliderHostView(context: Context) : FrameLayout(context),
  AppFlexibleStepRangeSlider.OnValueChangeListener {
  var slider: AppFlexibleStepRangeSlider

  init {
    slider = AppFlexibleStepRangeSlider(context)
    addView(slider)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    slider.addOnValueChangeListener(this)
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    slider.removeOnValueChangeListener(this)
  }

  override fun onValueChange(
    activeThumbIdx: Int,
    from: Float,
    to: Float,
    state: AppFlexibleStepRangeSlider.ValueChangeState
  ) {
    onValueChange(from, to)
  }

  override fun onTouchUp() {
    val event = Arguments.createMap()
    event.putString("type", "end")
    val reactContext = context as ReactContext
    reactContext.getJSModule(RCTEventEmitter::class.java).receiveEvent(id, "onValueChange", event)
  }
}
