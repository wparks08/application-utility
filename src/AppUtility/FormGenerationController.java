package AppUtility;

import AppUtility.db.Carrier;
import AppUtility.db.Form;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.*;

public class FormGenerationController {
    @FXML private JFXButton importCensus;
    @FXML private JFXButton importChangeReport;
    @FXML private JFXButton chooseOutputDirectory;
    @FXML private ScrollPane employeeListWrapper;
    @FXML private VBox employeeListView;
    @FXML private JFXRadioButton newHire;
    @FXML private JFXRadioButton openEnrollment;
    @FXML private ToggleGroup oe_nh;
    @FXML private HBox wrapper;
    @FXML private JFXTextField groupName;
    @FXML private JFXTextField groupNumber;

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
    public void generateForms() {
        for (EmployeeRow employeeRow : employeeRowList) {
            Form form = employeeRow.getSelectedForm();

//            Application
        }
    }
}