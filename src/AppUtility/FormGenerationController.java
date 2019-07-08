package AppUtility;

import AppUtility.db.*;
import AppUtility.mapping.Conditional;
import AppUtility.mapping.DataType;
import AppUtility.mapping.MapProperty;
import AppUtility.ui.EmployeeRow;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FormGenerationController {
    @FXML
    private JFXButton importCensus;
    @FXML
    private JFXButton importChangeReport;
    @FXML
    private JFXButton chooseOutputDirectory;
    @FXML
    private ScrollPane employeeListWrapper;
    @FXML
    private VBox employeeListView;
    @FXML
    private JFXRadioButton newHire;
    @FXML
    private JFXRadioButton openEnrollment;
    @FXML
    private ToggleGroup oe_nh;
    @FXML
    private HBox wrapper;
    @FXML
    private JFXTextField groupName;
    @FXML
    private JFXTextField groupNumber;

    private DataModel model;
    private ObservableList<Employee> employees = FXCollections.observableArrayList();
    private String outputDirectory;
    private OEChanges changes;
    private final ObservableList<EmployeeRow> employeeRowList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        System.out.println("Form Generation selected");

        openEnrollment.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    importChangeReport.setDisable(false);
                } else {
                    importChangeReport.setDisable(true);
                }
            }
        });

        newHire.setSelected(true); //by default
    }

    @FXML
    public void importCensus(ActionEvent e) {
        ExtensionHelper extensionHelper = new ExtensionHelper("CSV File", "*.csv");
        File censusFile = showFileChooser(extensionHelper);

        Census census = new Census(censusFile);
        employees.setAll(census.getEmployees());
//        employeeListView.setItems(employees);

        List<Carrier> carrierList = new Carrier().list();

        final JFXSpinner workingIndicator = new JFXSpinner();
        employeeListView.getChildren().add(workingIndicator);

        Task importWorker = new Task() {
            @Override
            protected Object call() throws Exception {
                System.out.println("In importThread");
                for (Employee employee : employees) {
                    employeeRowList.add(new EmployeeRow(employee, carrierList));
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        employeeListView.getChildren().setAll(employeeRowList);
                    }
                });

                return true;
            }
        };
        importWorker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        employeeListView.getChildren().remove(workingIndicator);
                    }
                });
            }
        });

        Thread importThread = new Thread(importWorker);
        importThread.setDaemon(true);

        importThread.start();
    }

    @FXML
    public void importChangeReport() {

        if (employeeRowList.isEmpty()) {
            Snackbar.show(wrapper, "Please import the Census first");
            return;
        }

        File changeReport = showFileChooser(new ExtensionHelper("Excel (97-2003)", "*.xls"));
        changes = new OEChanges(changeReport);

        List<Change> changeList = changes.getChanges();

        for (Change change : changeList) {
            Iterator<EmployeeRow> employeeRowIterator = employeeRowList.iterator();
            while (employeeRowIterator.hasNext()) {
                EmployeeRow currentRow = employeeRowIterator.next();
                if (currentRow.getEmployee().getInfo("Employee SSN").equals(change.getSsn())) {
                    if (change.getChangeType().equals(ChangeType.ADD_COVERAGE)) {
                        currentRow.setIsEnrollment(true);
                    } else {
                        currentRow.setIsChange(true);
                    }
                    break;
                }
            }
        }

        Snackbar.show(wrapper, "Change Report imported");
    }

    @FXML
    public void chooseOutputDirectory() {
        File directory = showDirectoryChooser();
        outputDirectory = directory.getPath();
    }

    private File showDirectoryChooser() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(model.getLastAccessedFilePath()));

        File directory = dc.showDialog(null);

        if (directory != null) {
            model.setLastAccessedFilePath(directory.getParent());
        }

        return directory;
    }

    private File showFileChooser(ExtensionHelper... extensionHelpers) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(model.getLastAccessedFilePath()));

        for (ExtensionHelper extension : extensionHelpers) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(extension.getDescription(), extension.getFileSystemExtension()));
        }

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            model.setLastAccessedFilePath(file.getParent());
        }

        return file;
    }

    @FXML
    public void initModel(DataModel model) {
        this.model = model;
    }

    @FXML
    public void generateForms() throws IOException {
        for (EmployeeRow employeeRow : employeeRowList) {
            if (employeeRow.getIsEnrollment()) {
                Form form = employeeRow.getSelectedForm();
                Application application = new Application(form.getFile());
                List<PDField> pdFields = application.getPDFields(); //FIXME this is here for debug only. Remove when done.
                PDAcroForm acroForm = application.getApplication().getDocumentCatalog().getAcroForm();
                List<Mapping> mappings = form.getMappings();
                Employee employee = employeeRow.getEmployee();
                List<Dependent> dependents = employee.getDependents();

                for (Mapping mapping : mappings) {
                    HashMap<String, String> mappingProperties = mapping.getMappingPropertiesAsMap();
                    DataType dataType = getDataType(mappingProperties);

                    String censusHeader = new CensusHeader().get(mapping.getCensusHeaderId()).getHeader();
                    String formField = new FormField().get(mapping.getFormFieldId()).getFieldName();

                    PDField field = acroForm.getField(formField);

                    if (!isDependentField(mappingProperties)) {

                        String info = employee.getInfo(censusHeader);

                        processFormFieldData(mapping, mappingProperties, dataType, field, info);
                    } else if (isDependentField(mappingProperties)) {
                        //find out which dependent
                        String relationship = getProperty(mappingProperties, MapProperty.DEPENDENT);
                        Dependent dependent = null;
                        if (relationship.equals("Spouse")) {
                            for (Dependent dep : dependents) { //Find spouse in list of dependents
                                if (dep.getInfo("Relationship").equals("Spouse")) {
                                    dependent = dep; //Assign found spouse to dependent variable
                                }
                            }
                        } else if (relationship.contains("Child")) {
                            int childNumber = Integer.valueOf(relationship.split(" ")[1]);
                            int childIndex = childNumber - 1;
                            List<Dependent> children = dependents.stream()
                                    .filter(dep -> dep.getInfo("Relationship").equals("Child"))
                                    .collect(Collectors.toList());
                            if (childNumber <= children.size()) {
                                dependent = children.get(childIndex);
                            }

                        }
                        //fill in the field
                        if (dependent != null) {
                            String info = dependent.getInfo(censusHeader);

                            processFormFieldData(mapping, mappingProperties, dataType, field, info);
                        }
                    }
                }

                int maximumChildrenPerForm = Integer.valueOf(form.getFormPropertiesAsMap().get(FormProperties.CHILDREN_COUNT.toString()));
                List<Dependent> children = dependents.stream()
                        .filter(dep -> dep.getInfo("Relationship").equals("Child"))
                        .collect(Collectors.toList());

                if (children.size() > maximumChildrenPerForm) {
                    processAdditionalDependents(children.subList(maximumChildrenPerForm, children.size()), maximumChildrenPerForm, employeeRow, 1);
                }

                String lastName = employee.getInfo("Last Name");
                String firstName = employee.getInfo("First Name");
                String fileNameText = lastName + ", " + firstName + " - Enrollment Form.pdf";
                application.getApplication().save(outputDirectory + "\\" + fileNameText);
                application.getApplication().close();
            }
        }
    }

    private void processAdditionalDependents(List<Dependent> dependents, int maximumChildrenPerForm, EmployeeRow employeeRow, int count) throws IOException {
        if (dependents.size() > maximumChildrenPerForm) {
            processAdditionalDependents(dependents.subList(maximumChildrenPerForm, dependents.size()), maximumChildrenPerForm, employeeRow, count++);
        }
        Form form = employeeRow.getSelectedForm();
        Application application = new Application(form.getFile());
        PDAcroForm acroForm = application.getApplication().getDocumentCatalog().getAcroForm();
        List<Mapping> mappings = form.getMappings();
        Employee employee = employeeRow.getEmployee();

        for (Mapping mapping : mappings) {
            HashMap<String, String> mappingProperties = mapping.getMappingPropertiesAsMap();
            DataType dataType = getDataType(mappingProperties);

            String censusHeader = new CensusHeader().get(mapping.getCensusHeaderId()).getHeader();
            String formField = new FormField().get(mapping.getFormFieldId()).getFieldName();

            PDField field = acroForm.getField(formField);

            if (!isDependentField(mappingProperties)) {
                String info = employee.getInfo(censusHeader);
                processFormFieldData(mapping, mappingProperties, dataType, field, info);
            } else if (isDependentField(mappingProperties)) {
                String relationship = getProperty(mappingProperties, MapProperty.DEPENDENT);
                Dependent dependent = null;
                if (relationship.contains("Child")) {
                    int childNumber = Integer.valueOf(relationship.split(" ")[1]);
                    int childIndex = childNumber - 1;
                    if (childNumber <= dependents.size()) {
                        dependent = dependents.get(childIndex);
                    }
                }
                //fill in the field
                if (dependent != null) {
                    String info = dependent.getInfo(censusHeader);
                    processFormFieldData(mapping, mappingProperties, dataType, field, info);
                }
            }
        }

        String lastName = employee.getInfo("Last Name");
        String firstName = employee.getInfo("First Name");
        String fileNameText = lastName + ", " + firstName + " - Enrollment Form - Additional Dependents (" + count + ").pdf";
        application.getApplication().save(outputDirectory + "\\" + fileNameText);
        application.getApplication().close();
    }

    private void processFormFieldData(Mapping mapping, HashMap<String, String> mappingProperties, DataType dataType, PDField field, String info) throws IOException {
        switch (dataType) {
            case TEXT:
                setFieldValue(field, info);
                break;
            case DATE:
                setDateFieldValue(mappingProperties, field, info);
                break;
            case SSN:
                setSsnFieldValue(mappingProperties, field, info);
                break;
            case PHONE:
                setPhoneFieldValue(mappingProperties, field, info);
                break;
            case RADIO:
                setRadioFieldValue(mapping, mappingProperties, field, info);
                break;
            case CHECKBOX:
                setCheckboxFieldValue(mappingProperties, field, info);
                break;
            case INITIAL:
                setInitialFieldValue(field, info);
                break;
            case GROUP_NAME:
                setFieldValue(field, groupName.getText());
                break;
            case GROUP_NUMBER:
                setFieldValue(field, groupNumber.getText());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + dataType);
        }
    }

    private String getProperty(HashMap<String, String> mappingProperties, MapProperty mapProperty) {
        return mappingProperties.get(mapProperty.toString());
    }

    private DataType getDataType(HashMap<String, String> mappingProperties) {
        return DataType.valueOf(getProperty(mappingProperties, MapProperty.DATA_TYPE));
    }

    private Boolean isDependentField(HashMap<String, String> mappingProperties) {
        return Boolean.valueOf(getProperty(mappingProperties, MapProperty.IS_DEPENDENT_FIELD));
    }

    private void setInitialFieldValue(PDField field, String info) throws IOException {
        if (info != null && !info.isEmpty()) {
            field.setValue(String.valueOf(info.charAt(0)));
        }
    }

    private void setCheckboxFieldValue(HashMap<String, String> mappingProperties, PDField field, String info) throws IOException {
        Conditional conditional = Conditional.valueOf(getProperty(mappingProperties, MapProperty.CONDITIONAL));
        String textValue = getProperty(mappingProperties, MapProperty.TEXT_VALUE);

        switch (conditional) {
            case EQUALS:
                if (textValue.equals(info)) {
                    field.setValue(((PDCheckBox) field).getOnValue());
                }
                break;
            case CONTAINS:
                String[] splitConditionArray = textValue.split(" ");
                if (Stream.of(splitConditionArray).allMatch(info::contains)) {
                    field.setValue(((PDCheckBox) field).getOnValue());
                }
                break;
        }

        //If "always checked"...
        if (Boolean.valueOf(getProperty(mappingProperties, MapProperty.ALWAYS_CHECKED))) {
            if (field instanceof PDCheckBox) {
                field.setValue(((PDCheckBox) field).getOnValue());
            }
        }
    }

    private void setRadioFieldValue(Mapping mapping, HashMap<String, String> mappingProperties, PDField field, String info) throws IOException {
        if (Boolean.valueOf(getProperty(mappingProperties, MapProperty.HAS_DEFAULT))) {
            setFieldValue(field, getProperty(mappingProperties, MapProperty.DEFAULT_VALUE));
        }
        List<RadioCondition> radioConditions = mapping.getRadioConditions();

        for (RadioCondition radioCondition : radioConditions) {
            Conditional conditional = radioCondition.getConditional();
            switch (conditional) {
                case EQUALS:
                    String equalsCondition = radioCondition.getCensusValue();
                    if (info.equals(equalsCondition)) {
                        setFieldValue(field, radioCondition.getFormValue());
                    }
                    break;
                case CONTAINS:
                    String[] splitConditionArray = radioCondition.getCensusValue().split(" ");
                    if (Stream.of(splitConditionArray).allMatch(info::contains)) {
                        setFieldValue(field, radioCondition.getFormValue());
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + conditional);
            }
        }
    }

    private void setPhoneFieldValue(HashMap<String, String> mappingProperties, PDField field, String info) throws IOException {
        if (Boolean.valueOf(getProperty(mappingProperties, MapProperty.PHONE_SPLIT))) {
            String phoneSection = getProperty(mappingProperties, MapProperty.PHONE_SECTION);
            String phoneValue = info;
            phoneValue = scrubPhoneValue(phoneValue); //Remove all non-numeric characters
            padPhoneString(phoneValue); //Pad with leading spaces, if less than 10 digits
            switch (phoneSection) {
                case "Area Code":
                    setFieldValue(field, phoneValue.substring(0, 3));
                    break;
                case "Prefix":
                    setFieldValue(field, phoneValue.substring(3, 6));
                    break;
                case "Suffix":
                    setFieldValue(field, phoneValue.substring(6));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + phoneSection);
            }
        } else {
            setFieldValue(field, info);
        }
    }

    private void setSsnFieldValue(HashMap<String, String> mappingProperties, PDField field, String info) throws IOException {
        if (Boolean.valueOf(getProperty(mappingProperties, MapProperty.SSN_SPLIT))) {
            String ssnSection = getProperty(mappingProperties, MapProperty.SSN_SECTION);
            String[] splitSsn = info.split("-");
            switch (ssnSection) {
                case "Beginning":
                    setFieldValue(field, splitSsn[0]);
                    break;
                case "Middle":
                    setFieldValue(field, splitSsn[1]);
                    break;
                case "End":
                    setFieldValue(field, splitSsn[2]);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + ssnSection);
            }
        } else if (Boolean.valueOf(getProperty(mappingProperties, MapProperty.SSN_UNDECORATED))) {
            String formattedSsn = info.replaceAll("-", "");
            setFieldValue(field, formattedSsn);
        } else {
            setFieldValue(field, info);
        }
    }

    private void setDateFieldValue(HashMap<String, String> mappingProperties, PDField field, String info) throws IOException {
        if (Boolean.valueOf(getProperty(mappingProperties, MapProperty.DATE_SPLIT))) {
            String dateSection = getProperty(mappingProperties, MapProperty.DATE_SECTION);
            String[] splitDate = info.split("/");
            switch (dateSection) {
                case "Month":
                    setFieldValue(field, splitDate[0]);
                    break;
                case "Day":
                    setFieldValue(field, splitDate[1]);
                    break;
                case "Year":
                    setFieldValue(field, splitDate[2]);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + dateSection);
            }
        } else {
            setFieldValue(field, info);
        }
    }

    private void setFieldValue(PDField field, String info) throws IOException {
        field.setValue(info);
    }

    private void padPhoneString(String phoneValue) {
        while (phoneValue.length() < 10) {
            phoneValue = " " + phoneValue;
        }
    }

    private String scrubPhoneValue(String phoneValue) {
        return phoneValue.replaceAll("[^\\d.]", "");
    }
}