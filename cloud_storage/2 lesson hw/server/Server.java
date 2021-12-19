package cloud_app_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8089)) {
            System.out.println("Server start");
            while (true) {
                Socket socket = serverSocket.accept();
                Handler handler = new Handler(socket);
                handler.handle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
