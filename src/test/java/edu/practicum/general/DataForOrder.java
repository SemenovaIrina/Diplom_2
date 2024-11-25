package edu.practicum.general;

import edu.practicum.client.OrderClient;
import edu.practicum.models.IngredientListInAmswer;
import edu.practicum.models.IngredientsHashList;
import edu.practicum.models.OrderCreateAnswer;
import edu.practicum.models.OrderListAnswer;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataForOrder {
    public static ArrayList<String> ingredientListGenerate() {
        Random rnd = new Random();
        ArrayList<String> randomListIngredient = new ArrayList<String>();
        //получаем список существующих ингредиентов
        Response response = OrderClient.ingredientList();
        //проверяем статус код ответа
        Assertions.assertEquals(200, response.statusCode(), "Получаемый статус код при получении списка ингредиентов не соответствует ожидаемому");
        //проверяем структуру ответа
        assertEquals(response.as(IngredientListInAmswer.class).getClass(), IngredientListInAmswer.class, "Структура ответа не соответствует ожидаемой");
        IngredientListInAmswer ingredientList = response.as(IngredientListInAmswer.class);
        int ingredientListSize = ingredientList.getData().size();
        int count = rnd.nextInt(ingredientListSize) + 1;
        for (int i = 0; i < count; i++) {
            randomListIngredient.add(ingredientList.getData().get(rnd.nextInt(ingredientListSize)).get_id());
        }
        return randomListIngredient;
    }

    public static Response createOneOrderWithRandomIngredientsWithAuth() {
        Random rnd = new Random();
        //сформируем случайный список ингредиентов для заказа
        ArrayList<String> ingredientList = ingredientListGenerate();
        IngredientsHashList listForOrder = new IngredientsHashList();
        listForOrder.setIngredients(ingredientList);
        //создадем заказ с полученным списком ингредиентов
        Response response = OrderClient.createWithAuth(listForOrder);
        return response;
    }

    public static ArrayList<Integer> createSomeOrderWithRandomIngredients(int orderCount) {
        ArrayList<Integer> listOrderNumbers = new ArrayList<Integer>();
        Response response;
        for (int i = 0; i < orderCount; i++) {
            response = createOneOrderWithRandomIngredientsWithAuth();
            Assertions.assertEquals(200, response.statusCode(), "Получаемый статус код при создании заказа не соответствует ожидаемому");
            OrderCreateAnswer orderInfo = response.as(OrderCreateAnswer.class);
            listOrderNumbers.add(new Integer(orderInfo.getOrder().getNumber()));
        }
        return listOrderNumbers;
    }

    public static ArrayList<Integer> getOrdersNumberFromAnswer(OrderListAnswer ordersInfo) {
        ArrayList<Integer> listOrderNumbers = new ArrayList<Integer>();
        for (int i = 0; i < ordersInfo.getOrders().size(); i++) {
            listOrderNumbers.add(new Integer(ordersInfo.getOrders().get(i).getNumber()));
        }
        return listOrderNumbers;
    }
}
