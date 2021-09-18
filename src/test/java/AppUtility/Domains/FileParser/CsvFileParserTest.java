package AppUtility.Domains.FileParser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

import java.io.File;

@Isolated
class CsvFileParserTest {

    @Test
    void toDataFile() {
        File sampleFile = new File("resources/sample-data.csv");
        try {
            CsvFileParser csvFileParser = new CsvFileParser(sampleFile);
        } catch (UnexpectedFileTypeException e) {
            e.printStackTrace();
        }
    }
}