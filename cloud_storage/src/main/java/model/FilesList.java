package model;

import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;


@Data
public class FilesList implements AbstractMessage {
    private final List<File> files;

    public FilesList(Path path) throws IOException {
        files = Arrays.asList(new File(String.valueOf(path)).listFiles());
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.FILES_LIST;
    }
}
