package ru.geekbrains.cloud_app_client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Date;

public class FileInfo {
    private String fileName;
    private boolean isDirectory;
    private long size;
    private Date lastModified;

    public FileInfo(Path path) {
        fileName = path.getFileName().toString();
        isDirectory = Files.isDirectory(path);
        if (!isDirectory) {
            size = path.toFile().length();
        } else {
            size = -1;
        }
        long time = new File(String.valueOf(path)).lastModified();
        lastModified = new Date(time);

    }

    public FileInfo(File file) {
        fileName = file.getName();
        isDirectory = file.isDirectory();
        if (!isDirectory) {
            size = file.length();
        } else {
            size = 0;
        }
        long time = file.lastModified();
        lastModified = new Date(time);
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

    public Date getLastModified() {return lastModified;}

}
