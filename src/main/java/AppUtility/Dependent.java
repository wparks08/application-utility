package AppUtility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.HashMap;

public class Dependent {
    private ObservableMap<String,String> info = FXCollections.observableHashMap();

    public Dependent(Employee employee) {
        info.putAll(employee.getInfo());
    }

    public void addInfo(String key, String value) {
        info.put(key, value);
    }

    public String getInfo(String key) {
        return info.get(key);
    }
}
