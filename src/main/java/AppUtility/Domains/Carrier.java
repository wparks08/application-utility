package AppUtility.Domains;

import AppUtility.Domains.Form.FormCollection;

public class Carrier {
    private Id id;
    private String name;
    private FormCollection forms;

    public Carrier(String name) {
        this.name = name;
        this.forms = new FormCollection();
    }

    public Carrier(int id, String name) {
        this.id = new Id(id);
        this.name = name;
        this.forms = new FormCollection();
    }

    public Carrier(Carrier carrier) {
        this.id = carrier.id;
        this.name = carrier.name;
        this.forms = carrier.forms;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public FormCollection getForms() {
        return this.forms;
    }
}
