package AppUtility.UseCases.FileParser;

public class UnexpectedFileTypeException extends Exception {
    public UnexpectedFileTypeException(String message) {
        super(message);
    }
}
