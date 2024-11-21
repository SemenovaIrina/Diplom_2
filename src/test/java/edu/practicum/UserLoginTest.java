package edu.practicum;

import edu.practicum.client.UserClient;
import edu.practicum.models.User;
import edu.practicum.models.UserAfterCreate;
import edu.practicum.models.UserForLogin;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

import static edu.practicum.data.UtilsForDataPrepare.*;
import static edu.practicum.general.Checks.checkLoginUserWithNotCorrectEmailOrPassword;
import static edu.practicum.general.Checks.checksCorrectCreateAndLoginUser;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class UserLoginTest {
    private static User user =
            new User.Builder()
                    .email(emailRandom(new Random().nextInt(254) + 1))
                    .password(stringRandomGenerate(new Random().nextInt(254) + 1))
                    .name(stringRandomGenerate(new Random().nextInt(254) + 1))
                    .build();

    private static UserAfterCreate userAfterCreate;

    @BeforeAll
    public static void setUp() {
        //чтобы проверять логин пользователя нужно чтобы он был успешно создан и мог залогиниться
        //делать это не нужно перед каждым тестом, достаточно один раз перед всеми тестами
        userAfterCreate = checksCorrectCreateAndLoginUser(user);
        userAfterCreate.setAccessToken(userAfterCreate.getAccessToken()); //убираем все лишнее из токена в ответе
    }

    public static Stream<Arguments> getUserDataForLoginWithoutRequiredField() {
        return Stream.of(
                arguments(new UserForLogin.Builder()
                        .password(user.getPassword())
                        .build()),
                arguments(new UserForLogin.Builder()
                        .email(user.getEmail())
                        .build()),
                arguments(new UserForLogin.Builder()
                        .build())
        );
    }

    public static Stream<Arguments> getUserDataForLoginWithError() {
        //имитируем ошибку путем выделения случайной подстроки в существующем логине или пароле
        //случай, когда длина логина или пароля не менялась, а была допущена ошибка в каком-либо символе
        // покрывается тестами для попытки залогиниться с несуществующими данными или данными, которые уже существуют
        Random rnd = new Random();
        return Stream.of(
                arguments(new UserForLogin.Builder()
                        .email(user.getEmail())
                        .password(substringRandom(user.getPassword(), rnd.nextInt(user.getPassword().length()) + 1))
                        .build()),
                arguments(new UserForLogin.Builder()
                        .email(substringRandom(user.getEmail(), rnd.nextInt(user.getEmail().length()) + 1))
                        .password(user.getPassword())
                        .build()),
                arguments(new UserForLogin.Builder()
                        .email(substringRandom(user.getEmail(), rnd.nextInt(user.getEmail().length()) + 1))
                        .password(substringRandom(user.getPassword(), rnd.nextInt(user.getPassword().length()) + 1))
                        .build())
        );
    }

    @ParameterizedTest
    @MethodSource("getUserDataForLoginWithoutRequiredField")
    @Description("Checking the impossibility of the user's login without required field")
    public void loginUserWithoutRequiredFieldNotPossible(UserForLogin user) {
        //пытаемся залогинить пользователя без указания обязательного поля
        Response response = UserClient.login(user);
        //проверяем статус код ответа
        Assertions.assertEquals(401, response.statusCode(), "Получаемый статус код при логине пользователя не соответствует ожидаемому");
        //проверяем тело ответа
        response.then().assertThat().body("message", equalTo("email or password are incorrect"));
    }

    @ParameterizedTest
    @MethodSource("getUserDataForLoginWithError")
    @Description("Checking the impossibility of the user's login with errors in data")
    public void loginUserWithErrorInDataNotPossible(UserForLogin user) {
        //пытаемся залогинить пользователя с ошибочными данными
        checkLoginUserWithNotCorrectEmailOrPassword(user);
    }

    @Test
    @Description("Checking the impossibility of the login of a non-existent user")
    public void loginNotExistingUserNotPossible() {
        Random rnd = new Random();
        UserForLogin userForLogin = new UserForLogin.Builder()
                .email(stringRandomGenerate(rnd.nextInt(254) + 1))
                .password(stringRandomGenerate(rnd.nextInt(254) + 1))
                .build();
        checkLoginUserWithNotCorrectEmailOrPassword(userForLogin);
    }

    @AfterAll
    public static void tearDown() {
        UserClient.delete(userAfterCreate.getAccessToken());
    }

}
