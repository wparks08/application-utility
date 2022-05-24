package AppUtility.domains.datakey;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataKeyTest {
    private final String key1 = "Test key 1";
    private final String key2 = "Test key 2";

    @Test
    void testToString() {
        DataKey dataKey = new DataKey(key1);

        assert(dataKey.toString().equals(key1));
    }

    @Test
    void testEquals() {
        DataKey dataKey1 = new DataKey(key1);
        DataKey dataKey2 = new DataKey(key2);

        assert(!dataKey1.equals(dataKey2));

        DataKey dataKey3 = new DataKey(key1);

        assert(dataKey1.equals(dataKey3));
    }
}