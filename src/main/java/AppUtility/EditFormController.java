package AppUtility;

import AppUtility.Controls.ChildrenComboBox;
import AppUtility.Domains.Form.Form;
import com.jfoenix.controls.*;
import com.jfoenix.controls.base.IFXValidatableControl;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class EditFormController {
    @FXML private VBox wrapper;
    @FXML private JFXTextField txtCensusName;
    @FXML private DatePicker dteEffectiveBegin;
    @FXML private DatePicker dteEffectiveEnd;
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
    @FXML private ChildrenComboBox numberOfChildren;

    private Application application;
    private Census census;
    private IFXValidatableControl[] requiredFields;

    @FXML
    public void initialize() {
        System.out.println("Edit Form controller initialized");
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Required");

        requiredFields = new IFXValidatableControl[]{txtFormName/*, dteEffectiveBegin, dteEffectiveEnd*/};

        addValidatorToRequiredFields(validator);
        addNumberOfChildrenComboBox();

        Form selectedForm = DataModel.getSelectedForm();

        txtFormName.setText(selectedForm.getName());
        HashMap<String, String> formPropertiesMap = selectedForm.getFormPropertiesAsMap();
        dteEffectiveBegin.setValue(LocalDate.parse(formPropertiesMap.get(FormProperties.EFFECTIVE_BEGIN.toString()), DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        dteEffectiveEnd.setValue(LocalDate.parse(formPropertiesMap.get(FormProperties.EFFECTIVE_END.toString()), DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        chbSpouse.setSelected(Boolean.parseBoolean(formPropertiesMap.get(FormProperties.HAS_SPOUSE.toString())));
        chbChildren.setSelected(Boolean.parseBoolean(formPropertiesMap.get(FormProperties.HAS_CHILDREN.toString())));
        numberOfChildren.getSelectionModel().select(Integer.valueOf(formPropertiesMap.get(FormProperties.CHILDREN_COUNT.toString())));

    }

    private void addNumberOfChildrenComboBox() {
        numberOfChildren = new ChildrenComboBox();
        numberOfChildren.bindCheckboxToDisableProperty(chbChildren);

        childrenWrapper.getChildren().add(numberOfChildren);
    }

    private void addValidatorToRequiredFields(ValidatorBase validator) {
        for (IFXValidatableControl requiredField : requiredFields) {
            addValidatorToControl(validator, requiredField);
        }
    }

    private void addValidatorToControl(ValidatorBase validator, IFXValidatableControl control) {
        control.getValidators().add(validator);
    }

    public void initModel() {
        Form selectedForm = DataModel.getSelectedForm();

        txtFormName.setText(selectedForm.getName());
        HashMap<String, String> formPropertiesMap = selectedForm.getFormPropertiesAsMap();
        dteEffectiveBegin.setValue(LocalDate.parse(formPropertiesMap.get(FormProperties.EFFECTIVE_BEGIN.toString()), DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        dteEffectiveEnd.setValue(LocalDate.parse(formPropertiesMap.get(FormProperties.EFFECTIVE_END.toString()), DateTimeFormatter.ofPattern("MM-dd-yyyy")));
        chbSpouse.setSelected(Boolean.parseBoolean(formPropertiesMap.get(FormProperties.HAS_SPOUSE.toString())));
        chbChildren.setSelected(Boolean.parseBoolean(formPropertiesMap.get(FormProperties.HAS_CHILDREN.toString())));
        numberOfChildren.getSelectionModel().select(Integer.valueOf(formPropertiesMap.get(FormProperties.CHILDREN_COUNT.toString())));
    }

    @FXML
    public void importFormAction(ActionEvent e) {
        ExtensionHelper extensionHelper = new ExtensionHelper("PDF File", "*.pdf");

        File form = showFileChooser(extensionHelper);

        if(form != null) {
            application = new Application(form);
            chkImportForm.setSelected(true);
            DataModel.setLastAccessedFilePath(form.getParent());
            txtFilePath.setText(form.getName());
        }
    }

    @FXML
    public void importCensusAction(ActionEvent e) {
        ExtensionHelper extensionHelper = new ExtensionHelper("CSV File", "*.csv");

        File censusFile = showFileChooser(extensionHelper);

        if (censusFile != null) {
            census = new Census(censusFile);
            DataModel.setLastAccessedFilePath(censusFile.getParent());
            txtCensusName.setText(censusFile.getName());
        }
    }

    private void updateCensusHeaders() {
        if (census == null) {
            return;
        }

        //Get all census headers that already exist
        Form selectedForm = DataModel.getSelectedForm();
        List<CensusHeader> currentHeaders = selectedForm.getCensusHeaders();

        //Remove all from existing list that don't exist in new file
        List<CensusHeader> toRemove = new ArrayList<>();
        List<String> newHeaders = Arrays.asList(census.getHeaders());

        //Gather censusHeaders that don't exist in new census
        for (CensusHeader censusHeader : currentHeaders) {
            if (!newHeaders.contains(censusHeader.getHeader())) {
                toRemove.add(censusHeader);
            }
        }
        //Remove them
        currentHeaders.removeAll(toRemove);
        //And delete them
        for (CensusHeader censusHeader : toRemove) {
            Mapping mapping = new Mapping().getBy("census_header_id", String.valueOf(censusHeader.getId()));
            if (mapping != null) {
                mapping.delete();
            }
            censusHeader.delete();
        }

        //Add ones from new file that don't exist
        for (String newHeader : newHeaders) {
            boolean existsInCurrent = false;
            for (CensusHeader censusHeader : currentHeaders) {
                if (censusHeader.getHeader().equals(newHeader)) {
                    existsInCurrent = true;
                    break;
                }
            }

            if (!existsInCurrent) {
                CensusHeader newCensusHeader = new CensusHeader(newHeader, selectedForm.getId());
                newCensusHeader.save();
            }
        }
    }

    private File showFileChooser(ExtensionHelper... extensionHelpers) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(DataModel.getLastAccessedFilePath()));

        for (ExtensionHelper extension : extensionHelpers) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extension.getDescription(), extension.getFileSystemExtension()));
        }

        return fileChooser.showOpenDialog(null);
    }

    @FXML
    public void saveForm(ActionEvent e) {
        if (validate()) {
            Form selectedForm = DataModel.getSelectedForm();
            updateForm(selectedForm);
            updateEffectiveDateProperties(selectedForm);
            updateDependentProperties(selectedForm);
            updateCensusHeaders();

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
        txtFormName.validate();
//        dteEffectiveBegin.validate();
//        dteEffectiveEnd.validate();
        return (txtFormName.validate() /*&& dteEffectiveBegin.validate() && dteEffectiveEnd.validate()*/);
    }
}
