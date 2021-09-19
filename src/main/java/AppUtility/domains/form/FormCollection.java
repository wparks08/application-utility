package AppUtility.domains.form;

import java.util.ArrayList;
import java.util.List;

public class FormCollection {
    private List<Form> formList;

    public FormCollection() {
        formList = new ArrayList<Form>();
    }

    public void add(Form form) {
        this.formList.add(form);
    }

    public Form[] toArray() {
        Form[] formArray = new Form[this.formList.size()];
        return formList.toArray(formArray);
    }
}
