package auth;

import cloud_app_server.Server;
import lombok.Data;
import model.UserAuth;
import model.UserRegister;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
public class User {
    private String login;
    private String password;
    private String name;
    Path path;


    public User(UserRegister userRegister) {
        this.login = userRegister.getLogin();
        this.password = userRegister.getPassword();
        this.name= userRegister.getName();
        path = Paths.get("storage").resolve(this.login);
        try {
            Files.createDirectory(path);
        }
        catch (IOException e) {
            Server.log.error(e);
        }

    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }


}
