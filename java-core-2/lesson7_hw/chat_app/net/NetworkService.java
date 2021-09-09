package ru.geekbrains.java_core_2.chat_app.net;

import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkService {
    private static final int PORT = 8089;
    private static final String HOST = "localhost";
    private DataInputStream in;
    private DataOutputStream out;
    private ChatMessageService chatMessageService;
    private Socket socket;

    public NetworkService(ChatMessageService chatMessageService) throws IOException {
        this.chatMessageService = chatMessageService;
        this.socket = new Socket(HOST, PORT);
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new DataInputStream((socket.getInputStream()));

    }

    public void readMessages() {
//        Thread t = new Thread(()->{
//            while (!Thread.currentThread().isInterrupted()) {
//                try {
//                    String message = in.readUTF();
//                    System.out.println(message);
//                    chatMessageService.receive(message);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        t.setDaemon(true);
//        t.start();

        Runnable task = () -> {
            Platform.runLater(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        String message = in.readUTF();
                        System.out.println(message);
                        chatMessageService.receive(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("I'm running later...");
            });
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public void sendMessage(String message) {
        try {
            //System.out.println(message);
            this.out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Socket getSocket() {
        return this.socket;
    }

}
