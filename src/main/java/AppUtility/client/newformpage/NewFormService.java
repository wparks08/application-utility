package AppUtility.client.newformpage;

import AppUtility.domains.form.Form;
import AppUtility.usecases.datafile.DataFile;
import AppUtility.usecases.imports.CensusImportHandler;
import AppUtility.usecases.imports.ImportStatus;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

public class NewFormService {
    private Form.FormBuilder formBuilder;
    private StringProperty censusPath = new SimpleStringProperty();
    private BooleanProperty censusImported = new SimpleBooleanProperty(false);
    private DataFile dataFile;

    private static final Logger log = LogManager.getLogger(NewFormService.class.getName());

    public NewFormService() {
        log.info("New Form Service initialized.");
    }

    public StringProperty getCensusPath() {
        return this.censusPath;
    }

    public BooleanProperty getCensusImported() {
        return this.censusImported;
    }

    public DataFile getDataFile() {
        return this.dataFile;
    }

    public void importCensus(Path path) {
        log.info("Importing census: " + path.toString());
        CensusImportHandler censusImportHandler = new CensusImportHandler(path);
        censusPath.set(path.toString());
        censusImportHandler.doImport();

        censusImportHandler.getStatus().addListener((ObservableValue<? extends ImportStatus> observable, ImportStatus oldValue, ImportStatus newValue) -> {
            censusImported.set(observable.getValue().equals(ImportStatus.DONE));
        });

        censusImportHandler.getStatus().addListener((ObservableValue<? extends ImportStatus> observable, ImportStatus oldValue, ImportStatus newValue) -> {
            if (observable.getValue().equals(ImportStatus.DONE)) {
                try {
                    dataFile = censusImportHandler.getDataFile();
                } catch (Exception e) {
                    log.error("Could not extract Data File.");
                    e.printStackTrace();
                }
            }
        });
    }
}
