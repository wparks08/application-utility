package AppUtility.Domains.FileParser;

import AppUtility.Domains.DataFile.DataFile;

import java.io.FileNotFoundException;

public interface FileParser {
    DataFile toDataFile() throws FileNotFoundException;
}
