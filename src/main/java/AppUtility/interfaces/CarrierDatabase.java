package AppUtility.interfaces;

import AppUtility.exception.NotFoundException;
import AppUtility.domains.Carrier;

import java.util.List;

public interface CarrierDatabase {
    public Carrier addCarrier(Carrier carrier) throws Exception;
    public Carrier getCarrierById(int id) throws NotFoundException;
    public List<Carrier> getAllCarriers();
    public Carrier updateCarrier(int id, Carrier carrier) throws Exception;
    public Carrier deleteCarrier(int id) throws Exception;
    public Boolean exists(int id);
}
