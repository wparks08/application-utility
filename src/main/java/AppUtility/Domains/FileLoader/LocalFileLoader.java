package AppUtility.Domains.FileLoader;

import AppUtility.Exception.NotFoundException;

import java.io.File;
import java.nio.file.Path;

public class LocalFileLoader implements FileLoader {
    Path filePath;
    File file;

    public LocalFileLoader(String path) {
        this.filePath = Path.of(path);
    }

    public LocalFileLoader(Path path) {
        this.filePath = path;
    }

    @Override
    public void load() throws NotFoundException {
        File file = filePath.toFile();

        if (!file.exists()) {
            throw new NotFoundException("File not found: " + filePath.toString());
        }

        this.file = file;
    }

    @Override
    public File getFile() {
        return this.file;
    }
}
