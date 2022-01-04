package cloud_app_client;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileInfo {
    private String fileName;
    private boolean isDirectory;
    private long size;

    public FileInfo(Path path) {
        fileName = path.getFileName().toString();
        isDirectory = Files.isDirectory(path);
        if (!isDirectory) {
            size = path.toFile().length();
        } else {
            size = 0;
        }
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public long getSize() {
        return size;
    }

}
