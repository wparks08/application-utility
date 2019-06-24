package AppUtility.db;

import AppUtility.mapping.Conditional;

public class RadioCondition extends DBObject<RadioCondition> {
    private long id;
    private String conditional;
    private String censusValue;
    private String formValue;
    private long mappingId;

    public RadioCondition() {
        //Default Constructor
    }

    public RadioCondition(Conditional conditional, String censusValue, String formValue) {
        this.conditional = conditional.name();
        this.censusValue = censusValue;
        this.formValue = formValue;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setConditional(Conditional conditional) {
        this.conditional = conditional.name();
    }

    public Conditional getConditional() {
        return this.conditional == null ? null : Conditional.valueOf(this.conditional);
    }

    public void setCensusValue(String censusValue) {
        this.censusValue = censusValue;
    }

    public String getCensusValue() {
        return this.censusValue;
    }

    public void setFormValue(String formValue) {
        this.formValue = formValue;
    }

    public String getFormValue() {
        return this.formValue;
    }

    public void setMappingId(long mappingId) {
        this.mappingId = mappingId;
    }

    public long getMappingId() {
        return this.mappingId;
    }

    public Boolean hasId() {
        return this.id != 0L;
    }
}
