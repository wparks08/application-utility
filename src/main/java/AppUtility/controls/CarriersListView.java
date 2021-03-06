package AppUtility.controls;

import AppUtility.db.Carrier;
import com.jfoenix.controls.JFXListView;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Collection;

public class CarriersListView extends JFXListView<Carrier> {

    public CarriersListView() {
    }

    public Carrier getSelectedCarrier() {
        return this.getSelectionModel().getSelectedItem();
    }

    public ObservableList<Carrier> getSelectedCarriersAsObservableList() {
        return this.getSelectionModel().getSelectedItems();
    }

    public void addChangeListener(ListChangeListener<Carrier> listChangeListener) {
        getSelectedCarriersAsObservableList().addListener(listChangeListener);
    }

    public void addInvalidationListener(InvalidationListener invalidationListener) {
        getSelectedCarriersAsObservableList().addListener(invalidationListener);
    }

    public Boolean hasSelectedItem() {
        return !getSelectedCarriersAsObservableList().isEmpty();
    }

    public void clearList() {
        this.getItems().clear();
    }

    public void add(Carrier carrier) {
        if (carrier == null) {
            return;
        }
        this.getItems().add(carrier);
    }

    public void addAllFromCollection(Collection<Carrier> collection) {
        if (collection == null) {
            return;
        }
        this.getItems().addAll(collection);
    }

    public void remove(Carrier carrier) {
        if (carrier == null) {
            return;
        }
        this.getItems().remove(carrier);
    }
}
