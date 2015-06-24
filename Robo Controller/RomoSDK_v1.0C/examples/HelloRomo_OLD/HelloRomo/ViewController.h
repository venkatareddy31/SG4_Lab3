//
//  ViewController.h
//  HelloRomo
//

#import <UIKit/UIKit.h>
#import <RMCore/RMCore.h>
#import <RMCharacter/RMCharacter.h>
#import <OpenEars/OEEventsObserver.h>

@interface ViewController : UIViewController <RMCoreDelegate, OEEventsObserverDelegate>

@property (nonatomic, strong) RMCoreRobotRomo3 *Romo3;
@property (nonatomic, strong) RMCharacter *Romo;


@property (strong, nonatomic) OEEventsObserver *openEarsEventsObserver;


- (void)addGestureRecognizers;

@end
