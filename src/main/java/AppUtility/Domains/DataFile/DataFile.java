package AppUtility.Domains.DataFile;

import AppUtility.Domains.DataEntry.DataEntry;
import AppUtility.Domains.DataKey.DataKey;
import AppUtility.Domains.DataKey.DataKeyCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DataFile {
    private List<DataEntry> dataEntries;

    public DataFile() {
        this.dataEntries = new ArrayList<>();
    }

    public DataKeyCollection getDataKeys() {
        DataKeyCollection dataKeyCollection = new DataKeyCollection();

        dataEntries.forEach(dataKeyDataValueMap -> {
            Set<DataKey> dataKeys = dataKeyDataValueMap.keySet();
            dataKeyCollection.addAll(dataKeys);
            dataKeyCollection.removeDuplicates();
        });

        return dataKeyCollection;
    }

    public void addDataEntry(DataEntry dataEntry) {
        this.dataEntries.add(dataEntry);
    }
}
