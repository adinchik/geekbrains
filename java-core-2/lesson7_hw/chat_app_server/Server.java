package ru.geekbrains.java_core2.chat_app_server;

import ru.geekbrains.java_core2.chat_app_server.auth.AuthService;
import ru.geekbrains.java_core2.chat_app_server.auth.inMemoryAuthService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public static final int PORT = 8089;
    private AuthService authService;
    private List<Handler> handlers;

    public Server() {
        this.authService = new inMemoryAuthService();
        this.handlers = new ArrayList<>();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server start");
            while (true) {
                Socket socket = serverSocket.accept();
                new Handler(socket, this).handle();
                System.out.println("Client connected!");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AuthService getAuthService() {

        return authService;
    }
    public synchronized void addAuthorizedClientToList(Handler handler) {

        this.handlers.add(handler);
    }
    public void broadcastMessage(String message) {
        for (Handler handler: handlers) {
            handler.sendMessage(message);
        }
    }

}
