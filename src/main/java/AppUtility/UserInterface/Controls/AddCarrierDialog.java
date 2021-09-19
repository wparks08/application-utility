package AppUtility.UserInterface.Controls;

import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class AddCarrierDialog {
    final TextInputDialog dialog = new TextInputDialog();
    final String title = "Add a New Carrier";
    final String headerText = title;
    final String contentText = "Carrier Name:";

    public AddCarrierDialog() {
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
    }

    public Optional<String> showAndWait() {
        return dialog.showAndWait();
    }
}
