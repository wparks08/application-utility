package AppUtility.collections;

import AppUtility.domains.form.Form;

import java.util.ArrayList;
import java.util.List;

class FormCollection implements Collection<Form> {
    private final List<Form> formList;

    public FormCollection() {
        formList = new ArrayList<>();
    }

    public void add(Form form) {
        this.formList.add(form);
    }

    @Override
    public void addAll(java.util.Collection<? extends Form> forms) {
        this.formList.addAll(forms);
    }

    @Override
    public void addAll(Collection<Form> forms) {
        this.formList.addAll(forms.toList());
    }

    @Override
    public void clear() {
        this.formList.clear();
    }

    public Form[] toArray() {
        Form[] formArray = new Form[this.formList.size()];
        return formList.toArray(formArray);
    }
}
