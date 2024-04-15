package thebakingbreak.seller.activities;

import static thebakingbreak.seller.helper.Constant.USER_DB;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import thebakingbreak.seller.R;
import thebakingbreak.seller.auth.LoginActivity;
import thebakingbreak.seller.databinding.ActivityMainBinding;
import thebakingbreak.seller.fragments.HomeFragment;
import thebakingbreak.seller.fragments.OrdersFragment;
import thebakingbreak.seller.fragments.PaymentFragment;
import thebakingbreak.seller.fragments.ProductFragment;
import thebakingbreak.seller.preferences.UserPreferences;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
//        setFragment(new HomeFragment());
//        binding.navbar.viewHome.setBackgroundColor(getColor(R.color.primaryColor));
//        binding.navbar.viewOrders.setBackgroundColor(getColor(R.color.white));
//        binding.navbar.viewProduct.setBackgroundColor(getColor(R.color.white));
//        binding.navbar.viewPayment.setBackgroundColor(getColor(R.color.white));
//
//        binding.navbar.textHome.setTextColor(getColor(R.color.white));
//        binding.navbar.textHome.setVisibility(View.GONE);
//        binding.navbar.textPayment.setTextColor(getColor(R.color.black));
//        binding.navbar.textPayment.setVisibility(View.VISIBLE);
//        binding.navbar.textOrder.setTextColor(getColor(R.color.black));
//        binding.navbar.textOrder.setVisibility(View.VISIBLE);
//        binding.navbar.textProduct.setTextColor(getColor(R.color.black));
//        binding.navbar.textProduct.setVisibility(View.VISIBLE);
//
//        binding.navbar.imgHome.setImageResource(R.drawable.ic_home_white);
//        binding.navbar.imgOrders.setImageResource(R.drawable.ic_orders);
//        binding.navbar.imgProduct.setImageResource(R.drawable.ic_product);
//        binding.navbar.imgPayment.setImageResource(R.drawable.ic_rupee);
//
//        binding.navbar.viewHome.setOnClickListener(v -> {
//            setFragment(new HomeFragment());
//            binding.navbar.viewHome.setBackgroundColor(getColor(R.color.primaryColor));
//            binding.navbar.viewOrders.setBackgroundColor(getColor(R.color.white));
//            binding.navbar.viewProduct.setBackgroundColor(getColor(R.color.white));
//            binding.navbar.viewPayment.setBackgroundColor(getColor(R.color.white));
//
//            binding.navbar.textHome.setTextColor(getColor(R.color.white));
//            binding.navbar.textHome.setVisibility(View.GONE);
//            binding.navbar.textOrder.setTextColor(getColor(R.color.black));
//            binding.navbar.textOrder.setVisibility(View.VISIBLE);
//            binding.navbar.textProduct.setTextColor(getColor(R.color.black));
//            binding.navbar.textProduct.setVisibility(View.VISIBLE);
//            binding.navbar.textPayment.setTextColor(getColor(R.color.black));
//            binding.navbar.textPayment.setVisibility(View.VISIBLE);
//
//            binding.navbar.imgHome.setImageResource(R.drawable.ic_home_white);
//            binding.navbar.imgOrders.setImageResource(R.drawable.ic_orders);
//            binding.navbar.imgProduct.setImageResource(R.drawable.ic_product);
//            binding.navbar.imgPayment.setImageResource(R.drawable.ic_rupee);
//        });
//
//        binding.navbar.viewOrders.setOnClickListener(v -> {
//            setFragment(new OrdersFragment());
//            binding.navbar.viewHome.setBackgroundColor(getColor(R.color.white));
//            binding.navbar.viewOrders.setBackgroundColor(getColor(R.color.primaryColor));
//            binding.navbar.viewProduct.setBackgroundColor(getColor(R.color.white));
//            binding.navbar.viewPayment.setBackgroundColor(getColor(R.color.white));
//
//            binding.navbar.textHome.setTextColor(getColor(R.color.black));
//            binding.navbar.textHome.setVisibility(View.VISIBLE);
//            binding.navbar.textOrder.setTextColor(getColor(R.color.white));
//            binding.navbar.textOrder.setVisibility(View.GONE);
//            binding.navbar.textProduct.setTextColor(getColor(R.color.black));
//            binding.navbar.textProduct.setVisibility(View.VISIBLE);
//            binding.navbar.textPayment.setTextColor(getColor(R.color.black));
//            binding.navbar.textPayment.setVisibility(View.VISIBLE);
//
//            binding.navbar.imgHome.setImageResource(R.drawable.ic_home);
//            binding.navbar.imgOrders.setImageResource(R.drawable.ic_orders_white);
//            binding.navbar.imgProduct.setImageResource(R.drawable.ic_product);
//            binding.navbar.imgPayment.setImageResource(R.drawable.ic_rupee);
//        });
//
//        binding.navbar.viewProduct.setOnClickListener(v -> {
//            setFragment(new ProductFragment());
//            binding.navbar.viewHome.setBackgroundColor(getColor(R.color.white));
//            binding.navbar.viewProduct.setBackgroundColor(getColor(R.color.primaryColor));
//            binding.navbar.viewOrders.setBackgroundColor(getColor(R.color.white));
//            binding.navbar.viewPayment.setBackgroundColor(getColor(R.color.white));
//
//            binding.navbar.textHome.setTextColor(getColor(R.color.black));
//            binding.navbar.textHome.setVisibility(View.VISIBLE);
//            binding.navbar.textProduct.setTextColor(getColor(R.color.white));
//            binding.navbar.textProduct.setVisibility(View.GONE);
//            binding.navbar.textOrder.setTextColor(getColor(R.color.black));
//            binding.navbar.textOrder.setVisibility(View.VISIBLE);
//            binding.navbar.textPayment.setTextColor(getColor(R.color.black));
//            binding.navbar.textPayment.setVisibility(View.VISIBLE);
//
//            binding.navbar.imgHome.setImageResource(R.drawable.ic_home);
//            binding.navbar.imgOrders.setImageResource(R.drawable.ic_orders);
//            binding.navbar.imgPayment.setImageResource(R.drawable.ic_rupee);
//            binding.navbar.imgProduct.setImageResource(R.drawable.ic_product_white);
//        });
//
//        binding.navbar.viewPayment.setOnClickListener(v -> {
//            setFragment(new PaymentFragment());
//            binding.navbar.viewHome.setBackgroundColor(getColor(R.color.white));
//            binding.navbar.viewProduct.setBackgroundColor(getColor(R.color.white));
//            binding.navbar.viewOrders.setBackgroundColor(getColor(R.color.white));
//            binding.navbar.viewPayment.setBackgroundColor(getColor(R.color.primaryColor));
//
//            binding.navbar.textHome.setTextColor(getColor(R.color.black));
//            binding.navbar.textHome.setVisibility(View.VISIBLE);
//            binding.navbar.textProduct.setTextColor(getColor(R.color.black));
//            binding.navbar.textProduct.setVisibility(View.VISIBLE);
//            binding.navbar.textOrder.setTextColor(getColor(R.color.black));
//            binding.navbar.textOrder.setVisibility(View.VISIBLE);
//            binding.navbar.textPayment.setTextColor(getColor(R.color.white));
//            binding.navbar.textPayment.setVisibility(View.GONE);
//
//            binding.navbar.imgHome.setImageResource(R.drawable.ic_home);
//            binding.navbar.imgOrders.setImageResource(R.drawable.ic_orders);
//            binding.navbar.imgProduct.setImageResource(R.drawable.ic_product);
//            binding.navbar.imgPayment.setImageResource(R.drawable.ic_rupee_white);
//
//        });

        getUserInfo();

        binding.toolbar.imgMenu.setVisibility(View.VISIBLE);
        binding.toolbar.imgMenu.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(MainActivity.this,binding.toolbar.imgMenu);
            menu.getMenu().add("Profile")
                    .setOnMenuItemClickListener(item -> {
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        return false;
                    });

            menu.getMenu().add("Logout")
                    .setOnMenuItemClickListener(item -> {
                        new UserPreferences(MainActivity.this).clear();
                        auth.signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                        return false;
                    });
            menu.show();
        });


        getSupportFragmentManager()
                .beginTransaction()
                .replace(binding.frameLayout.getId(), new HomeFragment())
                .commit();

        binding.navMain.textHome.setTextColor(Color.BLACK);
        binding.navMain.textHome.setBackgroundColor(getColor(R.color.white));

        binding.navMain.textProduct.setTextColor(Color.WHITE);
        binding.navMain.textProduct.setBackgroundColor(getColor(android.R.color.transparent));

        binding.navMain.textOrder.setTextColor(Color.WHITE);
        binding.navMain.textOrder.setBackgroundColor(getColor(android.R.color.transparent));

        binding.navMain.textPayment.setTextColor(Color.WHITE);
        binding.navMain.textPayment.setBackgroundColor(getColor(android.R.color.transparent));


        binding.navMain.textHome.setOnClickListener(v -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(binding.frameLayout.getId(), new HomeFragment())
                    .commit();

            binding.toolbar.textViewToolbar.setText("Home");
            binding.navMain.textHome.setTextColor(Color.BLACK);
            binding.navMain.textHome.setBackgroundColor(getColor(R.color.white));

            binding.navMain.textProduct.setTextColor(Color.WHITE);
            binding.navMain.textProduct.setBackgroundColor(getColor(android.R.color.transparent));

            binding.navMain.textOrder.setTextColor(Color.WHITE);
            binding.navMain.textOrder.setBackgroundColor(getColor(android.R.color.transparent));

            binding.navMain.textPayment.setTextColor(Color.WHITE);
            binding.navMain.textPayment.setBackgroundColor(getColor(android.R.color.transparent));

            binding.toolbar.imgMenu.setVisibility(View.VISIBLE);
            binding.toolbar.imgMenu.setOnClickListener(v1 -> {
                PopupMenu menu = new PopupMenu(MainActivity.this,binding.toolbar.imgMenu);
                menu.getMenu().add("Profile")
                        .setOnMenuItemClickListener(item -> {
                            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                            return false;
                        });

                menu.getMenu().add("Logout")
                        .setOnMenuItemClickListener(item -> {
                            new UserPreferences(MainActivity.this).clear();
                            auth.signOut();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                            return false;
                        });
                menu.show();
            });
            closeDrawer(binding.drawerLayout);
        });

        binding.navMain.textProduct.setOnClickListener(v -> {
            setFragment(new ProductFragment());
            binding.toolbar.textViewToolbar.setText("Products");
            binding.navMain.textHome.setTextColor(Color.WHITE);
            binding.navMain.textHome.setBackgroundColor(getColor(android.R.color.transparent));

            binding.navMain.textProduct.setTextColor(Color.BLACK);
            binding.navMain.textProduct.setBackgroundColor(getColor(R.color.white));

            binding.navMain.textOrder.setTextColor(Color.WHITE);
            binding.navMain.textOrder.setBackgroundColor(getColor(android.R.color.transparent));

            binding.navMain.textPayment.setTextColor(Color.WHITE);
            binding.navMain.textPayment.setBackgroundColor(getColor(android.R.color.transparent));
            binding.toolbar.imgMenu.setVisibility(View.VISIBLE);
            binding.toolbar.imgMenu.setOnClickListener(v1 -> {
                PopupMenu menu = new PopupMenu(MainActivity.this,binding.toolbar.imgMenu);
                menu.getMenu().add("Add Product")
                        .setOnMenuItemClickListener(item -> {
                            startActivity(new Intent(MainActivity.this, ProductAddActivity.class));
                            return false;
                        });

                menu.show();
            });
            closeDrawer(binding.drawerLayout);
        });

        binding.navMain.textOrder.setOnClickListener(v -> {
            setFragment(new OrdersFragment());
            binding.toolbar.textViewToolbar.setText("Orders");
            binding.navMain.textHome.setTextColor(Color.WHITE);
            binding.navMain.textHome.setBackgroundColor(getColor(android.R.color.transparent));

            binding.navMain.textProduct.setTextColor(Color.WHITE);
            binding.navMain.textProduct.setBackgroundColor(getColor(android.R.color.transparent));

            binding.navMain.textOrder.setTextColor(Color.BLACK);
            binding.navMain.textOrder.setBackgroundColor(getColor(R.color.white));

            binding.navMain.textPayment.setTextColor(Color.WHITE);
            binding.navMain.textPayment.setBackgroundColor(getColor(android.R.color.transparent));

            binding.toolbar.imgMenu.setVisibility(View.GONE);
            closeDrawer(binding.drawerLayout);
        });

        binding.navMain.textPayment.setOnClickListener(v -> {
            setFragment(new PaymentFragment());
            binding.toolbar.textViewToolbar.setText("Payments");
            binding.navMain.textHome.setTextColor(Color.WHITE);
            binding.navMain.textHome.setBackgroundColor(getColor(android.R.color.transparent));

            binding.navMain.textProduct.setTextColor(Color.WHITE);
            binding.navMain.textProduct.setBackgroundColor(getColor(android.R.color.transparent));

            binding.navMain.textOrder.setTextColor(Color.WHITE);
            binding.navMain.textOrder.setBackgroundColor(getColor(android.R.color.transparent));

            binding.navMain.textPayment.setTextColor(Color.BLACK);
            binding.navMain.textPayment.setBackgroundColor(getColor(R.color.white));

            binding.toolbar.imgMenu.setVisibility(View.GONE);
            closeDrawer(binding.drawerLayout);
        });


    }

    private void setFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(binding.frameLayout.getId(), fragment)
                .commit();
    }

    private void getUserInfo(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(USER_DB)
                .document(auth.getUid())
                .addSnapshotListener((value, error) -> {
                    if (value != null){
                        binding.navMain.textViewFirstChar.setText(String.valueOf(value.getString("name").charAt(0)));
                        binding.navMain.textViewUserName.setText(value.getString("name"));
                        binding.navMain.textViewEmail.setText(value.getString("email"));
                    }
                });
    }

    public void openDrawer(View view) {
        openDrawer(binding.drawerLayout);
    }

    private void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(binding.drawerLayout);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeDrawer(binding.drawerLayout);

    }

}