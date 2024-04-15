package thebakingbreak.seller.activities;

import static thebakingbreak.seller.helper.Constant.CATEGORY;
import static thebakingbreak.seller.helper.Constant.PRODUCT_DB;
import static thebakingbreak.seller.helper.Constant.PRODUCT_IMAGE;
import static thebakingbreak.seller.helper.Constant.TAG_PRODUCT;
import static thebakingbreak.seller.helper.Constant.USER_DB;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import thebakingbreak.seller.R;
import thebakingbreak.seller.databinding.ActivityProductAddBinding;
import thebakingbreak.seller.helper.Constant;
import thebakingbreak.seller.models.ProductModel;
import thebakingbreak.seller.models.ProductUpdatedModel;

public class ProductAddActivity extends AppCompatActivity {

    ActivityProductAddBinding binding;
    List<String> listStr = new ArrayList<>();
    String[] strArray;
    ArrayAdapter<String> strCategory;
    FirebaseAuth auth;
    String url = null;
    String selectedCategory = null;
    int positionValue;
    boolean isBanner = false;

    ProductUpdatedModel model = null;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        getCategoryData();
        validation();
        binding.btnSelect.setOnClickListener(v ->
            CropImage.activity()
                    .setAspectRatio(16,9)
                    .start(ProductAddActivity.this)
        );

        FirebaseFirestore firestore2 = FirebaseFirestore.getInstance();
        firestore2.collection(USER_DB)
                .document(auth.getUid())
                .addSnapshotListener((value, error) -> {
                    if (value != null){
                        username = value.getString("name");
                        Log.d(TAG_PRODUCT, "saveDataIntoDB: "+ value.getString("name"));
                    }
                });

        if (model == null){
            model = (ProductUpdatedModel) getIntent().getSerializableExtra("model");
        }

        if (binding.checkBox.isChecked()){
            isBanner = true;
        }else {
            isBanner = false;
        }

