//
//  RangeSliderManager.h
//  jesster2k10-react-native-range-slider
//
//  Created by Jesse Onolememen on 23/03/2020.
//

#if __has_include(<React/RCTViewManager.h>)
#import <React/RCTViewManager.h>
#elif __has_include("RCTViewManager.h")
#import "RCTViewManager.h"
#else
#import "React/RCTViewManager.h" // Required when used as a Pod in a Swift project
#endif

// Subclass your view manager off the RCTViewManager
// http://facebook.github.io/react-native/docs/native-components-ios.html#ios-mapview-example
@interface RNRangeSliderManager : RCTViewManager

@end


