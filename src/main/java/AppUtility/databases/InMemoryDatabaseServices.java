package AppUtility.databases;

import AppUtility.interfaces.CarrierDatabase;
import AppUtility.interfaces.DatabaseServices;
import AppUtility.interfaces.FormDatabase;

public class InMemoryDatabaseServices implements DatabaseServices {
    private final CarrierDatabase carrierDatabase;
    private final FormDatabase formDatabase;

    public InMemoryDatabaseServices() {
        carrierDatabase = InMemoryCarrierDatabase.getInstance();
        formDatabase = InMemoryFormDatabase.getInstance();
    }

    @Override
    public CarrierDatabase getCarrierDatabase() {
        return this.carrierDatabase;
    }

    @Override
    public FormDatabase getFormDatabase() { return this.formDatabase; }
}
