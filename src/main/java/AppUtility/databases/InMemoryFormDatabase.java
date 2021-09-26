package AppUtility.databases;

import AppUtility.domains.id.Id;
import AppUtility.domains.id.IdFactory;
import AppUtility.exception.NotFoundException;
import AppUtility.domains.form.Form;
import AppUtility.interfaces.FormDatabase;

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

        return new Form.FormBuilder(form.getName())
                .id(IdFactory.getIdObject(String.valueOf(formId)))
                .dataKeyCollection(form.getDataKeyCollection())
                .propertyCollection((form.getPropertyCollection()))
                .fieldCollection(form.getFieldCollection())
                .build();
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
    public Form updateForm(Form form) throws NotFoundException{
        int idInteger = Integer.parseInt(form.getId().toString());
        if (!exists(idInteger)) {
            throw notFound(idInteger);
        }

        formList.set(idInteger, form);

        return form;
    }

    @Override
    public Form deleteForm(Id id) throws NotFoundException {
        int idInteger = Integer.parseInt(id.toString());
        if (!exists(idInteger)) {
            throw notFound(idInteger);
        }

        Form formToDelete = formList.get(idInteger);
        formList.set(idInteger, null);

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
