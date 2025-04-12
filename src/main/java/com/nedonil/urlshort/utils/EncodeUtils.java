package com.nedonil.urlshort.utils;

public class EncodeUtils {

  private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

  private EncodeUtils() {}

  public static String encodeStringToBase62(String input) {
    byte[] bytes = input.getBytes();
    long number = 0;

    for (byte b : bytes) {
      number = (number << 8) + (b & 0xFF);
    }

    return encodeBase62(number);
  }

  private static String encodeBase62(long number) {
    if (number == 0) {
      return "0";
    }

    StringBuilder sb = new StringBuilder();
    while (number > 0) {
      int remainder = (int)(number % 62);
      sb.append(BASE62.charAt(remainder));
      number = number / 62;
    }
    return sb.reverse().toString();
  }
}
