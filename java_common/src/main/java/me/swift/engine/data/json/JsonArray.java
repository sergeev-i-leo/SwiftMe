package me.swift.engine.data.json;

import me.swift.engine.expected.ExpectedList;

public class JsonArray extends JsonElement {

  private ExpectedList<JsonElement> jsonElements = new ExpectedList<>();

  @Override
  public void destroy() {
    jsonElements.destroyAll();
    jsonElements.destroy();
    super.destroy();
  }

  @Override
  public String serialize() {
    StringBuilder text = new StringBuilder("[");
    for (int i = 0; i < size(); i++) {
      if (i > 0) {
        text.append(",");
      }
      text.append(get(i).serialize());
    }
    text.append("]");
    return text.toString();
  }

  public int size() {
    return jsonElements.size();
  }

  public boolean isEmpty() {
    return jsonElements.size() == 0;
  }

  public void add(Object value) {
    if (value instanceof JsonElement) {
      jsonElements.add((JsonElement) value);
      return;
    }
    jsonElements.add(value == null ? new JsonNull() : new JsonPrimitive(value));
  }

  public void set(int index, JsonElement jsonElement) {
    jsonElements.set(index, jsonElement != null ? jsonElement : new JsonNull());
  }

  public JsonElement remove(int index) {
    return jsonElements.removeAt(index);
  }

  public JsonElement get(int index) {
    return jsonElements.get(index);
  }
}
