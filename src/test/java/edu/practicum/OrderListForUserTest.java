package edu.practicum;

import edu.practicum.client.OrderClient;
import edu.practicum.client.UserClient;
import edu.practicum.models.OrderListAnswer;
import edu.practicum.models.User;
import edu.practicum.models.UserAfterCreate;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static edu.practicum.general.Checks.checksCorrectCreateAndLoginUser;
import static edu.practicum.general.DataForOrder.createSomeOrderWithRandomIngredients;
import static edu.practicum.general.DataForOrder.getOrdersNumberFromAnswer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderListForUserTest {
    private UserAfterCreate userAfterCreate;

    @ParameterizedTest
    @MethodSource("edu.practicum.general.DataForUser#getCorrectUserData")
    @Description("Checking get of the orders list for registered user")
    public void getListOrdersForRegisterUserPossible(User user) {
        //чтобы осуществлять авторизованные запросы, нужно чтобы пользователь был успешно создан и мог залогиниться
        userAfterCreate = checksCorrectCreateAndLoginUser(user); // возвращается user без лишнего в токене
        //сформируем дополнительную спецификацию для аторизованных запросов (добавим токен в заголовок всех последующих запросов для этого user)
        OrderClient.addBearerTokenInHeader(userAfterCreate.getAccessToken());
        //создаем случайное число заказов для пользователя, сохраняем их номера
        ArrayList<Integer> listOrderNumbersForUser = createSomeOrderWithRandomIngredients(new Random().nextInt(10));
        //получаем список заказов для пользователя с помощью соответствующей ручки
        Response response = OrderClient.orderListWithAuth();
        //проверяем статус код ответа
        Assertions.assertEquals(200, response.statusCode(), "Получаемый статус код при получении списка заказов с аутентификацией не соответствует ожидаемому");
        //достанем список номеров заказов из ответа
        OrderListAnswer ordersInfo = response.as(OrderListAnswer.class);
        ArrayList<Integer> listOrderNumbersFromAnswer = getOrdersNumberFromAnswer(ordersInfo);
        //чтобы не учитывать возможное изменение порядка номеров перобразуем списки в множества и сравним
        Set<Integer> OrderNumbersForUserSet = new HashSet<Integer>(listOrderNumbersForUser);
        Set<Integer> OrderNumbersFromAnswerSet = new HashSet<Integer>(listOrderNumbersFromAnswer);
        assertEquals(OrderNumbersForUserSet, OrderNumbersFromAnswerSet, "Список заказов сформирован без учета принадлежности пользователю");
    }

    @ParameterizedTest
    @MethodSource("edu.practicum.general.DataForUser#getCorrectUserData")
    @Description("Checking get of the orders list for unregistered user")
    public void getListOrdersForUnregisterUserNotPossible(User user) {
        //чтобы работать со списком заказов пользователя, нужно чтобы пользователь был успешно создан и мог залогиниться
        userAfterCreate = checksCorrectCreateAndLoginUser(user); // возвращается user без лишнего в токене
        //сформируем дополнительную спецификацию для аторизованных запросов (чтобы можно было создать заказы для этого user)
        OrderClient.addBearerTokenInHeader(userAfterCreate.getAccessToken());
        //создаем случайное число заказов для пользователя, сохраняем их номера
        ArrayList<Integer> listOrderNumbersForUser = createSomeOrderWithRandomIngredients(new Random().nextInt(10));
        //получаем список заказов для пользователя с помощью соответствующей ручки БЕЗ авторизации
        Response response = OrderClient.orderListWithoutAuth();
        //проверяем статус код ответа
        Assertions.assertEquals(401, response.statusCode(), "Получаемый статус код при получении списка заказов без аутентификации не соответствует ожидаемому");
    }

    @AfterAll
    public static void tearDown() {
        UserClient.delete();
    }

}
