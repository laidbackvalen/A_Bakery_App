package com.thebakingbreak.admin.activities.orders;

import static com.thebakingbreak.admin.helper.Constant.ORDER_DB;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thebakingbreak.admin.adapters.OrderAdapter;
import com.thebakingbreak.admin.databinding.ActivityOrdersBinding;
import com.thebakingbreak.admin.models.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    ActivityOrdersBinding binding;

    List<OrderModel> list = new ArrayList<>();
    OrderAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.swipeRefreshLayoutOrder.setOnRefreshListener(this::getOrderData);
        LinearLayoutManager manager = new LinearLayoutManager(OrdersActivity.this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        binding.recyclerViewOrder.setLayoutManager(manager);
        adapter = new OrderAdapter(OrdersActivity.this,list);
        binding.recyclerViewOrder.setAdapter(adapter);
        getOrderData();
    }

    private void getOrderData() {
        binding.swipeRefreshLayoutOrder.setRefreshing(true);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(ORDER_DB)
                .addSnapshotListener((value, error) -> {
                    list.clear();
                    if (value != null){
                        for (DocumentSnapshot snapshot: value.getDocuments()){
                            Log.d("TAG_DATA", "getUserData: "+snapshot.getId());
                            if (snapshot.getBoolean("paymentStatus")){
                                list.add(new OrderModel(
                                        snapshot.getString("productName"),
                                        snapshot.getString("price"),
                                        snapshot.getString("maxPrice"),
                                        snapshot.getString("qtyBuy"),
                                        snapshot.getString("address"),
                                        snapshot.getString("phone"),
                                        snapshot.getString("email"),
                                        snapshot.getLong("createdAt"),
                                        snapshot.getString("paymentId"),
                                        snapshot.getBoolean("paymentStatus"),
                                        snapshot.getString("vendorId"),
                                        snapshot.getString("userId"),
                                        snapshot.getString("imageUrl"),
                                        snapshot.getString("payableAmt"),
                                        snapshot.getString("sellerName"),
                                        snapshot.getString("userName"),
                                        snapshot.getId()
                                ));
                                binding.swipeRefreshLayoutOrder.setRefreshing(false);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}
