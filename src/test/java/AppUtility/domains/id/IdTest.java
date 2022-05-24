package AppUtility.domains.id;

import org.junit.jupiter.api.Test;

class IdTest {
    @Test
    void fromString() {
        Id id = new Id();
        Id other = Id.fromString(id.toString());

        assert id.equals(other);
    }

    @Test
    void equals() {
        Id id = new Id();

        assert id.equals(id);
    }
}