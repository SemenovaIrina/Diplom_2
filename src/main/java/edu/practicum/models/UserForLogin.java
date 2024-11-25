package edu.practicum.models;

public class UserForLogin {
    private String email;
    private String password;

    public static class Builder {
        private String email = null;
        private String password = null;

        public UserForLogin.Builder email(String val) {
            this.email = val;
            return this;
        }

        public UserForLogin.Builder password(String val) {
            this.password = val;
            return this;
        }

        public UserForLogin build() {
            return new UserForLogin(this);
        }
    }

    public UserForLogin(UserForLogin.Builder builder) {
        email = builder.email;
        password = builder.password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
