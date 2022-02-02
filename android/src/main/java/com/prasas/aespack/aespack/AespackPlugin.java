package com.prasas.aespack.aespack;

import android.os.Build;

import androidx.annotation.NonNull;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/** AespackPlugin */
public class AespackPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "aespack");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
   if (call.method.equals("encrypt")) {
      String text = call.argument("text");
      String secretKey = call.argument("key");
      String ivParameter = call.argument("iv");
      if (text == null) {
        result.error("ERROR", "text cannot be null", null);
        return;
      }
      if (secretKey == null) {
        result.error("ERROR", "key cannot be null", null);
        return;
      }
      if (ivParameter == null) {
        result.error("ERROR", "iv cannot be null", null);
        return;
      }
      encrypt(text, secretKey, ivParameter, result);
    } else if (call.method.equals("decrypt")) {
      String text = call.argument("text");
      String secretKey = call.argument("key");
      String ivParameter = call.argument("iv");
      if (text == null) {
        result.error("ERROR", "text cannot be null", null);
        return;
      }
      if (secretKey == null) {
        result.error("ERROR", "key cannot be null", null);
        return;
      }
      if (ivParameter == null) {
        result.error("ERROR", "iv cannot be null", null);
        return;
      }
      decrypt(text, secretKey, ivParameter, result);
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  public void encrypt(@NonNull String text, @NonNull String secretKey, @NonNull String ivParameter, @NonNull Result result) {
    try {
      byte[] encrypted = null;
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      byte[] raw = secretKey.getBytes();
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
      cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
      } else {
        encrypted = cipher.doFinal(text.getBytes("utf-8"));
      }
      String cipherString = new BASE64Encoder().encode(encrypted);

      result.success(cipherString);
    } catch (Exception e) {
      e.printStackTrace();
      result.error("ERROR", e.getMessage(), e);
    }
  }

  public void decrypt(@NonNull String sSrc, @NonNull String secretKey, @NonNull String ivParameter, @NonNull Result result) {
    try {
      byte[] raw;
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        raw = secretKey.getBytes(StandardCharsets.US_ASCII);
      } else {
        raw = secretKey.getBytes("ASCII");
      }
      SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
      cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

      byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);

      byte[] original = cipher.doFinal(encrypted1);
      String originalString;
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        originalString = new String(original, StandardCharsets.UTF_8);
      } else {
        originalString = new String(original, "utf-8");
      }

      result.success(originalString);
    } catch (Exception e) {
      e.printStackTrace();
      result.error("ERROR", e.getMessage(), e);
    }
  }
}
