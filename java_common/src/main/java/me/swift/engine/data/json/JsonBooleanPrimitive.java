package me.swift.engine.data.json;

public class JsonBooleanPrimitive extends JsonPrimitive {

  private boolean value;

  public JsonBooleanPrimitive(boolean value) {
    this.value = value;
  }

  @Override
  public String serialize() {
    return value ? "true" : "false";
  }

  @Override
  public boolean isJsonBooleanPrimitive() {
    return true;
  }

  public boolean getValue() {
    return value;
  }

  @Override
  public String getAsString() {
    return value ? "true" : "false";
  }
}
