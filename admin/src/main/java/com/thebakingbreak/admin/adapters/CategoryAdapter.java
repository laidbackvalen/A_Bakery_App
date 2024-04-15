package com.thebakingbreak.admin.adapters;

import static com.thebakingbreak.admin.helper.Constant.CATEGORY_DB;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.thebakingbreak.admin.R;
import com.thebakingbreak.admin.helper.Constant;
import com.thebakingbreak.admin.models.CategoryModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    List<CategoryModel> list;

    public CategoryAdapter(Context context, List<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_category_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        CategoryModel model = list.get(position);
        holder.textCategory.setText(model.getCategory());

        holder.delete.setOnClickListener(v -> openDialog(model.getKey(),v));

        holder.edit.setOnClickListener(v -> {
            CategoryModel model1  = list.get(position);
            editDialog(model1,v);
        });
    }

    @SuppressLint("SetTextI18n")
    private void editDialog(CategoryModel model1, View v) {
        View view1 = LayoutInflater.from(v.getContext()).inflate(R.layout.add_category,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setView(view1);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        EditText eAddCategory = view1.findViewById(R.id.eAddCategory);
        TextView btnAddCategory = view1.findViewById(R.id.btnAddCategory);
        eAddCategory.setText(model1.getCategory());

        view1.findViewById(R.id.btnCancelCategory).setOnClickListener(v1 -> {
            dialog.cancel();
            dialog.dismiss();
        });
        HashMap map = new HashMap();
        btnAddCategory.setText("Update");
        btnAddCategory.setOnClickListener(v1 -> {

            map.put("category",eAddCategory.getText().toString().trim());
            map.put("updatedTo",new Date().getTime());
            FirebaseFirestore.getInstance()
                    .collection(CATEGORY_DB)
                    .document(model1.getKey())
                    .update(map).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            dialog.cancel();
                            dialog.dismiss();
                            Constant.setMessage(context,"Updated successfully");
                        }
                    }).addOnFailureListener(e -> {
                        Constant.setMessage(context,e.getMessage());
                        dialog.dismiss();
                        dialog.cancel();
                    });
        });
        dialog.show();
    }

    private void openDialog(String key, View v) {
        View view1 = LayoutInflater.from(v.getContext()).inflate(R.layout.delete_dialog,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setView(view1);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        TextView ok = view1.findViewById(R.id.textViewOkDialog);
        TextView cancel = view1.findViewById(R.id.textViewCancelDialog);

        ok.setOnClickListener(v1 ->
            FirebaseFirestore.getInstance()
                    .collection(CATEGORY_DB)
                    .document(key)
                    .delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Constant.setMessage(context,"Delete successfully");
                            dialog.dismiss();
                            dialog.cancel();
                        }
                    }).addOnFailureListener(e -> Constant.setMessage(context,e.getMessage()))
        );

        cancel.setOnClickListener(v1 -> {
            dialog.dismiss();
            dialog.cancel();
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView textCategory;
        ImageView edit, delete;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategory = itemView.findViewById(R.id.textViewCategory);
            edit = itemView.findViewById(R.id.imgEditCategory);
            delete = itemView.findViewById(R.id.imgDeleteCategory);
        }
    }
}
