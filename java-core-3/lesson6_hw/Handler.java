package chat_app_server;

import chat_app_server.error.UserNotFoundException;
import chat_app_server.error.WrongCredentialsException;
import org.apache.logging.log4j.Level;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Handler {
    public static final String REGEX = "%&%";
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
            boolean f = authorized();
            try {
                this.socket.setSoTimeout(0);
                server.log.info("Client authorized");
            } catch(Exception e) {
                e.printStackTrace();
            }
            try{
                while (f && !this.handlerThread.isInterrupted() && socket.isConnected()) {
                    String message = in.readUTF();
                    //System.out.println(message);
                    server.log.info("Message recieved");
                    handleMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (f)
                    server.removeAuthorizedClientFromList(this);
                else {
                    try {
                        this.socket.close();
                        System.out.println(this.socket.isClosed());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                server.getAuthService().stop();
            }
        });
        handlerThread.start();
    }

    public boolean authorized() {
        try {
            this.socket.setSoTimeout(2000);
            while (true) {
                try {
                    String message = in.readUTF();
                    if (message.startsWith("/auth") || message.startsWith("/register")) {
                        if (handleMessage(message)) return true;
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                    server.log.error("Client not connected in 2 sec");
                    sendMessage("ERROR:" + REGEX + "Client not connect in 2 sec");
                    return false;
                }
            }
        } catch (Exception error) {
            error.printStackTrace();
            return false;
        }
    }

    private boolean handleMessage(String message) {
        try {
            String[] parsed = message.split(REGEX);
            //System.out.println(parsed[0]);
            switch (parsed[0]) {
                case "/w" :
                    server.sendPrivateMessage(this.currentUser, parsed[1], parsed[2], this);
                    server.log.info("Server received private message");
                    break;
                case "/ALL" :
                    server.broadcastMessage(this.currentUser, parsed[1]);
                    server.log.info("Server received broadcast message");
                    break;
                case "/change_nick" :
                    String nick = server.getAuthService().changeNickname(this.currentUser, parsed[1]);
                    server.removeAuthorizedClientFromList(this);
                    this.currentUser = nick;
                    server.addAuthorizedClientToList(this);
                    sendMessage("/change_nick_ok");
                    server.log.info("Server has changed nickname");
                    break;
                case "/change_pass" :
                    server.getAuthService().changePassword(this.currentUser, parsed[1], parsed[2]);
                    sendMessage("/change_pass_ok");
                    server.log.info("Server has changed password");
                    break;
                case "/remove" :
                    server.getAuthService().deleteUser(this.currentUser);
                    this.socket.close();
                    server.log.info("Server has removed user");
                    break;
                case "/register" :
                    server.getAuthService().createNewUser(parsed[1], parsed[2], parsed[3]);
                    sendMessage("register_ok");
                    server.log.info("Server has registered new user");
                    break;
                case "/auth" :
                    server.getAuthService().start();
                    this.currentUser = server.getAuthService().getNicknameByLoginAndPassword(parsed[1], parsed[2]);
                    if (server.isNicknameBusy(currentUser)) {
                        sendMessage("ERROR:" + REGEX  + "You are clone!");
                        server.log.error("Clone tried to connect");
                    } else {
                        this.server.addAuthorizedClientToList(this);
                        sendMessage("/authok:" + REGEX + this.currentUser);
                        server.log.info("User authorized");
                        return true;
                    }
                    break;
                default:
                    sendMessage("ERROR:" + REGEX + "command not found");
                    server.log.error("Command not found");
            }
        } catch (Exception e) {
            sendMessage("ERROR:" + REGEX + e.getMessage());
            server.log.throwing(Level.ERROR, e);
        }
        return false;
    }
    public void sendMessage(String message) {
        try {
            this.out.writeUTF(message);
            //System.out.println("Send message to client: " + message);
            server.log.info("Send message to client" + message);
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
