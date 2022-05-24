package AppUtility.collections;

import AppUtility.domains.datakey.DataKey;
import AppUtility.domains.form.Field.Field;

import java.util.ArrayList;
import java.util.List;

class FieldCollection implements Collection<Field> {
    private final List<Field> fields = new ArrayList<Field>();

    public FieldCollection() {
        //Default constructor
    }

    public void add(Field field) {
        this.fields.add(field);
    }

    @Override
    public void addAll(java.util.Collection<? extends Field> fields) {
        this.fields.addAll(fields);
    }

    @Override
    public void addAll(Collection<Field> fields) {
        this.fields.addAll(fields.toList());
    }

    @Override
    public void clear() {
        this.fields.clear();
    }

    public Field[] toArray() {
        Field[] fieldArray = new Field[this.fields.size()];
        return this.fields.toArray(fieldArray);
    }
}
