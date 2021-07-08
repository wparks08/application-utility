package AppUtility.domains;

public class Carrier {
    private int id;
    private String name;

    public Carrier(String name) {
        this.name = name;
    }

    public Carrier(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Carrier(Carrier carrier) {
        this.id = carrier.id;
        this.name = carrier.name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
