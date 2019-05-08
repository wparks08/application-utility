package utility.db;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.io.FilenameUtils.getExtension;

public class Form extends DBObject<Form> {

    private long id;
    private String name;
    private byte[] formBytes;
    private String formExtension;
    private long carrierId;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public byte[] getFormBytes() {
        return formBytes;
    }

    public void setFormBytes(byte[] formBytes) {
        this.formBytes = formBytes;
    }


    public String getFormExtension() {
        return formExtension;
    }

    public void setFormExtension(String formExtension) {
        this.formExtension = formExtension;
    }


    public long getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(long carrierId) {
        this.carrierId = carrierId;
    }

    public void loadFormFile(File file) {
        this.formExtension = getExtension(file.getName());
        try {
            this.formBytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<FormProperty> getFormProperties() {
        List<?> formPropertyList = getChildren(FormProperty.class);
        List<FormProperty> toReturn = new ArrayList<>();

        for (Object object : formPropertyList) {
            if (object instanceof FormProperty) {
                toReturn.add((FormProperty) object);
            }
        }
        return toReturn;
    }

    public void addFormProperty(String property, String value) {
        FormProperty formProperty = new FormProperty(property, value, this.id);
        formProperty.save();
    }

    //Add & get methods for CensusHeader, FormField, Mapping

    public List<CensusHeader> getCensusHeaders() {
        List<?> headersList = getChildren(CensusHeader.class);
        List<CensusHeader> toReturn = new ArrayList<>();

        for (Object object : headersList) {
            if (object instanceof CensusHeader) {
                toReturn.add((CensusHeader) object);
            }
        }
        return toReturn;
    }

    public void addCensusHeader(String header) {
        CensusHeader censusHeader = new CensusHeader(header, this.id);
        censusHeader.save();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
