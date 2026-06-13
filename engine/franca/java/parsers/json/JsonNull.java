package franca.java.parsers.json;

import franca.java.expected.StringBuffer;

public class JsonNull extends JsonElement {

  @Override
  public void serialize(StringBuffer stringBuffer, Integer spacesBefore) {
    stringBuffer.appendString("null");
  }

  @Override
  public JsonNull asJsonNull() {
    return this;
  }

  @Override
  public String getStringValue() {
    return "null";
  }
}
