package AppUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Employee {
    private HashMap<String,String> info = new HashMap<>();
    private List<Dependent> dependents = new ArrayList<>();

    public void addInfo(String key, String value) {
        info.put(key, value);
    }

    public String getInfo(String key) {
        return info.get(key);
    }

    public HashMap<String, String> getInfo() {
        return info;
    }

    public void addDependent(Dependent dependent) {
        dependents.add(dependent);
    }

    public List<Dependent> getDependents() {
        return dependents;
    }
}
