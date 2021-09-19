package AppUtility.databases;

import AppUtility.exception.NotFoundException;
import AppUtility.domains.Carrier;
import AppUtility.interfaces.CarrierDatabase;

import java.util.ArrayList;
import java.util.List;

class InMemoryCarrierDatabase implements CarrierDatabase {
    private static final List<Carrier> carrierList = new ArrayList<Carrier>();
    private static final InMemoryCarrierDatabase instance = new InMemoryCarrierDatabase();

    private InMemoryCarrierDatabase() {}

    public static CarrierDatabase getInstance() {
        return instance;
    }

    public Carrier addCarrier(Carrier carrier) throws Exception {
        carrierList.add(carrier);
        int carrierId = carrierList.indexOf(carrier);

        if (carrierId == -1) {
            throw new Exception("Carrier could not be added :: " + carrier.toString());
        }

        return new Carrier(carrierId, carrier.getName());
    }

    public Carrier getCarrierById(int id) throws NotFoundException {
        for (Carrier carrier : carrierList) {
            if (carrier.getId().toString().equals(String.valueOf(id))) {
                return carrier;
            }
        }
        throw notFound(id);
    }

    public List<Carrier> getAllCarriers() {
        return carrierList;
    }

    @Override
    public Carrier updateCarrier(int id, Carrier carrier) throws Exception {
        Carrier carrierToUpdate = this.getCarrierById(id);
        carrierToUpdate.setName(carrier.getName());
        return carrierToUpdate;
    }

    @Override
    public Carrier deleteCarrier(int id) throws Exception {
        if (!exists(id)) {
            throw notFound(id);
        }
        return carrierList.remove(id);
    }

    @Override
    public Boolean exists(int id) {
        try {
            //noinspection ResultOfMethodCallIgnored
            carrierList.get(id);
            return true;
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            return false;
        }
    }

    private NotFoundException notFound(int id) {
        return new NotFoundException("Carrier with ID " + id + " not found.");
    }
}
