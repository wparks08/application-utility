package AppUtility.db;


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

}
