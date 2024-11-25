package edu.practicum.models;

public class UserForLogout {
    private String token;

    public UserForLogout(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
