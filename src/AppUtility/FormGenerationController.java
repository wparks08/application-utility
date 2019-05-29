package AppUtility;

import javafx.fxml.FXML;

public class FormGenerationController {
    private DataModel model;

    @FXML
    public void initialize() {
        System.out.println("Form Generation selected");
    }

    @FXML
    public void initModel(DataModel model) {
        this.model = model;
    }
}
