package AppUtility.Domains.FileLoader;

import AppUtility.Exception.NotFoundException;

import java.io.File;

public interface FileLoader {
    void load() throws NotFoundException;
    File getFile();
}
