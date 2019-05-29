package AppUtility;

import AppUtility.db.CensusHeader;
import AppUtility.db.Form;
import AppUtility.db.FormField;
import AppUtility.db.FormProperty;
import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.File;
import java.time.format.DateTimeFormatter;

public class NewFormController {
    @FXML private VBox wrapper;
    @FXML private JFXTextField txtCensusName;
    @FXML private JFXDatePicker dteEffectiveBegin;
    @FXML private JFXDatePicker dteEffectiveEnd;
    @FXML private JFXButton btnSave;
    @FXML private JFXButton btnCancel;
    @FXML private Label lblEffectiveDateRange;
    @FXML private JFXButton btnImportForm;
    @FXML private JFXCheckBox chkImportForm;
    @FXML private JFXButton btnImportCensus;
    @FXML private JFXCheckBox chkImportCensus;
    @FXML private JFXTextField txtFilePath;
    @FXML private JFXTextField txtFormName;

    private DataModel model;
    private Application application;
    private Census census;

    @FXML
    public void initialize() {
        System.out.println("New Form controller initialized");
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Required");

        addValidatorToRequiredFields(validator);
    }

    private void addValidatorToRequiredFields(RequiredFieldValidator validator) {
        txtFilePath.getValidators().add(validator);
        txtCensusName.getValidators().add(validator);
        txtFormName.getValidators().add(validator);
        dteEffectiveBegin.getValidators().add(validator);
        dteEffectiveEnd.getValidators().add(validator);
    }

    public void initModel(DataModel model) {
        this.model = model;
    }

    @FXML
    public void importFormAction(ActionEvent e) {
        ExtensionHelper extensionHelper = new ExtensionHelper("PDF File", "*.pdf");

        File form = showFileChooser(extensionHelper);

        if(form != null) {
            application = new Application(form);
            chkImportForm.setSelected(true);
            model.setLastAccessedFilePath(form.getParent());
            txtFilePath.setText(form.getName());
        }
    }

    @FXML
    public void importCensusAction(ActionEvent e) {
        ExtensionHelper extensionHelper = new ExtensionHelper("CSV File", "*.csv");

        File censusFile = showFileChooser(extensionHelper);

        if (censusFile != null) {
            census = new Census(censusFile);
            chkImportCensus.setSelected(true);
            model.setLastAccessedFilePath(censusFile.getParent());
            txtCensusName.setText(censusFile.getName());
        }
    }

    private File showFileChooser(ExtensionHelper... extensionHelpers) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(model.getLastAccessedFilePath()));

        for (ExtensionHelper extension : extensionHelpers) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extension.getDescription(), extension.getFileSystemExtension()));
        }

        return fileChooser.showOpenDialog(null);
    }

    @FXML
    public void saveForm(ActionEvent e) {
        if (validate()) {
            Form form = createForm();
            form.save(); //Needs an ID before we continue
            createPDFields(form);
            createCensusHeaders(form);
            createEffectiveDateProperties(form);

            Snackbar.show(wrapper, "New Form Saved");
        } else {
            Snackbar.show(wrapper, "Please fill in the required fields.");
        }
    }

    @FXML
    public void cancel(ActionEvent e) {
        Stage stage = (Stage) wrapper.getScene().getWindow();
        stage.close();
    }

    private void createEffectiveDateProperties(Form form) {
        FormProperty effectiveBegin = new FormProperty();
        effectiveBegin.setProperty(FormProperties.EFFECTIVE_BEGIN.toString());
        effectiveBegin.setValue(dteEffectiveBegin.getValue().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        form.addFormProperty(effectiveBegin);

        FormProperty effectiveEnd = new FormProperty();
        effectiveEnd.setProperty(FormProperties.EFFECTIVE_END.toString());
        effectiveEnd.setValue(dteEffectiveEnd.getValue().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        form.addFormProperty(effectiveEnd);
    }

    private void createCensusHeaders(Form form) {
        for (String field : census.getHeaders()) {
            CensusHeader censusHeader = new CensusHeader(field, form.getId());
            censusHeader.save();
        }
    }

    private void createPDFields(Form form) {
        for (PDField pdField : application.getPDFields()) {
            FormField formField = new FormField(pdField.getFullyQualifiedName(), form.getId());
            formField.save();
        }
    }

    private Form createForm() {
        Form form = new Form();
        form.setName(txtFormName.getText());
        form.setCarrierId(model.getSelectedCarrier().getId());
        form.loadFormFile(application.getPdfFile());
        return form;
    }

    private boolean validate() {
        txtFilePath.validate();
        txtCensusName.validate();
        txtFormName.validate();
        dteEffectiveBegin.validate();
        dteEffectiveEnd.validate();
        return (txtFilePath.validate() && txtCensusName.validate() && txtFormName.validate() && dteEffectiveBegin.validate() && dteEffectiveEnd.validate());
    }
}
