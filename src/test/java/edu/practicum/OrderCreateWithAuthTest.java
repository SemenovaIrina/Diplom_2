package edu.practicum;

import edu.practicum.client.UserClient;
import edu.practicum.models.AnswerForUser;
import edu.practicum.models.User;
import edu.practicum.models.UserAfterCreate;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;

import static edu.practicum.data.UtilsForDataPrepare.emailRandom;
import static edu.practicum.data.UtilsForDataPrepare.stringRandomGenerate;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderCreateTest {
    private static User user =
            new User.Builder()
                    .email(emailRandom(new Random().nextInt(254) + 1))
                    .password(stringRandomGenerate(new Random().nextInt(254) + 1))
                    .name(stringRandomGenerate(new Random().nextInt(254) + 1))
                    .build();

    private static UserAfterCreate userAfterCreate;
    
    //@ParameterizedTest
    //@MethodSource("edu.practicum.general.TestDataForUser#getUserDataForUpdate")
    @Description("Checking the possibility of creating an order with ingredients for registered user")
    public void changeUserDataForRegisteredUserPossible() {
        //выполняем запрос на обновление данных пользователя
        Response response = UserClient.updateWithAuth(user);
        //проверяем статус код ответа
        Assertions.assertEquals(200, response.statusCode(), "Получаемый статус код при обновлении данных пользователя не соответствует ожидаемому");
        //проверим структуру ответа
        assertEquals(response.as(AnswerForUser.class).getClass(), AnswerForUser.class, "Структура ответа при обновлении данных пользователя не соответствует ожидаемой");
        //проверяем тело ответа
        response.then().assertThat().body("success", equalTo(true));
    }

}
