package cloud_app_client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/authPanel.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);
        scene.getStylesheets().add("/style.css");
        primaryStage.setScene(scene);

        primaryStage.setTitle("Cloud storage");
        primaryStage.show();
    }


}

