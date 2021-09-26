package AppUtility.client.newformpage;

import AppUtility.domains.form.Form;
import AppUtility.usecases.imports.CensusImportHandler;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

import java.nio.file.Path;

public class NewFormService {
    private Form.FormBuilder formBuilder;
    private StringProperty censusPath = new SimpleStringProperty();
    private BooleanProperty censusImported = new SimpleBooleanProperty(false);

    public NewFormService() {
        
    }

    public StringProperty getCensusPath() {
        return this.censusPath;
    }

    public BooleanProperty getCensusImported() {
        return this.censusImported;
    }

    public void importCensus(Path path) {
        CensusImportHandler censusImportHandler = new CensusImportHandler(path);
        censusPath.set(path.toString());
        censusImportHandler.doImport();

        censusImportHandler.getStatus().addListener((ObservableValue<? extends CensusImportHandler.Status> observable, CensusImportHandler.Status oldValue, CensusImportHandler.Status newValue) -> {
            censusImported.set(observable.getValue().equals(CensusImportHandler.Status.DONE));
        });
    }
}
