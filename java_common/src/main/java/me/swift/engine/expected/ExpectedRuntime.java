package me.swift.engine.expected;

public class ExpectedRuntime {

  public static boolean isInt(String input) {
    if ((input == null) || (input.isEmpty())) {
      return false;
    }

    try {
      Integer.parseInt(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static int parseInt(String input) {
    if (input == null) {
      return 0;
    }
    return Integer.parseInt(input);
  }

  public static boolean isDouble(String input) {
    if ((input == null) || (input.isEmpty())) {
      return false;
    }

    try {
      Double.parseDouble(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public static double parseDouble(String input) {
    if (input == null) {
      return 0.0;
    }
    return Double.parseDouble(input);
  }

  public static String intToString(int value) {
    return Integer.toString(value);
  }

  public static String doubleToString(double value) {
    return Double.toString(value);
  }
}
