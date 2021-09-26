package ru.geekbrains.java2.lesson6_hw;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Handler {
    private static int clientCounter = 0;
    private static ArrayList<Handler> handlers = new ArrayList<>();
    private int clientNumber;
    private Socket socket;
    private DataInputStream in;
    public DataOutputStream out;
    private Thread handlerThread;

    public Handler (Socket socket) {
        try {
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            clientCounter++;
            this.clientNumber = clientCounter;
            handlers.add(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handle() {
        handlerThread = new Thread(() -> {
            while (!this.handlerThread.isInterrupted() && socket.isConnected()) {
                try {
                    String message = in.readUTF();
                    System.out.printf("Client #%d: %s\n", this.clientNumber, message);
                    sendToAllClients(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        handlerThread.start();
    }

    private void sendToAllClients(String message) {
        for (Handler handler: handlers) {
            try {
                handler.out.writeUTF("The client #" + Integer.toString(this.clientNumber) + ": " + message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
