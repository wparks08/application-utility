package AppUtility.domains.DataValue;

public class DataValue {
    private String value;

    public DataValue() {
        value = "";
    }

    public DataValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
