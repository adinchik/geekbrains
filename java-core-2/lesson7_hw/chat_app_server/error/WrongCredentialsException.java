package ru.geekbrains.java_core2.chat_app_server.error;

public class WrongCredentialsException extends Exception{
    public WrongCredentialsException(String message) {
        super(message);
    }
}
