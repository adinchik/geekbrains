package cloud_app_client;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import model.AbstractMessage;
import model.FileMessage;
import model.FileRequest;
import model.FilesList;

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
    private byte[] buffer;
    public Socket socket;
    public ObjectEncoderOutputStream os;
    public ObjectDecoderInputStream in;
    public ListView<String> clientFiles;
    public ListView<String> serverFiles;
    private Path baseDir;


    /*public void readCommands() {
        Thread t = new Thread(()->{
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String command = in.readUTF();
                    System.out.println(command);
                    if (command.equals("#list")) {
                        Platform.runLater(() ->serverFiles.getItems().clear());
                        int fileCount = in.readInt();
                        for (int i = 0; i < fileCount; i++) {
                            String name = in.readUTF();
                            Platform.runLater(()->serverFiles.getItems().add(name));
                        }
                    }
                    else {
                        String fileName = in.readUTF();
                        long size = in.readLong();
                        try (FileOutputStream fos = new FileOutputStream(baseDir.resolve(fileName).toFile()))  {
                            for (int i = 0; i < (size + BUFFER_SIZE - 1) / BUFFER_SIZE; i++) {
                                int read = in.read(buffer);
                                fos.write(buffer, 0, read);
                            }
                            Platform.runLater(() -> clientFiles.getItems().add(fileName));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }*/

    private void readCommands() {
        Thread t;
        t = new Thread(()-> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    System.out.println("revived smth");
                    AbstractMessage msg = (AbstractMessage) in.readObject();
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
        try {
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

            socket = new Socket("localhost", 8089);
            os = new ObjectEncoderOutputStream(socket.getOutputStream());
            in = new ObjectDecoderInputStream(socket.getInputStream());
            buffer = new byte[BUFFER_SIZE];
            readCommands();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}

