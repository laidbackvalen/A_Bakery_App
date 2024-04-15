package thebakingbreak.seller.adapters;

import static thebakingbreak.seller.helper.Constant.TAG_PRODUCT;

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

import java.util.List;

import thebakingbreak.seller.R;
import thebakingbreak.seller.activities.OrderDetailsActivity;
import thebakingbreak.seller.models.OrderModel;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    Context context;
    List<OrderModel> list;

    public OrderAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_order_layout,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
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
            productPrice.setText(orderModel.getPrice());
            productQty.setText(orderModel.getQtyBuy());
            productTotalAmt.setText(orderModel.getPayableAmt());
            if (orderModel.getPaymentStatus()){
                paymentStatus.setText("Success");
            }else {
                paymentStatus.setText("None");
                Log.d(TAG_PRODUCT, "setData: "+orderModel.getPaymentStatus());
            }
        }
    }
}
