package AppUtility.Domains.DataKey;

import java.util.*;

public class DataKeyCollection {
    private List<DataKey> dataKeys;

    public DataKeyCollection() {
        this.dataKeys = new ArrayList<>();
    }

    public DataKeyCollection(List<DataKey> dataKeys) {
        this.dataKeys = new ArrayList<>();
        this.dataKeys.addAll(dataKeys);
    }

    public void add(DataKey dataKey) {
        this.dataKeys.add(dataKey);
    }

    public void addAll(Collection<? extends DataKey> dataKeys) {
        this.dataKeys.addAll(dataKeys);
    }

    public void addAll(DataKeyCollection dataKeyCollection) {
        this.dataKeys.addAll(dataKeyCollection.toList());
    }

    public void removeDuplicates() {
        Set<DataKey> dataKeySet = this.toSet();
        this.dataKeys = new ArrayList<>(dataKeySet);
    }

    public DataKey[] toArray() {
        DataKey[] dataKeys = new DataKey[this.dataKeys.size()];
        return this.dataKeys.toArray(dataKeys);
    }

    public Set<DataKey> toSet() {
        return new HashSet<>(this.dataKeys);
    }

    public List<DataKey> toList() {
        return this.dataKeys;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        this.dataKeys.forEach(dataKey -> stringBuilder.append(dataKey.toString()).append(", "));
        return stringBuilder.toString();
    }
}
