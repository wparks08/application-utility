package AppUtility.collections;

import AppUtility.domains.datakey.DataKey;

import java.util.*;

class DataKeyCollection implements Collection<DataKey> {
    private final List<DataKey> dataKeys;

    public DataKeyCollection() {
        this.dataKeys = new ArrayList<>();
    }

    @Override
    public void add(DataKey dataKey) {
        this.dataKeys.add(dataKey);
    }

    @Override
    public void addAll(java.util.Collection<? extends DataKey> dataKeys) {
        this.dataKeys.addAll(dataKeys);
    }

    @Override
    public void addAll(Collection<DataKey> dataKeyCollection) {
        DataKey[] dataKeys = dataKeyCollection.toArray();
        this.dataKeys.addAll(Arrays.asList(dataKeys));
    }

    @Override
    public void clear() {
        this.dataKeys.clear();
    }

    @Override
    public DataKey[] toArray() {
        DataKey[] dataKeys = new DataKey[this.dataKeys.size()];
        return this.dataKeys.toArray(dataKeys);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        this.dataKeys.forEach(dataKey -> stringBuilder.append(dataKey.toString()).append(", "));
        return stringBuilder.toString();
    }
}
