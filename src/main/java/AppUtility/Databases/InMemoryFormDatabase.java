package AppUtility.Databases;

import AppUtility.Exception.NotFoundException;
import AppUtility.Domains.Form.Form;
import AppUtility.Interfaces.FormDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryFormDatabase implements FormDatabase {
    private static final List<Form> formList = new ArrayList<Form>();
    private static final InMemoryFormDatabase instance = new InMemoryFormDatabase();

    private InMemoryFormDatabase() {}

    public static FormDatabase getInstance() {
        return instance;
    }

    @Override
    public Form addForm(Form form) throws Exception {
        formList.add(form);
        int formId = formList.indexOf(form);

        if (formId == -1) {
            throw new Exception("Form could not be added :: " + form.toString());
        }

        return new Form(formId, form.getName());
    }

    @Override
    public Form getFormById(int id) throws NotFoundException {
        Form form;

        try {
            form = formList.get(id);
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw notFound(id);
        }

        return form;
    }

    @Override
    public List<Form> getFormsByCarrierId(int carrierId) {
        return formList.stream()
//                .filter(form -> form.getCarrierId() == carrierId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Form> getAllForms() {
        return formList;
    }

    @Override
    public Form updateForm(int id, Form form) throws NotFoundException{
        if (!exists(id)) {
            throw notFound(id);
        }

        Form formToUpdate = formList.get(id);

        formToUpdate.setName(form.getName());

        return formToUpdate;
    }

    @Override
    public Form deleteForm(int id) throws NotFoundException {
        if (!exists(id)) {
            throw notFound(id);
        }

        Form formToDelete = formList.get(id);
        formList.set(id, null);

        return formToDelete;
    }

    @Override
    public Boolean exists(int id) {
        try {
            //noinspection ResultOfMethodCallIgnored
            formList.get(id);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    private NotFoundException notFound(int id) {
        return new NotFoundException("Form with ID " + id + " not found.");
    }
}
