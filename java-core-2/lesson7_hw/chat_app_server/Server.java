package ru.geekbrains.java_core2.chat_app_server;

import ru.geekbrains.java_core2.chat_app_server.auth.AuthService;
import ru.geekbrains.java_core2.chat_app_server.auth.inMemoryAuthService;
import ru.geekbrains.java_core2.chat_app_server.error.BadRequestException;
import ru.geekbrains.java_core2.chat_app_server.error.UserNotFoundException;
import ru.geekbrains.java_core2.chat_app_server.error.WrongCredentialsException;

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
    public synchronized void addAuthorizedClientToList(Handler handler) throws WrongCredentialsException {
        for (Handler curHandler: handlers) {
            if (curHandler.getCurrentUser().equals(handler.getCurrentUser())) {
                throw new WrongCredentialsException("This user is online");
            }
        }
        this.handlers.add(handler);
        sendClientsOnline();
    }

    public synchronized void removeAuthorizedClientFromList(Handler handler) {

        this.handlers.remove(handler);
        sendClientsOnline();
    }

    public void broadcastMessage(String message) {
        for (Handler handler: handlers) {
            handler.sendMessage(message);
        }
    }

    public void sendClientsOnline() {
        StringBuilder sb = new StringBuilder("$.list: ");
        for (Handler handler: handlers) {
            sb.append(handler.getCurrentUser()).append(" ");
        }
        broadcastMessage(sb.toString());
    }

    public void sendMessageToOneClient(Handler handler, String message) {
        // nick#: /w nick3 Hello
        //System.out.println("!!!");
        String info[] = message.substring(message.indexOf(':') + 5).split("\\s");
        //System.out.println(info[0] + " " + info[1]);
        for (Handler curHandler: handlers) {
            if (curHandler.getCurrentUser().equals(info[0])) {
                curHandler.sendMessage(handler.getCurrentUser() + ": " + info[1]);
            }
        }
        handler.sendMessage(handler.getCurrentUser() + ": " + info[1]);
    }

    public void changeNick(Handler handler, String message){
        // nick#: /changeNick nick2
        String info[] = message.substring(message.indexOf(':') + 14).split("\\s");
        authService.changeNickname(handler.getCurrentUser(), info[0]);
        handler.setCurrentUser(info[0]);
        sendClientsOnline();
        handler.sendMessage("/changeNick " + info[0]);
    }

    public void changePass(Handler handler, String message) {
        // nick#: /changePass pass1 pass2
        String info[] = message.substring(message.indexOf(':') + 14).split("\\s");
        try {
            authService.changePassword(handler.getCurrentUser(), info[0], info[1]);
        } catch (WrongCredentialsException e) {
            handler.sendMessage("ERROR: Old password is wrong");
        }
    }

    public void createNewUser(Handler handler, String message) {
        // nick#: /createUser login pass nick
        String info[] = message.substring(message.indexOf(':') + 14).split("\\s");
        try {
            authService.createNewUser(info[0], info[1], info[2]);
        } catch (WrongCredentialsException e) {
            handler.sendMessage("ERROR: User with this login or nickname is exist");
        }
    }

}
