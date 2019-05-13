package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Census {

    private File censusFile;
    private String headers[];
    private List<Employee> employees = new ArrayList<>();

    public Census() {
        censusFile = null;
    }

    public Census(File inFile) {
        this.censusFile = inFile;
        try {
            headers = getHeaders();
            createEmployees();
            assignDependents();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setCensusFile(File censusFile) {
        this.censusFile = censusFile;
        try {
            headers = getHeaders();
            createEmployees();
            assignDependents();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public File getCensusFile() {
        return censusFile;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public String[] getHeaders() {

        if (censusFile == null) {
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        String[] headers;

        Scanner scanner = null;
        try {
            scanner = new Scanner(censusFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert scanner != null;
        String firstLine = scanner.nextLine();
        headers = firstLine.split(",");

        for (String header : headers) {
            System.out.println(header);
        }

        scanner.close();

        return headers;
    }

    private void createEmployees() throws FileNotFoundException {
        if (censusFile == null) {
            throw new FileNotFoundException();
        }

        if (headers == null || headers.length == 0) {
            headers = getHeaders();
        }

        Scanner scanner = new Scanner(censusFile);
        //Skip header line
        scanner.nextLine();

        while (scanner.hasNextLine()) {
            String employeeRow = scanner.nextLine();
            Scanner employeeScanner = new Scanner(employeeRow).useDelimiter(",");
            Employee employee = new Employee();

            for (String header : headers) {
                String toAdd = employeeScanner.next();
                if (toAdd.contains("\"")) {
                    while (!toAdd.endsWith("\"")) {
                        toAdd = toAdd + "," + employeeScanner.next(); //accounts for cell data containing commas
                    }
                    toAdd = toAdd.replaceAll("\"", "");
                }
                employee.addInfo(header, toAdd);
            }

            employeeScanner.close();
            employees.add(employee);
        }
    }

    private void assignDependents() {
        Iterator<Employee> employeeIterator = employees.iterator();

        while (employeeIterator.hasNext()) {
            Employee employee = employeeIterator.next();
            if (!employee.getInfo("Relationship").equals("Employee")) { //e.g., Spouse, Child, etc...
                String ssn = employee.getInfo("Employee SSN");
                for (Employee current : employees) {
                    if (current.getInfo("Employee SSN").equals(employee.getInfo("Employee SSN")) && current.getInfo("Relationship").equals("Employee")) {
                        current.addDependent(new Dependent(employee));
                        employeeIterator.remove();
                        break;
                    }
                }
            }
        }
    }
}