        if (model != null){
            binding.eProductName.setText(model.getName());
            binding.eSubHeadline.setText(model.getShortDes());
            binding.ePrice.setText(model.getPrice());
            binding.eMaxPrice.setText(model.getMaxPrice());
            binding.eQuantity.setText(model.getQty());
            binding.eBrandName.setText(model.getBrandName());
            binding.eDescription.setText(model.getDescription());
            Glide.with(ProductAddActivity.this)
                    .load(model.getImageUrl())
                    .placeholder(R.drawable.logo)
                    .into(binding.imgProductImage);
            binding.imgProductImage.setVisibility(View.VISIBLE);
            url = model.getImageUrl();
        }

        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0){
                        selectedCategory = listStr.get(position);

                        Log.d("TAG_CATEGORY", "onItemSelected: "+selectedCategory);
                    }else {
                        selectedCategory = null;
                        positionValue = position;
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.imageViewDone.setOnClickListener(v -> {
            if (binding.eProductName.getText().toString().isEmpty()){
                binding.eProductName.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
            }else if (binding.eSubHeadline.getText().toString().isEmpty()){
                binding.eSubHeadline.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
            }else if (binding.ePrice.getText().toString().isEmpty()){
                binding.ePrice.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
            }else if (binding.eMaxPrice.getText().toString().isEmpty()){
                binding.eMaxPrice.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
            }else if (binding.eQuantity.getText().toString().isEmpty()){
                binding.eQuantity.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
            }else if (binding.eBrandName.getText().toString().isEmpty()){
                binding.eBrandName.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
            }else if (binding.eDescription.getText().toString().isEmpty()){
                binding.eDescription.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
            }if (url == null){
                Constant.setMessage(ProductAddActivity.this,"Please select an image");
            }else{
                if (selectedCategory == null && positionValue == 0){
                    Constant.setMessage(ProductAddActivity.this,"Please select a category");
                }else{
                    try {
                        if (model == null){
                            saveDataIntoDB(
                                    binding.eProductName.getText().toString().trim(),
                                    binding.eSubHeadline.getText().toString().trim(),
                                    selectedCategory,
                                    binding.ePrice.getText().toString().trim(),
                                    binding.eMaxPrice.getText().toString().trim(),
                                    binding.eQuantity.getText().toString().trim(),
                                    binding.eBrandName.getText().toString().trim(),
                                    url,
                                    binding.eDescription.getText().toString().trim(),
                                    isBanner
                            );
                        }else {
                            updateDataIntoDB(
                                    binding.eProductName.getText().toString().trim(),
                                    binding.eSubHeadline.getText().toString().trim(),
                                    selectedCategory,
                                    binding.ePrice.getText().toString().trim(),
                                    binding.eMaxPrice.getText().toString().trim(),
                                    binding.eQuantity.getText().toString().trim(),
                                    binding.eBrandName.getText().toString().trim(),
                                    url,
                                    binding.eDescription.getText().toString().trim(),
                                    isBanner,
                                    model.getKey()
                            );
                        }
                    }catch (Exception e){
                        Constant.setMessage(ProductAddActivity.this,e.getMessage());
                    }
                }
            }
        });
    }

    private void validation(){
        binding.eProductName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if (s.toString().isEmpty()){
                        binding.eProductName.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                    }else {
                        binding.eProductName.setBackgroundResource(R.drawable.edit_bg_drawable);
                    }
            }
        });

        binding.eSubHeadline.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if (s.toString().isEmpty()){
                        binding.eSubHeadline.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                    }else {
                        binding.eSubHeadline.setBackgroundResource(R.drawable.edit_bg_drawable);
                    }
            }
        });

        binding.ePrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if (s.toString().isEmpty()){
                        binding.ePrice.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                    }else {
                        binding.ePrice.setBackgroundResource(R.drawable.edit_bg_drawable);
                    }
            }
        });

        binding.eMaxPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if (s.toString().isEmpty()){
                        binding.eMaxPrice.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                    }else {
                        binding.eMaxPrice.setBackgroundResource(R.drawable.edit_bg_drawable);
                    }
            }
        });

        binding.eQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if (s.toString().isEmpty()){
                        binding.eQuantity.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                    }else {
                        binding.eQuantity.setBackgroundResource(R.drawable.edit_bg_drawable);
                    }
            }
        });

        binding.eBrandName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if (s.toString().isEmpty()){
                        binding.eBrandName.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                    }else {
                        binding.eBrandName.setBackgroundResource(R.drawable.edit_bg_drawable);
                    }
            }
        });

        binding.eDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    if (s.toString().isEmpty()){
                        binding.eDescription.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                    }else {
                        binding.eDescription.setBackgroundResource(R.drawable.edit_bg_drawable);
                    }
            }
        });
    }

    private void getCategoryData() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(CATEGORY)
                .addSnapshotListener(ProductAddActivity.this, (value, error) -> {
                    listStr.clear();
                    if (value != null){
                        if (value.size() > 0){
                            listStr.add("--Category--");
                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                String category = snapshot.getString("category");
                                Log.d(TAG_PRODUCT, "getCategoryData: "+category);
                                listStr.add(category);
                            }
                            strArray = new String[listStr.size()];
                            for (int i = 0; i < listStr.size(); i++){
                                strArray[i] = listStr.get(i);

                                strCategory = new ArrayAdapter<>(ProductAddActivity.this,
                                        R.layout.spinner_text_layout,
                                        R.id.text_spinner,
                                        strArray);
                                binding.categorySpinner.setAdapter(strCategory);
                            }
                        }else {
                            if (error !=null){
                                Constant.setMessage(ProductAddActivity.this,error.getMessage());
                            }
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            if (data != null){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri image_uri = result.getUri();
                uploadImage(image_uri);
            }
        }
    }

    private void uploadImage(Uri uri){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child(PRODUCT_IMAGE)
                .child(Objects.requireNonNull(auth.getUid()))
                .child("IMG_"+System.currentTimeMillis());
        reference.putFile(uri).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                task.getResult().getStorage().getDownloadUrl()
                        .addOnSuccessListener(uri1 -> {
                            if (uri1 != null){
                                url = uri1.toString();
                                Log.d("TAG_URL", "uploadImage: "+url);
                                Glide.with(ProductAddActivity.this)
                                        .load(uri)
                                        .placeholder(R.drawable.logo)
                                        .into(binding.imgProductImage);
                                binding.imgProductImage.setVisibility(View.VISIBLE);

                            }
                        }).addOnFailureListener(e -> Constant.setMessage(ProductAddActivity.this,e.getMessage()));

            }

        }).addOnFailureListener(e -> Constant.setMessage(ProductAddActivity.this,e.getMessage()));


    }

    private void saveDataIntoDB(String name,
                                String shortDes,
                                String category,
                                String price,
                                String maxPrice,
                                String qty,
                                String brandName,
                                String url,
                                String description,
                                boolean banner){
        ProductModel model = new ProductModel(
                name,
                shortDes,
                category,
                price,
                maxPrice,
                qty,
                brandName,
                url,
                description,
                Objects.requireNonNull(auth.getUid()),
                new Date().getTime(),
                new Date().getTime(),
                banner,
                username,
                ""
        );

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(PRODUCT_DB)
                .document()
                .set(model)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Constant.setMessage(ProductAddActivity.this,"Product save successfully");
                        finish();
                    }
                })
                .addOnFailureListener(e -> Constant.setMessage(ProductAddActivity.this, e.getMessage()));
    }

    private void updateDataIntoDB(String name,
                                String shortDes,
                                String category,
                                String price,
                                String maxPrice,
                                String qty,
                                String brandName,
                                String url,
                                String description,
                                boolean banner,String key){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name",name);
        map.put("shortDes",shortDes);
        map.put("category",category);
        map.put("price",price);
        map.put("maxPrice",maxPrice);
        map.put("qty",qty);
        map.put("brandName",brandName);
        map.put("imageUrl",url);
        map.put("description",description);
        map.put("uid",Objects.requireNonNull(auth.getUid()));
        map.put("updated",new Date().getTime());
        map.put("banner",banner);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(PRODUCT_DB)
                .document(key)
                .update(map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Constant.setMessage(ProductAddActivity.this,"Product updated successfully");
                        finish();
                    }
                })
                .addOnFailureListener(e -> Constant.setMessage(ProductAddActivity.this, e.getMessage()));
    }


}