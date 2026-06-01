package me.swift.engine.data.json;

import me.swift.engine.contract.SwiftRuntime;

public class JsonDoublePrimitive extends JsonPrimitive {

  private double value;

  public JsonDoublePrimitive(double value) {
    this.value = value;
  }

  @Override
  public void destroy() {
    super.destroy();
  }

  @Override
  public String serialize() {
    return SwiftRuntime.doubleToString(value);
  }

  @Override
  public boolean isJsonDoublePrimitive() {
    return true;
  }

  public double getValue() {
    return value;
  }

  @Override
  public String getAsString() {
    return SwiftRuntime.doubleToString(value);
  }
}
