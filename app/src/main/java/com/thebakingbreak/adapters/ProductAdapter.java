package com.thebakingbreak.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.thebakingbreak.R;
import com.thebakingbreak.activities.ProductDetailsActivity;
import com.thebakingbreak.models.ProductModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    List<ProductModel> list;
    FirebaseAuth auth;

    public ProductAdapter(Context context, List<ProductModel> list) {
        this.context = context;
        this.list = list;
        auth = FirebaseAuth.getInstance();
    }

    public void filterListIntern(ArrayList<ProductModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImages;
        TextView txtProduct_name, txtSlogan, txtPrice, txtMaxPrice, txtDiscountPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImages = itemView.findViewById(R.id.productImages);
            txtProduct_name = itemView.findViewById(R.id.txtProduct_name);
            txtSlogan = itemView.findViewById(R.id.txtSlogan);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtMaxPrice = itemView.findViewById(R.id.txtMaxPrice);
            txtDiscountPrice = itemView.findViewById(R.id.txtDiscountPrice);

        }

        @SuppressLint("SetTextI18n")
        public void setData(ProductModel model) {
            txtProduct_name.setText(model.getName());
            txtSlogan.setText(model.getShortDes());
            txtPrice.setText(model.getPrice());
            txtMaxPrice.setText(model.getMaxPrice());
            try{
                int a = Integer.parseInt(Objects.requireNonNull(model.getPrice()));
                int b = Integer.parseInt(Objects.requireNonNull(model.getMaxPrice()));
                String disPrice = String.valueOf(100-((a*100)/b));
                txtDiscountPrice.setText(disPrice+" %");
                Log.d("TAG_dis", a+" setData: "+b+" "+disPrice);
            }catch (ArithmeticException e){
                e.printStackTrace();
            }

            if (model.getImageUrl() != null){
                Glide.with(itemView.getContext())
                        .load(model.getImageUrl())
                        .placeholder(R.drawable.logo)
                        .into(productImages);
            }


            itemView.setOnClickListener(v -> context.startActivity(
                    new Intent(
                            context.getApplicationContext(),
                            ProductDetailsActivity.class).putExtra("productId",model.getKey())
            ));



        }
    }
}
