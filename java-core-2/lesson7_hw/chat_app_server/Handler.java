package ru.geekbrains.java_core2.chat_app_server;

import ru.geekbrains.java_core2.chat_app_server.auth.User;
import ru.geekbrains.java_core2.chat_app_server.error.UserNotFoundException;
import ru.geekbrains.java_core2.chat_app_server.error.WrongCredentialsException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Handler {
    private Socket socket;
    private DataInputStream in;
    public DataOutputStream out;
    private Thread handlerThread;
    private Server server;
    private String currentUser;

    public Handler (Socket socket, Server server) {
        try {
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            this.server = server;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handle() {
        handlerThread = new Thread(() -> {
            authorized();
            try{
                while (!this.handlerThread.isInterrupted() && socket.isConnected()) {
                    String message = in.readUTF();
                    System.out.printf("Client #%s: %s\n", this.currentUser, message);
                    String sbString = message.substring(message.indexOf(':') + 2);
                    if (sbString.startsWith("/w ")) {
                        server.sendMessageToOneClient(this, message);
                    } else if (sbString.startsWith("/changeNick ")) {
                        server.changeNick(this, message);
                    } else if (sbString.startsWith("/changePass ")) {
                        server.changePass(this, message);
                    } else if (sbString.startsWith("/createUser ")) {
                        server.createNewUser(this, message);
                    }
                    else {
                        server.broadcastMessage(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                server.removeAuthorizedClientFromList(this);
            }
        });
        handlerThread.start();
    }

    public void authorized() {
        while (true) {
            try {
                //System.out.println("!");
                String message = in.readUTF();
                //System.out.println(message);
                if (message.startsWith("auth:")) {
                    String credentials[] = message.substring(6).split("\\s");
                    try {
                        System.out.println(credentials[0] + " " + credentials[1]);
                        this.currentUser = server.getAuthService().getNicknameByLoginAndPassword(credentials[0], credentials[1]);
                        this.server.addAuthorizedClientToList(this);
                        sendMessage("authok: " + this.currentUser);
                        break;
                    } catch (WrongCredentialsException e) {
                        sendMessage("ERROR: " + e.getMessage());
                    } catch (UserNotFoundException e) {
                        sendMessage("ERROR: User not found");
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        try {
            this.out.writeUTF(message);
            System.out.println("Send message to client: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentUser(String newNickname){
        this.currentUser = newNickname;
    }

    public String getCurrentUser() {
        return this.currentUser;
    }
}
