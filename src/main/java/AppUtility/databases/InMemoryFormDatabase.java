package AppUtility.databases;

import AppUtility.domains.id.Id;
import AppUtility.exception.NotFoundException;
import AppUtility.domains.form.Form;
import AppUtility.interfaces.FormDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
                .id(form.getId())
                .dataKeyCollection(form.getDataKeyCollection())
                .propertyCollection((form.getPropertyCollection()))
                .fieldCollection(form.getFieldCollection())
                .build();
    }

    public Form getFormById(Id id) throws NotFoundException {
        for (Form form : formList) {
            if (form.getId().equals(id)) {
                return form;
            }
        }

        throw notFound(id);
    }

    public List<Form> getFormsByCarrierId(Id carrierId) {
        return formList.stream()
                .filter(form -> form.getCarrierId().equals(carrierId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Form> getAllForms() {
        return formList;
    }

    @Override
    public Form updateForm(Form form) throws NotFoundException {
        for (int i = 0; i < formList.size(); i++) {
            Form currentForm = formList.get(i);
            if (currentForm.getId().equals(form.getId())) {
                formList.set(i, form);
                return form;
            }
        }

        throw notFound(form.getId());
    }

    @Override
    public Form deleteForm(Id id) throws NotFoundException {
        Optional<Form> formOptional = formList.stream().filter(form1 -> form1.getId().equals(id)).findFirst();

        if (formOptional.isPresent()) {
            Form form = formOptional.get();
            formList.remove(form);
            return form;
        }

        throw notFound(id);
    }

    public Boolean exists(Id id) {
        try {
            this.getFormById(id);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    private NotFoundException notFound(Id id) {
        return new NotFoundException("Form with ID " + id + " not found.");
    }
}
