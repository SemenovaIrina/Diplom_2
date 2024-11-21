package edu.practicum.client;

import edu.practicum.data.Constants;
import edu.practicum.models.User;
import edu.practicum.models.UserForLogin;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static RequestSpecification request = given(Constants.REQUEST_SPEC);

    @Step("Creating a user with POST request to /api/auth/register")
    public static Response create(User user) {
        return request
                .body(user)
                .post(Constants.USER_CREATE_REQUEST);

    }

    @Step("Delete a user with DELETE request to /api/auth/user")
    public static Response delete(String bearerToken) {
        return request
                .headers(
                        "Authorization",
                        "Bearer " + bearerToken)
                .delete(Constants.USER_DELETE_REQUEST);

    }

    @Step("Login a user with POST request to /api/auth/login")
    public static Response login(UserForLogin user) {
        return request
                .body(user)
                .post(Constants.USER_LOGIN_REQUEST);
    }
}
