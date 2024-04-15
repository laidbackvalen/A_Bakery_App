package com.thebakingbreak.admin.activities.category;

import static com.thebakingbreak.admin.helper.Constant.CATEGORY_DB;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.thebakingbreak.admin.R;
import com.thebakingbreak.admin.adapters.CategoryAdapter;
import com.thebakingbreak.admin.databinding.ActivityCategoryBinding;
import com.thebakingbreak.admin.helper.Constant;
import com.thebakingbreak.admin.models.CategoryModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    ActivityCategoryBinding binding;
    List<CategoryModel> list = new ArrayList<>();
    CategoryAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());  //view binding //library

        binding.swipeRefreshLayoutCategory.setOnRefreshListener(this::getCategory);

        adapter = new CategoryAdapter(CategoryActivity.this,list);
        binding.recyclerViewCategory.setAdapter(adapter); //smart casting

        getCategory();
    }

    @SuppressLint("InflateParams")
    public void showDialog(View view) {
        View view1 = getLayoutInflater().inflate(R.layout.add_category,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
        builder.setView(view1);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        EditText category = view1.findViewById(R.id.eAddCategory);

        view1.findViewById(R.id.btnAddCategory)
                        .setOnClickListener(v -> addCategory(dialog, category.getText().toString().trim()));

        view1.findViewById(
                R.id.btnCancelCategory
                )
                .setOnClickListener(v ->{
                    dialog.cancel();
                    dialog.dismiss();
                });

        dialog.show();
    }

    private void addCategory(AlertDialog dialog, String category) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("category", category);
        map.put("createdAt", new Date().getTime());
        map.put("updatedTo", new Date().getTime());

        FirebaseFirestore.getInstance()
                .collection(CATEGORY_DB)
                .document()
                .set(map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Constant.setMessage(CategoryActivity.this,"Added successfully");
                        dialog.cancel();
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(e -> Constant.setMessage(CategoryActivity.this,e.getMessage()));

    }


    private void getCategory(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(CATEGORY_DB)
                .addSnapshotListener((value, error) -> {
                    if (value != null){
                        list.clear();
                        for (DocumentSnapshot snapshot: value.getDocuments()){
                            list.add(new CategoryModel(
                                    snapshot.getString("category"),
                                    snapshot.getLong("createdAt"),
                                    snapshot.getLong("updatedTo"),
                                    snapshot.getId()
                            ));
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}