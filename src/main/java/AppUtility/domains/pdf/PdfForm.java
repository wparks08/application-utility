package AppUtility.domains.pdf;

import AppUtility.collections.Collection;
import AppUtility.collections.CollectionFactory;
import AppUtility.domains.form.Field.Field;
import AppUtility.exception.NotFoundException;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;

class PdfForm {
    private final Collection<Field> fieldCollection = CollectionFactory.getFieldCollection();

    private final PDAcroForm pdAcroForm;

    PdfForm(PDAcroForm pdAcroForm) {
        this.pdAcroForm = pdAcroForm;
        mapFieldsToFieldCollection();
    }

    private void mapFieldsToFieldCollection() {
        pdAcroForm.getFields().forEach(pdField -> fieldCollection.add(new Field(pdField.getFullyQualifiedName())));
    }

    public Field getField(String name) throws NotFoundException {
        for (Field field : fieldCollection.toList()) {
            if (field.getName().equals(name)) {
                return field;
            }
        }

        throw new NotFoundException("No such field: " + name);
    }

    public Collection<Field> getFieldCollection() {
        return fieldCollection;
    }
}
