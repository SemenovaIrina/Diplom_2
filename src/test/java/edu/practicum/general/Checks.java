package edu.practicum.general;

import edu.practicum.client.UserClient;
import edu.practicum.models.User;
import edu.practicum.models.UserAfterCreate;
import edu.practicum.models.UserForLogin;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Checks {
    @Step("Checking the possibility of creating a new user and his login in the system")
    public static UserAfterCreate checksCorrectCreateAndLoginUser(User user) {
        //выполняем запрос на создание пользователя
        Response response = UserClient.create(user);
        UserAfterCreate userAfterCreate = response.as(UserAfterCreate.class);
        //проверяем статус код ответа
        Assertions.assertEquals(200, response.statusCode(), "Получаемый статус код при создании пользователя не соответствует ожидаемому");
        //проверяем тело ответа
        response.then().assertThat().body(notNullValue());
        assertEquals(response.as(UserAfterCreate.class).getClass(), UserAfterCreate.class, "Структура ответа не соответствует ожидаемой");
        //чтобы убедиться, что пользователь действительно создался попытаемся залогинить его
        userAfterCreate.setAccessToken(userAfterCreate.getAccessToken()); //убираем все лишнее из токена в ответе
        response = UserClient.login(
                new UserForLogin.Builder()
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .build());
        //проверяем статус код, полученный при логине
        Assertions.assertEquals(200, response.statusCode(), "Получаемый статус код при логине нового пользователя не соответствует ожидаемому");
        //проверяем тело ответа
        response.then().assertThat().body(notNullValue());
        assertEquals(userAfterCreate.getClass(), UserAfterCreate.class, "Структура ответа не соответствует ожидаемой");
        response.then().assertThat().body("success", equalTo(true));
        //возвращаем тело ответа после логина (в виде экземпляра соответствующего класса, чтобы сохранились преобразования токена), чтобы можно было в последствии удалить созданного пользователя
        return userAfterCreate;
    }

    @Step("Checking the impossibility of the user's login with an error in the email or password")
    public static void checkLoginUserWithNotCorrectEmailOrPassword(UserForLogin user) {
        //пытаемся залогинить пользователя
        Response response = UserClient.login(user);
        //проверяем статус код ответа
        Assertions.assertEquals(401, response.statusCode(), "Получаемый статус код при логине пользователя не соответствует ожидаемому");
        //проверяем тело ответа
        response.then().assertThat().body("message", equalTo("email or password are incorrect"));
    }
}
