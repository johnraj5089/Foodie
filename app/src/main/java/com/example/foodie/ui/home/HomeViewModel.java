package com.example.foodie.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import java.util.HashMap;

public class HomeViewModel extends ViewModel {

    public static final HashMap<String, Integer> itemQuantities = new HashMap<>();

    public HomeViewModel() {

        itemQuantities.put(QuantityViewIds.idlyId, 0);
        itemQuantities.put(QuantityViewIds.pooriId, 0);
        itemQuantities.put(QuantityViewIds.vadaId, 0);
        itemQuantities.put(QuantityViewIds.dosaId, 0);
        itemQuantities.put(QuantityViewIds.chappathiId, 0);
        itemQuantities.put(QuantityViewIds.mealsId, 0);
        itemQuantities.put(QuantityViewIds.thalimealsId, 0);
        itemQuantities.put(QuantityViewIds.chickenBriyaniId, 0);
        itemQuantities.put(QuantityViewIds.muttonBriyaniId, 0);

    }

    public HashMap<String, Integer> getItemQuantities() {
        return itemQuantities;
    }

    public void setItemQuantity(String itemId, int quantity) {
        itemQuantities.put(itemId, quantity);
    }

    public int getItemQuantity(String itemId) {
        Integer quantity = itemQuantities.get(itemId);
        return quantity != null ? quantity : 0;
    }
}
