package com.thebakingbreak.admin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.thebakingbreak.admin.activities.category.CategoryActivity;
import com.thebakingbreak.admin.activities.orders.OrdersActivity;
import com.thebakingbreak.admin.activities.payments.PaymentsActivity;
import com.thebakingbreak.admin.activities.products.ProductActivity;
import com.thebakingbreak.admin.activities.profile.ProfileActivity;
import com.thebakingbreak.admin.activities.users.UsersActivity;
import com.thebakingbreak.admin.auth.LoginActivity;
import com.thebakingbreak.admin.databinding.ActivityMainBinding;
import com.thebakingbreak.admin.preferences.UserPreferences;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        binding.category.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CategoryActivity.class)));
        binding.products.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ProductActivity.class)));
        binding.orders.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), OrdersActivity.class)));
        binding.payments.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), PaymentsActivity.class)));
        binding.users.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), UsersActivity.class)));


        binding.imageViewMenu.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(MainActivity.this,binding.imageViewMenu);
            menu.getMenu().add("Profile")
                    .setOnMenuItemClickListener(item -> {
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        return false;
                    });

            menu.getMenu().add("Logout")
                    .setOnMenuItemClickListener(item -> {
                        new UserPreferences(this).clear();
                        auth.signOut();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        return false;
                    });
            menu.show();
        });
    }
}