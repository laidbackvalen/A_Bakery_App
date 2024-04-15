package com.thebakingbreak.admin.adapters;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thebakingbreak.admin.R;
import com.thebakingbreak.admin.models.PaymentModel;

public class PaymentViewHolder extends RecyclerView.ViewHolder {

    private final PaymentAdapter paymentAdapter;
    TextView productName, transactionId, totalAmt, status;

    public PaymentViewHolder(PaymentAdapter paymentAdapter, @NonNull View itemView) {
        super(itemView);
        this.paymentAdapter = paymentAdapter;

        productName = itemView.findViewById(R.id.textViewProductName);
        transactionId = itemView.findViewById(R.id.textViewPaymentIdValue);
        totalAmt = itemView.findViewById(R.id.textViewPaymentAmtValue);
        status = itemView.findViewById(R.id.textViewPaymentStatusValue);
    }

    public void setData(PaymentModel paymentModel) {
        productName.setText(paymentModel.getProductName());
        transactionId.setText(paymentModel.getTransactionId());
        totalAmt.setText(paymentModel.getTotalAmt() + " " + paymentAdapter.context.getString(R.string.rupees));
        if (paymentModel.getStatus()) {
            status.setText("Success");
            status.setTextColor(Color.GREEN);
        } else {
            status.setText("Failed");
            status.setTextColor(Color.RED);
        }
    }
}
