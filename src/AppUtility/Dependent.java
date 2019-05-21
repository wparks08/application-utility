package AppUtility;

import java.util.HashMap;

public class Dependent {
    private HashMap<String,String> info = new HashMap<>();

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
