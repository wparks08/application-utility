package AppUtility.client.ui.controls;

import AppUtility.FileExtension;
import AppUtility.usecases.imports.CensusImportHandler;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class CensusFileChooser {
    private final FileChooser fileChooser = new FileChooser();
    FileExtension[] fileExtensions = CensusImportHandler.SUPPORTED_FILE_TYPES;

    private static final Logger log = LogManager.getLogger(CensusFileChooser.class.getName());

    public CensusFileChooser(File initialDirectory) {
        fileChooser.setInitialDirectory(initialDirectory);

        for (FileExtension extension : fileExtensions) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extension.getDescription(), extension.getFileSystemExtension()));
        }
        log.info("Census File Chooser initialized.");
    }

    public File showOpenDialog() {
        log.info("Showing file chooser...");
        File censusFile = fileChooser.showOpenDialog(null);
        if (censusFile != null) {
            log.info("File selected: " + censusFile.getPath());
        } else {
            log.info("No file selected.");
        }
        return censusFile;
    }
}
