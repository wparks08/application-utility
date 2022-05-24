package AppUtility.domains.id;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdTest {
    private static final String TEST_ID_VALUE = "1";

    @Test
    void idCreation() {
        Id id = IdFactory.getIdObject(TEST_ID_VALUE);
        assert id.toString().equals(TEST_ID_VALUE);
    }

    @Test
    void equals() {
        Id id = IdFactory.getIdObject(TEST_ID_VALUE);
        Id other = IdFactory.getIdObject(TEST_ID_VALUE);

        assert id.equals(other);
    }
}