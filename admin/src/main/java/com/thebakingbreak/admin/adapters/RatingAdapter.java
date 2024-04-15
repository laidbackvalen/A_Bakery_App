package com.thebakingbreak.admin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thebakingbreak.admin.R;
import com.thebakingbreak.admin.models.ReviewModel;

import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {

    Context context;
    List<ReviewModel> list;

    public RatingAdapter(Context context, List<ReviewModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RatingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.user_review,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RatingViewHolder extends RecyclerView.ViewHolder {
        RatingBar rating;
        TextView message;
        public RatingViewHolder(@NonNull View itemView) {
            super(itemView);
            rating = itemView.findViewById(R.id.ratingBarView);
            message = itemView.findViewById(R.id.textMessage);
        }

        public void setData(ReviewModel reviewModel) {
            rating.setRating(Float.parseFloat(String.valueOf(reviewModel.getRatingCount())));
            message.setText(reviewModel.getMessage());
        }
    }
}
