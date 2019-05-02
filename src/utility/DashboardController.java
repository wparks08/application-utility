package utility;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import utility.db.Carrier;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DashboardController {
    @FXML private Label lblStatus = new Label();
    @FXML private Button btnMapping;
    @FXML private Button btnGenerating;
    @FXML private AnchorPane mainWindow;

    @FXML
    public void initialize() {
        btnMapping.setText("Form Mapping");
        btnGenerating.setText("Form Generation");

        Database db = new Database();
        db.connect();

//        Carrier carrier = new Carrier();
//        try {
//            carrier = carrier.get(1);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(carrier.toString());
//        try {
//            List<Carrier> carrierList = carrier.list();
//            System.out.println("Carrier List Size: " + carrierList.size());
//            for (Carrier carrier1 : carrierList) {
//                System.out.println(carrier1.toString());
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        Carrier carrier2 = new Carrier();
        carrier2.save();
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


