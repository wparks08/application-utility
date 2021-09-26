package AppUtility.usecases.datafile;

import AppUtility.collections.Collection;
import AppUtility.collections.CollectionFactory;
import AppUtility.domains.datakey.DataKey;
import AppUtility.usecases.dataentry.DataEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DataFile {
    private final List<DataEntry> dataEntries;
    private static final Logger logger = LogManager.getLogger(DataFile.class.getName());

    public DataFile() {
        this.dataEntries = new ArrayList<>();
    }

    public Collection<DataKey> getDataKeys() {
        Collection<DataKey> dataKeyCollection = CollectionFactory.getDataKeyCollection();

        logger.info("Extracting DataKeys from DataEntries...");
        dataEntries.forEach(dataKeyDataValueMap -> {
            dataKeyCollection.addAll(dataKeyDataValueMap.keySet());
            Set<DataKey> dataKeys = dataKeyCollection.toSet();
            dataKeyCollection.clear();
            dataKeyCollection.addAll(dataKeys);
        });

        logger.info("Extracted keys: " + dataKeyCollection);
        return dataKeyCollection;
    }

    public void addDataEntry(DataEntry dataEntry) {
        this.dataEntries.add(dataEntry);
    }

    public DataEntry[] getDataEntriesAsArray() {
        DataEntry[] dataEntries = new DataEntry[this.dataEntries.size()];
        return this.dataEntries.toArray(dataEntries);
    }


}
