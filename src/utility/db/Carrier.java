package utility.db;


import utility.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Carrier extends DBObject<Carrier> {

    public static final String tableName = "carrier";

    private long id;
    private String name;

    private List<Form> forms;

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

    public void addForm(Form form) {
        //TODO
    }

    @Override
    public String toString() {
        return "Id: " + this.id + "\nName: " + this.name;
    }
}
