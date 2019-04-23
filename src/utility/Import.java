package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Import {

    public static String[] getHeaders(File inFile) {

        String[] headers = new String[0];

        try {
            Scanner scanner = new Scanner(inFile);
            String firstLine = scanner.nextLine();
            headers = firstLine.split(",");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (String header : headers) {
            System.out.println(header);
        }

        return headers;

    }

}
