package franca.java.parsers.json;

import franca.java.expected.Runtime;
import franca.java.expected.StringBuffer;

public class JsonDoublePrimitive extends JsonPrimitive {

  private final double value;

  public JsonDoublePrimitive(double value) {
    this.value = value;
  }

  @Override
  public void destroy() {
    super.destroy();
  }

  @Override
  public void serialize(StringBuffer stringBuffer, Integer spacesBefore) {
    stringBuffer.appendString(Runtime.doubleToString(value));
  }

  @Override
  public Double getDoubleValue() {
    return value;
  }
}
