package AppUtility.Databases;

import AppUtility.Interfaces.CarrierDatabase;
import AppUtility.Interfaces.DatabaseServices;
import AppUtility.Interfaces.FormDatabase;

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
