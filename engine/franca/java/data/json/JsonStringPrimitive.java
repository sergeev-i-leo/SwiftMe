package franca.java.data.json;

import franca.java.expected.StringBuffer;

public class JsonStringPrimitive extends JsonPrimitive {

  private String value;

  public JsonStringPrimitive(String value) {
    super();
    this.value = value;
  }

  public String getClassName() {
    return "JsonStringPrimitive";
  }

  @Override
  public void serialize(StringBuffer stringBuffer, Integer spacesBefore) {
    String string = value;
    if (string == null) {
      string = "";
    }
    string = string.replace("\"", "\\\"");
    stringBuffer.appendString("\"" + string + "\"");
  }

  @Override
  public String getStringValue() {
    return value;
  }
}
