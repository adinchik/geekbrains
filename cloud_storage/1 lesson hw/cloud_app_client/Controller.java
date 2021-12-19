package cloud_app_client;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public TextArea outputArea;
    public TextField inputField;
    public Socket socket;
    public FileInputStream fis;
    public DataInputStream in;
//    public DataOutputStream out;
    public OutputStream outStream;

    public void sendCommand(ActionEvent actionEvent) {
        String text = inputField.getText();  //sendFile Path:C:\Users\adina\hello.txt FileName:hello.txt
        System.out.println(text);
        if (text.isEmpty()) return;
        String[] parsed = text.split(" ");
        if (parsed[0].equals("sendFile")) {
            String path = parsed[1].substring(5); //Path:
            String fileName = parsed[2].substring(9); //FileName:
            sendFile(path, fileName);
            outputArea.appendText(text + "\n");
        } else if (parsed[0].equals("getListOfFiles")) {
            sendListOfFiles(parsed[0]);
            outputArea.appendText(text + "\n");
        }
        inputField.clear();
    }

    public void sendListOfFiles(String s) {
        try {
            byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
            outStream.write(bytes.length);
            outStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(String path, String fileName) {
        try
        {
            File file = new File(path);
            byte[] bytes = new byte[1024];
            fis = new FileInputStream(file);
            System.out.println(path);
            System.out.println(fileName);
            byte[] name = fileName.getBytes(StandardCharsets.UTF_8);
            outStream.write(name.length);
            outStream.write(name);
            long fileSize = file.length();
            ByteBuffer buf = ByteBuffer.allocate(Long.BYTES);
            buf.putLong(fileSize);
            outStream.write(buf.array());
            int count;
            while ((count = fis.read(bytes, 0, bytes.length)) > 0) {
                outStream.write(bytes, 0, count);
            }
            outStream.flush();
            System.out.println("File was send");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMessages() {
        Thread t = new Thread(()->{
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String message = in.readUTF();
                    System.out.println(message);
                    if (message.length() == 0)
                        outputArea.appendText("There is nothing in the directory" + "\n");
                    else
                        outputArea.appendText(message + "\n");
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
            socket = new Socket("localhost", 8089);
            in = new DataInputStream(socket.getInputStream());
            outStream = socket.getOutputStream();
            //out = new DataOutputStream(socket.getOutputStream());
            readMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

