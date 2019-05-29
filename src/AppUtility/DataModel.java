package AppUtility;

import AppUtility.db.Carrier;
import AppUtility.db.Form;

import java.util.List;

public class DataModel {

    private List<Carrier> carriers;
    private List<Form> forms;
    private Carrier selectedCarrier;
    private Form selectedForm;

    private String lastAccessedFilePath = System.getProperty("user.home");

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

    public void setSelectedCarrier(Carrier selectedItem) {
        this.selectedCarrier = selectedItem;
    }

    public Carrier getSelectedCarrier() {
        return this.selectedCarrier;
    }

    public String getLastAccessedFilePath() {
        return lastAccessedFilePath;
    }

    public void setLastAccessedFilePath(String lastAccessedFilePath) {
        this.lastAccessedFilePath = lastAccessedFilePath;
    }

    public Form getSelectedForm() {
        return selectedForm;
    }

    public void setSelectedForm(Form selectedForm) {
        this.selectedForm = selectedForm;
    }
}
