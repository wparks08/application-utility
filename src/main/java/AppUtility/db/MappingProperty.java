package AppUtility.db;

public class MappingProperty extends DBObject<MappingProperty> {
    private long id;
    private String property;
    private String value;
    private long mappingId;

    public MappingProperty() {
    }

    public MappingProperty(String property, String value) {
        this.property = property;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getMappingId() {
        return mappingId;
    }

    public void setMappingId(long mappingId) {
        this.mappingId = mappingId;
    }
}
