package ru.geekbrains.java_core_2.chat_app;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ru.geekbrains.java_core_2.chat_app.net.ChatMessageService;
import ru.geekbrains.java_core_2.chat_app.net.MessageProcessor;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainChatController implements Initializable, MessageProcessor {

    public VBox loginPanel;
    public TextField loginField;
    public PasswordField passwordField;
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
       // if (contactList.getSelectionModel().isEmpty())
       //     text = "ME: " + text;
       // else
       //     text = contactList.getSelectionModel().getSelectedItem() + ": " + text;
       // mainChatArea.appendText(text + "\n");
        chatMessageService.send(this.nickname + ": " + text + "\n");
        inputField.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String>  list = FXCollections.observableArrayList("Adina", "Madina", "Timur", "Insar", "Temirlan");
        contactList.setItems(list);
        this.chatMessageService = new ChatMessageService(this);
    }

    @Override
    public void processMessage(String message) {
        parseMessage(message);
    }

    public void sendAuth() {
        if (loginField.getText().isBlank() && passwordField.getText().isBlank()) return;
        chatMessageService.connect();
        chatMessageService.send("auth: " + loginField.getText() + " " + passwordField.getText());
    }

    private void parseMessage(String message) {
        if (message.startsWith("authok: ")) {
            this.nickname = message.substring(8);
            //System.out.println(this.nickname);
            loginPanel.setVisible(false);
            MainChatPanel.setVisible(true);
        } else if (message.startsWith("ERROR: ")) {
            showError(message);
        } else {
            mainChatArea.appendText(message);
        }
    }

    private void showError(String message) {
        Alert alert;
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
