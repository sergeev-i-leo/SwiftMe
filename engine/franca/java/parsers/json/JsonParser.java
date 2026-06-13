package franca.java.parsers.json;

import franca.java.expected.StringBuffer;
import franca.java.expected.Runtime;
import franca.java.parsers.Parser;

import java.util.ArrayList;

public class JsonParser extends Parser {

  @Override
  public void destroy() {
    super.destroy();
  }

  public JsonElement parse(String input) {
    if (input == null) {
      return new JsonObject();
    }

    if (this.input != null) {
      delete(this.input);
    }
    this.input = copyOf(input);

    position = 0;
    JsonElement jsonElement = parseJsonElement();
    if (jsonElement != null) {
      return jsonElement;
    }
    return new JsonElement();
  }

  private JsonElement parseJsonElement() {
    skipWhitespaces();
    JsonElement jsonElement = parseJsonNode();
    skipWhitespaces();
    return jsonElement;
  }

  private JsonElement parseJsonNode() {
    skipWhitespaces();
    JsonElement jsonElement = parseJsonObject();
    if (jsonElement != null) {
      return jsonElement;
    }
    jsonElement = parseJsonArray();
    if (jsonElement != null) {
      return jsonElement;
    }
    String string = parseString();
    if (string != null) {
      return new JsonStringPrimitive(string);
    }
    JsonPrimitive numberJsonPrimitive = parseNumber();
    if (numberJsonPrimitive != null) {
      return numberJsonPrimitive;
    }
    jsonElement = parseLiteral();
    if (jsonElement != null) {
      return jsonElement;
    }
    System.out.println("Invalid Json element at " + position);
    return null;
  }

  private JsonObject parseJsonObject() {
    if (input.charAt(position) != '{') {
      return null;
    }
    position++;
    JsonObject jsonObject0 = new JsonObject();
    skipWhitespaces();
    if (input.charAt(position) == '}') {
      position++;
      return jsonObject0;
    }
    while (true) {
      parseJsonObjectMember(jsonObject0);
      if (input.charAt(position) == ',') {
        position++;
        continue;
      }
      break;
    }
    String className = jsonObject0.getStringValue("$className");
    if (className != null) {
      JsonObject jsonObject1 = createJsonObjectByClassName(className);
      ArrayList<String> keys = jsonObject0.keys();
      for (int i = 0; i < keys.size(); i++) {
        String key = keys.get(i);
        jsonObject1.put(key, jsonObject0.get(key));
      }
      jsonObject1.deserialize(jsonObject0);
      jsonObject0 = jsonObject1;
    }
    skipWhitespaces();
    if (input.charAt(position) == '}') {
      position++;
    }
    return jsonObject0;
  }

  protected JsonObject createJsonObjectByClassName(String className) {
    return new JsonObject();
  }

  private void parseJsonObjectMember(JsonObject jsonObject) {
    try {
      skipWhitespaces();
      String name = parseString();
      if (name == null) {
        System.out.println("JsonObject name expected at " + position);
        return;
      }
      skipWhitespaces();
      if (input.charAt(position) == ':') {
        position++;
        JsonElement jsonElement = parseJsonElement();
        jsonObject.put(name, jsonElement);
      }
    } catch (Exception e) {
      System.out.println("End of input at " + position);
    }
  }

  private JsonArray parseJsonArray() {
    try {
      if (input.charAt(position) != '[') {
        return null;
      }
      position++;
      JsonArray jsonArray = new JsonArray();
      skipWhitespaces();
      while (position < input.length()) {
        if (input.charAt(position) == ']') {
          break;
        }
        JsonElement jsonElement = parseJsonElement();
        jsonArray.add(jsonElement);
        if (input.charAt(position) != ',') {
          break;
        }
        position++;
      }
      position++;
      return jsonArray;
    } catch (Exception e) {
      System.out.println("End of input at " + position);
      return null;
    }
  }

  private JsonElement parseLiteral() {
    try {
      if (input.startsWith("null", position)) {
        position += 4;
        return new JsonNull();
      }
    } catch (Exception e) {
      System.out.println("End of input at " + position);
    }
    return null;
  }
}
