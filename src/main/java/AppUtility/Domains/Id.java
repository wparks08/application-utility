package AppUtility.Domains;

public class Id {
    private long id;

    public Id(long id) {
        this.id = id;
    }

    public Id(int id) {
        this.id = id;
    }

    public Id(String id) {
        this.id = Long.parseLong(id);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long toLong() {
        return this.id;
    }

    public int toInt() {
        return (int) this.id;
    }

    @Override
    public String toString() {
        return String.valueOf(this.id);
    }
}
