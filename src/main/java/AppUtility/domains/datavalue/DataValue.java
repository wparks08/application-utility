package AppUtility.domains.datavalue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataValue {
    private String value;
    private static final Logger logger = LogManager.getLogger(DataValue.class.getName());

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
        logger.info("Replacing " + this.value + " with " + value);
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
