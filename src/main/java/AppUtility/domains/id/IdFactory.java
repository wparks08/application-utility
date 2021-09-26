package AppUtility.domains.id;

public class IdFactory {
    private IdFactory() {

    }

    public static Id getIdObject(String id) {
        return new LongId(id);
    }
}
