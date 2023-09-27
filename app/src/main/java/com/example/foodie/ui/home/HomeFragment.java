package com.example.foodie.ui.home;


import static com.example.foodie.ui.home.QuantityViewIds.*;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodie.MainActivity;
import com.example.foodie.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        com.example.foodie.databinding.FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);


        View root = binding.getRoot();

        setupTextViewsWithListeners(binding.idlyQuantity, binding.idlyAdd);
        setupTextViewsWithListeners(binding.pooriQuantity, binding.pooriAdd);
        setupTextViewsWithListeners(binding.vadaQuantity, binding.vadaAdd);
        setupTextViewsWithListeners(binding.dosaQuantity, binding.dosaAdd);
        setupTextViewsWithListeners(binding.chappathiQuantity, binding.chappathiAdd);
        setupTextViewsWithListeners(binding.mealsQuantity, binding.mealsAdd);
        setupTextViewsWithListeners(binding.thaliMealsQuantity, binding.thaliMealsAdd);
        setupTextViewsWithListeners(binding.chickenBriyaniQuantity, binding.chickenBriyaniAdd);
        setupTextViewsWithListeners(binding.muttonBriyaniQuantity, binding.muttonBriyaniAdd);


        return root;
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setupTextViewsWithListeners(TextView textView, TextView addButton) {
        addButton.setOnClickListener(view -> {
            quantityVisibleAddInvisible(textView,addButton);
            Log.d("ADD---", String.valueOf(textView.getId()));
            homeViewModel.setItemQuantity(String.valueOf(textView.getId()), 1);
            String qua = String.valueOf(homeViewModel.getItemQuantity(String.valueOf(textView.getId())));
            textView.setText(qua);
            MainActivity.itemsInCart+=1;

        });

        textView.setOnTouchListener((v, event) -> {
            Log.d("StringDisplay", "Touch works");
            if (event.getAction() == MotionEvent.ACTION_UP) {
                float touchX = event.getRawX();
                float drawableStartInPixel = textView.getCompoundPaddingStart()+ 602 ;
                float drawableEndInPixel = textView.getCompoundPaddingEnd() + 792;
                Log.d("StringDisplay", "Touch X: " + touchX + ", Padding Start: " + drawableStartInPixel + ",Padding End : " + drawableEndInPixel);

                if (event.getRawX() <= drawableStartInPixel) {
                    Log.d("StringDisplay", "Minus Touch works");
                    int qua = homeViewModel.getItemQuantity(String.valueOf(textView.getId()));
                    if (qua > 1) {
                        qua -= 1;
                        textView.setText(String.valueOf(qua));
                        homeViewModel.setItemQuantity(String.valueOf(textView.getId()), qua);
                    } else {
                        addVisibleQuantityInvisible(textView,addButton);
                        homeViewModel.setItemQuantity(String.valueOf(textView.getId()), 0);
                        MainActivity.itemsInCart-=1;
                    }
                    return true;
                }
                if (event.getRawX() >= drawableEndInPixel) {
                    Log.d("StringDisplay", "Plus Touch works");
                    int qua = homeViewModel.getItemQuantity(String.valueOf(textView.getId()));
                    qua += 1;
                    Log.d("StringDisplay", String.valueOf(qua));
                    textView.setText(String.valueOf(qua));
                    homeViewModel.setItemQuantity(String.valueOf(textView.getId()), qua);
                    return true;
                }
            }

            return true;
        });
    }

    private void quantityVisibleAddInvisible(TextView textView, TextView addButton) {
        addButton.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.VISIBLE);
    }

    private void addVisibleQuantityInvisible(TextView textView, TextView addButton) {
        addButton.setVisibility(View.VISIBLE);
        textView.setVisibility(View.INVISIBLE);
    }
}

