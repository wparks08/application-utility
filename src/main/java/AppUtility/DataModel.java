package AppUtility;

import AppUtility.db.Carrier;
import AppUtility.db.Form;

import java.util.List;

public final class DataModel {

    private static List<Carrier> carriers;
    private static List<Form> forms;
    private static Carrier selectedCarrier;
    private static Form selectedForm;

    private static String lastAccessedFilePath = System.getProperty("user.home");
    private static String outputDirectory;

    public DataModel() {
        //Default constructor
    }

    public static void refreshCarriers() {
        carriers = new Carrier().list();
    }

    public static void refreshForms(Carrier carrier) {
        forms = (List<Form>) carrier.getChildren(Form.class);
    }

    public static List<Carrier> getCarriers() {
        return carriers;
    }

    public static List<Form> getForms() {
        return forms;
    }

    public static void setSelectedCarrier(Carrier selectedItem) {
        selectedCarrier = selectedItem;
        System.out.println("Selected " + selectedCarrier.getName());
    }

    public static Carrier getSelectedCarrier() {
        return selectedCarrier;
    }

    public static String getLastAccessedFilePath() {
        return lastAccessedFilePath;
    }

    public static void setLastAccessedFilePath(String lastAccessedFilePath) {
        DataModel.lastAccessedFilePath = lastAccessedFilePath;
    }

    public static Form getSelectedForm() {
        return selectedForm;
    }

    public static void setSelectedForm(Form selectedForm) {
        DataModel.selectedForm = selectedForm;
    }

    public static void setOutputDirectory(String outputDirectory) {
        DataModel.outputDirectory = outputDirectory;
    }

    public static String getOutputDirectory() {
        return outputDirectory;
    }
}
