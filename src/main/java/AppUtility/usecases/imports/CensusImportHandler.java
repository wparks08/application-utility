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

import java.io.FileNotFoundException;
import java.nio.file.Path;

public class CensusImportHandler {
    public static final FileExtension[] SUPPORTED_FILE_TYPES = {
            new FileExtension("CSV File", "*.csv"),
    };
    public enum Status {
        WAITING, IN_PROGRESS, DONE
    }

    private final String path;
    private Thread importThread;
    private StringProperty statusMessage = new SimpleStringProperty();
    private ObjectProperty<Status> status = new SimpleObjectProperty<>(Status.WAITING);
    private DataFile dataFile;

    public CensusImportHandler(String path) {
        this.path = path;
    }

    public CensusImportHandler(Path path) {
        this.path = path.toString();
    }

    public void doImport() {
        createImportThread();
        Platform.runLater(importThread);
    }

    private void createImportThread() {
        importThread = new Thread() {
            @Override
            public void run() {
                statusMessage.set("Starting import...");
                status.set(Status.IN_PROGRESS);
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
                statusMessage.set("Census import complete!");
                status.set(Status.DONE);
                importThread = null;
            }


        };
        importThread.setDaemon(true);
    }

    public ObservableStringValue getStatusMessage() {
        return this.statusMessage;
    }

    public ObjectProperty<Status> getStatus() {
        return this.status;
    }

    public DataFile getDataFile() throws Exception {
        if (!status.get().equals(Status.DONE)) {
            throw new Exception("Attempted to retrieve DataFile before import was complete. Status must be set to Status.DONE to show completion.");
        }

        return this.dataFile;
    }
}
