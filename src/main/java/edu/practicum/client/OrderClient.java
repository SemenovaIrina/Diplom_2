package edu.practicum.client;

import edu.practicum.data.Constants;
import edu.practicum.models.IngredientsHashList;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static RequestSpecification request = given(Constants.REQUEST_SPEC);
    private static RequestSpecification requestWithAuth = given(Constants.REQUEST_SPEC);

    public static void addBearerTokenInHeader(String bearerToken) {
        requestWithAuth = given(Constants.REQUEST_SPEC);
        requestWithAuth = requestWithAuth
                .headers(
                        "Authorization",
                        "Bearer " + bearerToken);
    }

    @Step("Create an order with POST request to /api/orders")
    public static Response createWithAuth(IngredientsHashList ingredients) {
        return requestWithAuth
                .body(ingredients)
                .post(Constants.ORDER_CREATE_REQUEST);
    }

    @Step("Create an order with POST request to /api/orders")
    public static Response createWithoutAuth(IngredientsHashList ingredients) {
        return request
                .body(ingredients)
                .post(Constants.ORDER_CREATE_REQUEST);
    }

    @Step("Get ingredient list with GET request to /api/ingredients")
    public static Response ingredientList() {
        return request
                .get(Constants.INGREDIENT_LIST);
    }

    @Step("Get order list with auth with GET request to /api/orders")
    public static Response orderListWithAuth() {
        return requestWithAuth
                .get(Constants.ORDER_LIST);
    }

    @Step("Get order list without auth with GET request to /api/orders")
    public static Response orderListWithoutAuth() {
        return request
                .get(Constants.ORDER_LIST);
    }

    public static RequestSpecification getRequest() {
        return request;
    }
}
