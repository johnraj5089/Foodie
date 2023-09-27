package com.example.foodie;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.foodie.ui.home.HomeViewModel;
import com.example.foodie.ui.home.QuantityViewIds;

import java.util.Map;

public class CartActivity extends AppCompatActivity {

    LinearLayout cartLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Button checkout;

        cartLayout = findViewById(R.id.cart_item_layout);

        float total = 0;

        TextView totalTextView = findViewById(R.id.total_item_amount);

        for (Map.Entry<String, Integer> entry : HomeViewModel.itemQuantities.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();

            if (value > 0) {

                View cartItemLayout = LayoutInflater.from(cartLayout.getContext()).inflate(R.layout.cart_cardview, null);

                TextView itemNameTextView = cartItemLayout.findViewById(R.id.cart_item_name);
                TextView itemQuantityTextView = cartItemLayout.findViewById(R.id.cart_item_quantity);
                TextView itemAmountTextView = cartItemLayout.findViewById(R.id.cart_item_amount);

                itemNameTextView.setText(getItemNameByKey(key));
                itemQuantityTextView.setText(String.valueOf(value));
                itemAmountTextView.setText(String.valueOf(getItemAmount(key) * ((float) value)));
                total += getItemAmount(key) * value;
                totalTextView.setText(String.valueOf(total));

                cartLayout.addView(cartItemLayout);

            }
        }

        checkout = findViewById(R.id.checkout_button);

        float finalTotal = total;
        checkout.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), CheckoutActivity.class);
            i.putExtra("total", String.valueOf(finalTotal));
            Log.d("ADD---", String.valueOf(finalTotal));
            startActivity(i);
        });

    }

    private float getItemAmount(String key) {
        if (key.equals(QuantityViewIds.idlyId) || key.equals(QuantityViewIds.vadaId)) {
            return 10.00F;
        } else if (key.equals(QuantityViewIds.pooriId) || key.equals(QuantityViewIds.chappathiId)) {
            return 15.00F;
        } else if (key.equals(QuantityViewIds.dosaId)) {
            return 35.00F;
        } else if (key.equals(QuantityViewIds.mealsId)) {
            return 70.00F;
        } else if (key.equals(QuantityViewIds.thalimealsId)) {
            return 90.00F;
        } else if (key.equals(QuantityViewIds.chickenBriyaniId)) {
            return 120.00F;
        } else if (key.equals(QuantityViewIds.muttonBriyaniId)) {
            return 150.00F;
        }
        return 0;
    }

    private String getItemNameByKey(String key) {

        if (key.equals(QuantityViewIds.idlyId)) {
            return "Idly";
        } else if (key.equals(QuantityViewIds.pooriId)) {
            return "Poori";
        } else if (key.equals(QuantityViewIds.vadaId)) {
            return "Vada";
        } else if (key.equals(QuantityViewIds.dosaId)) {
            return "Dosa";
        } else if (key.equals(QuantityViewIds.chappathiId)) {
            return "Chappathi";
        } else if (key.equals(QuantityViewIds.mealsId)) {
            return "Meals";
        } else if (key.equals(QuantityViewIds.thalimealsId)) {
            return "Thali Meals";
        } else if (key.equals(QuantityViewIds.chickenBriyaniId)) {
            return "Chicken Briyani";
        } else if (key.equals(QuantityViewIds.muttonBriyaniId)) {
            return "Mutton Briyani";
        }
        return " ";
    }
}