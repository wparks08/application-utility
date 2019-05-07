package utility.db;


import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
}
