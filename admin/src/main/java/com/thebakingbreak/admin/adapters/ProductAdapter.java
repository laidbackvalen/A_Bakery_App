package com.thebakingbreak.admin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thebakingbreak.admin.R;
import com.thebakingbreak.admin.activities.products.ProductDetailsActivity;
import com.thebakingbreak.admin.models.ProductModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    List<ProductModel> list;

    public ProductAdapter(Context context, List<ProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.rv_product_layout,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (list.size() > 0){
            holder.setData(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView textProductTitle,textProductPriceShow,textProductMaxPriceShow;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            textProductTitle = itemView.findViewById(R.id.textProductTitle);
            textProductPriceShow = itemView.findViewById(R.id.textProductPriceShow);
            textProductMaxPriceShow = itemView.findViewById(R.id.textProductMaxPriceShow);

        }

        public void setData(ProductModel productModel) {
            Glide.with(itemView.getContext())
                    .load(productModel.getImageUrl())
                    .placeholder(R.drawable.logo)
                    .into(imgProduct);
            textProductTitle.setText(productModel.getName());
            textProductPriceShow.setText(productModel.getPrice()+" "+ context.getString(R.string.rupees));
            textProductMaxPriceShow.setText(productModel.getMaxPrice()+" "+ context.getString(R.string.rupees));

            itemView.setOnClickListener(v -> context.startActivity(
                    new Intent(context, ProductDetailsActivity.class).putExtra("model",productModel)));
        }
    }
}
