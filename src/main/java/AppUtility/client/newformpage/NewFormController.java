package AppUtility.client.newformpage;

import AppUtility.*;
import AppUtility.client.ui.controls.CensusFileChooser;
import AppUtility.db.Form;
import AppUtility.db.FormField;
import AppUtility.db.FormProperty;
import AppUtility.client.ui.controls.ChildrenComboBox;
import AppUtility.domains.pdf.PdfFile;
import AppUtility.usecases.imports.ImportStatus;
import AppUtility.usecases.imports.PdfFileImportHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.File;
import java.time.format.DateTimeFormatter;

public class NewFormController {
    @FXML
    private VBox wrapper;
    @FXML
    private JFXTextField txtCensusName;
    @FXML
    private DatePicker dteEffectiveBegin;
    @FXML
    private DatePicker dteEffectiveEnd;
    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private Label lblEffectiveDateRange;
    @FXML
    private JFXButton btnImportForm;
    @FXML
    private JFXCheckBox chkImportForm;
    @FXML
    private JFXButton btnImportCensus;
    @FXML
    private JFXCheckBox chkImportCensus;
    @FXML
    private JFXTextField txtFilePath;
    @FXML
    private JFXTextField txtFormName;
    @FXML
    private HBox childrenWrapper;
    @FXML
    private JFXCheckBox chbSpouse;
    @FXML
    private JFXCheckBox chbChildren;
    @FXML
    private ComboBox<Integer> numberOfChildren;
    @FXML
    ChildrenComboBox childrenComboBox;

    private Application application;
    private final NewFormService newFormService = new NewFormService();
    private String formPath;

    @FXML
    public void initialize() {
        System.out.println("New Form controller initialized");
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Required");

        addValidatorToRequiredFields(validator);
        addChildrenComboBox();
        txtCensusName.textProperty().bind(newFormService.getCensusPath());
        chkImportCensus.selectedProperty().bind(newFormService.getCensusImported());
    }

    private void addChildrenComboBox() {
        childrenComboBox = new ChildrenComboBox();
        childrenComboBox.bindCheckboxToDisableProperty(chbChildren);
        childrenWrapper.getChildren().add(childrenComboBox);
    }

    private void addValidatorToRequiredFields(RequiredFieldValidator validator) {
        txtFilePath.getValidators().add(validator);
        txtCensusName.getValidators().add(validator);
        txtFormName.getValidators().add(validator);
//        dteEffectiveBegin.getValidators().add(validator);
//        dteEffectiveEnd.getValidators().add(validator);
    }

    @FXML
    public void importFormAction(ActionEvent e) {
        FileExtension fileExtension = new FileExtension("PDF File", "*.pdf");

        File form = showFileChooser(fileExtension);

        if (form != null) {
            application = new Application(form);
            chkImportForm.setSelected(true);
            AppProperties.getInstance().setLastAccessedFilePath(form.getParent());
            txtFilePath.setText(form.getName());
            formPath = form.getPath();
        }
    }

    @FXML
    public void importCensusAction() {
        File censusFile = new CensusFileChooser(new File(AppProperties.getInstance().getLastAccessedFilePath())).showOpenDialog();

        if (censusFile != null) {
            newFormService.importCensus(censusFile.toPath());
            AppProperties.getInstance().setLastAccessedFilePath(censusFile.getParent());
        }
    }

    private File showFileChooser(FileExtension... fileExtensions) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(AppProperties.getInstance().getLastAccessedFilePath()));

        for (FileExtension extension : fileExtensions) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extension.getDescription(), extension.getFileSystemExtension()));
        }

        return fileChooser.showOpenDialog(null);
    }

    @FXML
    public void saveForm(ActionEvent e) {
        PdfFileImportHandler pdfFileImportHandler = new PdfFileImportHandler(formPath);
        pdfFileImportHandler.doImport();

        pdfFileImportHandler.getStatus().addListener((observable, oldValue, newValue) -> {
            if (newValue == ImportStatus.DONE) {
                PdfFile pdfFile = pdfFileImportHandler.getPdfFile();
                System.out.println(pdfFile);
            }
        });
//        if (validate()) {
//            Form form = createForm();
//            form.save(); //Needs an ID before we continue
//            createPDFields(form);
//            createCensusHeaders(form);
//            createEffectiveDateProperties(form);
//            createDependentProperties(form);
//
//            Snackbar.show(wrapper, "New Form Saved");
//        } else {
//            Snackbar.show(wrapper, "Please fill in the required fields.");
//        }
    }

    private void createDependentProperties(Form form) {
        FormProperty hasSpouse = new FormProperty();
        hasSpouse.setProperty(FormProperties.HAS_SPOUSE.toString());
        hasSpouse.setValue(chbSpouse.selectedProperty().getValue().toString());
        form.addFormProperty(hasSpouse);

        FormProperty hasChildren = new FormProperty();
        hasChildren.setProperty(FormProperties.HAS_CHILDREN.toString());
        hasChildren.setValue(chbChildren.selectedProperty().getValue().toString());
        form.addFormProperty(hasChildren);

        FormProperty childCount = new FormProperty();
        childCount.setProperty(FormProperties.CHILDREN_COUNT.toString());
        childCount.setValue(childrenComboBox.getSelectionModel().getSelectedItem().toString());
        form.addFormProperty(childCount);
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
//        for (String field : census.getHeaders()) {
//            CensusHeader censusHeader = new CensusHeader(field, form.getId());
//            censusHeader.save();
//        }
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
//        form.setCarrierId(DataModel.getSelectedCarrier().getId());
        form.loadFormFile(application.getPdfFile());
        return form;
    }

    private boolean validate() {
        txtFilePath.validate();
        txtCensusName.validate();
        txtFormName.validate();
//        dteEffectiveBegin.validate();
//        dteEffectiveEnd.validate();
        return (txtFilePath.validate() && txtCensusName.validate() && txtFormName.validate() /*&& dteEffectiveBegin.validate() && dteEffectiveEnd.validate()*/);
    }
}
