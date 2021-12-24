package model;

import lombok.Data;

@Data
public class UserAuth implements AbstractMessage {

    private String login;
    private String password;
    private boolean status;

    public UserAuth(String login, String password, boolean status) {
        this.login = login;
        this.password = password;
        this.status = status;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.USER_AUTH;
    }

    public boolean getStatus() {
        return this.status;
    }
}
