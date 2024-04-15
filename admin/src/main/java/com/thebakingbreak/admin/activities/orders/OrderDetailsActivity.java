package com.thebakingbreak.admin.activities.orders;

import static com.thebakingbreak.admin.helper.Constant.PRODUCT_STATUS_DB;
import static com.thebakingbreak.admin.helper.Constant.TAG_PRODUCT;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.thebakingbreak.admin.R;
import com.thebakingbreak.admin.databinding.ActivityOrderDetailsBinding;
import com.thebakingbreak.admin.models.OrderModel;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderDetailsActivity extends AppCompatActivity {

    ActivityOrderDetailsBinding binding;
    OrderModel model = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (model == null){
            model = (OrderModel) getIntent().getSerializableExtra("model");
        }

        if (model != null){
            Glide.with(OrderDetailsActivity.this)
                    .load(model.getImageUrl())
                    .placeholder(R.drawable.logo)
                    .into(binding.productImages);

            binding.txtProductName.setText(model.getProductName());
            binding.txtPrice.setText(model.getPrice()+" \u20B9");
            binding.txtMaxPrice.setText(model.getPayableAmt()+" \u20B9");
            binding.txtDiscountPrice.setText(model.getQtyBuy());
            binding.txtPaymentIdValue.setText(model.getPaymentId());
            binding.textDescriptions.setText("Address");
            binding.textDescriptionsValue.setText(
                    model.getUserName()+ "\n" +model.getAddress()+ "\n" + model.getPhone()+"\n"
                    +model.getEmail()+"\nSeller Name: "+model.getSellerName()
                    );

//            Log.d(TAG_PRODUCT, "getStatus: "+model.getKey());
            getStatus(model.getPaymentId());
        }

    }

    @SuppressLint("SetTextI18n")
    private void getStatus(String key) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(PRODUCT_STATUS_DB)
                .addSnapshotListener((value, error) -> {
                    if (value != null){
                        for (DocumentSnapshot snapshot: value.getDocuments()){
                            if (snapshot.getString("paymentId").equals(key)) {
                                Log.d(TAG_PRODUCT, "getStatus: " + snapshot.getBoolean("orderStatus"));
                                if (snapshot.getBoolean("orderStatus")) {
                                    binding.textOrder.setText("Order: Success");
                                } else {
                                    Log.d(TAG_PRODUCT, "getStatus: Order");
                                }
                                if (snapshot.getBoolean("paymentStatus")) {
                                    binding.textPayment.setText("Payment: Success");
                                } else {
                                    Log.d(TAG_PRODUCT, "getStatus: Payment");
                                }

                                if (snapshot.getBoolean("confirmStatus")) {
                                    binding.textConfirmed.setText("Order Confirmed");
                                } else {
                                    Log.d(TAG_PRODUCT, "getStatus: Confirmed");
                                }
                                if (snapshot.getBoolean("completedStatus")) {
                                    binding.textConfirmed.setText("Order Completed");
                                } else {
                                    Log.d(TAG_PRODUCT, "getStatus: Completed");
                                }
                                break;
                            }
                        }

                    }
                });
    }
}