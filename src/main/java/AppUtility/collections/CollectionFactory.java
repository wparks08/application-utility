package AppUtility.collections;

import AppUtility.domains.datakey.DataKey;
import AppUtility.domains.form.Field.Field;
import AppUtility.domains.form.Form;
import AppUtility.domains.form.Property.Property;

public class CollectionFactory {
    private CollectionFactory() {

    }

    public static Collection<DataKey> getDataKeyCollection() {
        return new DataKeyCollection();
    }

    public static Collection<Field> getFieldCollection() {
        return new FieldCollection();
    }

    public static Collection<Form> getFormCollection() {
        return new FormCollection();
    }

    public static Collection<Property> getPropertyCollection() {
        return new PropertyCollection();
    }
}
