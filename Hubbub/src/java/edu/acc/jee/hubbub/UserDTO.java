package edu.acc.jee.hubbub;

import edu.acc.jee.hubbub.domain.User;
import java.io.Serializable;

public class UserDTO implements Serializable {
    public static final String PASSWORD_PATTERN =
            "^[^<>'%\"]{8,50}$";    
    private String username;
    private String password;

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isValid() {
        if (username == null || password == null)
            return false;
        return (username.matches(User.USERNAME_PATTERN) &&
                password.matches(PASSWORD_PATTERN));
    }

    @Override
    public String toString() {
        return "UserDTO{" + "username=" + username + ", password=" + password + '}';
    }
}
