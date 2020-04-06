package AppUtility;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        DataModel model = new DataModel();

        FXMLLoader docLoader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
        Parent root = docLoader.load();
        DashboardController docController = docLoader.getController();

        docController.initModel(model);

        Scene scene = new Scene(root);
//        scene.getStylesheets().add(AppUtility.class.getResource("AppUtility/ApplicationStyles.css").toExternalForm());

        primaryStage.setTitle("VANTREO Application Utility");
//        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {

        /*Credit: https://www.dev2qa.com/how-to-write-console-output-to-text-file-in-java/ */
        // Save original out stream.
        PrintStream originalOut = System.out;
        // Save original err stream.
        PrintStream originalErr = System.err;

        // Create a new file output stream.
        PrintStream fileOut = null;
        try {
            fileOut = new PrintStream(new FileOutputStream("./out.txt", true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Create a new file error stream.
        PrintStream fileErr = null;
        try {
            fileErr = new PrintStream(new FileOutputStream("./err.txt", true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//         Redirect standard out to file.
        System.setOut(fileOut);
//         Redirect standard err to file.
        System.setErr(fileErr);

        launch(args);
    }
}
