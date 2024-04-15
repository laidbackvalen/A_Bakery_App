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
import com.thebakingbreak.activities.OrderActivity;
import com.thebakingbreak.models.OrderUpdateModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderVH> {

    Context context;
    List<OrderUpdateModel> list;

    public OrderAdapter(Context context, List<OrderUpdateModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderVH holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrderVH extends RecyclerView.ViewHolder {
        ImageView imgProductOrder;
        TextView txtProduct_name_order, txtProduct_des_order, txtProduct_price_order, txtMaxPriceOrder,
                txtDiscountPriceOrder, txtQuantityOrder;
        public OrderVH(@NonNull View itemView) {
            super(itemView);
            imgProductOrder = itemView.findViewById(R.id.imgProductOrder);
            txtProduct_name_order = itemView.findViewById(R.id.txtProduct_name_order);
            txtProduct_price_order = itemView.findViewById(R.id.txtProduct_price_order);
            txtMaxPriceOrder = itemView.findViewById(R.id.txtMaxPriceOrder);
            txtDiscountPriceOrder = itemView.findViewById(R.id.txtDiscountPriceOrder);
            txtQuantityOrder = itemView.findViewById(R.id.txtQuantityOrder);

            itemView.setOnClickListener(v -> {
                OrderUpdateModel model = list.get(getAdapterPosition());
                context.startActivity(new Intent(context, OrderActivity.class)
                        .putExtra("orderId",model.getKey()));
            });
        }

        @SuppressLint("SetTextI18n")
        public void setData(OrderUpdateModel orderModel) {
            Glide.with(context)
                    .load(orderModel.getImageUrl())
                    .placeholder(R.drawable.logo)
                    .into(imgProductOrder);
            txtProduct_name_order.setText(orderModel.getProductName());
            txtProduct_price_order.setText(orderModel.getPrice());
            txtMaxPriceOrder.setText(orderModel.getMaxPrice());
            int a = Integer.parseInt(orderModel.getPrice());
            int b = Integer.parseInt(orderModel.getMaxPrice());
            txtDiscountPriceOrder.setText((100-((a*100)/b))+" %");
            txtQuantityOrder.setText(orderModel.getQtyBuy());
        }
    }
}
