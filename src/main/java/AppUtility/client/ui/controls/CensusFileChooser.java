package AppUtility.client.ui.controls;

import AppUtility.FileExtension;
import AppUtility.usecases.imports.CensusImportHandler;
import javafx.stage.FileChooser;

import java.io.File;

public class CensusFileChooser {
    private final FileChooser fileChooser = new FileChooser();
    FileExtension[] fileExtensions = CensusImportHandler.SUPPORTED_FILE_TYPES;

    public CensusFileChooser(File initialDirectory) {
        fileChooser.setInitialDirectory(initialDirectory);

        for (FileExtension extension : fileExtensions) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extension.getDescription(), extension.getFileSystemExtension()));
        }
    }

    public File showOpenDialog() {
        return fileChooser.showOpenDialog(null);
    }
}
