#import "AespackPlugin.h"
#import "AesKit/SecurityUtil.h"
#import <CommonCrypto/CommonCryptor.h>

@implementation AespackPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"aespack"
            binaryMessenger:[registrar messenger]];
  AespackPlugin* instance = [[AespackPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"getPlatformVersion" isEqualToString:call.method]) {
    result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
  } else if ([@"encrypt" isEqualToString:call.method]) {
      NSDictionary *args = call.arguments;
      NSString *text = [args objectForKey:@"text"];
      NSString *key = [args objectForKey:@"key"];
      NSString *iv = [args objectForKey:@"iv"];
      if ([[SecurityUtil encryptAESData:text app_key:key gIv:iv] length] <= 0) {
          result([FlutterError errorWithCode:@"ERROR" message:@"Failed to encrypt" details:nil]);
          return;
      }
      result([SecurityUtil encryptAESData:text app_key:key gIv:iv]);
  } else if ([@"decrypt" isEqualToString:call.method]) {
      NSDictionary *args = call.arguments;
      NSString *text = [args objectForKey:@"text"];
      NSString *key = [args objectForKey:@"key"];
      NSString *iv = [args objectForKey:@"iv"];
      if ([[SecurityUtil decryptAESNString:text app_key:key gIv:iv] length] <= 0) {
          result([FlutterError errorWithCode:@"ERROR" message:@"Failed to encrypt" details:nil]);
          return;
      }
      result([SecurityUtil decryptAESNString:text app_key:key gIv:iv]);
  } else {
    result(FlutterMethodNotImplemented);
  }
}

@end
