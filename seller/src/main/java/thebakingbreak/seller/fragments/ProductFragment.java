package thebakingbreak.seller.fragments;

import static thebakingbreak.seller.helper.Constant.PRODUCT_DB;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import thebakingbreak.seller.adapters.ProductAdapter;
import thebakingbreak.seller.databinding.FragmentProductBinding;
import thebakingbreak.seller.helper.Constant;
import thebakingbreak.seller.models.ProductUpdatedModel;

public class ProductFragment extends Fragment {

    FragmentProductBinding binding;
    ProductAdapter adapter;
    List<ProductUpdatedModel> list = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProductBinding.inflate(inflater, container, false);
        binding.swipeRefreshLayoutProduct.setOnRefreshListener(this::getData);
        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        binding.RecyclerViewProduct.setLayoutManager(manager);
        adapter = new ProductAdapter(requireContext(), list);
        binding.RecyclerViewProduct.setAdapter(adapter);

//        binding.imgMenu.setOnClickListener(v -> {
//            PopupMenu menu = new PopupMenu(getContext(),binding.imgMenu);
//            menu.getMenu().add("Add Product")
//                    .setOnMenuItemClickListener(item -> {
//                        startActivity(new Intent(getContext(), ProductAddActivity.class));
//                        return false;
//                    });
//            menu.show();
//        });

            getData();
        return binding.getRoot();
    }

    private void getData() {
        binding.swipeRefreshLayoutProduct.setRefreshing(true);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(PRODUCT_DB)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        list.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            String uid = snapshot.getString("uid");
                            if (uid != null) {
                                if (uid.equals(auth.getUid())) {
                                    list.add(new ProductUpdatedModel(
                                            snapshot.getString("name"),
                                            Objects.requireNonNull(snapshot.getString("shortDes")),
                                            snapshot.getString("category"),
                                            snapshot.getString("price"),
                                            snapshot.getString("maxPrice"),
                                            snapshot.getString("qty"),
                                            Objects.requireNonNull(snapshot.getString("brandName")),
                                            Objects.requireNonNull(snapshot.getString("imageUrl")),
                                            Objects.requireNonNull(snapshot.getString("description")),
                                            snapshot.getString("uid"),
                                            snapshot.getLong("createdAt"),
                                            snapshot.getLong("updated"),
                                            snapshot.getBoolean("banner"),
                                            snapshot.getId()
                                    ));
                                    binding.swipeRefreshLayoutProduct.setRefreshing(false);
                                    adapter.notifyDataSetChanged();
                                }else {
                                    binding.swipeRefreshLayoutProduct.setRefreshing(false);
                                }
                                binding.swipeRefreshLayoutProduct.setRefreshing(false);
                            }else {
                                binding.swipeRefreshLayoutProduct.setRefreshing(false);
                            }
                        }
                    }else {
                        binding.swipeRefreshLayoutProduct.setRefreshing(false);
                        Constant.setMessage(getContext(),error.getMessage());
                    }
                });
    }

}