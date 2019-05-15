package utility;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import utility.db.*;

import java.util.List;
import java.util.Optional;

public class formMappingController {
    public DataModel model;

    @FXML JFXListView<Form> listViewForms = new JFXListView<>();
    @FXML JFXListView<Carrier> listViewCarriers = new JFXListView<>();
    @FXML JFXButton btnNew;
    @FXML JFXButton btnEdit;
    @FXML JFXButton btnDelete;
    @FXML JFXButton btnAddCarrier;
    @FXML JFXButton btnEditCarrier;

    public void initModel(DataModel model) {
        this.model = model;

        model.refreshCarriers();
        listViewCarriers.getItems().addAll(model.getCarriers());
    }

    @FXML
    public void initialize() {

        btnEdit.setDisable(true);
        btnDelete.setDisable(true);
        btnEditCarrier.setDisable(true);

        listViewForms.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Form>) c -> {
            if (listViewForms.getSelectionModel().getSelectedItems().isEmpty()) {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            } else {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            }
        });

        listViewCarriers.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Carrier>) c -> {
            listViewForms.getItems().clear();
            Carrier selectedCarrier = listViewCarriers.getSelectionModel().getSelectedItem();
            model.refreshForms(selectedCarrier);
            listViewForms.getItems().addAll(model.getForms());
        });

        listViewCarriers.getSelectionModel().getSelectedItems().addListener((InvalidationListener) observable -> {
            if (!listViewCarriers.getSelectionModel().getSelectedItems().isEmpty()) {
                btnEditCarrier.setDisable(false);
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
}
