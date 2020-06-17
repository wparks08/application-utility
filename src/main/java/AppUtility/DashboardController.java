package AppUtility;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
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
    }

    public void handleBtnClickAction(ActionEvent event) {
        Button current = (Button)event.getSource();
        btnMapping.setStyle("-fx-background-color: #424242;");
        btnGenerating.setStyle("-fx-background-color: #424242;");

        current.setStyle("-fx-background-color: #111111;");

        switch(current.getText()) {
            case "Form Mapping":
                System.out.println("Form Mapping selected");
                Node mapping = null;
                try {
                    FXMLLoader docLoader = new FXMLLoader(getClass().getResource("/formMapping.fxml"));
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
                    FXMLLoader docLoader = new FXMLLoader(getClass().getResource("/formGeneration.fxml"));
                    generation = docLoader.load();
                    FormGenerationController docController = docLoader.getController();
                    docController.initModel(model);
                    VBox.setVgrow(generation, Priority.ALWAYS);
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


