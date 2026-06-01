package me.swift.engine.data.json;

public class JsonNull extends JsonElement {

  @Override
  public void destroy() {
    super.destroy();
  }

  @Override
  public String serialize() {
    return "null";
  }

  @Override
  public boolean isJsonNull() {
    return true;
  }

  @Override
  public String getAsString() {
    return "null";
  }
}
