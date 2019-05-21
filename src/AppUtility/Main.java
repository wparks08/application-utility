package AppUtility;

import com.sun.deploy.association.utility.AppUtility;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        launch(args);
    }
}
