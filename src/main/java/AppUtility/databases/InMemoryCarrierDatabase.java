package AppUtility.databases;

import AppUtility.Exception.NotFoundException;
import AppUtility.domains.Carrier;
import AppUtility.interfaces.CarrierDatabase;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCarrierDatabase implements CarrierDatabase {
    private static final List<Carrier> carrierList = new ArrayList<Carrier>();
    private static final InMemoryCarrierDatabase instance = new InMemoryCarrierDatabase();

    private InMemoryCarrierDatabase() {}

    public static InMemoryCarrierDatabase getInstance() {
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
            if (carrier.getId() == id) {
                return carrier;
            }
        }
        throw notFound(id);
    }

    public List<Carrier> getAllCarriers() throws Exception {
        if (carrierList.isEmpty()) {
            throw new Exception("No carriers have been added.");
        }
        return carrierList;
    }

    @Override
    public Carrier updateCarrier(int id, Carrier carrier) throws Exception {
        Carrier oldCarrier = this.getCarrierById(id);
        oldCarrier.setName(carrier.getName());
        return oldCarrier;
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
