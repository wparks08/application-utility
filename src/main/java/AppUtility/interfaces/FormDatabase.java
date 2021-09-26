package AppUtility.interfaces;

import AppUtility.domains.form.Form;
import AppUtility.domains.id.Id;
import AppUtility.exception.NotFoundException;

import java.util.List;

public interface FormDatabase {
    Form addForm(Form form) throws Exception;
    Form getFormById(int id) throws NotFoundException;
    List<Form> getFormsByCarrierId(int carrierId);
    List<Form> getAllForms();
    Form updateForm(Form form) throws NotFoundException;
    Form deleteForm(Id id) throws NotFoundException;
    Boolean exists(int id);
}
