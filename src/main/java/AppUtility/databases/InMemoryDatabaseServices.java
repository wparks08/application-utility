package AppUtility.databases;

import AppUtility.interfaces.CarrierDatabase;
import AppUtility.interfaces.DatabaseServices;

public class InMemoryDatabaseServices implements DatabaseServices {
    private final CarrierDatabase carrierDatabase;

    public InMemoryDatabaseServices() {
        carrierDatabase = InMemoryCarrierDatabase.getInstance();
    }

    @Override
    public CarrierDatabase getCarrierDatabase() {
        return this.carrierDatabase;
    }
}
