package AppUtility.Domains.DataEntry;

import AppUtility.Domains.DataKey.DataKey;
import AppUtility.Domains.DataValue.DataValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataEntry {
    Map<DataKey, DataValue> entryMap;

    public DataEntry() {
        this.entryMap = new HashMap<>();
    }

    public void add(DataKey key, DataValue value) {
        this.entryMap.put(key, value);
    }

    public DataValue get(DataKey dataKey) {
        return this.entryMap.get(dataKey);
    }

    public Set<DataKey> keySet() {
        return this.entryMap.keySet();
    }
}
