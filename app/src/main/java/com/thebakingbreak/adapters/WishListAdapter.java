package com.thebakingbreak.adapters;

import static com.thebakingbreak.helper.Constant.WISH_LIST_DB;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thebakingbreak.R;
import com.thebakingbreak.helper.Constant;
import com.thebakingbreak.models.WishListUpdateModel;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishListViewHolder> {

    Context context;
    List<WishListUpdateModel> list;

    public WishListAdapter(Context context, List<WishListUpdateModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public WishListAdapter.WishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WishListViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_layout,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.WishListViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class WishListViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProductWishlist;
        TextView txtProductNameWishlist, txtProductDesWishlist, txtProductPriceWishlist, txtMaxPriceWishlist,
                txtDiscountPriceWishlist, txtRemove;

        public WishListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProductWishlist = itemView.findViewById(R.id.imgProductWishlist);
            txtProductNameWishlist = itemView.findViewById(R.id.txtProductNameWishlist);
            txtProductDesWishlist = itemView.findViewById(R.id.txtProductDesWishlist);
            txtProductPriceWishlist = itemView.findViewById(R.id.txtProductPriceWishlist);
            txtMaxPriceWishlist = itemView.findViewById(R.id.txtMaxPriceWishlist);
            txtDiscountPriceWishlist = itemView.findViewById(R.id.txtDiscountPriceWishlist);
            txtRemove = itemView.findViewById(R.id.txtRemove);
        }

        @SuppressLint("SetTextI18n")
        public void setData(WishListUpdateModel model) {
            Glide.with(context.getApplicationContext())
                    .load(model.getImgUrl())
                    .placeholder(R.drawable.logo)
                    .into(imgProductWishlist);

            txtProductNameWishlist.setText(model.getProductName());
            txtMaxPriceWishlist.setText(model.getMaxPrice());
            txtProductPriceWishlist.setText(model.getPrice());

            int a = Integer.parseInt(Objects.requireNonNull(model.getPrice()));
            int b = Integer.parseInt(Objects.requireNonNull(model.getMaxPrice()));
            txtDiscountPriceWishlist.setText((100-((a*100)/b))+" %");

            txtRemove.setOnClickListener(v -> {
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                firestore.collection(WISH_LIST_DB)
                        .document(Objects.requireNonNull(model.getWishListId()))
                        .delete()
                        .addOnCompleteListener(task -> {
                           if (task.isSuccessful()){
                               Constant.setMessage(context,"Remove successfully");
                           }
                        });
            });
        }

    }
}
