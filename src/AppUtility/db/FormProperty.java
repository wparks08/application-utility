package AppUtility.db;


public class FormProperty extends DBObject<FormProperty> {

  private long id;
  private String property;
  private String value;
  private long formId;

  public FormProperty() {
    //Default constructor
  }

  public FormProperty(String property, String value, long formId) {
    this.property = property;
    this.value = value;
    this.formId = formId;
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


  public long getFormId() {
    return formId;
  }

  public void setFormId(long formId) {
    this.formId = formId;
  }

}
