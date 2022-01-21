package auth;

import cloud_app_server.Server;
import model.UserAuth;
import model.UserRegister;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private List<User> users;
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedInsertStatement;
    private String insertStatement = "insert into users (login, name, password, path) values (?, ?, ?, ?);";

    public AuthService() {
        try {
            this.users = new ArrayList<>();
            start();
            statement.execute("create table if not exists users (id int auto_increment, login VARCHAR(45), name VARCHAR(45), password VARCHAR(45), path VARCHAR(100), primary key(id));");
            addDbToList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addDbToList() {
        try {
            this.users.clear();
            try (ResultSet resultSet = statement.executeQuery("select login, name, password, path from users;")) {
                while (resultSet.next()) {
                    this.users.add(new User(resultSet.getString("login"), resultSet.getString("name"),
                            resultSet.getString("password"), resultSet.getString("path")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            connection.close();
            preparedInsertStatement.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public boolean isUserExist(UserAuth userAuth) {
        for(User user:users) {
            System.out.println(user.getLogin() + " " + user.getPassword());
            System.out.println(userAuth.getLogin() + " " + userAuth.getPassword());
            if (user.getLogin().equals(userAuth.getLogin()) && user.getPassword().equals(userAuth.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public User createNewUser(UserRegister userRegister) {
        User user = new User(userRegister);
        users.add(user);
        try {
            preparedInsertStatement.setString(1, user.getLogin());
            preparedInsertStatement.setString(2, user.getName());
            preparedInsertStatement.setString(3, user.getPassword());
            preparedInsertStatement.setString(4, user.getPath().toString());
            preparedInsertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUserByLoginAndPassword(UserAuth userAuth) {
        for(User user:users) {
            if (user.getLogin().equals(userAuth.getLogin()) && user.getPassword().equals(userAuth.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public void start() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost/cloud_storage", "root", "1111");
        statement = connection.createStatement();
        preparedInsertStatement = connection.prepareStatement(insertStatement);
        Server.log.info("Auth server started");
    }
}
