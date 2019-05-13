package utility;

import com.jfoenix.controls.JFXListView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import utility.db.Form;

import java.util.List;

public class formMappingController {

    ObservableList<Form> forms = FXCollections.observableArrayList();
    @FXML
    JFXListView<Form> listViewForms = new JFXListView<>();

    @FXML
    public void initialize() {
        listViewForms.getStyleClass().add("mylistview");
        //Don't keep this. Do it in a model.
        List<Form> formList = new Form().list();
        forms.setAll(formList);
        listViewForms.setItems(forms);
    }
}
