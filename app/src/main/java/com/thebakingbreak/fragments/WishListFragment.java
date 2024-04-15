package com.thebakingbreak.fragments;

import static com.thebakingbreak.helper.Constant.WISH_LIST_DB;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thebakingbreak.adapters.WishListAdapter;
import com.thebakingbreak.databinding.FragmentWishListBinding;
import com.thebakingbreak.helper.Constant;
import com.thebakingbreak.models.WishListUpdateModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WishListFragment extends Fragment {

    FragmentWishListBinding binding;
    List<WishListUpdateModel> list = new ArrayList<>();
    FirebaseAuth auth;
    WishListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWishListBinding.inflate(inflater,container,false);
        auth = FirebaseAuth.getInstance();

        binding.swipeLayoutWishList.setOnRefreshListener(this::getWishList);

        binding.wishListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new WishListAdapter(requireContext(), list);
        binding.wishListRecyclerView.setAdapter(adapter);

        getWishList();

        return binding.getRoot();
    }

    private void getWishList() {
        binding.swipeLayoutWishList.setRefreshing(true);
        FirebaseFirestore.getInstance()
                .collection(WISH_LIST_DB)
                .get()
                .addOnCompleteListener(task -> {
                    list.clear();
                    if (task.getResult()!= null){
                        for (DocumentSnapshot snapshot : task.getResult()){
                            if (Objects.equals(snapshot.getString("userId"), auth.getUid())){
                                list.add(new WishListUpdateModel(
                                        snapshot.getString("productName"),
                                        snapshot.getString("imgUrl"),
                                        snapshot.getString("price"),
                                        snapshot.getString("maxPrice"),
                                        snapshot.getString("productId"),
                                        snapshot.getString("vendorId"),
                                        snapshot.getString("userId"),
                                        snapshot.getId(),
                                        snapshot.getLong("CreatedAt")
                                ));
                                binding.swipeLayoutWishList.setRefreshing(false);
                            }
                            binding.swipeLayoutWishList.setRefreshing(false);
                            adapter.notifyDataSetChanged();
                        }
                    }else {
                        binding.swipeLayoutWishList.setRefreshing(false);
                    }
                })
                .addOnFailureListener(e -> {
                    Constant.setMessage(requireContext(),e.getMessage());
                    binding.swipeLayoutWishList.setRefreshing(false);
                });
    }
}