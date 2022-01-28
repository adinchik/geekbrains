package ru.geekbrains.model;

import lombok.Data;

@Data
public class UserRegister implements AbstractMessage {
    private String login;
    private String password;
    private String name;


    public UserRegister(String login, String password, String name, boolean status) {
        this.login = login;
        this.password = password;
        this.name = name;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.USER_REGISTER;
    }


}
