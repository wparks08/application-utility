package AppUtility.interfaces;

import AppUtility.domains.id.Id;
import AppUtility.exception.NotFoundException;
import AppUtility.domains.Carrier;

import java.util.List;

public interface CarrierDatabase {
    public Carrier addCarrier(Carrier carrier) throws Exception;
    public Carrier getCarrierById(Id id) throws NotFoundException;
    public List<Carrier> getAllCarriers();
    public Carrier updateCarrier(Id id, Carrier carrier) throws Exception;
    public Carrier deleteCarrier(Id id) throws Exception;
    public Boolean exists(Id id);
}
