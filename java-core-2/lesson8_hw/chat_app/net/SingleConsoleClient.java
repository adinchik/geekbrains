package chat_app.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SingleConsoleClient {
    private static final int PORT = 8089;
    private static final String HOST = "localhost";
    private DataInputStream in;
    private DataOutputStream out;
    private Thread clientThread;

    public static void main(String[] args) {
        new SingleConsoleClient().start();
    }

    public void start() {
        try (Socket socket = new Socket(HOST, PORT)) {
            System.out.println("Client start!");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            clientThread = new Thread(() -> {
                try (Scanner sc = new Scanner(System.in)) {
                    System.out.println("Enter message for sending to server >>>> \n");
                    while (!this.clientThread.isInterrupted()) {
                        if (sc.hasNextLine()) {
                            String message = sc.nextLine();
                            out.writeUTF(message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            clientThread.start();

            while (true) {
                String message = in.readUTF();
                if (message.startsWith("/end")) {
                    shutdown();
                    break;
                }
                System.out.println("Received: " + message);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

    private void shutdown() {
        if (clientThread.isAlive()) clientThread.interrupt();
        System.out.println("Client stop");
    }
}
