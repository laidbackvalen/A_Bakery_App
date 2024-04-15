package thebakingbreak.seller.adapters;

import static thebakingbreak.seller.helper.Constant.PRODUCT_DB;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import thebakingbreak.seller.R;
import thebakingbreak.seller.activities.ProductAddActivity;
import thebakingbreak.seller.models.ProductUpdatedModel;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    List<ProductUpdatedModel> list;

    public ProductAdapter(Context context, List<ProductUpdatedModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_items_layout,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        if (list.size() > 0){
            holder.setData(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView textProductTitle,txtSlogan,textPrice,textMaxPrice,txtDiscountPrice;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.productImages);
            textProductTitle = itemView.findViewById(R.id.txtProduct_name);
            txtSlogan = itemView.findViewById(R.id.txtSlogan);
            textPrice = itemView.findViewById(R.id.txtPrice);
            textMaxPrice = itemView.findViewById(R.id.txtMaxPrice);
            txtDiscountPrice = itemView.findViewById(R.id.txtDiscountPrice);
        }

        @SuppressLint("SetTextI18n")
        public void setData(ProductUpdatedModel productModel) {
            Glide.with(itemView.getContext())
                    .load(productModel.getImageUrl())
                    .placeholder(R.drawable.logo)
                    .into(imgProduct);
            textProductTitle.setText(productModel.getName());
            textPrice.setText(productModel.getPrice());
            textMaxPrice.setText(productModel.getMaxPrice());

            txtSlogan.setText(productModel.getShortDes());

            int a = Integer.parseInt(productModel.getPrice());
            int b = Integer.parseInt(productModel.getMaxPrice());
            txtDiscountPrice.setText((100-((a*100)/b))+" %");

            itemView.setOnLongClickListener(v -> {
                PopupMenu menu = new PopupMenu(context, itemView);
                menu.getMenu().add("DELETE")
                        .setOnMenuItemClickListener(item -> {
                            FirebaseFirestore.getInstance()
                                    .collection(PRODUCT_DB)
                                    .document(productModel.getKey())
                                    .delete()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()){
                                            notifyItemRemoved(getAdapterPosition());
                                            Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            return false;
                        });

                menu.getMenu().add("EDIT")
                        .setOnMenuItemClickListener(item -> {
                            context.startActivity(new Intent(context, ProductAddActivity.class)
                                    .putExtra("model",productModel));
                            return false;
                        });
                menu.show();

                return true;
            });

        }
    }
}
