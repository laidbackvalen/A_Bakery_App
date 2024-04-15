package com.thebakingbreak.admin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thebakingbreak.admin.R;
import com.thebakingbreak.admin.models.PaymentModel;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentViewHolder> {

    Context context;
    List<PaymentModel> list;

    public PaymentAdapter(Context context, List<PaymentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaymentViewHolder(this, LayoutInflater
                .from(parent.getContext()).inflate(R.layout.payment_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
