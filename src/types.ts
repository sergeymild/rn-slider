import {NativeSyntheticEvent, StyleProp, ViewStyle} from 'react-native';

export type OnRangeValueChange = { from: number; to: number };
export type OnValueChange = { to: number };

export type CommomSliderProps = {
  maximumValue: number;
  minimumValue: number;
  trackHeight: number;
  thumbStrokeWidth: number;
  thumbStrokeColor: string;
  thumbFillColor: string;
  thumbElevation: number;
  thumbRadius: number;
  tickColor?: string;
  trackColorActive: string;
  trackColorInactive: string;
  to: number;
};

export type RangeSliderProps = {
  params: { from: number } & CommomSliderProps;
  style: StyleProp<ViewStyle>;
  onValueChange?: (event: NativeSyntheticEvent<OnRangeValueChange>) => void;
};

export type SliderProps = {
  params: CommomSliderProps;
  style: StyleProp<ViewStyle>;
  onValueChange?: (event: NativeSyntheticEvent<OnValueChange>) => void;
  onPremiumValue?: (event: NativeSyntheticEvent<any>) => void;
};
