package utility;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import utility.db.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DashboardController {
    @FXML private Label lblStatus = new Label();
    @FXML private Button btnMapping;
    @FXML private Button btnGenerating;
    @FXML private VBox mainWindow;

    @FXML
    public void initialize() {
        btnMapping.setText("Form Mapping");
        btnGenerating.setText("Form Generation");

//        Form form = new Form().get(2);
//        System.out.println("Got it");
//
//        try (FileOutputStream fos = new FileOutputStream("./SampleData/sample form.pdf")) {
//            fos.write(form.getFormBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        Carrier carrier = new Carrier().get(1);
//
//        Form form = new Form();
//        form.setName("Cal Choice Enrollment Form Q1 2019");
//        form.setCarrierId(1);
//        form.loadFormFile(new File("C:\\JavaFXProjects\\ApplicationUtility\\SampleData\\Cal Choice Enrollment form 1-1-19.pdf"));
//        form.save();
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


