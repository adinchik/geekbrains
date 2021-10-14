package chat_app_server;

import chat_app_server.auth.AuthService;
import chat_app_server.auth.inMemoryAuthService;
import chat_app_server.error.WrongCredentialsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class Server {

    public static final int PORT = 8089;
    private AuthService authService;
    //private List<Handler> handlers;
    private Map<String, Handler> handlers;
    public static final Logger log = LogManager.getLogger(Server.class);

    public Server() {
        this.authService = new inMemoryAuthService();
        //this.handlers = new ArrayList<>();
        handlers = new HashMap<>();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            //System.out.println("Server start");
            log.info("Server start");
            while (true) {
                Socket socket = serverSocket.accept();
                ExecutorService executorService = Executors.newCachedThreadPool();
                executorService.execute(() -> {
                    new Handler(socket, this).handle();
                    //System.out.println("Client connected");
                    log.info("Client connected");
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AuthService getAuthService() {

        return authService;
    }
    public synchronized void addAuthorizedClientToList(Handler handler) throws WrongCredentialsException {
        this.handlers.put(handler.getCurrentUser(), handler);
        sendClientsOnline();
    }

    public synchronized void removeAuthorizedClientFromList(Handler handler) {

        this.handlers.remove(handler.getCurrentUser());
        sendClientsOnline();
    }

    public void broadcastMessage(String from, String message) {
        message = String.format("[%s]: %s", from, message);
        for (Handler handler: handlers.values()) {
            handler.sendMessage(message);
        }
    }

    public void sendClientsOnline() {
        StringBuilder sb = new StringBuilder("/list:").append(Handler.REGEX);
        for (Handler handler: handlers.values()) {
            sb.append(handler.getCurrentUser()).append(Handler.REGEX);
        }
        String message = sb.toString();
        for (Handler handler: handlers.values()) {
            handler.sendMessage(message);
        }
    }

    public void sendPrivateMessage(String sender, String recipient, String message, Handler senderHandler) {
        Handler handler = handlers.get(recipient);
        if (handler == null) {
            senderHandler.sendMessage(String.format("ERROR:%s recipient not found: %s", Handler.REGEX, recipient));
            return;
        }
        message = String.format("[%s] -> [%s]: [%s]", sender, recipient, message);
        handler.sendMessage(message);
        senderHandler.sendMessage(message);
    }

    public boolean isNicknameBusy(String nickname) {
        return handlers.containsKey(nickname);
    }
}
