package com.thebakingbreak.admin.activities.products;

import static com.thebakingbreak.admin.helper.Constant.REVIEW_DB;
import static com.thebakingbreak.admin.helper.Constant.TAG_PRODUCT;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thebakingbreak.admin.R;
import com.thebakingbreak.admin.adapters.RatingAdapter;
import com.thebakingbreak.admin.databinding.ActivityProductDetailsBinding;
import com.thebakingbreak.admin.models.ProductModel;
import com.thebakingbreak.admin.models.ReviewModel;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {

    ActivityProductDetailsBinding binding;
    ProductModel model = null;

    List<ReviewModel> list = new ArrayList<>();
    RatingAdapter adapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (model == null){
            model = (ProductModel) getIntent().getSerializableExtra("model");
        }

        if (model != null){
            Glide.with(ProductDetailsActivity.this)
                    .load(model.getImageUrl())
                    .placeholder(R.drawable.logo)
                    .into(binding.productImages);
            binding.txtProductName.setText(model.getName());
            binding.txtSlogan.setText(model.getShortDes());
            binding.txtPrice.setText(model.getPrice() + " " +getString(R.string.rupees));
            binding.txtMaxPrice.setText(model.getMaxPrice()+" "+ getString(R.string.rupees));
            int a = Integer.parseInt(model.getPrice());
            int c = Integer.parseInt(model.getMaxPrice());
            binding.txtDiscountPrice.setText(((a*100)/c)+" %");
            binding.textDescriptions.setText("Product Description");
            binding.textDescriptionsValue.setText(model.getDescription()+"\nSeller Name: "+model.getSellerName());

            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setReverseLayout(true);
            binding.recyclerViewReview.setLayoutManager(manager);
            adapter = new RatingAdapter(this,list);
            binding.recyclerViewReview.setAdapter(adapter);

            getRatingValues(model.getKey());
        }

    }

    private void getRatingValues(String key) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(REVIEW_DB)
                .addSnapshotListener((value, error) -> {
                    double rating = 0.0;
                   if (value != null){
                       for (DocumentSnapshot snapshot : value.getDocuments()){
                           if (snapshot.getString("productId").equals(key)){
//                                Log.d(TAG_PRODUCT, "getRating: "+snapshot.getDouble("ratingCount"));
                               list.add(new ReviewModel(
                                       snapshot.getDouble("ratingCount"),
                                       snapshot.getString("message"),
                                       snapshot.getLong("createdAt"),
                                       snapshot.getString("vendorId"),
                                       snapshot.getString("cuId"),
                                       snapshot.getString("productId")
                               ));
                               rating = rating + snapshot.getDouble("ratingCount");

                           }else {
                               Log.d(TAG_PRODUCT, "getRating: "+snapshot.getString("message"));
                           }
                           adapter.notifyDataSetChanged();
                        }
                       rating = rating / list.size();
                       Log.d(TAG_PRODUCT, "getRating: "+rating);
                       binding.ratingBarProduct.setRating(Float.parseFloat(String.valueOf(rating)));
                   }
                });
    }
}