package AppUtility.controllers.dashboard;

import AppUtility.exception.NotSupportedException;
import AppUtility.client.ui.Snackbar;
import AppUtility.client.ui.Styles;
import AppUtility.client.ui.controls.menu.MenuButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class DashboardController {

    @FXML private MenuButton btnMapping;
    @FXML private MenuButton btnGenerating;
    @FXML private Pane mainWindow;

    @FXML
    public void initialize() {
//        btnMapping.setText(Config.MAPPING_MENU_BUTTON_TEXT);
//        btnGenerating.setText(Config.GENERATION_MENU_BUTTON_TEXT);
    }

    public void handleBtnClickAction(ActionEvent event) throws NotSupportedException {
        Button selected = (Button)event.getSource();
        btnMapping.setStyle(Styles.ButtonDeselected);
        btnGenerating.setStyle(Styles.ButtonDeselected);

        selected.setStyle(Styles.ButtonSelected);

        switch (selected.getText()) {
            case Config.MAPPING_MENU_BUTTON_TEXT -> setMainWindow(getNode(Config.FORM_MAPPING_FXML_PATH));
            case Config.GENERATION_MENU_BUTTON_TEXT -> setMainWindow(getNode(Config.FORM_GENERATION_FXML_PATH));
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
            e.printStackTrace();
        }

        return null;
    }

    private FXMLLoader getLoader(String name) {
        return new FXMLLoader(getClass().getResource(name));
    }
}


