package cloud_app_server;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class Handler {
    private Socket socket;
    private InputStream inStream;
    private DataOutputStream out;
    private FileOutputStream fos;

    public Handler(Socket socket) {
        try{
            this.socket = socket;
            inStream = socket.getInputStream();
            out = new DataOutputStream(socket.getOutputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void handle() {
        Thread handlerThread = new Thread(() -> {
            try{
                while (!Thread.currentThread().isInterrupted() && socket.isConnected()) {
                    int nameSize;
                    while((nameSize = inStream.read()) != -1) {
                        byte[] name = new byte[nameSize + 1];
                        inStream.read(name, 0, nameSize);
                        String message = new String(name, "utf-8").trim();
                        if (message.equals("getListOfFiles")) {
                            sendListOfFiles();
                        }
                        else {
                            receiveFile(message);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        handlerThread.start();
    }

    private void sendListOfFiles() {
        String list = "";
        File dir = new File("C:\\Users\\adina\\IdeaProjects\\cloud_storage\\server\\storage\\");
        for(File item : dir.listFiles()) {
            if (item.isFile()) {
                list += item.getName() + "\n";
            }
        }
        try {
            out.writeUTF(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveFile(String fileName) {
        try {
            System.out.println(fileName);
            File file = new File("C:\\Users\\adina\\IdeaProjects\\cloud_storage\\server\\storage\\" + fileName);
            file.createNewFile();
            fos = new FileOutputStream(file);
            byte[] fileSizeBuf = new byte[8];
            inStream.read(fileSizeBuf, 0, 8);
            ByteBuffer buf = ByteBuffer.allocate(Long.BYTES);
            buf.put(fileSizeBuf);
            buf.flip();
            long fileSize = buf.getLong();
            System.out.println(fileSize);
            int read = 0;
            byte[] data = new byte[1024];
            while (read < fileSize) {
                int k = inStream.read(data);
                read += k;
                System.out.println("! " + k);
                fos.write(data, 0, k);
            }
            out.writeUTF("File was received and stored in the server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}