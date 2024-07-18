import React from 'react';
import { Platform, processColor, requireNativeComponent } from "react-native";
import { RangeSliderProps, RangeSliderChangeEvent } from './types';

const Slider = requireNativeComponent('RNRangeSlider');
const RNSlider = requireNativeComponent('RNSlider');

const RangeSlider: React.FC<RangeSliderProps> = ({
  min,
  max,
  onChange,
  tintColor,
  tintColorBetweenHandles,
  step,
  handleBorderColor,
  handleColor,
  handleDiameter,
  handleBorderWidth,
  type = 'range',
  selectedMaximum,
  selectedMinimum,
  minLabelColor,
  maxLabelColor,
  lineHeight,
  prefix,
  suffix,
  hideLabels,
  maxLabelFont,
  minLabelFont,
  maxLabelFontSize,
  minLabelFontSize,
  lineBorderWidth,
  lineBorderColor,
  labelPadding,
  minDistance,
  maxDistance,
  leftHandleColor,
  rightHandleColor,
  leftHandlePressedColor,
  rightHandlePressedColor,
  handlePressedColor,
  minStartValue,
  maxStartValue,
  fixGap,
  style = {},
  cornerRadius,
}: RangeSliderProps) => {
  const defaultStyle = {
    width: '100%',
    height: 70,
  };
  const handleChange = ({ nativeEvent }: RangeSliderChangeEvent) => {
    onChange && onChange(nativeEvent.min, nativeEvent.max);
  };

  if (Platform.OS === 'android') {
    const Component = type === 'slider' ? RNSlider : Slider
    return (
      <Component
        min={Number(min)}
        max={Number(max)}
        step={Number(step)}
        tintColor={processColor(tintColor) ?? undefined}
        tintColorBetweenHandles={processColor(tintColorBetweenHandles) ?? undefined}
        handleColor={processColor(handleColor) ?? undefined}
        onChange={handleChange}
        leftHandleColor={processColor(leftHandleColor) ?? undefined}
        rightHandleColor={processColor(rightHandleColor) ?? undefined}
        minStartValue={minStartValue}
        maxStartValue={maxStartValue}
        fixGap={fixGap}
        leftHandlePressedColor={processColor(leftHandlePressedColor) ?? undefined}
        rightHandlePressedColor={processColor(rightHandlePressedColor) ?? undefined}
        handlePressedColor={processColor(handlePressedColor || handleColor) ?? undefined}
        cornerRadius={cornerRadius}
        prefix={prefix}
        suffix={suffix}
        style={[defaultStyle, style]}
      />
    );
  } else {
    return (
      <Slider
        disableRange={type === 'slider'}
        minValue={Number(min)}
        maxValue={Number(max)}
        step={Number(step)}
        selectedMaximum={selectedMaximum}
        selectedMinimum={selectedMinimum}
        tintColor={tintColor ? processColor(tintColor) : undefined}
        tintColorBetweenHandles={tintColorBetweenHandles ? processColor(tintColorBetweenHandles) : undefined}
        handleBorderColor={handleBorderColor ? processColor(handleBorderColor) : undefined}
        handleBorderWidth={handleBorderWidth}
        handleColor={handleColor ? processColor(handleColor) : undefined}
        handleDiameter={handleDiameter}
        minLabelColour={minLabelColor ? processColor(minLabelColor) : undefined}
        minLabelFont={minLabelFont}
        minLabelFontSize={minLabelFontSize}
        maxLabelFont={maxLabelFont}
        maxLabelFontSize={maxLabelFontSize}
        maxLabelColour={maxLabelColor ? processColor(maxLabelColor) : undefined}
        lineHeight={lineHeight}
        lineBorderWidth={lineBorderWidth}
        lineBorderColor={lineBorderColor ? processColor(lineBorderColor) : undefined}
        prefix={prefix}
        suffix={suffix}
        hideLabels={hideLabels}
        labelPadding={labelPadding}
        minDistance={minDistance}
        maxDistance={maxDistance}
        onChange={handleChange}
        style={[defaultStyle, style]}
      />
    );
  }
};

RangeSlider.defaultProps = {
  min: 0,
  max: 100,
  step: 1,
  type: 'range',
  selectedMinimum: 0,
  selectedMaximum: 100,
  tintColor: '#DCDCDC', // extra light gray
};

export default RangeSlider;
