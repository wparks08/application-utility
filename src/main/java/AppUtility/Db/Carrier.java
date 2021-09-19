package AppUtility.Db;


import java.util.ArrayList;
import java.util.List;

@Deprecated
public class Carrier extends DBObject<Carrier> {

//    public static final String tableName = "carrier";

    private long id;
    private String name;

    public Carrier() {
        //Default constructor
    }

    public Carrier(String name) {
        this.name = name;
    }

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
        form.setCarrierId(this.id);
    }

    public List<Form> getForms() {
        List<Form> formList = new Form().list();
        List<Form> returnList = new ArrayList<>();

        for (Form form : formList) {
            if (form.getCarrierId() == this.id) {
                returnList.add(form);
            }
        }

        return returnList;
    }

    @Override
    public String toString() {

        int formCount = this.getChildren(Form.class).size();

        return this.name + " (" + formCount + ")";
    }
}
