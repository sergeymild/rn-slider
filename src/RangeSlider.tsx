import React from 'react';
import { processColor, requireNativeComponent } from 'react-native';
import { RangeSliderProps, SliderProps } from './types';

const Slider = requireNativeComponent('RNRangeSlider');

type Props =
  | { type: 'slider'; props: SliderProps }
  | { type: 'range'; props: RangeSliderProps };
export const RangeSlider: React.FC<Props> = props => {
  const defaultStyle = {
    width: '100%',
    height: 70,
  };
  const handleChange = ({ nativeEvent }: any) => {
    if (props.type === 'slider') {
      //@ts-ignore
      props.props.onValueChange({ nativeEvent: { to: nativeEvent.max } });
    } else {
      //@ts-ignore
      props.props.onValueChange({
        nativeEvent: { from: nativeEvent.min, to: nativeEvent.max },
      });
    }
    //onChange && onChange(nativeEvent.min, nativeEvent.max);
  };

  const commonProps = props.props.params;

  let minimum: number | undefined;
  let maximum: number | undefined;
  if (props.type === 'slider') {
    maximum = props.props.params.to;
  }
  if (props.type === 'range') {
    minimum = props.props.params.from;
    maximum = props.props.params.to;
  }

  return (
    <Slider
      // @ts-ignore
      disableRange={props.type === 'slider'}
      minValue={commonProps.minimumValue}
      maxValue={commonProps.maximumValue}
      selectedMaximum={maximum}
      selectedMinimum={minimum}
      //step={Number(step)}
      tintColor={processColor(commonProps.trackColorInactive)}
      tintColorBetweenHandles={processColor(commonProps.trackColorActive)}
      handleBorderColor={processColor(commonProps.thumbStrokeColor)}
      handleBorderWidth={commonProps.thumbStrokeWidth}
      handleColor={processColor(commonProps.thumbFillColor)}
      handleDiameter={commonProps.thumbRadius * 2}
      minimumRange={commonProps.minimumRange as any}
      // minLabelColour={minLabelColor}
      // minLabelFont={minLabelFont}
      // minLabelFontSize={minLabelFontSize}
      // maxLabelFont={maxLabelFont}
      // maxLabelFontSize={maxLabelFontSize}
      // maxLabelColour={maxLabelColor}
      lineHeight={commonProps.trackHeight}
      // lineBorderWidth={lineBorderWidth}
      //lineBorderColor={processColor(commonProps.trackColorActive)}
      prefix={''}
      suffix={''}
      hideLabels
      labelPadding={0}
      minDistance={undefined}
      maxDistance={undefined}
      onChange={handleChange}
      style={[defaultStyle, props.props.style]}
    />
  );
};
