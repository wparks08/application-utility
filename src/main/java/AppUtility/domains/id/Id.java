package AppUtility.domains.id;

public interface Id {
    void setId(String id);

    @Override
    String toString();

    Boolean equals(Id other);
}
