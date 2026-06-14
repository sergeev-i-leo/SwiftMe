package franca.java.data.json;

import franca.java.expected.StringBuffer;

public class JsonBooleanPrimitive extends JsonPrimitive {

  private final boolean value;

  public String getClassName() {
    return "JsonBooleanPrimitive";
  }

  public JsonBooleanPrimitive(boolean value) {
    super();
    this.value = value;
  }

  @Override
  public void serialize(StringBuffer stringBuffer, Integer spacesBefore) {
    stringBuffer.appendString(value ? "true" : "false");
  }

  @Override
  public Boolean getBooleanValue() {
    return value;
  }
}
