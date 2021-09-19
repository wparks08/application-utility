package AppUtility.domains.datakey;

public class DataKey {
    private final String key;

    public DataKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }

    public Boolean equals(DataKey other) {
        return this.key.equals(other.key);
    }
}
