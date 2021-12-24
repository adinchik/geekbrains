package model;

import lombok.Data;

import java.io.File;

@Data
public class FileRequest implements AbstractMessage{
    private final String fileName;

    public FileRequest(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.FILE_REQUEST;
    }
}
