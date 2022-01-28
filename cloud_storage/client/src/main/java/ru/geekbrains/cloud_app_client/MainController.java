package ru.geekbrains.cloud_app_client;

import javafx.beans.property.*;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import ru.geekbrains.model.FileMessage;
import ru.geekbrains.model.FileRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class MainController implements Initializable {

    public static final Logger log = LogManager.getLogger(MainController.class);
    public VBox mainPanel;
    public Label labelUserLogin;
    public TableView<FileInfo> clientFiles;
    public TableView<FileInfo> serverFiles;
    public static Path baseDir;
    private NetworkService networkService;
    private Stack<Path> parentPaths;
    private List<File> serverFilesList;

    public MainController(NetworkService networkService) {
        this.networkService = networkService;
        this.baseDir = Paths.get(System.getProperty("user.home"));
        this.parentPaths = new Stack<>();
        this.serverFilesList = new ArrayList<>();
    }

    public void fillView(TableView<FileInfo> tableView, List<FileInfo> list) {
        tableView.getItems().clear();
        tableView.getItems().addAll(list);
    }

    public void setServerFilesList(List<File> list) {
        this.serverFilesList = list;
    }

    public List<FileInfo> getServerFileNames() {
        List<FileInfo> list = serverFilesList.stream()
                .map(FileInfo::new)
                .collect(Collectors.toList());
        return list;
    }

    public void printLabelUserLogin(String text) {
        labelUserLogin.setText(text);
        labelUserLogin.setTextAlignment(TextAlignment.CENTER);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addColumnsToTableView(serverFiles);
        fillView(serverFiles, getServerFileNames());
        addColumnsToTableView(clientFiles);
        showClientFiles();
    }

    public void addColumnsToTableView(TableView<FileInfo> tableView) {
        TableColumn<FileInfo, Boolean> columnImage = new TableColumn<>();
        columnImage.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isDirectory()));
        columnImage.setPrefWidth(50);
        columnImage.setCellFactory(tc -> new TableCell<FileInfo, Boolean>() {
            @Override
            public void updateItem(Boolean isDirectory, boolean empty){
                super.updateItem(isDirectory, empty);
                if (isDirectory != null){
                    ImageView imgView = new ImageView();
                    if (isDirectory) {
                        imgView.setImage(new Image(getClass().getResourceAsStream("/folder.png")));
                        imgView.setFitHeight(30);
                        imgView.setFitWidth(30);
                    }else{
                        imgView.setImage(new Image(getClass().getResourceAsStream("/document.png")));
                        imgView.setFitHeight(30);
                        imgView.setFitWidth(30);
                    }
                    setGraphic(imgView);
                }else {
                    setGraphic(null);
                }
            }
        });
        tableView.getColumns().add(columnImage);

        TableColumn<FileInfo, String> columnName = new TableColumn<>("Name");
        columnName.setPrefWidth(120);
        columnName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFileName()));
        columnName.setCellFactory(tc -> new TableCell<FileInfo, String>(){
            @Override
            public void updateItem(String fileName, boolean empty){
                if (fileName != null && !empty) {
                    setText(fileName);
                }
                else {
                    setText("");
                }
            }
        });
        tableView.getColumns().add(columnName);

        TableColumn<FileInfo, Number> columnSize = new TableColumn<>("Size");
        columnSize.setPrefWidth(100);
        columnSize.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getSize()));
        columnSize.setCellFactory(tc -> new TableCell<FileInfo, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                if (item != null && !empty) {
                    if (item.longValue() == -1)
                        setText("DIR");
                    else
                        setText(item.toString() + " bytes");
                }
                else
                    setText("");
            }
        });
        tableView.getColumns().add(columnSize);

        TableColumn<FileInfo, Date> columnLastModified = new TableColumn<>("Last modified");
        columnLastModified.setPrefWidth(120);
        columnLastModified.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLastModified()));
        columnLastModified.setCellFactory(tc -> new TableCell<FileInfo, Date>() {
            private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    setText(format.format(item));
                }
                else {
                    setText("");
                }
            }
        });
        tableView.getColumns().add(columnLastModified);

    }

    public void showClientFiles() {
        clientFiles.getItems().addAll(getClientFileNames());
        clientFiles.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                FileInfo file = clientFiles.getSelectionModel().getSelectedItem();
                Path path = baseDir.resolve(file.getFileName());
                if (Files.isDirectory(path)) {
                    parentPaths.push(baseDir);
                    baseDir = path;
                    fillView(clientFiles, getClientFileNames());
                }
            }
        });
    }


    public List<FileInfo> getClientFileNames()  {
        try {
            return Files.list(baseDir)
                    .map(FileInfo::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void upload(ActionEvent actionEvent) throws IOException {
        FileInfo file = clientFiles.getSelectionModel().getSelectedItem();
        Path filePath = baseDir.resolve(file.getFileName());
        System.out.println(filePath.toString());
        networkService.getOutputStream().writeObject(new FileMessage(filePath));
    }

    public void downLoad(ActionEvent actionEvent) throws IOException {
        FileInfo file = serverFiles.getSelectionModel().getSelectedItem();
        networkService.getOutputStream().writeObject(new FileRequest(file.getFileName()));
    }


    public void back(ActionEvent actionEvent) {
        if (!parentPaths.empty()) {
            baseDir = parentPaths.peek();
            parentPaths.pop();
            fillView(clientFiles, getClientFileNames());
        }
    }
}

