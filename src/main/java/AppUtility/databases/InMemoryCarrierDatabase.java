package AppUtility.databases;

import AppUtility.domains.id.Id;
import AppUtility.exception.NotFoundException;
import AppUtility.domains.Carrier;
import AppUtility.interfaces.CarrierDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class InMemoryCarrierDatabase implements CarrierDatabase {
    private static final List<Carrier> carrierList = new ArrayList<>();
    private static final InMemoryCarrierDatabase instance = new InMemoryCarrierDatabase();

    private InMemoryCarrierDatabase() {}

    public static CarrierDatabase getInstance() {
        return instance;
    }

    public Carrier addCarrier(Carrier carrier) {
        if (carrier.getId() == null) {
            carrier.setId(new Id());
        }

        carrierList.add(carrier);

        return new Carrier(carrier);
    }

    public Carrier getCarrierById(Id id) throws NotFoundException {
        for (Carrier carrier : carrierList) {
            if (carrier.getId().equals(id)) {
                return carrier;
            }
        }
        throw notFound(id);
    }

    public List<Carrier> getAllCarriers() {
        return carrierList;
    }

    @Override
    public Carrier updateCarrier(Id id, Carrier carrier) throws Exception {
        Carrier carrierToUpdate = this.getCarrierById(id);
        carrierToUpdate.setName(carrier.getName());
        return carrierToUpdate;
    }

    public Carrier deleteCarrier(Id id) throws Exception {
        if (!exists(id)) {
            throw notFound(id);
        }
        Optional<Carrier> carrierOptional = carrierList.stream().filter(carrier -> carrier.getId().equals(id)).findFirst();
        if (carrierOptional.isPresent()) {
            Carrier carrier = carrierOptional.get();
            carrierList.remove(carrier);
            return carrier;
        }
        throw notFound(id);
    }

    public Boolean exists(Id id) {
        try {
            this.getCarrierById(id);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    private NotFoundException notFound(Id id) {
        return new NotFoundException("Carrier with ID " + id.toString() + " not found.");
    }
}
