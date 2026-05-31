package me.swift.engine.data.json;

import me.swift.engine.expected.ExpectedList;
import me.swift.engine.expected.ExpectedMap;

public class JsonObject extends JsonElement {

  private ExpectedMap<JsonElement> jsonElements = new ExpectedMap<>();

  @Override
  public void destroy() {
    jsonElements.destroyAll();
    jsonElements.destroy();
    super.destroy();
  }

  @Override
  public String serialize() {
    StringBuilder text = new StringBuilder("{");
    addMember("$className", getClassName());
    ExpectedList<String> keys = keySet();
    for (int i = 0; i < keys.size(); i++) {
      if (i > 0) {
        text.append(",");
      }
      text.append("\"").append(keys.get(i)).append("\":");
      JsonElement member = getMember(keys.get(i));
      if (member != null) {
        text.append(member.serialize());
      }
    }
    text.append("}");
    return text.toString();
  }

  public String getClassName() {
    return "JsonObject";
  }

  public void deserialize(JsonObject parsedJsonObject) {
  }

  public ExpectedList<String> keySet() {
    return jsonElements.keys();
  }

  public void add(String memberName, JsonElement value) {
    jsonElements.set(memberName, value != null ? value : new JsonNull());
  }

  public void addMember(String memberName, Object value) {
    if (value == null) {
      jsonElements.set(memberName, new JsonNull());
      return;
    }
    if (value instanceof JsonElement) {
      jsonElements.set(memberName, (JsonElement) value);
      return;
    }
    jsonElements.set(memberName, new JsonBooleanPrimitive(value));
  }

  public JsonElement getMember(String memberName) {
    return jsonElements.get(memberName);
  }

  public Boolean getMemberAsBoolean(String memberName) {
    JsonElement jsonElement = jsonElements.get(memberName);
    if (jsonElement instanceof JsonBooleanPrimitive) {
      JsonBooleanPrimitive jsonPrimitive = (JsonBooleanPrimitive) jsonElement;
      if (jsonPrimitive.isBoolean()) {
        return jsonPrimitive.getAsBoolean();
      }
    }
    return null;
  }

  public Integer getMemberAsInteger(String memberName) {
    JsonElement jsonElement = jsonElements.get(memberName);
    if (jsonElement instanceof JsonBooleanPrimitive) {
      JsonBooleanPrimitive jsonPrimitive = (JsonBooleanPrimitive) jsonElement;
      if (jsonPrimitive.isInteger()) {
        return jsonPrimitive.getAsInteger();
      }
    }
    return null;
  }

  public Double getMemberAsDouble(String memberName) {
    JsonElement jsonElement = jsonElements.get(memberName);
    if (jsonElement instanceof JsonBooleanPrimitive) {
      JsonBooleanPrimitive jsonPrimitive = (JsonBooleanPrimitive) jsonElement;
      if (jsonPrimitive.isDouble()) {
        return jsonPrimitive.getAsDouble();
      }
    }
    return null;
  }

  public String getMemberAsString(String memberName) {
    JsonElement jsonElement = jsonElements.get(memberName);
    if (jsonElement instanceof JsonBooleanPrimitive) {
      JsonBooleanPrimitive jsonPrimitive = (JsonBooleanPrimitive) jsonElement;
      if (jsonPrimitive.isString()) {
        return jsonPrimitive.getAsString();
      }
    }
    return null;
  }

  public JsonArray getMemberAsJsonArray(String memberName) {
    JsonElement jsonElement = jsonElements.get(memberName);
    if (jsonElement instanceof JsonArray) {
      return (JsonArray) jsonElement;
    }
    return null;
  }

  public JsonObject getMemberAsJsonObject(String memberName) {
    JsonElement jsonElement = jsonElements.get(memberName);
    if (jsonElement instanceof JsonObject) {
      return (JsonObject) jsonElement;
    }
    return null;
  }

  public void removeMember(String memberName) {
    jsonElements.remove(memberName);
  }
}
