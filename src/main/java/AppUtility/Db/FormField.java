package AppUtility.Db;


public class FormField extends DBObject<FormField> {

    private long id;
    private String fieldName;
    private long formId;

    public FormField() {
        //Default constructor
    }

    public FormField(String fieldName, long formId) {
        this.fieldName = fieldName;
        this.formId = formId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }


    public long getFormId() {
        return formId;
    }

    public void setFormId(long formId) {
        this.formId = formId;
    }

    @Override
    public String toString() {
        return this.fieldName;
    }
}
