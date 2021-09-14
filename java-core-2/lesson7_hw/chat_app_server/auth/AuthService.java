package ru.geekbrains.java_core2.chat_app_server.auth;

import ru.geekbrains.java_core2.chat_app_server.error.UserNotFoundException;
import ru.geekbrains.java_core2.chat_app_server.error.WrongCredentialsException;

public interface AuthService {
    void start();
    void stop();
    String getNicknameByLoginAndPassword(String login, String password) throws WrongCredentialsException, UserNotFoundException;
    void changeNickname(String oldNick, String newNick);
    void changePassword(String nickname, String oldPassword, String newPassword) throws WrongCredentialsException;
    void createNewUser(String login, String password, String nickname) throws WrongCredentialsException;
    void deleteUser(String nickname);
}
