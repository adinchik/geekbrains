package ru.geekbrains.java_core2.chat_app_server.error;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String message) {
        super(message);
    }
}
