package AppUtility.UseCases.FileParser;

import AppUtility.UseCases.DataFile.DataFile;

import java.io.FileNotFoundException;

public interface FileParser {
    DataFile toDataFile() throws FileNotFoundException;
}
