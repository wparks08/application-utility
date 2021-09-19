package AppUtility.ui.controls;

import AppUtility.Domains.Form.Form;
import com.jfoenix.controls.JFXListView;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Collection;

public class FormsListView extends JFXListView<Form> {

    public FormsListView() {}

    public Form getSelectedForm() {
        return this.getSelectionModel().getSelectedItem();
    }

    public ObservableList<Form> getSelectedFormsAsObservableList() {
        return this.getSelectionModel().getSelectedItems();
    }

    public void addChangeListener(ListChangeListener<Form> listChangeListener) {
        getSelectedFormsAsObservableList().addListener(listChangeListener);
    }

    public Boolean hasSelectedItem() {
        return !getSelectedFormsAsObservableList().isEmpty();
    }

    public void clearList() {
        this.getItems().clear();
    }

    public void addAllFromCollection(Collection<Form> collection) {
        if (collection == null) {
            return;
        }
        this.getItems().addAll(collection);
    }
}
