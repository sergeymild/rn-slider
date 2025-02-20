package com.flexible

import android.content.Context
import android.widget.FrameLayout
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter

fun SliderHostView.onValueChange(
  to: Float
) {
  val event = Arguments.createMap()
  event.putDouble("to", to.toDouble())
  val reactContext = context as ReactContext
  reactContext.getJSModule(RCTEventEmitter::class.java).receiveEvent(id, "onValueChange", event)
}

fun SliderHostView.onPremiumValue() {
  val event = Arguments.createMap()
  val reactContext = context as ReactContext
  reactContext.getJSModule(RCTEventEmitter::class.java).receiveEvent(id, "onPremiumValue", event)
}

class SliderHostView(context: Context) : FrameLayout(context),
  AppFlexibleSlider.OnValueChangeListener {
  var slider: AppFlexibleSlider
  var previousValue = Float.MIN_VALUE
  var startValue = Float.MIN_VALUE

  init {
    slider = AppFlexibleSlider(context)
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
    from: Float,
    to: Float,
    state: AppFlexibleSlider.ValueChangeState
  ) {
    if (state == AppFlexibleSlider.ValueChangeState.Start) {
      startValue = to
      return
    }
    if (state == AppFlexibleSlider.ValueChangeState.Idle) {
      if (to <= slider.premiumValue) {
        slider.setValueTo(startValue)
        onPremiumValue()
        return onValueChange(startValue)
      }
    }


    if (previousValue == to) return
    previousValue = to
    onValueChange(to)
  }

  override fun onTouchUp() {
    val event = Arguments.createMap()
    event.putString("type", "end")
    val reactContext = context as ReactContext
    reactContext.getJSModule(RCTEventEmitter::class.java).receiveEvent(id, "onValueChange", event)
  }
}
