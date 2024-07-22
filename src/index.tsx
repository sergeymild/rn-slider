import React, { memo } from 'react';
import { RangeSlider } from './RangeSlider';
import {
  requireNativeComponent,
  UIManager,
  Platform,
  processColor,
} from 'react-native';

import {OnRangeValueChange, OnValueChange, RangeSliderProps, SliderProps} from './types';
export type {OnRangeValueChange, OnValueChange, RangeSliderProps, SliderProps}


const LINKING_ERROR =
  `The package 'react-native-slider' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';



const _RangeSliderView =
  UIManager.getViewManagerConfig('RangeSliderView') != null
    ? requireNativeComponent<RangeSliderProps>('RangeSliderView')
    : () => {
      throw new Error(LINKING_ERROR);
    };

const _SliderView =
  UIManager.getViewManagerConfig('SliderView') != null
    ? requireNativeComponent<SliderProps>('SliderView')
    : () => {
      throw new Error(LINKING_ERROR);
    };

export const RangeSliderView: React.FC<RangeSliderProps> = memo((props) => {
  if (Platform.OS === 'ios') {
    return <RangeSlider props={props} type={'range'} />
  }
  return (
    <_RangeSliderView
      style={props.style}
      onValueChange={props.onValueChange}
      params={{
        ...props.params,
        thumbStrokeColor: processColor(props.params.thumbStrokeColor) as any,
        thumbFillColor: processColor(props.params.thumbFillColor) as any,
        tickColor: processColor(props.params.tickColor) as any,
        trackColorActive: processColor(props.params.trackColorActive) as any,
        trackColorInactive: processColor(
          props.params.trackColorInactive
        ) as any,
      }}
    />
  );
});

export const SliderView: React.FC<SliderProps> = memo((props) => {
  if (Platform.OS === 'ios') {
    return <RangeSlider props={props} type={'slider'} />
  }

  return (
    <_SliderView
      style={props.style}
      onValueChange={props.onValueChange}
      onPremiumValue={props.onPremiumValue}
      params={{
        ...props.params,
        thumbStrokeColor: processColor(props.params.thumbStrokeColor) as any,
        thumbFillColor: processColor(props.params.thumbFillColor) as any,
        tickColor: processColor(props.params.tickColor) as any,
        trackColorActive: processColor(props.params.trackColorActive) as any,
        trackColorInactive: processColor(
          props.params.trackColorInactive
        ) as any,
      }}
    />
  );
});

