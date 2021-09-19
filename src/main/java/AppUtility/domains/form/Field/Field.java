package AppUtility.domains.form.Field;

import AppUtility.domains.Id;

public class Field {
    private Id id;
    private final String name;

    public Field(String name) {
        this.name = name;
    }

    public Field(int id, String name) {
        this.id = new Id(id);
        this.name = name;
    }

    public Id getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
