package ru.geekbrains.java_core2.chat_app_server.auth;

import ru.geekbrains.java_core2.chat_app_server.error.BadRequestException;
import ru.geekbrains.java_core2.chat_app_server.error.UserNotFoundException;
import ru.geekbrains.java_core2.chat_app_server.error.WrongCredentialsException;

import javax.security.auth.login.CredentialException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class inMemoryAuthService implements AuthService{
    private List<User> users;

    public inMemoryAuthService() {
        this.users = new ArrayList<>(
                Arrays.asList(
                        new User("log1", "pass", "nick1"),
                        new User("log2", "pass", "nick2"),
                        new User("log3", "pass", "nick3")
                )
        );
    }
    @Override
    public void start() {
        System.out.println("Auth server started");
    }

    @Override
    public void stop() {
        System.out.println("Auth service stopped");
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) throws WrongCredentialsException, UserNotFoundException {
        for (User user: users) {
            if (login.equals(user.getLogin())) {
                if (password.equals(user.getPassword())) {
                    return user.getNickname();
                } else throw new WrongCredentialsException("Wrong Credentials");
            }
        }
        throw new UserNotFoundException("User not found");

    }

    @Override
    public void changeNickname(String oldNick, String newNick) {
        for (User user: users) {
            if (user.getNickname() == oldNick) {
                user.setNickname(newNick);
            }
        }
    }

    @Override
    public void changePassword(String nickname, String oldPassword, String newPassword) throws WrongCredentialsException {
        for (User user: users) {
            if (user.getNickname().equals(nickname)) {
                if (user.getPassword().equals(oldPassword)) {
                    user.setPassword(newPassword);
                } else throw new WrongCredentialsException("");
            }
        }
    }

    @Override
    public void createNewUser(String login, String password, String nickname) throws WrongCredentialsException{
        for (User user: users) {
            if (user.getNickname().equals(nickname) || user.getLogin().equals(login)) {
                throw new WrongCredentialsException("");
            }
        }
        users.add(new User(login, password, nickname));
    }

    @Override
    public void deleteUser(String nickname) {

    }
}
