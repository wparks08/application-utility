package AppUtility.domains.form;

import AppUtility.domains.form.Field.FieldCollection;
import AppUtility.domains.Id;

public class Form {
    private Id id;
    private String name;
    private FieldCollection fields;

    public Form(String name) {
        this.name = name;
    }

    public Form(long id, String name) {
        this.id = new Id(id);
        this.name = name;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Id getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public FieldCollection getFields() {
        return this.fields;
    }
}
