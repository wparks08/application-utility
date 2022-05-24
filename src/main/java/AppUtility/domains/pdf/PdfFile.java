package AppUtility.domains.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

public class PdfFile {
    File pdfFile;
    private PDDocument pdDocument;

    public PdfFile(File pdfFile) {
        this.pdfFile = pdfFile;
        try {
            loadPDDocument();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void loadPDDocument() throws IOException {
        this.pdDocument = PDDocument.load(this.pdfFile);
    }

    public PdfForm getPdfForm() {
        return new PdfForm(this.pdDocument.getDocumentCatalog().getAcroForm());
    }
}
