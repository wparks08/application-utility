package AppUtility.usecases.fileloader;

import AppUtility.exception.NotFoundException;

import java.io.File;

public interface FileLoader {
    void load() throws NotFoundException;
    File getFile();
}
