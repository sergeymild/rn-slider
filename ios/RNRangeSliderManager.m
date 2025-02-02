#import <Foundation/Foundation.h>
#import "RNRangeSlider.h"
#import "RNRangeSliderManager.h"

// import RCTBridge
#if __has_include(<React/RCTBridge.h>)
#import <React/RCTBridge.h>
#elif __has_include("RCTBridge.h")
#import "RCTBridge.h"
#else
#import "React/RCTBridge.h" // Required when used as a Pod in a Swift project
#endif

@implementation RNRangeSliderManager

@synthesize bridge = _bridge;

// Export a native module
RCT_EXPORT_MODULE();

// Return the native view that represents your React component
- (UIView *)view
{
    return [[RNRangeSlider alloc] initWithEventDispatcher:self.bridge.eventDispatcher];
}

RCT_EXPORT_VIEW_PROPERTY(minValue, float)
RCT_EXPORT_VIEW_PROPERTY(maxValue, float)
RCT_EXPORT_VIEW_PROPERTY(selectedMinimum, float)
RCT_EXPORT_VIEW_PROPERTY(selectedMaximum, float)
RCT_EXPORT_VIEW_PROPERTY(tintColor, NSNumber);
RCT_EXPORT_VIEW_PROPERTY(tintColorBetweenHandles, NSNumber);
RCT_EXPORT_VIEW_PROPERTY(handleBorderColor, NSNumber);
RCT_EXPORT_VIEW_PROPERTY(handleBorderWidth, float);
RCT_EXPORT_VIEW_PROPERTY(handleColor, NSNumber);
RCT_EXPORT_VIEW_PROPERTY(handleDiameter, float);
RCT_EXPORT_VIEW_PROPERTY(minLabelColour, NSNumber);
RCT_EXPORT_VIEW_PROPERTY(maxLabelColour, NSNumber);
RCT_EXPORT_VIEW_PROPERTY(lineHeight, float);
RCT_EXPORT_VIEW_PROPERTY(lineBorderWidth, float);
RCT_EXPORT_VIEW_PROPERTY(lineBorderColor, NSNumber);
RCT_EXPORT_VIEW_PROPERTY(prefix, NSString);
RCT_EXPORT_VIEW_PROPERTY(suffix, NSString);
RCT_EXPORT_VIEW_PROPERTY(disableRange, BOOL);
RCT_EXPORT_VIEW_PROPERTY(hideLabels, BOOL);
RCT_EXPORT_VIEW_PROPERTY(minLabelFont, NSString);
RCT_EXPORT_VIEW_PROPERTY(maxLabelFont, NSString);
RCT_EXPORT_VIEW_PROPERTY(minLabelFontSize, float);
RCT_EXPORT_VIEW_PROPERTY(maxLabelFontSize, float);
RCT_EXPORT_VIEW_PROPERTY(labelPadding, float);
RCT_EXPORT_VIEW_PROPERTY(step, float);
RCT_EXPORT_VIEW_PROPERTY(minDistance, float);
RCT_EXPORT_VIEW_PROPERTY(maxDistance, float);
RCT_EXPORT_VIEW_PROPERTY(premiumColor, NSNumber);
RCT_EXPORT_VIEW_PROPERTY(premiumValue, float);

@end
