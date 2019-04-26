package utility;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OEChanges {

    private File oeChangeFile;
    private HSSFWorkbook oeChangeWorkbook;
    private int changeTypeColumnIndex = 4;
    private int oldValueColumnIndex = 5;
    private int newValueColumnIndex = 6;
    private int ssnColumnIndex = 1;

    public OEChanges(File oeChangeFile) {
        this.oeChangeFile = oeChangeFile;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(oeChangeFile);
            oeChangeWorkbook = new HSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public File getOeChangeFile() {
        return oeChangeFile;
    }

    public void setOeChangeFile(File oeChangeFile) {
        this.oeChangeFile = oeChangeFile;
    }

    public HSSFWorkbook getOeChangeWorkbook() {
        return oeChangeWorkbook;
    }

    public void setOeChangeWorkbook(HSSFWorkbook oeChangeWorkbook) {
        this.oeChangeWorkbook = oeChangeWorkbook;
    }

    public int getChangeTypeColumnIndex() {
        return changeTypeColumnIndex;
    }

    public void setChangeTypeColumnIndex(int changeTypeColumnIndex) {
        this.changeTypeColumnIndex = changeTypeColumnIndex;
    }

    public int getOldValueColumnIndex() {
        return oldValueColumnIndex;
    }

    public void setOldValueColumnIndex(int oldValueColumnIndex) {
        this.oldValueColumnIndex = oldValueColumnIndex;
    }

    public int getNewValueColumnIndex() {
        return newValueColumnIndex;
    }

    public void setNewValueColumnIndex(int newValueColumnIndex) {
        this.newValueColumnIndex = newValueColumnIndex;
    }

    public int getSsnColumnIndex() {
        return ssnColumnIndex;
    }

    public void setSsnColumnIndex(int ssnColumnIndex) {
        this.ssnColumnIndex = ssnColumnIndex;
    }

    public List<Change> getChanges(ChangeType changeType) {
        List<Change> changes = new ArrayList<>();

        HSSFSheet worksheet = oeChangeWorkbook.getSheetAt(0);

        switch (changeType) {
            case PLAN:
                for (int i = 0; i < worksheet.getLastRowNum(); i++) {
                    Row row = worksheet.getRow(i);
                    if (row.getCell(changeTypeColumnIndex).getStringCellValue().equals("Plan")) {
                        changes.add(new Change(
                                row.getCell(ssnColumnIndex).getStringCellValue(),
                                row.getCell(oldValueColumnIndex).getStringCellValue(),
                                row.getCell(newValueColumnIndex).getStringCellValue(),
                                ChangeType.PLAN
                        ));
                    }
                }
                break;

            case ADD_COVERAGE:
                for (int i = 0; i < worksheet.getLastRowNum(); i++) {
                    Row row = worksheet.getRow(i);
                    if (row.getCell(changeTypeColumnIndex).getStringCellValue().equals("Membership: Added Coverage")) {
                        changes.add(new Change(
                                row.getCell(ssnColumnIndex).getStringCellValue(),
                                /*row.getCell(oldValueColumnIndex).getStringCellValue()*/"",
                                row.getCell(newValueColumnIndex).getStringCellValue(),
                                ChangeType.ADD_COVERAGE
                        ));
                    }
                }
                break;

            case DROP_COVERAGE:
                for (int i = 0; i < worksheet.getLastRowNum(); i++) {
                    Row row = worksheet.getRow(i);
                    if (row.getCell(changeTypeColumnIndex).getStringCellValue().equals("Membership: Dropped Coverage")) {
                        changes.add(new Change(
                                row.getCell(ssnColumnIndex).getStringCellValue(),
                                row.getCell(oldValueColumnIndex).getStringCellValue(),
                                /*row.getCell(newValueColumnIndex).getStringCellValue()*/"",
                                ChangeType.DROP_COVERAGE
                        ));
                    }
                }
                break;

            case ADD_DEPENDENT:
                for (int i = 0; i < worksheet.getLastRowNum(); i++) {
                    Row row = worksheet.getRow(i);
                    if (row.getCell(changeTypeColumnIndex).getStringCellValue().equals("Membership: Added Dependent")) {
                        changes.add(new Change(
                                row.getCell(ssnColumnIndex).getStringCellValue(),
                                /*row.getCell(oldValueColumnIndex).getStringCellValue()*/"",
                                row.getCell(newValueColumnIndex).getStringCellValue(),
                                ChangeType.ADD_DEPENDENT
                        ));
                    }
                }
                break;

            case DROP_DEPENDENT:
                for (int i = 0; i < worksheet.getLastRowNum(); i++) {
                    Row row = worksheet.getRow(i);
                    if (row.getCell(changeTypeColumnIndex).getStringCellValue().equals("Membership: Dropped Dependent")) {
                        changes.add(new Change(
                                row.getCell(ssnColumnIndex).getStringCellValue(),
                                row.getCell(oldValueColumnIndex).getStringCellValue(),
                                row.getCell(newValueColumnIndex).getStringCellValue(),
                                ChangeType.DROP_DEPENDENT
                        ));
                    }
                }
                break;

            default:
                break; //do nothing
        }

        return changes;
    }
}
