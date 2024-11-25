package edu.practicum.data;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Constants {


    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    public static final String USER_CREATE_REQUEST = "/api/auth/register";
    public static final String USER_DELETE_REQUEST = "/api/auth/user";
    public static final String USER_LOGIN_REQUEST = "/api/auth/login";
    public static final String USER_UPDATE_REQUEST = "/api/auth/user";
    public static final String USER_LOGOUT_REQUEST = "/api/auth/logout";
    public static final String INGREDIENT_LIST = "/api/ingredients";
    public static final String ORDER_CREATE_REQUEST = "/api/orders";
    public static final String ORDER_LIST = "/api/orders";

    public static final RequestSpecification REQUEST_SPEC = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .setContentType(ContentType.JSON)
            .build();


}
