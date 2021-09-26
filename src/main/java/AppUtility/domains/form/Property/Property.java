package AppUtility.domains.form.Property;

import AppUtility.domains.id.Id;

public class Property {
    private Id id;
    private final String key;
    private String value;

    public Property(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Property(Id id, String key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    public Id getId() {
        return this.id;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
