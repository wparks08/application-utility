package utility;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import utility.db.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DashboardController {
    private DataModel dataModel;

    @FXML private Label lblStatus = new Label();
    @FXML private Button btnMapping;
    @FXML private Button btnGenerating;
    @FXML private VBox mainWindow;

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

        Application app = new Application(new File("SampleData/Cal Choice Enrollment form 4-1-19.pdf"));
        Form form = new Form();
        form.loadFormFile(app.getPdfFile());
        form.setName("Cal Choice Q2 2019");
        form.save();
        System.out.println(form.getId());

        Census census = new Census(new File("C:\\JavaFXProjects\\ApplicationUtility\\SampleData\\fullsamplewithdependents.csv"));
        String[] headers = census.getHeaders();

        for (String header : headers) {
            CensusHeader censusHeader = new CensusHeader(header, form.getId());
            censusHeader.save();
        }

        List<PDField> pdFields = app.getPDFields();

        for (PDField pdField : pdFields) {
            FormField formField = new FormField(pdField.getPartialName(), form.getId());
            formField.save();
        }

        List<CensusHeader> censusHeaders = form.getCensusHeaders();
        List<FormField> formFields = form.getFormFields();

        System.out.println(censusHeaders.size() + " Census Headers");
        System.out.println(formFields.size() + " Form Fields");

        for (int i = 0; i < formFields.size(); i++) {
            Mapping mapping = new Mapping(censusHeaders.get(i).getId(), formFields.get(i).getId(), form.getId());
            mapping.save();
        }
    }

//    @FXML
//    public void handleBtnMappingClickAction(ActionEvent e) {
//
//    }

    public void handleBtnMappingClickAction(ActionEvent event) {
        btnMapping.setStyle("-fx-background-color: #111111");
    }

    public void handleBtnClickAction(ActionEvent event) {
        Button current = (Button)event.getSource();
        btnMapping.setStyle("-fx-background-color: #424242");
        btnGenerating.setStyle("-fx-background-color: #424242");

        current.setStyle("-fx-background-color: #111111");

        switch(current.getText()) {
            case "Form Mapping":
                System.out.println("Form Mapping selected");
                Node node = null;
                try {
                    node = FXMLLoader.load(getClass().getResource("formMapping.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mainWindow.getChildren().setAll(node);
                break;
            case "Form Generation":
                System.out.println("Form Generation selected");
                break;
            default:
                //do nothing
        }
    }
}


