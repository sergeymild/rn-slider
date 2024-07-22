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
    console.log('🍓[RangeSlider.handleChange]', nativeEvent);
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

  console.log('🍓[RangeSlider.RangeSlider]', commonProps);

  return (
    <Slider
      disableRange={props.type === 'slider'}
      minValue={commonProps.minimumValue}
      maxValue={commonProps.maximumValue}
      selectedMaximum={commonProps.from}
      selectedMinimum={
        props.type === 'range' ? props.props.params.from : undefined
      }
      //step={Number(step)}
      tintColor={processColor(commonProps.trackColorActive)}
      tintColorBetweenHandles={processColor(commonProps.trackColorInactive)}
      //handleBorderColor={handleBorderColor}
      // handleBorderWidth={handleBorderWidth}
      handleColor={processColor(commonProps.thumbFillColor)}
      // handleDiameter={handleDiameter}
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
