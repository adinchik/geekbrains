package ru.geekbrains.auth;

import ru.geekbrains.model.UserRegister;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class User {
    private final String login;
    private final String password;
    private final String name;
    private final Path path;

    public User(UserRegister userRegister) {
        this.login = userRegister.getLogin();
        this.password = userRegister.getPassword();
        this.name= userRegister.getName();
        path = Paths.get("storage").resolve(this.login);
        try {
            Files.createDirectory(path);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public User(String login, String name, String password, String path) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.path = Paths.get(path);

    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Path getPath() {
        return path;
    }
}
