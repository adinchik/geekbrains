package ru.geekbrains.cloud_app_client;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.model.*;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;


public class NetworkService {
    public static final Logger log = LogManager.getLogger(MainController.class);
    private Socket socket;
    private ObjectEncoderOutputStream os;
    private ObjectDecoderInputStream in;
    private MainController mainController;
    private AuthController authController;
    private RegisterController registerController;

    public NetworkService(AuthController authController) throws IOException {
        connect();
        this.authController = authController;
        readCommands();
    }

    public NetworkService(RegisterController registerController) throws IOException {
        connect();
        this.registerController = registerController;
        readCommands();
    }

    private void connect() throws IOException {
        this.socket = new Socket("localhost", 8089);
        this.os = new ObjectEncoderOutputStream(socket.getOutputStream());
        this.in = new ObjectDecoderInputStream(socket.getInputStream());
    }
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
                                    MainController.baseDir.resolve(fileMessage.getFileName()),
                                    fileMessage.getBytes()
                            );
                            Platform.runLater(() -> mainController.fillView(mainController.clientFiles, mainController.getClientFileNames()));
                            break;
                        case FILE_REQUEST:
                            break;
                        case FILES_LIST:
                            FilesList files = (FilesList) msg;
                            mainController.setServerFilesList(files.getFiles());
                            Platform.runLater(() -> mainController.fillView(mainController.serverFiles, mainController.getServerFileNames()));
                            break;
                        case USER_AUTH:
                            UserAuth userAuth = (UserAuth) msg;
                            if (userAuth.getStatus()) {
                                authController.showMainPanel();
                                String text = "Hello, " + userAuth.getLogin();
                                Platform.runLater(() -> mainController.printLabelUserLogin(text));
                            } else {
                                authController.setUserNotFound();
                            }
                            break;
                        case USER_REGISTER:
                            UserRegister userRegister = (UserRegister) msg;
                            registerController.showMainPanel();
                            String text = "Hello, " + userRegister.getLogin();
                            Platform.runLater(() -> mainController.printLabelUserLogin(text));
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

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public ObjectEncoderOutputStream getOutputStream() {
        return os;
    }
}
