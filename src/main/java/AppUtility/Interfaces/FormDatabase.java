package AppUtility.Interfaces;

import AppUtility.Exception.NotFoundException;
import AppUtility.Domains.Form.Form;

import java.util.List;

public interface FormDatabase {
    Form addForm(Form form) throws Exception;
    Form getFormById(int id) throws NotFoundException;
    List<Form> getFormsByCarrierId(int carrierId);
    List<Form> getAllForms();
    Form updateForm(int id, Form form) throws NotFoundException;
    Form deleteForm(int id) throws NotFoundException;
    Boolean exists(int id);
}
