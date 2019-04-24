package utility;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        File file = new File("C:\\JavaFXProjects\\ApplicationUtility\\SampleData\\samplecensus.csv");

        Census census = new Census(new File("C:\\JavaFXProjects\\ApplicationUtility\\SampleData\\samplecensus.csv"));

        String[] headers = census.getHeaders();

        Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
