package com.thebakingbreak.admin.activities.products;

import static com.thebakingbreak.admin.helper.Constant.PRODUCT_DB;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thebakingbreak.admin.adapters.ProductAdapter;
import com.thebakingbreak.admin.databinding.ActivityProductBinding;
import com.thebakingbreak.admin.models.ProductModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductActivity extends AppCompatActivity {
    ActivityProductBinding binding;
    List<ProductModel> list = new ArrayList<>();

    ProductAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LinearLayoutManager manager = new LinearLayoutManager(ProductActivity.this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        binding.RecyclerViewProduct.setLayoutManager(manager);
        adapter = new ProductAdapter(ProductActivity.this, list);
        binding.RecyclerViewProduct.setAdapter(adapter);
        binding.swipeRefreshLayoutProduct.setOnRefreshListener(this::getData);

        getData();

    }
    private void getData() {
        binding.swipeRefreshLayoutProduct.setRefreshing(true);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(PRODUCT_DB)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        list.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            list.add(new ProductModel(
                                    snapshot.getString("name"),
                                    Objects.requireNonNull(snapshot.getString("shortDes")),
                                    snapshot.getString("category"),
                                    snapshot.getString("price"),
                                    snapshot.getString("maxPrice"),
                                    snapshot.getString("qty"),
                                    Objects.requireNonNull(snapshot.getString("brandName")),
                                    Objects.requireNonNull(snapshot.getString("imageUrl")),
                                    Objects.requireNonNull(snapshot.getString("description")),
                                    snapshot.getString("uid"),
                                    snapshot.getLong("createdAt"),
                                    snapshot.getLong("updated"),
                                    snapshot.getBoolean("banner"),
                                    snapshot.getString("sellerName"),
                                    snapshot.getId()
                            ));
                            binding.swipeRefreshLayoutProduct.setRefreshing(false);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}