package cloud_app_client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {

    private static final int BUFFER_SIZE = 8192;
    private byte[] buffer;
    public Socket socket;
    public DataInputStream in;
    public DataOutputStream out;
    public ListView<String> clientFiles;
    public ListView<String> serverFiles;
    private Path baseDir;


    public void readCommands() {
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
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            baseDir = Paths.get(System.getProperty("user.home"));
            //clientFiles.getItems().addAll(getClientFiles());
            clientFiles.getItems().addAll(getFileNames());
            socket = new Socket("localhost", 8089);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            buffer = new byte[BUFFER_SIZE];
            /*clientFiles.setCellFactory(list -> new ListCell<FileInfo>() {
                @Override
                protected void updateItem(FileInfo item, boolean empty) {
                    String text;
                    if (item != null && !empty) {
                        if (item.isDirectory()) {
                            text = item.getFileName() + "[DIR]";
                        } else {
                            text = item.getFileName() + " " + item.getSize() + " bytes";
                        }
                        setText(text);
                    } else {
                        setText("");
                    }
                }

            });*/
            readCommands();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<FileInfo> getClientFiles() throws IOException {
        return Files.list(baseDir)
                .map(FileInfo::new)
                .collect(Collectors.toList());
    }

    private List<String> getFileNames() throws IOException {
        return Files.list(baseDir)
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
    }

    public void upload(ActionEvent actionEvent) {
        try {
            String fileName = clientFiles.getSelectionModel().getSelectedItem();
            System.out.println(fileName);
            Path filePath = baseDir.resolve(fileName);
            System.out.println(filePath.toString());
            out.writeUTF("#upload");
            out.writeUTF(fileName);
            System.out.println(Files.size(filePath));
            out.writeLong(Files.size(filePath));
            out.write(Files.readAllBytes(filePath));
            out.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void downLoad(ActionEvent actionEvent) {
        try {
            String fileName = serverFiles.getSelectionModel().getSelectedItem();
            System.out.println(fileName);
            out.writeUTF("#download");
            out.writeUTF(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

