package AppUtility.usecases.fileparser;

import AppUtility.usecases.datafile.DataFile;
import AppUtility.domains.datakey.DataKey;
import AppUtility.domains.datakey.DataKeyCollection;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.fail;

public class CsvFileParserTest {

    @Test
    public void toDataFile() {
        File sampleFile = new File("src/test/resources/sample-data.csv");
        try {
            CsvFileParser csvFileParser = new CsvFileParser(sampleFile);
            DataFile dataFile = csvFileParser.toDataFile();

            DataKeyCollection dataKeyCollection = dataFile.getDataKeys();
            DataKey[] dataKeys = dataKeyCollection.toArray();

            assert(Arrays.stream(dataKeys).anyMatch(dataKey -> dataKey.equals(new DataKey("SSN"))));
            assert(Arrays.stream(dataKeys).anyMatch(dataKey -> dataKey.equals(new DataKey("First Name"))));
            assert(Arrays.stream(dataKeys).anyMatch(dataKey -> dataKey.equals(new DataKey("Last Name"))));
            assert(Arrays.stream(dataKeys).anyMatch(dataKey -> dataKey.equals(new DataKey("Date of Birth"))));
            assert(Arrays.stream(dataKeys).anyMatch(dataKey -> dataKey.equals(new DataKey("Name, Spy Name"))));

        } catch (UnexpectedFileTypeException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void throwExceptionIfFileIsWrongType() {
        File sampleFile = new File("src/test/resources/fake.xls");
        try {
            CsvFileParser csvFileParser = new CsvFileParser(sampleFile);
            fail();
        } catch (UnexpectedFileTypeException e) {
            assert(e.getMessage().equals("Unexpected file type: Expected a CSV file."));
        }
    }
}