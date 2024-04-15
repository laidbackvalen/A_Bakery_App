package com.thebakingbreak.admin.activities.payments;

import static com.thebakingbreak.admin.helper.Constant.ORDER_DB;
import static com.thebakingbreak.admin.helper.Constant.TAG_PRODUCT;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thebakingbreak.admin.adapters.PaymentAdapter;
import com.thebakingbreak.admin.databinding.ActivityPaymentsBinding;
import com.thebakingbreak.admin.models.PaymentModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PaymentsActivity extends AppCompatActivity {

    ActivityPaymentsBinding binding;
    List<PaymentModel> list = new ArrayList<>();
    PaymentAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.swipeRefreshLayoutPayment.setOnRefreshListener(this::getProduct);

        binding.recyclerViewPayment.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PaymentAdapter(PaymentsActivity.this, list);
        binding.recyclerViewPayment.setAdapter(adapter);
        getProduct();
    }

    private void getProduct() {
        binding.swipeRefreshLayoutPayment.setRefreshing(true);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(ORDER_DB)
                .addSnapshotListener((value, error) -> {
                   if (value != null){
                       list.clear();
                       for (DocumentSnapshot snapshot : value.getDocuments()) {
                           Log.d(TAG_PRODUCT, "getProduct: " + snapshot.getId());
                           list.add(new PaymentModel(
                                   snapshot.getString("productName"),
                                   snapshot.getString("paymentId"),
                                   snapshot.getString("payableAmt"),
                                   snapshot.getBoolean("paymentStatus"),
                                   snapshot.getId(),
                                   snapshot.getString("userId"),
                                   snapshot.getString("vendorId")
                           ));
                           binding.swipeRefreshLayoutPayment.setRefreshing(false);
                           adapter.notifyDataSetChanged();
                       }
                   }
                });
    }
}
