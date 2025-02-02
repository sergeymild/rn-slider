#import <UIKit/UIKit.h>

@class RCTEventDispatcher;

@interface RNRangeSlider : UIView

// Define view properties here with @property
@property (nonatomic, assign) float minValue;
@property (nonatomic, assign) float maxValue;
@property (nonatomic, assign) float selectedMinimum;
@property (nonatomic, assign) float selectedMaximum;

@property (nonatomic, assign) float minDistance;
@property (nonatomic, assign) float maxDistance;

@property (nonatomic, assign) NSNumber* tintColor;
@property (nonatomic, assign) NSNumber* tintColorBetweenHandles;
@property (nonatomic, assign) NSNumber* minLabelColour;
@property (nonatomic, assign) NSNumber* maxLabelColour;
@property (nonatomic, assign) NSNumber* handleColor;
@property (nonatomic, assign) NSNumber* handleBorderColor;
@property (nonatomic, assign) float handleDiameter;
@property (nonatomic, assign) float handleBorderWidth;
@property (nonatomic, assign) float lineHeight;
@property (nonatomic, assign) float lineBorderWidth;
@property (nonatomic, assign) NSNumber* lineBorderColor;
@property (nonatomic, assign) NSString* suffix;
@property (nonatomic, assign) NSString* prefix;
@property (nonatomic, assign) BOOL disableRange;
@property (nonatomic, assign) BOOL hideLabels;

@property (nonatomic, assign) float step;
@property (nonatomic, assign) float labelPadding;
@property (nonatomic, assign) NSString* minLabelFont;
@property (nonatomic, assign) NSString* maxLabelFont;
@property (nonatomic, assign) float minLabelFontSize;
@property (nonatomic, assign) float maxLabelFontSize;

@property (nonatomic, assign) float premiumValue;
@property (nonatomic, assign) NSNumber* premiumColor;

// Initializing with the event dispatcher allows us to communicate with JS
- (instancetype)initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher NS_DESIGNATED_INITIALIZER;

@end
