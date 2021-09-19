package AppUtility.domains.datavalue;

import org.junit.jupiter.api.Test;

class DataValueTest {
    private final String testValue = "Test";
    private final String secondTestValue = "Test2";

    @Test
    void constructors() {
        DataValue dataValue = new DataValue();
        DataValue dataValue1 = new DataValue(testValue);

        assert(dataValue.toString().equals(""));
        assert(dataValue1.toString().equals(testValue));
    }

    @Test
    void getValue() {
        DataValue dataValue = new DataValue(testValue);
        assert(dataValue.getValue().equals(testValue));
    }

    @Test
    void setValue() {
        DataValue dataValue = new DataValue(testValue);
        dataValue.setValue(secondTestValue);
        assert (dataValue.getValue().equals(secondTestValue));
    }

    @Test
    void testToString() {
        DataValue dataValue = new DataValue(testValue);
        assert (dataValue.toString().equals(testValue));
    }
}