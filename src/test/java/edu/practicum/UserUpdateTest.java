package edu.practicum;

import edu.practicum.client.UserClient;
import edu.practicum.models.AnswerForUser;
import edu.practicum.models.User;
import edu.practicum.models.UserAfterCreate;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;

import static edu.practicum.data.UtilsForDataPrepare.emailRandom;
import static edu.practicum.data.UtilsForDataPrepare.stringRandomGenerate;
import static edu.practicum.general.Checks.checksCorrectCreateAndLoginUser;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserUpdateTest {
    private static User user =
            new User.Builder()
                    .email(emailRandom(new Random().nextInt(254) + 1))
                    .password(stringRandomGenerate(new Random().nextInt(254) + 1))
                    .name(stringRandomGenerate(new Random().nextInt(254) + 1))
                    .build();

    private static UserAfterCreate userAfterCreate;

    @BeforeAll
    public static void setUp() {
        //чтобы осуществлять авторизованные запросы, нужно чтобы пользователь был успешно создан и мог залогиниться
        //делать это не нужно перед каждым тестом, достаточно один раз перед всеми тестами
        userAfterCreate = checksCorrectCreateAndLoginUser(user); // возвращается user без лишнего в токене
        //сформируем дополнительную спецификацию для аторизованных запросов (добавим токен в заголовок всех последующих запросов для этого user)
        UserClient.addBearerTokenInHeader(userAfterCreate.getAccessToken());
    }

    @ParameterizedTest
    @MethodSource("edu.practicum.general.DataForUser#getUserDataForUpdate")
    @Description("Checking the possibility of changing your data for a registered user")
    public void changeUserDataForRegisteredUserPossible(String user) {
        //выполняем запрос на обновление данных пользователя
        Response response = UserClient.updateWithAuth(user);
        //проверяем статус код ответа
        Assertions.assertEquals(200, response.statusCode(), "Получаемый статус код при обновлении данных пользователя не соответствует ожидаемому");
        //проверим структуру ответа
        assertEquals(response.as(AnswerForUser.class).getClass(), AnswerForUser.class, "Структура ответа при обновлении данных пользователя не соответствует ожидаемой");
        //проверяем тело ответа
        response.then().assertThat().body("success", equalTo(true));
    }

    @ParameterizedTest
    @MethodSource("edu.practicum.general.DataForUser#getUserDataForUpdate")
    @Description("Checking the possibility of changing your data for a unregistered user")
    public void changeUserDataForUnregisteredUserNotPossible(String user) {
        //выполняем запрос на обновление данных пользователя
        Response response = UserClient.updateWithoutAuth(user);
        //проверяем статус код ответа
        Assertions.assertEquals(401, response.statusCode(), "Получаемый статус код при обновлении данных пользователя не соответствует ожидаемому");
        //проверяем тело ответа
        response.then().assertThat().body("message", equalTo("You should be authorised"));
    }

    @AfterAll
    public static void tearDown() {
        UserClient.delete();
    }
}
