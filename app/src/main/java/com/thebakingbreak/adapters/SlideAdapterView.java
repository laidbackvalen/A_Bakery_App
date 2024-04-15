package com.thebakingbreak.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.thebakingbreak.R;
import com.thebakingbreak.models.SlideModel;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class SlideAdapterView extends RecyclerView.Adapter<SlideAdapterView.SlideVH> {

    private Context ctx;
    private ArrayList<SlideModel> list;
    private ViewPager2 viewPager2;

    public SlideAdapterView(Context ctx, ArrayList<SlideModel> list, ViewPager2 viewPager2) {
        this.ctx = ctx;
        this.list = list;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SlideVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SlideVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_slide_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SlideVH holder, int position) {
        SlideModel model = list.get(position);
        Glide.with(ctx.getApplicationContext())
                .load(model.getUrl())
                .centerCrop()
                .placeholder(R.drawable.logo)
                .into(holder.imgView);
        if (position == list.size() - 2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : list.size();
    }


    public static class SlideVH extends RecyclerView.ViewHolder{
        RoundedImageView imgView;
        public SlideVH(View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.roundSlideImages);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private Runnable runnable = () -> {
            list.addAll(list);
        notifyDataSetChanged();
    };

}

