package utility.db;


public class Mapping {

  private long id;
  private long censusHeadersId;
  private long formFieldsId;
  private long formId;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getCensusHeadersId() {
    return censusHeadersId;
  }

  public void setCensusHeadersId(long censusHeadersId) {
    this.censusHeadersId = censusHeadersId;
  }


  public long getFormFieldsId() {
    return formFieldsId;
  }

  public void setFormFieldsId(long formFieldsId) {
    this.formFieldsId = formFieldsId;
  }


  public long getFormId() {
    return formId;
  }

  public void setFormId(long formId) {
    this.formId = formId;
  }

}
