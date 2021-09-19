package AppUtility.db;

@Deprecated
public class CensusHeader extends DBObject<CensusHeader> {

    private long id;
    private String header;
    private long formId;

    public CensusHeader() {
        //Default constructor
    }

    public CensusHeader(String header, long formId) {
        this.header = header;
        this.formId = formId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }


    public long getFormId() {
        return formId;
    }

    public void setFormId(long formId) {
        this.formId = formId;
    }

    @Override
    public String toString() {
        return this.header;
    }
}
