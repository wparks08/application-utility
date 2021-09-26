package AppUtility.domains;

import AppUtility.collections.Collection;
import AppUtility.collections.CollectionFactory;
import AppUtility.domains.form.Form;
import AppUtility.domains.id.Id;

public class Carrier {
    private Id id;
    private String name;
    private Collection<Form> forms = CollectionFactory.getFormCollection();

    public Carrier(String name) {
        this.name = name;
    }

    public Carrier(Id id, String name) {
        this.id = id;
        this.name = name;
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

    public Collection<Form> getFormCollection() {
        return this.forms;
    }
}
