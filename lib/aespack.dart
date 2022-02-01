
import 'dart:async';

import 'package:flutter/services.dart';

class Aespack {
  static const MethodChannel _channel = MethodChannel('aespack');

  static Future<String?> encrypt(String text, String key, String iv) async {
    final String? encryptedText = await _channel.invokeMethod('encrypt', {'text': text, 'key': key, 'iv': iv});
    return encryptedText;
  }

  static Future<String?> decrypt(String text, String key, String iv) async {
    final String? encryptedText = await _channel.invokeMethod('decrypt', {'text': text, 'key': key, 'iv': iv});
    return encryptedText;
  }
}
