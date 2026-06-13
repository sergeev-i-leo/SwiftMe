package franca.java.parsers.json;

import franca.java.expected.Runtime;
import franca.java.expected.StringBuffer;

public class JsonIntegerPrimitive extends JsonPrimitive {

  private final int value;

  public JsonIntegerPrimitive(int value) {
    this.value = value;
  }

  @Override
  public void destroy() {
    super.destroy();
  }

  @Override
  public void serialize(StringBuffer stringBuffer, Integer spacesBefore) {
    stringBuffer.appendString(Runtime.intToString(value));
  }

  @Override
  public Integer getIntegerValue() {
    return value;
  }
}
