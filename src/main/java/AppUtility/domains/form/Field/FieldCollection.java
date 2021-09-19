package AppUtility.domains.form.Field;

import java.util.ArrayList;
import java.util.List;

public class FieldCollection {
    private List<Field> fields = new ArrayList<Field>();


    public FieldCollection() {
        //Default constructor
    }

    public void add(Field field) {
        this.fields.add(field);
    }

    public Field[] toArray() {
        Field[] fieldArray = new Field[this.fields.size()];
        return this.fields.toArray(fieldArray);
    }
}
