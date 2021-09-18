package AppUtility.domains.FileParser;

import AppUtility.domains.DataEntry.DataEntry;
import AppUtility.domains.DataFile.DataFile;
import AppUtility.domains.DataKey.DataKey;
import AppUtility.domains.DataKey.DataKeyCollection;
import AppUtility.domains.DataValue.DataValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CsvFileParser implements FileParser {
    private File file;
    public String VALUE_SEPARATOR = ",";

    public CsvFileParser(File file) throws UnexpectedFileTypeException {
        if (!file.getPath().endsWith(".csv")) {
            throw new UnexpectedFileTypeException("Unexpected file type: Expected a CSV file.");
        }

        this.file = file;
    }

    private DataKeyCollection getDataKeysFromFile() throws FileNotFoundException {
        DataKeyCollection dataKeyCollection = new DataKeyCollection();
        String[] headers = extractHeaders();
        for (String header : headers) {
            dataKeyCollection.add(new DataKey(header));
        }

        return dataKeyCollection;
    }

    private String[] extractHeaders() throws FileNotFoundException {
        Scanner scanner = new Scanner(this.file);
        String headerString = scanner.nextLine();
        scanner.close();
        return headerString.split(VALUE_SEPARATOR);
    }

    @Override
    public DataFile toDataFile() throws FileNotFoundException {
        DataFile dataFile = new DataFile();
        DataKeyCollection dataKeyCollection = getDataKeysFromFile();
        DataKey[] dataKeys = dataKeyCollection.toArray();

        Scanner scanner = new Scanner(this.file);
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            String[] lineEntries = readLineEntries(currentLine);
            DataEntry dataEntry = new DataEntry();
            for (int i = 0; i < lineEntries.length; i++) {
                dataEntry.add(dataKeys[i], new DataValue());
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
