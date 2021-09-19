package AppUtility.Controllers;

import AppUtility.Exception.NotSupportedException;
import AppUtility.UserInterface.Snackbar;
import AppUtility.UserInterface.Styles;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class DashboardController {

    @FXML private Button btnMapping;
    @FXML private Button btnGenerating;
    @FXML private Pane mainWindow;

    @FXML
    public void initialize() {
        btnMapping.setText("Form Mapping");
        btnGenerating.setText("Form Generation");
    }

    public void handleBtnClickAction(ActionEvent event) throws NotSupportedException {
        Button current = (Button)event.getSource();
        btnMapping.setStyle(Styles.ButtonDeselected);
        btnGenerating.setStyle(Styles.ButtonDeselected);

        current.setStyle(Styles.ButtonSelected);

        switch (current.getText()) {
            case "Form Mapping" -> {
                final String FORM_MAPPING = "/formMapping.fxml";
                setMainWindow(getNode(FORM_MAPPING));
            }
            case "Form Generation" -> {
                final String FORM_GENERATION = "/formGeneration.fxml";
                setMainWindow(getNode(FORM_GENERATION));
            }
            default -> throw new NotSupportedException("Invalid selection.");
        }
    }

    @FXML
    public void setMainWindow(Node node) {
        mainWindow.getChildren().setAll(node);
    }

    private Node getNode(String name) {
        try {
            return getLoader(name).load();
        } catch (IOException|IllegalStateException e) {
            Snackbar.show(mainWindow, "Error: Could not load node " + name);
        }

        return null;
    }

    private FXMLLoader getLoader(String name) {
        return new FXMLLoader(getClass().getResource(name));
    }
}


