package com.jesster2k10reactnativerangeslider


import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.flexible.RangeSliderViewManager
import com.flexible.SliderViewManager

class ReactNativeRangeSliderPackage : ReactPackage {
  override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
    return emptyList()
  }

  override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return mutableListOf(
          RNRangeSliderManager(),
          SliderViewManager(),
              RangeSliderViewManager()
        )
    }
}
