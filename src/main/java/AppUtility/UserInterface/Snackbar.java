package AppUtility.UserInterface;

import com.jfoenix.controls.JFXSnackbar;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public final class Snackbar {

    private Snackbar() {
        //Utility class. Cannot be instantiated.
    }

    public static void show(Pane container, String message) {
        Node node = new Label(message);
        node.getStyleClass().add("jfx-snackbar-toast");
        JFXSnackbar bar = new JFXSnackbar(container);
        node.setStyle("-fx-wrap-text: true");
        bar.enqueue(new JFXSnackbar.SnackbarEvent(node));
    }
}
