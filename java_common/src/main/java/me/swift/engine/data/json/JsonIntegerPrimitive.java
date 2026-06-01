package me.swift.engine.data.json;

import me.swift.engine.contract.SwiftRuntime;

public class JsonIntegerPrimitive extends JsonPrimitive {

  private int value;

  public JsonIntegerPrimitive(int value) {
    this.value = value;
  }

  @Override
  public void destroy() {
    super.destroy();
  }

  @Override
  public String serialize() {
    return SwiftRuntime.intToString(value);
  }

  @Override
  public boolean isJsonIntegerPrimitive() {
    return true;
  }

  public int getValue() {
    return value;
  }

  @Override
  public String getAsString() {
    return SwiftRuntime.intToString(value);
  }
}
