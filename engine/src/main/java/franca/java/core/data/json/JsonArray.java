package franca.java.core.data.json;

import franca.java.core.contracted.ContractedArray;
import franca.java.core.contracted.ContractedStringBuffer;

public class JsonArray extends JsonElement {

  private ContractedArray<JsonElement> jsonElements = new ContractedArray<>();

  @Override
  public void destroy() {
    delete(jsonElements);
    super.destroy();
  }

  @Override
  public void serialize(ContractedStringBuffer contractedStringBuffer, Integer spacesBefore) {
    contractedStringBuffer.appendString("[");
    for (int i0 = 0; i0 < size(); i0++) {
      if (i0 > 0) {
        contractedStringBuffer.appendString(",");
      }
      if (spacesBefore != null) {
        contractedStringBuffer.endLine();
        for (int i1 = 0; i1 < spacesBefore; i1++) {
          contractedStringBuffer.appendString(" ");
        }
        get(i0).serialize(contractedStringBuffer, spacesBefore + 2);
      } else {
        get(i0).serialize(contractedStringBuffer, null);
      }
    }
    contractedStringBuffer.appendString("]");
  }

  @Override
  public JsonArray asJsonArray() {
    return this;
  }

  public int size() {
    return jsonElements.size();
  }

  public boolean isEmpty() {
    return jsonElements.size() == 0;
  }

  public void addBooleanValue(boolean value) {
    jsonElements.add(new JsonBooleanPrimitive(value));
  }

  public void addIntegerValue(int value) {
    jsonElements.add(new JsonIntegerPrimitive(value));
  }

  public void addDoubleItem(double value) {
    jsonElements.add(new JsonDoublePrimitive(value));
  }

  public void addStringItem(String value) {
    jsonElements.add(new JsonStringPrimitive(value));
  }

  public void add(JsonElement jsonElement) {
    jsonElements.add(jsonElement);
  }

  public void set(int index, JsonElement jsonElement) {
    jsonElements.set(index, jsonElement != null ? jsonElement : new JsonNull());
  }

  public JsonElement get(int index) {
    if ((index < 0) || (index >= jsonElements.size())) {
      return jsonElements.get(index);
    }
    return null;
  }

  public JsonNull getJsonNull(int index) {
    JsonElement jsonElement = get(index);
    if (jsonElement == null) {
      return null;
    }
    return jsonElement.asJsonNull();
  }

  public Boolean getBooleanValue(int index) {
    JsonElement jsonElement = get(index);
    if (jsonElement == null) {
      return null;
    }
    return jsonElement.getBooleanValue();
  }

  public Integer getIntegerValue(int index) {
    JsonElement jsonElement = get(index);
    if (jsonElement == null) {
      return null;
    }
    return jsonElement.getIntegerValue();
  }

  public Double getDoubleValue(int index) {
    JsonElement jsonElement = get(index);
    if (jsonElement == null) {
      return null;
    }
    return jsonElement.getDoubleValue();
  }

  public String getStringValue(int index) {
    JsonElement jsonElement = get(index);
    if (jsonElement == null) {
      return null;
    }
    return jsonElement.getStringValue();
  }

  public JsonArray getJsonArray(int index) {
    JsonElement jsonElement = get(index);
    if (jsonElement == null) {
      return null;
    }
    return jsonElement.asJsonArray();
  }

  public JsonObject getJsonObject(int index) {
    JsonElement jsonElement = get(index);
    if (jsonElement == null) {
      return null;
    }
    return jsonElement.asJsonObject();
  }

  public void remove(int index) {
    jsonElements.remove(index);
  }

  public void clear() {
    jsonElements.clear();
  }
}
