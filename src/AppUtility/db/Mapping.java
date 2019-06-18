package AppUtility.db;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Mapping extends DBObject<Mapping> {

    private long id;
    private long censusHeaderId;
    private long formFieldId;
    private long formId;

    public Mapping() {
        //Default constructor
    }

    public Mapping(long censusHeaderId, long formFieldId, long formId) {
        this.censusHeaderId = censusHeaderId;
        this.formFieldId = formFieldId;
        this.formId = formId;
    }

    private void getMappedValues() {
        if (censusHeaderId == 0 || formFieldId == 0) {
            return;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getCensusHeaderId() {
        return censusHeaderId;
    }

    public void setCensusHeaderId(long censusHeaderId) {
        this.censusHeaderId = censusHeaderId;
    }


    public long getFormFieldId() {
        return formFieldId;
    }

    public void setFormFieldId(long formFieldId) {
        this.formFieldId = formFieldId;
    }


    public long getFormId() {
        return formId;
    }

    public void setFormId(long formId) {
        this.formId = formId;
    }

    public Mapping findByFormFieldId(long formFieldId) {
        return this.getBy("form_field_id", String.valueOf(formFieldId));
    }

    public void addMappingProperty(MappingProperty mappingProperty) {
        mappingProperty.setMappingId(this.id);
        mappingProperty.save();
    }

    public void addMappingProperty(String property, String value) {
        MappingProperty mappingProperty = new MappingProperty(property, value);
        addMappingProperty(mappingProperty);
    }

    public List<MappingProperty> getMappingProperties() {
        List<?> mappingPropertiesList = getChildren(MappingProperty.class);
        List<MappingProperty> mappingProperties = new ArrayList<>();
        for (Object object : mappingPropertiesList) {
            mappingProperties.add((MappingProperty)object);
        }

        return mappingProperties;
    }

    public HashMap<String,String> getMappingPropertiesAsMap() {
        HashMap<String, String> mappingPropertiesMap = new LinkedHashMap<>();

        for (MappingProperty mappingProperty : getMappingProperties()) {
            mappingPropertiesMap.put(mappingProperty.getProperty(),mappingProperty.getValue());
        }

        return mappingPropertiesMap;
    }
}
