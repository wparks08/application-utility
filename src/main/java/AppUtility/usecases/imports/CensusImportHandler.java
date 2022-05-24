package AppUtility.usecases.imports;

import AppUtility.FileExtension;
import AppUtility.exception.NotFoundException;
import AppUtility.usecases.datafile.DataFile;
import AppUtility.usecases.fileloader.LocalFileLoader;
import AppUtility.usecases.fileparser.CsvFileParser;
import AppUtility.usecases.fileparser.UnexpectedFileTypeException;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.nio.file.Path;

public class CensusImportHandler {
    public static final FileExtension[] SUPPORTED_FILE_TYPES = {
            new FileExtension("CSV File", "*.csv"),
    };

    private final String path;
    private Thread importThread;
    private final StringProperty statusMessage = new SimpleStringProperty();
    private final ObjectProperty<ImportStatus> status = new SimpleObjectProperty<>(ImportStatus.WAITING);
    private DataFile dataFile;

    private static final Logger log = LogManager.getLogger(CensusImportHandler.class.getName());

    public CensusImportHandler(String path) {
        this.path = path;
        log.info("Census Import Handler initialized.");
    }

    public CensusImportHandler(Path path) {
        this.path = path.toString();
        log.info("Census Import Handler initialized.");
    }

    public void doImport() {
        log.info("Creating import thread...");
        createImportThread();
        Platform.runLater(importThread);
    }

    private void createImportThread() {
        importThread = new Thread() {
            @Override
            public void run() {
                log.info("Import thread started...");
                statusMessage.set("Starting import...");
                status.set(ImportStatus.IN_PROGRESS);
                LocalFileLoader localFileLoader = new LocalFileLoader(path);

                try {
                    localFileLoader.load();
                } catch (NotFoundException notFoundException) {
                    notFoundException.printStackTrace();
                    statusMessage.set("Error: File not found.");
                }

                try {
                    CsvFileParser csvFileParser = new CsvFileParser(localFileLoader.getFile());
                    dataFile = csvFileParser.toDataFile();
                } catch (UnexpectedFileTypeException | FileNotFoundException exception) {
                    exception.printStackTrace();
                    statusMessage.set("Error: Could not import file. Message is: " + exception.getMessage());
                }
                log.info("Import complete.");
                statusMessage.set("Census import complete!");
                status.set(ImportStatus.DONE);
                importThread = null;
            }


        };
        importThread.setDaemon(true);
    }

    public ObservableStringValue getStatusMessage() {
        return this.statusMessage;
    }

    public ObjectProperty<ImportStatus> getStatus() {
        return this.status;
    }

    public DataFile getDataFile() throws Exception {
        if (!status.get().equals(ImportStatus.DONE)) {
            throw new Exception("Attempted to retrieve DataFile before import was complete. Status must be set to Status.DONE to show completion.");
        }

        return this.dataFile;
    }
}
