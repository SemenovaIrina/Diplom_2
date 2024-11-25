package edu.practicum.client;

import edu.practicum.data.Constants;
import edu.practicum.models.User;
import edu.practicum.models.UserForLogin;
import edu.practicum.models.UserForLogout;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static RequestSpecification request = given(Constants.REQUEST_SPEC);
    private static RequestSpecification requestWithAuth = given(Constants.REQUEST_SPEC);

    public static void addBearerTokenInHeader(String bearerToken) {
        requestWithAuth = given(Constants.REQUEST_SPEC);
        requestWithAuth = requestWithAuth
                .headers(
                        "Authorization",
                        "Bearer " + bearerToken);
    }

    @Step("Create a user with POST request to /api/auth/register")
    public static Response create(User user) {
        return request
                .body(user)
                .post(Constants.USER_CREATE_REQUEST);
    }

    @Step("Delete a user with DELETE request to /api/auth/user")
    public static Response delete() {
        return requestWithAuth
                .delete(Constants.USER_DELETE_REQUEST);
    }

    @Step("Login a user with POST request to /api/auth/login")
    public static Response login(UserForLogin user) {
        return request
                .body(user)
                .post(Constants.USER_LOGIN_REQUEST);
    }

    @Step("Logout a user with POST request to /api/auth/logout")
    public static void logot(UserForLogout user) {
        request
                .body(user)
                .post(Constants.USER_LOGOUT_REQUEST);
    }

    @Step("Update a user data for registered user with PATCH request to /api/auth/user")
    public static Response updateWithAuth(String user) {
        return requestWithAuth
                .body(user)
                .patch(Constants.USER_UPDATE_REQUEST);
    }

    @Step("Update a user data for unregistered user with PATCH request to /api/auth/user")
    public static Response updateWithoutAuth(String user) {
        return request
                .body(user)
                .patch(Constants.USER_UPDATE_REQUEST);
    }

    public static RequestSpecification getRequest() {
        return request;
    }
}
