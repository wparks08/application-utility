package AppUtility.domains.id;

import java.io.Serializable;
import java.util.UUID;

public class Id implements Serializable {
    private UUID uuid;

    public Id() {
        this.uuid = UUID.randomUUID();
    }

    private Id(UUID uuid) {
        this.uuid = uuid;
    }

    public static Id fromString(String id) {
        return new Id(UUID.fromString(id));
    }

    Boolean equals(Id other) {
        return this.uuid.equals(other.uuid);
    }

    @Override
    public String toString() {
        return this.uuid.toString();
    }
}
