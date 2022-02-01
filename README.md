# AES Pack
aespack use AES-CBC-PKCS5Padding. This library originally from [react-native-aes-kit](https://github.com/rocwong-cn/react-native-aes-kit), i just porting each platform native code to work with Flutter.

## Methods

### encrypt
| Name | Description |
| ------ | ------ |
| text | plain text |
| key | secret key, max 16 bytes |
| iv | initialization vector, max 16 bytes |

### decrypt
| Name | Description |
| ------ | ------ |
| text | cipher text |
| key | secret key, max 16 bytes |
| iv | initialization vector, max 16 bytes |

## How To Use
### encrypt
```
import 'package:aespack/aespack.dart';
...
// inside async method
var text = 'Test';
var key = '0102030405060708';
var iv = '1112131415161718';
var result = await Aespack.encrypt(text, key, iv) ?? '';
// result is 'IOCQgs4aK+K4lVWSg/W81w=='
```

### encrypt
```
import 'package:aespack/aespack.dart';
...
// inside async method
var text = 'IOCQgs4aK+K4lVWSg/W81w==';
var key = '0102030405060708';
var iv = '1112131415161718';
var result = await Aespack.encrypt(text, key, iv) ?? '';
// result is 'Test'
```