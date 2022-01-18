package cloud_app_client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.UserAuth;

import java.io.IOException;

public class AuthController {
    public TextField loginField;
    public PasswordField passwordField;
    public Label labelUserNotFound;
    public NetworkService networkService;
    public MainController mainController;
    public AnchorPane loginPanel;

    public void sendAuth(ActionEvent actionEvent) throws IOException {
        if (loginField.getText().isBlank() && passwordField.getText().isBlank()) return;
        networkService = new NetworkService(this);
        String login = loginField.getText();
        String password = passwordField.getText();
        networkService.getOutputStream().writeObject(new UserAuth(login, password,false));
    }

    public void setUserNotFound() {
        Platform.runLater(() -> labelUserNotFound.setText("User not found"));
        labelUserNotFound.setVisible(true);
    }


    public void showMainPanel() throws IOException {
        Stage stage = (Stage) loginField.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainPanel.fxml"));
        loader.setControllerFactory(contr -> {
            if (contr == MainController.class) {
                mainController = new MainController(networkService);
                networkService.setMainController(mainController);
                return mainController;
            } else return null;
        });
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        scene.getStylesheets().add("/style.css");
        Platform.runLater(() -> stage.setScene(scene));
        Platform.runLater(() -> stage.show());
    }

    public void register(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) loginField.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/registerPanel.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        scene.getStylesheets().add("/style.css");
        Platform.runLater(() -> stage.setScene(scene));
        Platform.runLater(() -> stage.show());
    }
}
