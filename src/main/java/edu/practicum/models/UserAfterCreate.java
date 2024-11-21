package edu.practicum.models;

public class UserAfterCreate {
    private boolean success;
    private UserInAnswer user;
    private String accessToken;
    private String refreshToken;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken.substring(7); //убираем Bearer, присоединенное к токену в ответе
    }

    public UserInAnswer getUser() {
        return user;
    }

    public void setUser(UserInAnswer user) {
        this.user = user;
    }
}
