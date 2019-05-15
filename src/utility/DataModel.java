package utility;

import utility.db.Carrier;
import utility.db.Form;

import java.util.List;

public class DataModel {

    private List<Carrier> carriers;
    private List<Form> forms;

    public DataModel() {
        //Default constructor
    }

    public void refreshCarriers() {
        carriers = new Carrier().list();
    }

    public void refreshForms(Carrier carrier) {
        forms = (List<Form>) carrier.getChildren(Form.class);
    }

    public List<Carrier> getCarriers() {
        return carriers;
    }

    public List<Form> getForms() {
        return forms;
    }
}
