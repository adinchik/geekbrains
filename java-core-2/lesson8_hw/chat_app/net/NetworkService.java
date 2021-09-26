package chat_app.net;

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
        Thread t = new Thread(()->{
            while (!Thread.currentThread().isInterrupted() && socket.isConnected()) {
                try {
                    String message = in.readUTF();
                    System.out.println(message);
                    chatMessageService.receive(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        this.socket.close();
                        System.out.println(this.socket.isClosed());
                        Thread.currentThread().interrupt();
                    }
                    catch (Exception error){
                        error.printStackTrace();
                    }
                }
            }
        });
        t.setDaemon(true);
        t.start();
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
