package edu.practicum;

import edu.practicum.client.OrderClient;
import edu.practicum.models.IngredientsHashList;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static edu.practicum.general.DataForOrder.ingredientListGenerate;

public class OrderCreateWithoutAuthTest {
    @Test
    @Description("Checking the impossibility of creating an order with ingredients for unregistered user")
    public void createOrderWithIngredientForUnregisteredUserNotPossible() {
        Random rnd = new Random();
        //сформируем случайный список ингредиентов для заказа
        ArrayList<String> ingredientList = ingredientListGenerate();
        IngredientsHashList listForOrder = new IngredientsHashList();
        listForOrder.setIngredients(ingredientList);
        //создадем заказ с полученным списком ингредиентов
        Response response = OrderClient.createWithoutAuth(listForOrder);
        //проверяем статус код ответа
        Assertions.assertEquals(401, response.statusCode(), "Получаемый статус код при создании заказа не соответствует ожидаемому");
    }
}
