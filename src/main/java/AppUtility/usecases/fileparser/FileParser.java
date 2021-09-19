package AppUtility.usecases.fileparser;

import AppUtility.usecases.datafile.DataFile;

import java.io.FileNotFoundException;

public interface FileParser {
    DataFile toDataFile() throws FileNotFoundException;
}
