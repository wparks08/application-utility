package utility;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDNonTerminalField;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Application {

    private File pdfFile;
    private PDDocument application;
    private PDAcroForm form;

    Application(File pdfFile) {
        this.pdfFile = pdfFile;

        try {
            application = PDDocument.load(this.pdfFile);
            form = application.getDocumentCatalog().getAcroForm();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public File getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(File pdfFile) {
        this.pdfFile = pdfFile;
    }

    public PDDocument getApplication() {
        return application;
    }

    public void setApplication(PDDocument application) {
        this.application = application;
    }

    public List<PDField> getPDFields() {

        List<PDField> fields = new ArrayList<>();

        for (PDField field : form.getFields()) {
            PDFieldsListHelper(field, fields);
        }

        return fields;
    }

    private void PDFieldsListHelper(PDField field, List<PDField> list) {
        list.add(field);
        if (field instanceof PDNonTerminalField) {
            PDNonTerminalField nonTerminalField = (PDNonTerminalField) field;
            for (PDField child : nonTerminalField.getChildren()) {
                PDFieldsListHelper(child, list);
            }
        }
    }
}
