package franca.java.parsers.json;

import franca.java.expected.StringBuffer;

public class JsonStringPrimitive extends JsonPrimitive {

  private String value;

  public JsonStringPrimitive(String value) {
    super();
    this.value = value;
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
