package thebakingbreak.seller.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import thebakingbreak.seller.R;
import thebakingbreak.seller.models.OrderModel;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {

    Context context;
    List<OrderModel> list;

    public PaymentAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PaymentAdapter.PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaymentViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_layout,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.PaymentViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PaymentViewHolder extends RecyclerView.ViewHolder {

        TextView payment, paymentAmt;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            payment = itemView.findViewById(R.id.textViewPaymentSuccess);
            paymentAmt = itemView.findViewById(R.id.textPaymentAmt);
        }

        @SuppressLint("SetTextI18n")
        public void setData(OrderModel orderModel) {
            paymentAmt.setText(orderModel.getPayableAmt()+ context.getString(R.string.rupees));
        }
    }
}
