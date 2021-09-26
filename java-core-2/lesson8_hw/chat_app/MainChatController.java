package chat_app;

import chat_app.net.ChatMessageService;
import chat_app.net.MessageProcessor;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;


import java.net.URL;
import java.util.ResourceBundle;

public class MainChatController implements Initializable, MessageProcessor {
    public static final String REGEX = "%&%";
    public VBox loginPanel;
    public TextField loginField;
    public PasswordField passwordField;
    public VBox changePasswordPanel;
    public PasswordField oldPassField;
    public PasswordField newPassField;
    public VBox changeNickPanel;
    public TextField newNickField;
    private ChatMessageService chatMessageService;
    private String nickname;
    public VBox MainChatPanel;
    public TextArea mainChatArea;
    public TextField inputField;
    public Button btnSendMessage;
    public ListView<String> contactList;

    public void mockAction(ActionEvent actionEvent) {
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void sendMessage(ActionEvent actionEvent) {
        String text = inputField.getText();
        if (text.isEmpty()) return;
        String recipient = contactList.getSelectionModel().getSelectedItem();
        String message = "";
        if (recipient.equals("ALL")) {
            message = "/" + recipient + REGEX + text;
        }
        else {
            message = "/w" + REGEX + recipient + REGEX + text;
        }
        chatMessageService.send(message);
        inputField.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String>  list = FXCollections.observableArrayList("ALL");
        contactList.setItems(list);
        contactList.getSelectionModel().select(0);
        this.chatMessageService = new ChatMessageService(this);
    }

    @Override
    public void processMessage(String message) {

        Platform.runLater(() -> parseMessage(message));
    }

    public void sendAuth() {
        if (loginField.getText().isBlank() && passwordField.getText().isBlank()) return;
        chatMessageService.connect();
        chatMessageService.send("/auth" + REGEX + loginField.getText() + REGEX + passwordField.getText());
    }

    public void sendRegister() {
//        if (loginField.getText().isBlank() && passwordField.getText().isBlank()) return;
//        chatMessageService.connect();
//        chatMessageService.send("/auth" + REGEX + loginField.getText() + REGEX + passwordField.getText());
    }

    public void sendChangeNick() {
        if (newNickField.getText().isBlank()) return;
        chatMessageService.send("/change_nick" + REGEX + newNickField.getText());

    }

    public void sendChangePass() {
        if (oldPassField.getText().isBlank() || newPassField.getText().isBlank()) return;
        chatMessageService.send("/change_pass" + REGEX + oldPassField.getText() + REGEX + newPassField.getText());


    }

    public void sendEternalLogout() {
        chatMessageService.send("/remove");
    }

    private void parseMessage(String message) {
        String[] parsedMessage = message.split(REGEX);
        switch (parsedMessage[0]) {
            case "/authok:":
                this.nickname = parsedMessage[1];
                loginPanel.setVisible(false);
                MainChatPanel.setVisible(true);
                break;
            case "ERROR:":
                showError(parsedMessage[1]);
                break;
            case "/list:":
                parsedMessage[0] = "ALL";
                ObservableList<String>  list = FXCollections.observableArrayList(parsedMessage);
                contactList.setItems(list);
                contactList.getSelectionModel().select(0);
                break;
            case "/change_nick_ok" :
                changeNickPanel.setVisible(false);
                MainChatPanel.setVisible(true);
                break;
            case "/change_pass_ok" :
                changePasswordPanel.setVisible(false);
                MainChatPanel.setVisible(true);
                break;
            default:
                mainChatArea.appendText(parsedMessage[0] + System.lineSeparator());
        }
    }

    private void showError(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public void returnToChat(ActionEvent actionEvent) {
        changeNickPanel.setVisible(false);
        changePasswordPanel.setVisible(false);
        MainChatPanel.setVisible(true);
    }

    public void showChangePass(ActionEvent actionEvent) {
        MainChatPanel.setVisible(false);
        changePasswordPanel.setVisible(true);
    }

    public void showChangeNick(ActionEvent actionEvent) {
        MainChatPanel.setVisible(false);
        changeNickPanel.setVisible(true);
    }
}
