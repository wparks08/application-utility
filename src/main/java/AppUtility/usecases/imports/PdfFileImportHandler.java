package AppUtility.usecases.imports;

import AppUtility.domains.pdf.PdfFile;
import AppUtility.exception.NotFoundException;
import AppUtility.usecases.fileloader.LocalFileLoader;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.nio.file.Path;

public class PdfFileImportHandler {
    private final String path;
    private Thread importThread;
    private final StringProperty statusMessage = new SimpleStringProperty();
    private final ObjectProperty<ImportStatus> status = new SimpleObjectProperty<>(ImportStatus.WAITING);
    private PdfFile pdfFile;


    public PdfFileImportHandler(Path path) {
        this.path = path.toString();
    }
    public PdfFileImportHandler(String path) { this.path = path; }

    public void doImport() {
        createImportThread();
        Platform.runLater(importThread);
    }

    private void createImportThread() {
        importThread = new Thread() {
            @Override
            public void run () {
                statusMessage.set("PDF Import in progress...");
                status.set(ImportStatus.IN_PROGRESS);

                LocalFileLoader localFileLoader = new LocalFileLoader(path);

                try {
                    localFileLoader.load();
                } catch (NotFoundException notFoundException) {
                    notFoundException.printStackTrace();
                    statusMessage.set("Error: File not found.");
                }

                pdfFile = new PdfFile(localFileLoader.getFile());

                statusMessage.set("Done!");
                status.set(ImportStatus.DONE);

                importThread = null;
            }
        };
        importThread.setDaemon(true);
    }

     public PdfFile getPdfFile() {
        return this.pdfFile;
    }

    public ObjectProperty<ImportStatus> getStatus() {
        return status;
    }
}
