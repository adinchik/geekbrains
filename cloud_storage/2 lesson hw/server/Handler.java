package cloud_app_server;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Handler {
    private static final int BUFFER_SIZE = 8192;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Path currentDir;
    private byte[] buffer;

    public Handler(Socket socket) {
        try{
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            currentDir = Paths.get("C:\\Users\\adina\\IdeaProjects\\cloud_storage\\server\\storage\\");
            buffer = new byte[BUFFER_SIZE];
            sendListFiles();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getFileNames() throws IOException {
        return Files.list(currentDir)
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
    }

    private void sendListFiles() {
        try {
            out.writeUTF("#list");
            List<String> names = getFileNames();
            out.writeInt(names.size());
            for (String name: names) {
                out.writeUTF(name);
            }
            out.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void handle() {
        Thread handlerThread = new Thread(() -> {
            try{
                while (!Thread.currentThread().isInterrupted() && socket.isConnected()) {
                    String command = in.readUTF();
                    System.out.println(command);
                    if (command.equals("#upload")) {
                        String fileName = in.readUTF();
                        long size = in.readLong();
                        try (FileOutputStream fos = new FileOutputStream(currentDir.resolve(fileName).toFile()))  {
                            for (int i = 0; i < (size + BUFFER_SIZE - 1) / BUFFER_SIZE; i++) {
                                int read = in.read(buffer);
                                fos.write(buffer, 0, read);
                            }
                        }
                    }
                    else {
                        String fileName = in.readUTF();
                        System.out.println(fileName);
                        Path filePath = currentDir.resolve(fileName);
                        System.out.println(filePath.toString());
                        out.writeUTF("#upload");
                        out.writeUTF(fileName);
                        System.out.println(Files.size(filePath));
                        out.writeLong(Files.size(filePath));
                        out.write(Files.readAllBytes(filePath));
                        out.flush();
                    }
                    sendListFiles();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        handlerThread.start();
    }

}