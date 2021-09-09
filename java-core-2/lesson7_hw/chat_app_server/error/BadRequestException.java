package ru.geekbrains.java_core2.chat_app_server.error;

public class BadRequestException extends Exception{
    public BadRequestException(String message) {
        super(message);
    }
}
