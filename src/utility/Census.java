package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Census {

    File censusFile;

    Census() {
        censusFile = null;
    }

    Census(File inFile) {
        this.censusFile = inFile;
    }

    public void setCensusFile(File censusFile) {
        this.censusFile = censusFile;
    }

    public File getCensusFile() {
        return censusFile;
    }

    String[] getHeaders() throws FileNotFoundException {

        if (censusFile == null) {
            throw new FileNotFoundException();
        }

        String headers[];

        Scanner scanner = new Scanner(censusFile);
        String firstLine = scanner.nextLine();
        headers = firstLine.split(",");

        for (String header : headers) {
            System.out.println(header);
        }

        return headers;

    }

}
