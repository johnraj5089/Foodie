package com.example.foodie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.foodie.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    public static int itemsInCart = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();

        FirebaseApp.initializeApp(this);

        String userid = user.getUid();

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://foodie-a2f04-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Member").child(userid);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(v -> {
            if (itemsInCart > 0 ){
                Intent i = new Intent(v.getContext(), CartActivity.class);
                startActivity(i);
            }else{
                Snackbar.make(v, "No Item in Cart", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name_value = dataSnapshot.child("name").getValue(String.class);
                String email_value = dataSnapshot.child("email").getValue(String.class);

                Log.d("Firebase", "Retrieving the value from the firebase.");

                View headerView = binding.navView.getHeaderView(0);
                TextView emailTextView = headerView.findViewById(R.id.email_nav_header);
                TextView usernameTextView = headerView.findViewById(R.id.username_nav_header);
                emailTextView.setText(email_value);
                usernameTextView.setText(name_value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // This method is called if there's an error
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_settings) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            mAuth.signOut();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            finish();
            startActivity(i);
        }
    }

    public void userEdit(View view){
        Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
        startActivity(intent);
    }
}
