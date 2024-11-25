package edu.practicum.models;

public class User {
    private String email;
    private String password;
    private String name;

    public static class Builder {
        private String email = null;
        private String password = null;
        private String name = null;

        public Builder email(String val) {
            this.email = val;
            return this;
        }

        public Builder password(String val) {
            this.password = val;
            return this;
        }

        public Builder name(String val) {
            this.name = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public User(Builder builder) {
        email = builder.email;
        password = builder.password;
        name = builder.name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
