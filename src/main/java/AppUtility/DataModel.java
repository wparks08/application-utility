package AppUtility;

import AppUtility.Config.Dependencies;
import AppUtility.Domains.Carrier;
import AppUtility.Domains.Form.Form;
import AppUtility.Domains.Id;
import AppUtility.Interfaces.CarrierDatabase;
import AppUtility.Interfaces.FormDatabase;

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
        CarrierDatabase carrierDatabase = Dependencies.databaseServices.getCarrierDatabase();
        carriers = carrierDatabase.getAllCarriers();
    }

    public static void refreshForms(Carrier carrier) {
        FormDatabase formDatabase = Dependencies.databaseServices.getFormDatabase();
        Id carrierId = carrier.getId();
        forms = formDatabase.getFormsByCarrierId(0);
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
