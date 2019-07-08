package AppUtility;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import AppUtility.db.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class formMappingController {
    public DataModel model;
    @FXML public AnchorPane wrapper;

    @FXML JFXListView<Form> listViewForms = new JFXListView<>();
    @FXML JFXListView<Carrier> listViewCarriers = new JFXListView<>();
    @FXML JFXButton btnNew;
    @FXML JFXButton btnEditForm;
    @FXML JFXButton btnEditMapping;
    @FXML JFXButton btnDelete;
    @FXML JFXButton btnAddCarrier;
    @FXML JFXButton btnEditCarrier;
    @FXML JFXButton btnDeleteCarrier;

    public void initModel(DataModel model) {
        this.model = model;

        model.refreshCarriers();
        listViewCarriers.getItems().addAll(model.getCarriers());
    }

    @FXML
    public void initialize() {

        btnNew.setDisable(true);
        btnEditForm.setDisable(true);
        btnEditMapping.setDisable(true);
        btnDelete.setDisable(true);
        btnEditCarrier.setDisable(true);
        btnDeleteCarrier.setDisable(true);

        listViewForms.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Form>) c -> {
            if (listViewForms.getSelectionModel().getSelectedItems().isEmpty()) {
                btnEditForm.setDisable(true);
                btnEditMapping.setDisable(true);
                btnDelete.setDisable(true);
            } else {
                btnEditForm.setDisable(false);
                btnEditMapping.setDisable(false);
                btnDelete.setDisable(false);
                model.setSelectedForm(listViewForms.getSelectionModel().getSelectedItem());
            }
        });

        listViewCarriers.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Carrier>) c -> {
            listViewForms.getItems().clear();
            Carrier selectedCarrier = listViewCarriers.getSelectionModel().getSelectedItem();
            if (selectedCarrier != null) {
                model.refreshForms(selectedCarrier);
                listViewForms.getItems().addAll(model.getForms());
                btnNew.setDisable(false);
            } else {
                btnNew.setDisable(true);
            }
        });

        listViewCarriers.getSelectionModel().getSelectedItems().addListener((InvalidationListener) observable -> {
            if (!listViewCarriers.getSelectionModel().getSelectedItems().isEmpty()) {
                btnEditCarrier.setDisable(false);
                btnDeleteCarrier.setDisable(false);
                model.setSelectedCarrier(listViewCarriers.getSelectionModel().getSelectedItem());
            }
        });
    }

    @FXML
    public void addCarrier() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add a New Carrier");
        dialog.setHeaderText("Add a New Carrier");
        dialog.setContentText("Carrier name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent( carrierName -> {
            if (!carrierName.isEmpty()) {
                Carrier carrier = new Carrier(carrierName);
                carrier.save();
                listViewCarriers.getItems().add(carrier);
                model.refreshCarriers();
            }
        });
    }

    @FXML
    public void editCarrier() {
        Carrier selected = listViewCarriers.getSelectionModel().getSelectedItem();
        TextInputDialog dialog = new TextInputDialog(selected.getName());
        dialog.setTitle("Edit Carrier");
        dialog.setHeaderText("Edit Carrier");
        dialog.setContentText("Carrier name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent( carrierName -> {
            if (!carrierName.isEmpty() && !carrierName.equals(selected.getName())) {
                selected.setName(carrierName);
                selected.save();
                listViewCarriers.refresh();
                model.refreshCarriers();
            }
        });
    }

    @FXML
    public void deleteCarrier() {
        Carrier selected = listViewCarriers.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText("Delete Carrier");
        alert.setContentText("This carrier, and all Forms associated with it, will be PERMANENTLY deleted. Do you wish to proceed?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            List<Form> formList = (List<Form>) selected.getChildren(Form.class);
            for (Form form : formList) {
                List<FormField> formFieldList = (List<FormField>) form.getChildren(FormField.class);
                for (FormField formField : formFieldList) {
                    formField.delete();
                }

                List<FormProperty> formPropertyList = (List<FormProperty>) form.getChildren(FormProperty.class);
                for (FormProperty formProperty : formPropertyList) {
                    formProperty.delete();
                }

                List<CensusHeader> censusHeaderList = (List<CensusHeader>) form.getChildren(CensusHeader.class);
                for (CensusHeader censusHeader : censusHeaderList) {
                    censusHeader.delete();
                }

                List<Mapping> mappingList = (List<Mapping>) form.getChildren(Mapping.class);
                for (Mapping mapping : mappingList) {
                    mapping.delete();
                }

                form.delete();
            }
            selected.delete();
            listViewCarriers.getItems().remove(selected);
        }
    }

    @FXML
    public void handleBtnNewClick(ActionEvent e) {
        FXMLLoader newFormLoader = new FXMLLoader(getClass().getResource("newForm.fxml"));
        Stage stage = prepStage(newFormLoader);

        NewFormController newFormController = newFormLoader.getController();
        newFormController.initModel(model);

        stage.setTitle("New Form");

        stage.showAndWait();

        model.refreshCarriers();
        listViewCarriers.refresh();
        model.refreshForms(listViewCarriers.getSelectionModel().getSelectedItem());
        listViewForms.refresh();
    }

    @FXML
    public void handleBtnEditFormClick(ActionEvent e) {
        FXMLLoader editFormLoader = new FXMLLoader(getClass().getResource("editForm.fxml"));
        Stage stage = prepStage(editFormLoader);

        EditFormController editFormController = editFormLoader.getController();
        editFormController.initModel(model);

        stage.setTitle("Edit Form");

        stage.showAndWait();
    }

    @FXML
    public void handleBtnEditMappingClick(ActionEvent e) {
        FXMLLoader editFormLoader = new FXMLLoader((getClass().getResource("editMapping.fxml")));
        Stage stage = prepStage(editFormLoader);

        EditMappingController editMappingController = editFormLoader.getController();
        editMappingController.initModel(model);

        stage.setTitle("Edit Mapping");

        stage.showAndWait();
    }

    private Stage prepStage(FXMLLoader newFormLoader) {
        Parent root = null;
        try {
            root = newFormLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);

        return stage;
    }
}
