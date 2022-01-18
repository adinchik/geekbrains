package cloud_app_client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.UserRegister;

import java.io.IOException;

public class RegisterController {

    private NetworkService networkService;
    private MainController mainController;
    public TextField regNameField;
    public TextField regLoginField;
    public PasswordField regPasswordField;
    public PasswordField regRepeatPasswordField;

    public void createNewUser(ActionEvent actionEvent) throws IOException {
        networkService = new NetworkService(this);
        if (regPasswordField.getText().equals(regRepeatPasswordField.getText())) {
            networkService.getOutputStream().writeObject(new UserRegister(regLoginField.getText(), regPasswordField.getText(), regNameField.getText(), false));
        }
    }

    public void showMainPanel() throws IOException {
        Stage stage = (Stage) regLoginField.getScene().getWindow();
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
}
