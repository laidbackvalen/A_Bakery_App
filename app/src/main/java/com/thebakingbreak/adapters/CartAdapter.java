package com.thebakingbreak.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thebakingbreak.R;
import com.thebakingbreak.activities.ProductDetailsActivity;
import com.thebakingbreak.helper.PriceListener;
import com.thebakingbreak.models.CartUpdateModel;

import java.util.List;
import java.util.Objects;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    List<CartUpdateModel> list;
    Context context;

    PriceListener listener;
    public CartAdapter(List<CartUpdateModel> list, Context context, PriceListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {

        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProductCard, imgSubtractCard,imgAddCard,imgDelete;
        TextView txtProduct_name_Card, txtProduct_price_Card,
                txtMaxPriceCard, txtDiscountPriceCard,txtCountCard;

        int i;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProductCard = itemView.findViewById(R.id.imgProductCard);
            imgSubtractCard = itemView.findViewById(R.id.imgSubtractCard);
            imgAddCard = itemView.findViewById(R.id.imgAddCard);
            txtProduct_name_Card = itemView.findViewById(R.id.txtProduct_name_Card);
            txtProduct_price_Card = itemView.findViewById(R.id.txtProduct_price_Card);
            txtMaxPriceCard = itemView.findViewById(R.id.txtMaxPriceCard);
            txtDiscountPriceCard = itemView.findViewById(R.id.txtDiscountPriceCard);
            txtCountCard = itemView.findViewById(R.id.txtCountCard);
            imgDelete = itemView.findViewById(R.id.imgDelete);

            itemView.setOnClickListener(v -> {
                CartUpdateModel model = list.get(getAdapterPosition());
                context.startActivity(new Intent(context, ProductDetailsActivity.class)
                        .putExtra("productId", model.getProductId()));
            });
        }


        @SuppressLint("SetTextI18n")
        public void setData(CartUpdateModel model) {
            i = Integer.parseInt(model.getCountProduct());

            Glide.with(context.getApplicationContext())
                    .load(model.getImgUrl())
                    .placeholder(R.drawable.logo)
                    .into(imgProductCard);

            txtProduct_name_Card.setText(model.getProductName());
            txtProduct_price_Card.setText(model.getPrice());
            txtMaxPriceCard.setText(model.getMaxPrice());

            if (Integer.parseInt(Objects.requireNonNull(model.getQty())) > 0){
                txtCountCard.setText(model.getCountProduct());
            }


            int a = Integer.parseInt(Objects.requireNonNull(model.getPrice()));
            int b = Integer.parseInt(Objects.requireNonNull(model.getMaxPrice()));
            String dis = String.valueOf(100-((a*100)/b));
            txtDiscountPriceCard.setText(dis+" %");

            imgSubtractCard.setOnClickListener(v -> {
                if (i > 1){
                    i--;
                    txtCountCard.setText(String.valueOf(i));
                    listener.onClickMaxSubtractPriceListener(String.valueOf((i-1)*Integer.parseInt(model.getMaxPrice())),getAdapterPosition());
                    listener.onClickSubtractPriceListener(String.valueOf((i-1)*Integer.parseInt(model.getPrice())),getAdapterPosition());
                }
            });

            imgAddCard.setOnClickListener(v -> {
                if (i <= Integer.parseInt(model.getQty())){
                    i++;
                    txtCountCard.setText(String.valueOf(i));
                    listener.onClickAddPriceListener(String.valueOf((i-1)*Integer.parseInt(model.getPrice())),getAdapterPosition());
                    listener.onClickMaxAddPriceListener(String.valueOf((i-1)*Integer.parseInt(model.getMaxPrice())),getAdapterPosition());
                }
            });

            imgDelete.setOnClickListener(v -> listener.deleteCardItem(model,getAdapterPosition()));

        }
    }
}
