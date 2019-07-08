package AppUtility;

import AppUtility.db.CensusHeader;
import AppUtility.db.Form;
import AppUtility.db.FormField;
import AppUtility.db.FormProperty;
import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class EditFormController {
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
    @FXML private HBox childrenWrapper;
    @FXML private JFXCheckBox chbSpouse;
    @FXML private JFXCheckBox chbChildren;
    @FXML private JFXComboBox<Integer> numberOfChildren;

    private DataModel model;
    private Application application;
    private Census census;

    @FXML
    public void initialize() {
        System.out.println("Edit Form controller initialized");
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Required");

        addValidatorToRequiredFields(validator);
        addNumberOfChildrenComboBox();
    }

    private void addNumberOfChildrenComboBox() {
        numberOfChildren = new JFXComboBox<>();
        numberOfChildren.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        numberOfChildren.setPromptText("Number of Children per Form");
        numberOfChildren.setDisable(true);

        chbChildren.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    numberOfChildren.setDisable(false);
                } else {
                    numberOfChildren.setDisable(true);
                }
            }
        });

        childrenWrapper.getChildren().add(numberOfChildren);
    }

    private void addValidatorToRequiredFields(RequiredFieldValidator validator) {
        txtFormName.getValidators().add(validator);
        dteEffectiveBegin.getValidators().add(validator);
        dteEffectiveEnd.getValidators().add(validator);
    }

    public void initModel(DataModel model) {
        this.model = model;

        Form selectedForm = model.getSelectedForm();

        txtFormName.setText(selectedForm.getName());
        HashMap<String, String> formPropertiesMap = selectedForm.getFormPropertiesAsMap();
        dteEffectiveBegin.setValue(LocalDate.parse(formPropertiesMap.get(FormProperties.EFFECTIVE_BEGIN.toString()), DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        dteEffectiveEnd.setValue(LocalDate.parse(formPropertiesMap.get(FormProperties.EFFECTIVE_END.toString()), DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        chbSpouse.setSelected(Boolean.valueOf(formPropertiesMap.get(FormProperties.HAS_SPOUSE.toString())));
        chbChildren.setSelected(Boolean.valueOf(formPropertiesMap.get(FormProperties.HAS_CHILDREN.toString())));
        numberOfChildren.getSelectionModel().select(Integer.valueOf(formPropertiesMap.get(FormProperties.CHILDREN_COUNT.toString())));
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
            Form selectedForm = model.getSelectedForm();
            updateForm(selectedForm);
            updateEffectiveDateProperties(selectedForm);
            updateDependentProperties(selectedForm);

            Snackbar.show(wrapper, "Form Updated");
        } else {
            Snackbar.show(wrapper, "Please fill in the required fields.");
        }
    }

    private void updateDependentProperties(Form form) {
        List<FormProperty> formPropertyList = form.getFormProperties();

        for (FormProperty formProperty : formPropertyList) {
            if (formProperty.getProperty().equals(FormProperties.HAS_SPOUSE.toString())) {
                formProperty.setValue(chbSpouse.selectedProperty().getValue().toString());
            }

            if (formProperty.getProperty().equals(FormProperties.HAS_CHILDREN.toString())) {
                formProperty.setValue(chbChildren.selectedProperty().getValue().toString());
            }

            if (formProperty.getProperty().equals(FormProperties.CHILDREN_COUNT.toString())) {
                formProperty.setValue(numberOfChildren.getSelectionModel().getSelectedItem().toString());
            }

            formProperty.save();
        }
    }

    @FXML
    public void cancel(ActionEvent e) {
        Stage stage = (Stage) wrapper.getScene().getWindow();
        stage.close();
    }

    private void updateEffectiveDateProperties(Form form) {
        List<FormProperty> formPropertyList = form.getFormProperties();

        for (FormProperty formProperty : formPropertyList) {
            if (formProperty.getProperty().equals(FormProperties.EFFECTIVE_BEGIN.toString())) {
                formProperty.setValue(dteEffectiveBegin.getValue().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
            }

            if (formProperty.getProperty().equals(FormProperties.EFFECTIVE_END.toString())) {
                formProperty.setValue(dteEffectiveEnd.getValue().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
            }

            formProperty.save();
        }
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

    private void updateForm(Form form) {
        form.setName(txtFormName.getText());
        form.save();
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
