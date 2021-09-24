package ru.geekbrains.java2.lesson6_hw;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static final int PORT = 8089;

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server start");
            while (true) {
                Socket socket = serverSocket.accept();
                Handler h = new Handler(socket);
                System.out.println("Client connected!");
                h.handle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
