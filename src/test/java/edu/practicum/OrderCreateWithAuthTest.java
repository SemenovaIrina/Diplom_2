package edu.practicum;

import edu.practicum.client.OrderClient;
import edu.practicum.client.UserClient;
import edu.practicum.models.IngredientsHashList;
import edu.practicum.models.OrderCreateAnswer;
import edu.practicum.models.User;
import edu.practicum.models.UserAfterCreate;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static edu.practicum.data.UtilsForDataPrepare.emailRandom;
import static edu.practicum.data.UtilsForDataPrepare.stringRandomGenerate;
import static edu.practicum.general.Checks.checksCorrectCreateAndLoginUser;
import static edu.practicum.general.DataForOrder.createOneOrderWithRandomIngredientsWithAuth;
import static edu.practicum.general.DataForOrder.ingredientListGenerate;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderCreateWithAuthTest {
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
        OrderClient.addBearerTokenInHeader(userAfterCreate.getAccessToken());
    }

    @Test
    @Description("Checking the possibility of creating an order with ingredients for registered user")
    public void createOrderWithIngredientForRegisteredUserPossible() {
        Response response = createOneOrderWithRandomIngredientsWithAuth();
        //проверяем статус код ответа
        Assertions.assertEquals(200, response.statusCode(), "Получаемый статус код при создании заказа не соответствует ожидаемому");
        //проверим структуру ответа
        assertEquals(response.as(OrderCreateAnswer.class).getClass(), OrderCreateAnswer.class, "Структура ответа при обновлении данных пользователя не соответствует ожидаемой");
        //проверяем тело ответа
        response.then().assertThat().body("success", equalTo(true));
    }

    @Test
    @Description("Checking the impossibility of creating an order without ingredients for registered user")
    public void createOrderWithoutIngredientForRegisteredUserNotPossible() {
        IngredientsHashList listForOrder = new IngredientsHashList();
        listForOrder.setIngredients(new ArrayList<>());
        //создадем заказ без ингредиентов
        Response response = OrderClient.createWithAuth(listForOrder);
        //проверяем статус код ответа
        Assertions.assertEquals(400, response.statusCode(), "Получаемый статус код при создании заказа не соответствует ожидаемому");
    }

    @Test
    @Description("Checking the impossibility of creating an order with not correct ingredients for registered user")
    public void createOrderWithNotCorrectIngredientForRegisteredUserNotPossible() {
        Random rnd = new Random();
        //сформируем случайный список из ингредиентов для заказа
        ArrayList<String> ingredientList = ingredientListGenerate();
        //имитируем ошибку в хеше путем отбрасывания нескольких символов в существующем хеше
        String ingredientWithError = ingredientList.get(0).substring(0, ingredientList.get(0).length() - 2);
        ingredientList.remove(0);
        ingredientList.add(ingredientWithError);
        IngredientsHashList listForOrder = new IngredientsHashList();
        listForOrder.setIngredients(ingredientList);
        //создадем заказ с ошибочным хешем ингредиента
        Response response = OrderClient.createWithAuth(listForOrder);
        //проверяем статус код ответа
        Assertions.assertEquals(500, response.statusCode(), "Получаемый статус код при создании заказа не соответствует ожидаемому");
    }

    @AfterAll
    public static void tearDown() {
        UserClient.delete();
    }
}
