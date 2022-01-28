package ru.geekbrains.model;

import lombok.Data;

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
