package me.swift.engine.data.json;

public class JsonPrimitive extends JsonElement {

  private Object value;

  public JsonPrimitive() {
    this(null);
  }

  public JsonPrimitive(Object value) {
    this.value = value;
  }

  @Override
  public String serialize() {
    if (isBoolean()) {
      Boolean bool = getAsBoolean();
      return bool != null && bool ? "true" : "false";
    }
    if (isInteger()) {
      Integer intVal = getAsInteger();
      return intVal != null ? String.valueOf(intVal) : "";
    }
    if (isDouble()) {
      Double doubleVal = getAsDouble();
      return doubleVal != null ? String.valueOf(doubleVal) : "";
    }
    if (isString()) {
      String string = getAsString();
      if (string == null) {
        string = "";
      }
      string = string.replace("\"", "\\\"");
      return "\"" + string + "\"";
    }
    return "";
  }

  @Override
  public boolean isBoolean() {
    return value instanceof Boolean;
  }

  @Override
  public boolean isInteger() {
    return value instanceof Integer;
  }

  @Override
  public boolean isDouble() {
    return value instanceof Double;
  }

  @Override
  public boolean isString() {
    return value instanceof String;
  }

  @Override
  public Boolean getAsBoolean() {
    if (isBoolean()) {
      return (Boolean) value;
    }
    return null;
  }

  @Override
  public Integer getAsInteger() {
    if (value instanceof Integer) {
      return (Integer) value;
    }
    return null;
  }

  @Override
  public Double getAsDouble() {
    if (value instanceof Double) {
      return (Double) value;
    }
    return null;
  }

  @Override
  public String getAsString() {
    if (value instanceof String) {
      return (String) value;
    } else if (value instanceof Integer) {
      return String.valueOf(value);
    } else if (value instanceof Double) {
      return String.valueOf(value);
    } else if (value instanceof Boolean) {
      return String.valueOf(value);
    }
    return null;
  }
}
