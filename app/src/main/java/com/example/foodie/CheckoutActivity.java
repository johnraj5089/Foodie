package com.example.foodie;

import static com.example.foodie.OrderIdGenerator.generateOrderId;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodie.ui.home.HomeFragment;
import com.example.foodie.ui.home.HomeViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    TextView totalAmt, orderId;
    Button codButton, upiButton;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        String total = getIntent().getStringExtra("total");
        String order_id = generateOrderId();

        totalAmt = findViewById(R.id.totalAmount);
        orderId = findViewById(R.id.orderId);

        totalAmt.setText(total);
        orderId.setText(order_id);

        codButton = findViewById(R.id.codbutton);
        upiButton = findViewById(R.id.upiButton);

       // FirebaseApp.initializeApp(this);

        auth = FirebaseAuth.getInstance();

        user = auth.getCurrentUser();

        ref = FirebaseDatabase.getInstance("https://foodie-a2f04-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Member");

        Map<String, Object> orderData = new HashMap<>();
        for (Map.Entry<String, Integer> entry : HomeViewModel.itemQuantities.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();

            switch (key){
                case("2131296636"):
                    key = "Idly";
                    break;
                case("2131296806"):
                    key = "Poori";
                    break;
                case("2131297009"):
                    key = "Vada";
                    break;
                case("2131296561"):
                    key = "Dosa";
                    break;
                case("2131296501"):
                    key = "Chappathi";
                    break;
                case("2131296705"):
                    key = "Meals";
                    break;
                case("2131296963"):
                    key = "Thali Meals";
                    break;
                case("2131296509"):
                    key = "Chicken Briyani";
                    break;
                case("2131296746"):
                    key = "Mutton Briyani";
                    break;
            }

            if (value > 0){
                orderData.put( key, String.valueOf(value));
            }

        }
        orderData.put("Total",total);
        orderData.put("Id",order_id);

        codButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //.child(order_id).setValue("Total" + total)
                Log.d("ADD---", "on click executed");
                ref.child(user.getUid()).child("Orders").child(order_id).setValue(orderData)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("ADD---", "order updated in database");
                            Intent i = new Intent(v.getContext(), MainActivity.class);
                            Toast.makeText(CheckoutActivity.this, "Order Confirmed", Toast.LENGTH_SHORT).show();
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            MainActivity.itemsInCart = 0;

                            for (Map.Entry<String, Integer> entry : HomeViewModel.itemQuantities.entrySet()) {
                                String key = entry.getKey();

                                HomeViewModel.itemQuantities.put(key, 0);
                                Log.d("ADD---", key);
                                Log.d("ADD---", String.valueOf(entry.getValue()));

                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e("ADD---", "onFailure: " + e.getMessage());
                            // Handle any errors here
                            Toast.makeText(CheckoutActivity.this, "Failed to save details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                }
        });

        upiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Feature not available", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}