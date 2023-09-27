package com.example.foodie.ui.myorders;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.foodie.R;
import com.example.foodie.databinding.FragmentMyordersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MyordersFragment extends Fragment {

    private FragmentMyordersBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser user;

    LinearLayout myOrdersLayout;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("ADD---", "--------myorders fragment loads--------");

       binding = FragmentMyordersBinding.inflate(inflater, container, false);
       View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        String userid = Objects.requireNonNull(user).getUid();

        myOrdersLayout = root.findViewById(R.id.linear_myorder);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://foodie-a2f04-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Member").child(userid).child("Orders");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("ADD---", "--------onDataChange--------");
                Log.d("ADD---", dataSnapshot.getChildren().toString());


                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Log.d("ADD---", "--------for loop in onDatachange--------");
                    String key = snapshot.getKey();

                    View myOrdersCard = LayoutInflater.from(myOrdersLayout.getContext()).inflate(R.layout.myorders_card_layout, null);
                    String item = "";
                    String qua = "";
                    String total_value = "";
                    for (DataSnapshot snap : snapshot.getChildren()){
                        String keyOrder = snap.getKey();

                        if (Objects.equals(keyOrder, "Total")) {
                            total_value = (String) snap.getValue();
                        } else {
                            if (!Objects.equals(keyOrder, "Id")) {
                                String item_value = (String) snap.getValue();
                                item+= keyOrder + "\n";
                                qua+= ":      " + item_value + "\n";
                            }
                        }

                    }
                    item+= "Total    "   + "\n";
                    qua+= ":      " + total_value + "\n";

                    TextView OrderItem = myOrdersCard.findViewById(R.id.itemsInMyorder);
                    TextView orderNum = myOrdersCard.findViewById(R.id.orderIdNumber_myorders);
                    TextView orderqua = myOrdersCard.findViewById(R.id.itemsInMyorder2);

                    orderNum.setText(String.valueOf(key));
                    OrderItem.setText(item);
                    orderqua.setText(qua);
                    myOrdersLayout.addView(myOrdersCard);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}