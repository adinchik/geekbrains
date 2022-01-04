package cloud_app_client;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {

    private static final int BUFFER_SIZE = 8192;
    public static final Logger log = LogManager.getLogger(Controller.class);
    public TextField loginField;
    public PasswordField passwordField;
    public AnchorPane loginPanel;
    public AnchorPane mainPanel;
    public Label labelUserLogin;
    public AnchorPane registerPanel;
    public TextField regNameField;
    public TextField regLoginField;
    public PasswordField regPasswordField;
    public PasswordField regRepeatPasswordField;
    public Label labelUserNotFound;
    private byte[] buffer;
    public Socket socket;
    public ObjectEncoderOutputStream os;
    public ObjectDecoderInputStream in;
    public ListView<String> clientFiles;
    public ListView<String> serverFiles;
    private Path baseDir;

    private void readCommands() {
        Thread t;
        t = new Thread(()-> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    AbstractMessage msg = (AbstractMessage) in.readObject();
                    log.debug("received: {}", msg);
                    switch (msg.getMessageType()) {
                        case FILE:
                            FileMessage fileMessage = (FileMessage) msg;
                            Files.write(
                                    baseDir.resolve(fileMessage.getFileName()),
                                    fileMessage.getBytes()
                            );
                            Platform.runLater(() -> fillClientView(getFileNames()));
                            break;
                        case FILES_LIST:
                            FilesList files = (FilesList) msg;
                            Platform.runLater(() -> fillServerView(files.getFiles()));
                            break;
                        case USER_AUTH:
                            UserAuth userAuth = (UserAuth) msg;
                            if (userAuth.getStatus()) {
                                loginPanel.setVisible(false);
                                mainPanel.setVisible(true);
                                Platform.runLater(() ->labelUserLogin.setText("Hello, " + userAuth.getLogin()));
                                showClientFiles();
                            } else {
                                Platform.runLater(() -> labelUserNotFound.setText("User not found, please sign up"));
                                labelUserNotFound.setVisible(true);
                            }
                            break;
                        case USER_REGISTER:
                            UserRegister userRegister = (UserRegister) msg;
                            registerPanel.setVisible(false);
                            mainPanel.setVisible(true);
                            Platform.runLater(() ->labelUserLogin.setText("Hello, " + userRegister.getLogin()));
                            showClientFiles();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private void fillServerView(List<String> list) {
        serverFiles.getItems().clear();
        serverFiles.getItems().addAll(list);
    }

    private void fillClientView(List<String> list) {
        clientFiles.getItems().clear();
        clientFiles.getItems().addAll(list);
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private void connect() {
        try {
            socket = new Socket("localhost", 8089);
            os = new ObjectEncoderOutputStream(socket.getOutputStream());
            in = new ObjectDecoderInputStream(socket.getInputStream());
            buffer = new byte[BUFFER_SIZE];
            readCommands();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showClientFiles() {
        baseDir = Paths.get(System.getProperty("user.home"));
        clientFiles.getItems().addAll(getFileNames());
        clientFiles.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                String file = clientFiles.getSelectionModel().getSelectedItem();
                Path path = baseDir.resolve(file);
                if (Files.isDirectory(path)) {
                    baseDir = path;
                    fillClientView(getFileNames());
                }
            }
        });
    }


    private List<String> getFileNames()  {
        try {
            return Files.list(baseDir)
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void upload(ActionEvent actionEvent) throws IOException {
        String file = clientFiles.getSelectionModel().getSelectedItem();
        Path filePath = baseDir.resolve(file);
        System.out.println(filePath.toString());
        os.writeObject(new FileMessage(filePath));
    }

    public void downLoad(ActionEvent actionEvent) throws IOException {
        String file = serverFiles.getSelectionModel().getSelectedItem();
        os.writeObject(new FileRequest(file));
    }

    public void sendAuth(ActionEvent actionEvent) throws IOException {
        if (loginField.getText().isBlank() && passwordField.getText().isBlank()) return;
        connect();
        String login = loginField.getText();
        String password = passwordField.getText();
        os.writeObject(new UserAuth(login, password,false));
    }

    public void register(ActionEvent actionEvent) {
        loginPanel.setVisible(false);
        registerPanel.setVisible(true);
    }

    public void createNewUser(ActionEvent actionEvent) throws IOException {
        connect();
        if (regPasswordField.getText().equals(regRepeatPasswordField.getText())) {
            os.writeObject(new UserRegister(regLoginField.getText(), regPasswordField.getText(), regNameField.getText(), false));
        }
    }
}

