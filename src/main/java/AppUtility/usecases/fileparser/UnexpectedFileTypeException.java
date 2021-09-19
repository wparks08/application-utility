package AppUtility.usecases.fileparser;

public class UnexpectedFileTypeException extends Exception {
    public UnexpectedFileTypeException(String message) {
        super(message);
    }
}
