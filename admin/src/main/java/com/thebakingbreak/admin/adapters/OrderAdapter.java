package com.thebakingbreak.admin.adapters;

import static com.thebakingbreak.admin.helper.Constant.TAG_PRODUCT;

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

import com.thebakingbreak.admin.R;
import com.thebakingbreak.admin.activities.orders.OrderDetailsActivity;
import com.thebakingbreak.admin.models.OrderModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    Context context;
    List<OrderModel> list;

    public OrderAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_order_layout,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView productName, productPrice, productQty, productTotalAmt, paymentStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgProductOrder);
            productName = itemView.findViewById(R.id.txtProductNameOrder);
            productPrice = itemView.findViewById(R.id.txtProductPriceOrder);
            productQty = itemView.findViewById(R.id.txtProductQtyValueOrder);
            productTotalAmt = itemView.findViewById(R.id.txtProductAmtValueOrder);
            paymentStatus = itemView.findViewById(R.id.txtProductPaymentValueOrder);

            itemView.setOnClickListener(v -> {
                OrderModel model = list.get(getAdapterPosition());
                context.startActivity(new Intent(context, OrderDetailsActivity.class)
                        .putExtra("model",model));
            });
        }

        public void setData(OrderModel orderModel) {

            Glide.with(context)
                    .load(orderModel.getImageUrl())
                    .placeholder(R.drawable.logo)
                    .into(imgProduct);

            productName.setText(orderModel.getProductName());
            productPrice.setText(orderModel.getPrice()+" \u20B9");
            productQty.setText(orderModel.getQtyBuy());
            productTotalAmt.setText(orderModel.getPayableAmt()+" \u20B9");
            if (orderModel.getPaymentStatus()){
                paymentStatus.setText("Success");
            }else {
                paymentStatus.setText("None");
                Log.d(TAG_PRODUCT, "setData: "+orderModel.getPaymentStatus());
            }
        }
    }
}
