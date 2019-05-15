package utility;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import utility.db.Carrier;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        DataModel model = new DataModel();

        FXMLLoader docLoader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
        Parent root = docLoader.load();
        DashboardController docController = docLoader.getController();

        docController.initModel(model);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
