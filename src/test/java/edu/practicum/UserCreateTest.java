package edu.practicum;

import edu.practicum.client.UserClient;
import edu.practicum.models.User;
import edu.practicum.models.UserAfterCreate;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static edu.practicum.general.Checks.checksCorrectCreateAndLoginUser;
import static org.hamcrest.Matchers.equalTo;

public class UserCreateTest {
    private UserAfterCreate userAfterCreate;

    @ParameterizedTest
    @MethodSource("edu.practicum.general.DataForUser#getCorrectUserData")
    @Description("Checking the creation of a user with correct data")
    public void createNewUserWithCorrectData(User user) {
        userAfterCreate = checksCorrectCreateAndLoginUser(user);
        //сформируем дополнительную спецификацию для аторизованных запросов (добавим токен в заголовок всех последующих запросов для этого user)
        UserClient.addBearerTokenInHeader(userAfterCreate.getAccessToken());
        //удялем созданного пользователя
        UserClient.delete();
    }

    @ParameterizedTest
    @MethodSource("edu.practicum.general.DataForUser#getEqualUserData")
    @Description("Checking if it is impossible to create two identical users")
    public void createTwoEqualUserNotPossible(User user1, User user2) {
        userAfterCreate = checksCorrectCreateAndLoginUser(user1);
        //выполняем запрос на создание точно такого же пользователя
        Response response = UserClient.create(user2);
        //проверяем статус код ответа
        Assertions.assertEquals(403, response.statusCode(), "Получаемый статус код при повторном создании пользователя не соответствует ожидаемому");
        //проверяем тело ответа
        response.then().assertThat().body("message", equalTo("User already exists"));
        //сформируем дополнительную спецификацию для аторизованных запросов (добавим токен в заголовок всех последующих запросов для этого user)
        UserClient.addBearerTokenInHeader(userAfterCreate.getAccessToken());
        //удаляем созданного пользователя
        UserClient.delete();
    }

    @ParameterizedTest
    @MethodSource("edu.practicum.general.DataForUser#getUserDataWithoutRequiredField")
    @Description("Checking if it is impossible to create a user without a required field")
    public void createNewUserWithoutRequiredFildNotPossible(User user) {
        //выполняем запрос на создание пользователя
        Response response = UserClient.create(user);
        //проверяем статус код ответа
        Assertions.assertEquals(403, response.statusCode(), "Получаемый статус код при создании пользователя не соответствует ожидаемому");
        //проверяем тело ответа
        response.then().assertThat().body("message", equalTo("Email, password and name are required fields"));
        //пользователь не создается, поэтому его не нужно удалять
    }

}
