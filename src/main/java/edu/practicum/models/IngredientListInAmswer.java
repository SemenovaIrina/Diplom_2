package edu.practicum.models;

import java.util.ArrayList;
import java.util.List;

public class IngredientListInAmswer {
    private boolean success;
    private ArrayList<Ingredient> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<Ingredient> getData() {
        return data;
    }

    public void setData(ArrayList<Ingredient> data) {
        this.data = data;
    }
}
