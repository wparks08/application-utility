package AppUtility.usecases.fileparser;

import AppUtility.collections.Collection;
import AppUtility.collections.CollectionFactory;
import AppUtility.usecases.dataentry.DataEntry;
import AppUtility.usecases.datafile.DataFile;
import AppUtility.domains.datakey.DataKey;
import AppUtility.domains.datavalue.DataValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CsvFileParser implements FileParser {
    private final File file;
    public String VALUE_SEPARATOR = ",";
    private static final Logger logger = LogManager.getLogger(
            CsvFileParser.class.getName()
    );

    public CsvFileParser(File file) throws UnexpectedFileTypeException {
        if (!file.getPath().endsWith(".csv")) {
            throw new UnexpectedFileTypeException("Unexpected file type: Expected a CSV file.");
        }

        this.file = file;
        logger.info(file.getPath() + " ready to parse");
    }

    private Collection<DataKey> getDataKeysFromFile() throws FileNotFoundException {
        Collection<DataKey> dataKeyCollection = CollectionFactory.getDataKeyCollection();
        String[] headers = extractHeaders();
        for (String header : headers) {
            dataKeyCollection.add(new DataKey(header));
        }
        logger.info("Data keys: " + dataKeyCollection);
        return dataKeyCollection;
    }

    private String[] extractHeaders() throws FileNotFoundException {
        logger.info("Extracting headers...");
        Scanner scanner = new Scanner(this.file);
        String headerString = scanner.nextLine();
        logger.info("Raw header string: " + headerString);
        scanner.close();
        return readLineEntries(headerString);
    }

    @Override
    public DataFile toDataFile() throws FileNotFoundException {
        logger.info("Converting to data file");
        DataFile dataFile = new DataFile();
        Collection<DataKey> dataKeyCollection = getDataKeysFromFile();
        DataKey[] dataKeys = dataKeyCollection.toArray();
        logger.info("Data key array: " + Arrays.toString(dataKeys));

        Scanner scanner = new Scanner(this.file);
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            String[] lineEntries = readLineEntries(currentLine);
            DataEntry dataEntry = new DataEntry();
            for (int i = 0; i < lineEntries.length; i++) {
                dataEntry.add(dataKeys[i], new DataValue(lineEntries[i]));
            }
            dataFile.addDataEntry(dataEntry);
        }

        return dataFile;
    }

    private String[] readLineEntries(String line) {
        List<String> entries = new ArrayList<>();

        Scanner lineScanner = new Scanner(line);
        lineScanner.useDelimiter(VALUE_SEPARATOR);

        while (lineScanner.hasNext()) {
            String entry = lineScanner.next();
            String formattedEntry = formatEntry(entry, lineScanner);

            entries.add(formattedEntry);
        }

        lineScanner.close();

        return entries.toArray(new String[0]);
    }

    private String formatEntry(String entry, Scanner lineScanner) {
        if (entry.contains("\"")) {
            while (!entry.endsWith("\"")) {
                entry = entry + VALUE_SEPARATOR + lineScanner.next();
            }
            entry = entry.replaceAll("\"", "");
        }

        return entry;
    }
}
