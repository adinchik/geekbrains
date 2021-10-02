package chat_app_server.auth;


import chat_app_server.error.BadRequestException;
import chat_app_server.error.UserNotFoundException;
import chat_app_server.error.WrongCredentialsException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class inMemoryAuthService implements AuthService{
    private List<User> users;
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedInsertStatement;
    private PreparedStatement preparedUpdateNickStatement;
    private PreparedStatement preparedUpdatePassStatement;
    private PreparedStatement preparedDeleteStatement;
    private String insertStatement = "insert into users (login, password, nickname) values (?, ?, ?);";
    private String updateNickStatement = "update users set nickname = ? WHERE login = ?;";
    private String updatePassStatement = "update users set password = ? WHERE login = ?;";
    private String deleteStatement = "delete from users where login = ?;";

    public inMemoryAuthService() {
        try {
            this.users = new ArrayList<>();
            start();
            statement.executeUpdate("drop table if exists users;");
            statement.execute("create table if not exists users (id integer primary key autoincrement, login text, password text, nickname text);");
            preparedInsertStatement = connection.prepareStatement(insertStatement);
            preparedUpdateNickStatement = connection.prepareStatement(updateNickStatement);
            preparedUpdatePassStatement = connection.prepareStatement(updatePassStatement);
            preparedDeleteStatement = connection.prepareStatement(deleteStatement);
            statement.executeUpdate("insert into users (login, password, nickname) values ('log1', 'pass', 'nick1'), ('log2', 'pass', 'nick2'), ('log3', 'pass', 'nick3');");
            addDbToList();
        } catch(SQLException e) {
            e.printStackTrace();
        }


//            this.users = new ArrayList<>(
//                Arrays.asList(
//                        new User("log1", "pass", "nick1"),
//                        new User("log2", "pass", "nick2"),
//                        new User("log3", "pass", "nick3")
//                )
//        );
    }

    private void addDbToList() {
        try {
            this.users.clear();
            try (ResultSet resultSet = statement.executeQuery("select login, password, nickname from users;")) {
                while (resultSet.next()) {
                    this.users.add(new User(resultSet.getString("login"), resultSet.getString("password"),
                            resultSet.getString("nickname")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void start() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:db/online_chat");
        statement = connection.createStatement();
        System.out.println("Auth server started");
    }

    @Override
    public void stop() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (preparedUpdateNickStatement != null) preparedUpdateNickStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (preparedInsertStatement != null) preparedInsertStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (preparedUpdatePassStatement != null) preparedUpdatePassStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public String changeNickname(String oldNick, String newNick) {
        for (User user: users) {
            if (user.getNickname().equals(newNick)) {
                throw new BadRequestException("This nick is busy");
            }
        }
        for (User user: users) {
            if (user.getNickname().equals(oldNick)) {
                user.setNickname(newNick);
                try {
                    preparedUpdateNickStatement.setString(1, newNick);
                    preparedUpdateNickStatement.setString(2, user.getLogin());
                    preparedUpdateNickStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return newNick;
            }
        }
        throw new UserNotFoundException("User not found");
    }

    @Override
    public void changePassword(String nickname, String oldPassword, String newPassword) {
        for (User user: users) {
            if (user.getNickname().equals(nickname)) {
                if (user.getPassword().equals(oldPassword)) {
                    user.setPassword(newPassword);
                    try {
                        preparedUpdatePassStatement.setString(1, newPassword);
                        preparedUpdatePassStatement.setString(2, user.getLogin());
                        preparedUpdatePassStatement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return;
                } else throw new BadRequestException("Wrong password");
            }
        }
        throw new UserNotFoundException("User not found");
    }

    @Override
    public void createNewUser(String login, String password, String nickname) {
        for (User user: users) {
            if (user.getNickname().equals(nickname) || user.getLogin().equals(login)) {
                throw new BadRequestException("This nick or login busy");
            }
        }
        users.add(new User(login, password, nickname));
        try {
            preparedInsertStatement.setString(1, login);
            preparedInsertStatement.setString(2, password);
            preparedInsertStatement.setString(3, nickname);
            preparedInsertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(String nickname) {
        for (User user: users) {
            if (user.getNickname().equals(nickname)){
                this.users.remove(user);
                try {
                    preparedDeleteStatement.setString(1, user.getLogin());
                    preparedDeleteStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
