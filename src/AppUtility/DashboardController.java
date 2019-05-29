package AppUtility;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class DashboardController {
    public DataModel model;

    @FXML private Label lblStatus = new Label();
    @FXML private Button btnMapping;
    @FXML private Button btnGenerating;
    @FXML private Pane mainWindow;

    public void initModel(DataModel model) {
        this.model = model;
    }

    @FXML
    public void initialize() {
        btnMapping.setText("Form Mapping");
        btnGenerating.setText("Form Generation");

//        Form form = new Form().get(1);
//        List<CensusHeader> censusHeaderList = form.getCensusHeaders();
//
//        for (CensusHeader censusHeader : censusHeaderList) {
//            System.out.println(censusHeader.getId() + " : " + censusHeader.getHeader());
//        }
//
//        System.out.println(com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());

        //Upload a form
        //Upload a census
        //Get all census headers
        //Get all form fields
        //Tie everything up - assign relationships

//        Application app = new Application(new File("SampleData/Cal Choice Enrollment form 4-1-19.pdf"));
//        Form form = new Form();
//        form.loadFormFile(app.getPdfFile());
//        form.setName("Cal Choice Q2 2019");
//        form.save();
//        System.out.println(form.getId());
//
//        Census census = new Census(new File("C:\\JavaFXProjects\\ApplicationUtility\\SampleData\\fullsamplewithdependents.csv"));
//        String[] headers = census.getHeaders();
//
//        for (String header : headers) {
//            CensusHeader censusHeader = new CensusHeader(header, form.getId());
//            censusHeader.save();
//        }
//
//        List<PDField> pdFields = app.getPDFields();
//
//        for (PDField pdField : pdFields) {
//            FormField formField = new FormField(pdField.getPartialName(), form.getId());
//            formField.save();
//        }
//
//        List<CensusHeader> censusHeaders = form.getCensusHeaders();
//        List<FormField> formFields = form.getFormFields();
//
//        System.out.println(censusHeaders.size() + " Census Headers");
//        System.out.println(formFields.size() + " Form Fields");
//
//        for (int i = 0; i < formFields.size(); i++) {
//            Mapping mapping = new Mapping(censusHeaders.get(i).getId(), formFields.get(i).getId(), form.getId());
//            mapping.save();
//        }
    }

    public void handleBtnClickAction(ActionEvent event) {
        Button current = (Button)event.getSource();
        btnMapping.setStyle("-fx-background-color: #424242;");
//        btnMapping.setStyle("-fx-text-fill: #EEEEEE;");
        btnGenerating.setStyle("-fx-background-color: #424242;");
//        btnGenerating.setStyle("-fx-text-fill: #EEEEEE;");

        current.setStyle("-fx-background-color: #111111;");

        switch(current.getText()) {
            case "Form Mapping":
                System.out.println("Form Mapping selected");
                Node mapping = null;
                try {
                    FXMLLoader docLoader = new FXMLLoader(getClass().getResource("formMapping.fxml"));
                    mapping = docLoader.load();
                    formMappingController docController = docLoader.getController();
                    docController.initModel(model);
                } catch (IOException e) {
//                    e.printStackTrace();
                    Logger logger = LogManager.getLogger("default");
                    logger.debug("Caught exception");
                    logger.debug(e.getStackTrace());
                }
                mainWindow.getChildren().setAll(mapping);
                break;

            case "Form Generation":
                System.out.println("Form Generation selected");
                Node generation = null;
                try {
                    FXMLLoader docLoader = new FXMLLoader(getClass().getResource("formGeneration.fxml"));
                    generation = docLoader.load();
                    FormGenerationController docController = docLoader.getController();
                    docController.initModel(model);
                } catch (IOException e) {
//                    e.printStackTrace();
                    Logger logger = LogManager.getLogger("default");
                    logger.debug("Caught exception");
                    logger.debug(e.getStackTrace());
                }
                mainWindow.getChildren().setAll(generation);
                break;
            default:
                //do nothing
        }
    }

    @FXML
    public void setMainWindow(Node node) {
        mainWindow.getChildren().setAll(node);
    }
}


