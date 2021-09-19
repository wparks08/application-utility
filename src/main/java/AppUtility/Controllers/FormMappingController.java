package AppUtility.Controllers;

import AppUtility.DataModel;
import AppUtility.Config.Dependencies;
import AppUtility.ui.controls.AddCarrierDialog;
import AppUtility.ui.controls.CarriersListView;
import AppUtility.ui.controls.FormsListView;
//import AppUtility.db.*;
import AppUtility.Domains.Carrier;
import AppUtility.Domains.Form.Form;
import AppUtility.Interfaces.CarrierDatabase;
import AppUtility.Interfaces.FormDatabase;
import com.jfoenix.controls.JFXButton;
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

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public class FormMappingController {
    @FXML FormsListView formsListView;
    @FXML CarriersListView carriersListView;

    @FXML public AnchorPane wrapper;

    @FXML JFXButton btnNew;
    @FXML JFXButton btnEditForm;
    @FXML JFXButton btnEditMapping;
    @FXML JFXButton btnDelete;
    @FXML JFXButton btnAddCarrier;
    @FXML JFXButton btnEditCarrier;
    @FXML JFXButton btnDeleteCarrier;

    public ListChangeListener<Form> formViewChangeListener = change -> {
        if (formsListView.hasSelectedItem()) {
            btnEditForm.setDisable(true);
            btnEditMapping.setDisable(true);
            btnDelete.setDisable(true);
        } else {
            btnEditForm.setDisable(false);
            btnEditMapping.setDisable(false);
            btnDelete.setDisable(false);
            DataModel.setSelectedForm(formsListView.getSelectedForm());
        }
    };

    public ListChangeListener<Carrier> carrierViewChangeListener = change -> {
        formsListView.clearList();
        Carrier selectedCarrier = carriersListView.getSelectedCarrier();
        if (selectedCarrier != null) {
            Collection<Form> allFormsCollection = DataModel.getForms();
            DataModel.refreshForms(selectedCarrier);
            formsListView.addAllFromCollection(allFormsCollection);
            btnNew.setDisable(false);
        } else {
            btnNew.setDisable(true);
        }
    };

    InvalidationListener carrierListSelectionInvalidationListener = observable -> {
        if (carriersListView.hasSelectedItem()) {
            btnEditCarrier.setDisable(false);
            btnDeleteCarrier.setDisable(false);
            DataModel.setSelectedCarrier(carriersListView.getSelectedCarrier());
        }
    };

    @FXML
    public void initialize() {
        DataModel.refreshCarriers();
        carriersListView.clearList();
        carriersListView.addAllFromCollection(DataModel.getCarriers());

        disableAllButtons();

        formsListView.addChangeListener(formViewChangeListener);
        carriersListView.addChangeListener(carrierViewChangeListener);
        carriersListView.addInvalidationListener(carrierListSelectionInvalidationListener);
    }

    public void disableAllButtons() {
        btnNew.setDisable(true);
        btnEditForm.setDisable(true);
        btnEditMapping.setDisable(true);
        btnDelete.setDisable(true);
        btnEditCarrier.setDisable(true);
        btnDeleteCarrier.setDisable(true);
    }

    @FXML
    public void addCarrier() {
        AddCarrierDialog addCarrierDialog = new AddCarrierDialog();

        Optional<String> result = addCarrierDialog.showAndWait();
        result.ifPresent( carrierName -> {
            if (!carrierName.isEmpty()) {
                Carrier carrier = new Carrier(carrierName);
                CarrierDatabase carrierDatabase = Dependencies.databaseServices.getCarrierDatabase();
                try {
                    carrierDatabase.addCarrier(carrier);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                carriersListView.add(carrier);
                DataModel.refreshCarriers();
            }
        });
    }

    @FXML
    public void editCarrier() {
        Carrier selected = carriersListView.getSelectedCarrier();
        TextInputDialog dialog = new TextInputDialog(selected.getName());
        dialog.setTitle("Edit Carrier");
        dialog.setHeaderText("Edit Carrier");
        dialog.setContentText("Carrier name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent( carrierName -> {
            if (!carrierName.isEmpty() && !carrierName.equals(selected.getName())) {
                CarrierDatabase carrierDatabase = Dependencies.databaseServices.getCarrierDatabase();
                Carrier updatedCarrier = new Carrier(carrierName);
//                try {
//                    carrierDatabase.updateCarrier(selected.getId(), updatedCarrier);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                carriersListView.refresh();
                DataModel.refreshCarriers();
            }
        });
    }

    @FXML
    public void deleteCarrier() {
        Carrier selected = carriersListView.getSelectedCarrier();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText("Delete Carrier");
        alert.setContentText("This carrier, and all Forms associated with it, will be PERMANENTLY deleted. Do you wish to proceed?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            CarrierDatabase carrierDatabase = Dependencies.databaseServices.getCarrierDatabase();
//            try {
//                carrierDatabase.deleteCarrier(selected.getId());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            List<Form> formList = (List<Form>) selected.getChildren(Form.class);
//            for (Form form : formList) {
//                List<FormField> formFieldList = (List<FormField>) form.getChildren(FormField.class);
//                for (FormField formField : formFieldList) {
//                    formField.delete();
//                }
//
//                List<FormProperty> formPropertyList = (List<FormProperty>) form.getChildren(FormProperty.class);
//                for (FormProperty formProperty : formPropertyList) {
//                    formProperty.delete();
//                }
//
//                List<CensusHeader> censusHeaderList = (List<CensusHeader>) form.getChildren(CensusHeader.class);
//                for (CensusHeader censusHeader : censusHeaderList) {
//                    censusHeader.delete();
//                }
//
//                List<Mapping> mappingList = (List<Mapping>) form.getChildren(Mapping.class);
//                for (Mapping mapping : mappingList) {
//                    mapping.delete();
//                }
//
//                form.delete();
//            }
//            selected.delete();
            carriersListView.remove(selected);
        }
    }

    @FXML
    public void handleBtnNewClick(ActionEvent e) {
        FXMLLoader newFormLoader = new FXMLLoader(getClass().getResource("/newForm.fxml"));
        Stage stage = prepStage(newFormLoader);

        NewFormController newFormController = newFormLoader.getController();

        stage.setTitle("New Form");

        stage.showAndWait();

        DataModel.refreshCarriers();
        carriersListView.refresh();
        DataModel.refreshForms(carriersListView.getSelectedCarrier());
        formsListView.refresh();
    }

    @FXML
    public void handleBtnEditFormClick(ActionEvent e) {
        FXMLLoader editFormLoader = new FXMLLoader(getClass().getResource("/editForm.fxml"));
        Stage stage = prepStage(editFormLoader);

        EditFormController editFormController = editFormLoader.getController();
//        editFormController.initModel(DataModel);

        stage.setTitle("Edit Form");

        stage.showAndWait();
    }

    @FXML
    public void handleBtnDeleteFormClick(ActionEvent e) {
        Form form = DataModel.getSelectedForm();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure?");
        alert.setHeaderText("Delete Form");
        alert.setContentText("This form, and all Mappings associated with it, will be PERMANENTLY deleted. Do you wish to proceed?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            FormDatabase formDatabase = Dependencies.databaseServices.getFormDatabase();
//            try {
//                formDatabase.deleteForm(form.getId());
//            } catch (NotFoundException notFoundException) {
//                notFoundException.printStackTrace();
//            }
//                List<FormField> formFieldList = (List<FormField>) form.getChildren(FormField.class);
//                for (FormField formField : formFieldList) {
//                    formField.delete();
//                }
//
//                List<FormProperty> formPropertyList = (List<FormProperty>) form.getChildren(FormProperty.class);
//                for (FormProperty formProperty : formPropertyList) {
//                    formProperty.delete();
//                }
//
//                List<CensusHeader> censusHeaderList = (List<CensusHeader>) form.getChildren(CensusHeader.class);
//                for (CensusHeader censusHeader : censusHeaderList) {
//                    censusHeader.delete();
//                }
//
//                List<Mapping> mappingList = (List<Mapping>) form.getChildren(Mapping.class);
//                for (Mapping mapping : mappingList) {
//                    mapping.delete();
//                }
//
//                form.delete();
        }
    }

    @FXML
    public void handleBtnEditMappingClick(ActionEvent e) {
        FXMLLoader editFormLoader = new FXMLLoader((getClass().getResource("/editMapping.fxml")));
        Stage stage = prepStage(editFormLoader);

        EditMappingController editMappingController = editFormLoader.getController();

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
