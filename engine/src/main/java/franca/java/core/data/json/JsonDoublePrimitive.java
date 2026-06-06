package franca.java.core.data.json;

import franca.java.core.contracted.ContractedRuntime;

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
  public void serialize(StringBuilder stringBuilder) {
    stringBuilder.append(ContractedRuntime.doubleToString(value));
  }

  @Override
  public Double getDoubleValue() {
    return value;
  }
}
