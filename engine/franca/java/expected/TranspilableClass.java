package franca.java.expected;

public class TranspilableClass {

  private long id = 0L;
  private String dataName = null;

  public TranspilableClass() {
    id = Runtime.getId();
  }

  public void destroy() {
  }

  public long getId() {
    return id;
  }

  public void setDataName(String dataName) {
    this.dataName = dataName;
  }

  public String getDataName() {
    return dataName;
  }

  public String copyOf(String string) {
    return string;
  }

  public void delete(Object value) {

  }
}
