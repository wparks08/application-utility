package AppUtility;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;

public final class AppProperties {
    private final String FILENAME = "application.properties";
    private final String PROPERTIES_DIRECTORY = System.getProperty("user.dir");
    private final String PROPERTIES_FILEPATH = PROPERTIES_DIRECTORY + "/" + FILENAME;

    private static final AppProperties instance = new AppProperties();
    private final Properties properties = new Properties();

    private static class PropertyKeys {
        private static final String LAST_ACCESSED = "lastAccessedFilePath";
        private static final String OUTPUT_DIRECTORY = "outputDirectory";
    }

    private final Thread writeThread = new Thread(() -> {
        try {
            OutputStream out = new FileOutputStream(PROPERTIES_FILEPATH);
            this.properties.store(out, "File updated");
        } catch (IOException e) {
            e.printStackTrace();
        }
    });

    private static Logger log;

    private AppProperties() {
        log = LogManager.getLogger(AppProperties.class.getName());
        //Default constructor
        log.info("loading " + FILENAME);
        File file = new File(PROPERTIES_FILEPATH);
        try {
            if (!Files.exists(file.toPath())) {
                log.warn(FILENAME + " not found, creating file...");
                setDefaults();
                OutputStream out = new FileOutputStream(PROPERTIES_FILEPATH);
                this.properties.store(out, "File created");
            }
            InputStream in = new FileInputStream(file);
            properties.load(in);
            log.info(FILENAME + " loaded");
        } catch (IOException e) {
            log.error("Could not load " + FILENAME);
            e.printStackTrace();
        }
    }

    private void setDefaults() {
        properties.setProperty(PropertyKeys.LAST_ACCESSED, System.getProperty("user.home"));
        properties.setProperty(PropertyKeys.OUTPUT_DIRECTORY, System.getProperty("user.home"));
        Platform.runLater(writeThread);
    }

    public static AppProperties getInstance() {
        return instance;
    }

    public String getLastAccessedFilePath() {
        return properties.getProperty(PropertyKeys.LAST_ACCESSED);
    }

    public void setLastAccessedFilePath(String path) {
        properties.setProperty(PropertyKeys.LAST_ACCESSED, path);
        Platform.runLater(writeThread);
    }

    public void setOutputDirectory(String outputDirectory) {
        properties.setProperty(PropertyKeys.OUTPUT_DIRECTORY, outputDirectory);
        Platform.runLater(writeThread);
    }

    public String getOutputDirectory() {
        return properties.getProperty(PropertyKeys.OUTPUT_DIRECTORY);
    }
}
