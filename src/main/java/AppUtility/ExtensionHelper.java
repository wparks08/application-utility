package AppUtility;

public class ExtensionHelper {
    private final String description;
    private final String fileSystemExtension;

    public ExtensionHelper(String description, String fileSystemExtension) {
        this.description = description;
        this.fileSystemExtension = fileSystemExtension;
    }

    public String getDescription() {
        return description;
    }

    public String getFileSystemExtension() {
        return fileSystemExtension;
    }
}
