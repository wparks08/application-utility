package AppUtility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Employee {
    private ObservableMap<String, String> info = FXCollections.observableHashMap();
    private ObservableList<Dependent> dependents = FXCollections.observableArrayList();

    public void addInfo(String key, String value) {
        info.put(key, value);
    }

    public String getInfo(String key) {
        return info.get(key);
    }

    public ObservableMap<String, String> getInfo() {
        return info;
    }

    public void addDependent(Dependent dependent) {
        dependents.add(dependent);
    }

    public List<Dependent> getDependents() {
        return dependents;
    }
}
