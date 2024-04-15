package thebakingbreak.seller.activities;

import static thebakingbreak.seller.helper.Constant.TAG_PRODUCT;
import static thebakingbreak.seller.helper.Constant.TRACKING_DB;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import thebakingbreak.seller.R;
import thebakingbreak.seller.databinding.ActivityOrderDetailsBinding;
import thebakingbreak.seller.helper.Constant;
import thebakingbreak.seller.models.OrderModel;

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
            binding.txtPrice.setText(model.getPrice());
            binding.txtMaxPrice.setText(model.getPayableAmt());
            binding.txtDiscountPrice.setText(model.getQtyBuy());
            binding.textDescriptions.setText("Address");
            binding.txtPaymentIdValue.setText(model.getPaymentId());
            binding.textDescriptionsValue.setText(
                    model.getUserName()+",\n"+model.getAddress()+",\n"+model.getPhone()+",\n"+model.getEmail()+"\nSeller Name: "+model.getSellerName());
            getStatus(model.getPaymentId());


        }
    }

    private void getStatus(String key) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(TRACKING_DB)
                .addSnapshotListener((value, error) -> {
                    if (value != null){
                        for (DocumentSnapshot snapshot: value.getDocuments()){
                            if (snapshot.getString("paymentId").equals(key)) {
                                Log.d(TAG_PRODUCT, "getStatus: " + snapshot.getBoolean("orderStatus"));
                                if (snapshot.getBoolean("orderStatus")) {
                                    binding.textOrder.setBackgroundResource(R.drawable.btn_bg_drawable_disable);
                                    binding.textOrder.setEnabled(false);
                                } else {
                                    binding.textOrder.setBackgroundResource(R.drawable.btn_bg_drawable_orange);
                                    binding.textOrder.setEnabled(true);
                                    Log.d(TAG_PRODUCT, "getStatus: Order");
                                }
                                if (snapshot.getBoolean("paymentStatus")) {
                                    binding.textPayment.setBackgroundResource(R.drawable.btn_bg_drawable_disable);
                                    binding.textPayment.setEnabled(false);
                                } else {
                                    binding.textPayment.setBackgroundResource(R.drawable.btn_bg_drawable_blue);
                                    binding.textPayment.setEnabled(true);
                                    Log.d(TAG_PRODUCT, "getStatus: Payment");
                                }
                                if (snapshot.getBoolean("confirmStatus")) {
                                    binding.textConfirmed.setBackgroundResource(R.drawable.btn_bg_drawable_disable);
                                    binding.textConfirmed.setEnabled(false);
                                } else {
                                    binding.textConfirmed.setBackgroundResource(R.drawable.btn_bg_drawable_green);
                                    binding.textConfirmed.setEnabled(true);
                                    binding.textConfirmed.setOnClickListener(v -> {
                                        updateStatus(snapshot.getId(),"confirmStatus",true);
                                        binding.textConfirmed.setBackgroundResource(R.drawable.btn_bg_drawable_disable);
                                        binding.textConfirmed.setEnabled(false);
                                    });
                                    Log.d(TAG_PRODUCT, "getStatus: Confirmed");
                                }
                                if (snapshot.getBoolean("completedStatus")) {
                                    binding.textCompleted.setBackgroundResource(R.drawable.btn_bg_drawable_disable);
                                    binding.textCompleted.setEnabled(false);
                                } else {
                                    binding.textCompleted.setBackgroundResource(R.drawable.btn_bg_drawable_yellow);
                                    binding.textCompleted.setEnabled(true);
                                    binding.textCompleted.setOnClickListener(v -> {
                                        if (snapshot.getBoolean("confirmStatus")){
                                            updateStatus(snapshot.getId(),"completedStatus",true);
                                        }else {
                                            Constant.setMessage(OrderDetailsActivity.this,"Confirm first");
                                        }
                                    });
                                    Log.d(TAG_PRODUCT, "getStatus: Completed");
                                }
                                break;
                            }
                        }

                    }
                });
    }

    private void updateStatus(String key, String keyPair, boolean value){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        HashMap<String, Object> map = new HashMap<>();
        map.put(keyPair,value);
        firestore.collection(TRACKING_DB)
                .document(key)
                .update(map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Constant.setMessage(OrderDetailsActivity.this,"send..!");
                    }
                }).addOnFailureListener(e -> Constant.setMessage(OrderDetailsActivity.this,e.getMessage()));
    }
}