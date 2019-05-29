package AppUtility;

import AppUtility.db.CensusHeader;
import AppUtility.db.Form;
import AppUtility.db.FormField;
import AppUtility.db.Mapping;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXScrollPane;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditMappingController {
    @FXML private HBox wrapper;
    @FXML private VBox mapping;
    @FXML private VBox settings;
    @FXML private JFXListView<FormField> formFieldListView;

    private DataModel model;
    private Form form;
    private List<FormField> formFields = new ArrayList<>();
    private List<CensusHeader> censusHeaders = new ArrayList<>();
    private HashMap<String, String> mappings = new HashMap<>();

    @FXML
    public void initialize() {
        formFieldListView = new JFXListView<>();
    }

    @FXML
    public void initModel(DataModel model) {
        this.model = model;
        this.form = model.getSelectedForm();
        this.formFields = form.getFormFields();
        this.censusHeaders = form.getCensusHeaders();
        this.mappings = form.getMappingsAsMap();

        setupScene();
    }

    private void setupScene() {
        formFieldListView.getItems().addAll(formFields);

        mapping.getChildren().add(createContainer("list-view-container", formFieldListView));

        formFieldListView.getSelectionModel().getSelectedItems().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                populateSettingsPane(formFieldListView.getSelectionModel().getSelectedItem());
            }
        });

        JFXComboBox<CensusHeader> comboBox = new JFXComboBox<>();
        comboBox.getItems().addAll(censusHeaders);
        comboBox.setPromptText("Census Header Selection");
        comboBox.setDisable(true);
        settings.getChildren().clear();
        settings.getChildren().add(createContainer(comboBox));
    }

    private void populateSettingsPane(FormField selectedItem) {
        JFXComboBox<CensusHeader> comboBox = new JFXComboBox<>();
        comboBox.getItems().addAll(censusHeaders);
        if (mappings.containsKey(selectedItem.getFieldName())) {
            String mappedHeaderString = mappings.get(selectedItem.getFieldName());
            CensusHeader mappedHeader = null;
            for (CensusHeader censusHeader : censusHeaders) {
                if (censusHeader.getHeader().equals(mappedHeaderString)) {
                    mappedHeader = censusHeader;
                    break;
                }
            }
            comboBox.getSelectionModel().select(mappedHeader);
        }

        comboBox.setPromptText("Census Header Selection");
        System.out.println(comboBox.getStyleClass());

        //Add items to pane
        settings.getChildren().clear();
        settings.getChildren().add(createContainer(comboBox));

        JFXButton saveButton = new JFXButton("Save");
        saveButton.setButtonType(JFXButton.ButtonType.RAISED);
        saveButton.getStyleClass().add("btn-success");
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Mapping mapping = null;
                mapping = new Mapping().findByFormFieldId(formFieldListView.getSelectionModel().getSelectedItem().getId());
                if (mapping != null) {
                    mapping.setCensusHeaderId(comboBox.getSelectionModel().getSelectedItem().getId());
                    mapping.save();
                    Snackbar.show(wrapper, "Mapping updated.");
                } else {
                    mapping = new Mapping();
                    mapping.setFormId(form.getId());
                    mapping.setCensusHeaderId(comboBox.getSelectionModel().getSelectedItem().getId());
                    mapping.setFormFieldId(formFieldListView.getSelectionModel().getSelectedItem().getId());
                    mapping.save();
                    Snackbar.show(wrapper, "Mapping saved.");
                }

                refreshMappings();
            }
        });

        settings.getChildren().add(createContainer(saveButton));
    }

    private void refreshMappings() {
        this.mappings = form.getMappingsAsMap();
    }

    private StackPane createContainer(Node... children) {
        StackPane stackPane = new StackPane(children);
        stackPane.setPadding(new Insets(20));

        return stackPane;
    }

    private StackPane createContainer(String styleClass, Node... children) {
        StackPane stackPane = new StackPane(children);
        stackPane.getStyleClass().add(styleClass);
        stackPane.setPadding(new Insets(20));

        return stackPane;
    }
}
