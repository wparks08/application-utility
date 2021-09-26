package AppUtility.domains.id;

class LongId implements Id {
    private long id;

    public LongId(String id) {
        this.id = Long.parseLong(id);
    }

    @Override
    public void setId(String id) {
        this.id = Long.parseLong(id);
    }

    @Override
    public String toString() {
        return String.valueOf(this.id);
    }

    @Override
    public Boolean equals(Id other) {
        return this.toString().equals(other.toString());
    }
}
