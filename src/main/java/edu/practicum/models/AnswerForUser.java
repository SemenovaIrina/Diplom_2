package edu.practicum.models;

public class AnswerForUser {
    private boolean success;
    private UserInAnswer user;

    public AnswerForUser(boolean success, UserInAnswer user) {
        this.success = success;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public UserInAnswer getUser() {
        return user;
    }

    public void setUser(UserInAnswer user) {
        this.user = user;
    }
}
