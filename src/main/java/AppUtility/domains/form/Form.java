package AppUtility.domains.form;

import AppUtility.collections.Collection;
import AppUtility.domains.datakey.DataKey;
import AppUtility.domains.form.Field.Field;
import AppUtility.domains.form.Property.Property;
import AppUtility.domains.id.Id;

import java.nio.file.Path;

public class Form {
    private final Id id;
    private final String name;
    private final Collection<Field> fieldCollection;
    private final Collection<DataKey> dataKeyCollection;
    private final Collection<Property> propertyCollection;
    private final Path pdfFilePath;

    private Form(FormBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.fieldCollection = builder.fieldCollection;
        this.dataKeyCollection = builder.dataKeyCollection;
        this.propertyCollection = builder.propertyCollection;
        this.pdfFilePath = builder.pdfFilePath;
    }

    public Id getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public Collection<Field> getFieldCollection() {
        return this.fieldCollection;
    }

    public Collection<DataKey> getDataKeyCollection() {
        return this.dataKeyCollection;
    }

    public Collection<Property> getPropertyCollection() {
        return this.propertyCollection;
    }

    public static class FormBuilder {
        private final String name;
        private Id id;
        private Collection<Field> fieldCollection;
        private Collection<DataKey> dataKeyCollection;
        private Collection<Property> propertyCollection;
        private Path pdfFilePath;

        public FormBuilder(String name) {
            this.name = name;
        }

        public FormBuilder id(Id id) {
            this.id = id;
            return this;
        }

        public FormBuilder fieldCollection(Collection<Field> fieldCollection) {
            this.fieldCollection = fieldCollection;
            return this;
        }

        public FormBuilder dataKeyCollection(Collection<DataKey> dataKeyCollection) {
            this.dataKeyCollection = dataKeyCollection;
            return this;
        }

        public FormBuilder propertyCollection(Collection<Property> propertyCollection) {
            this.propertyCollection = propertyCollection;
            return this;
        }

        public FormBuilder pdfFilePath(Path pdfFilePath) {
            this.pdfFilePath = pdfFilePath;
            return this;
        }

        public Form build() {
            return new Form(this);
        }
    }
}
