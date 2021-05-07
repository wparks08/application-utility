package AppUtility.controls;

import AppUtility.db.Form;
import com.jfoenix.controls.JFXListView;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.util.Collection;

public class FormsListView {
    @FXML JFXListView<Form> listViewForms = new JFXListView<>();

    public FormsListView() {}

    public Form getSelectedForm() {
        return listViewForms.getSelectionModel().getSelectedItem();
    }

    public ObservableList<Form> getSelectedFormsList() {
        return listViewForms.getSelectionModel().getSelectedItems();
    }

    public void addChangeListener(ListChangeListener<Form> listChangeListener) {
        getSelectedFormsList().addListener(listChangeListener);
    }

    public JFXListView<Form> getJFXElement() {
        return listViewForms;
    }

    public Boolean hasSelectedItem() {
        return getSelectedFormsList().isEmpty();
    }

    public void clearList() {
        listViewForms.getItems().clear();
    }

    public void addAllFromCollection(Collection<Form> collection) {
        if (collection == null) {
            return;
        }
        listViewForms.getItems().addAll(collection);
    }

    public void refresh() {
        listViewForms.refresh();
    }
}
