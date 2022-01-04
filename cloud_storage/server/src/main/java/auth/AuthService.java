package auth;

import model.UserAuth;
import model.UserRegister;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuthService {
    private List<User> users;

    public AuthService() {
        this.users = new ArrayList<>();
    }


    public boolean isUserExist(UserAuth userAuth) {
        for(User user:users) {
            if (user.getLogin().equals(userAuth.getLogin()) && user.getPassword().equals(userAuth.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public User createNewUser(UserRegister userRegister) {
        User user = new User(userRegister);
        users.add(user);
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
}
