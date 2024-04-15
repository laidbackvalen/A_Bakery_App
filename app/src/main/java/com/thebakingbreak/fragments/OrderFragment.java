package com.thebakingbreak.fragments;

import static com.thebakingbreak.helper.Constant.ORDER_DB;
import static com.thebakingbreak.helper.Constant.TAG_HOME;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thebakingbreak.adapters.OrderAdapter;
import com.thebakingbreak.databinding.FragmentOrderBinding;
import com.thebakingbreak.models.OrderUpdateModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderFragment extends Fragment {

    FragmentOrderBinding binding;
    List<OrderUpdateModel> list = new ArrayList<>();
    FirebaseAuth auth;
    OrderAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(inflater,container,false);
        auth = FirebaseAuth.getInstance();

        binding.swipeLayoutOrder.setOnRefreshListener(this::getOrder);

        LinearLayoutManager manager =  new LinearLayoutManager(getContext());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        binding.orderRecyclerView.setLayoutManager(manager);
        adapter = new OrderAdapter(getContext(),list);
        binding.orderRecyclerView.setAdapter(adapter);

        getOrder();
        return binding.getRoot();
    }

    private void getOrder() {
        binding.swipeLayoutOrder.setRefreshing(true);
        FirebaseFirestore.getInstance()
                .collection(ORDER_DB)
                .addSnapshotListener((value, error) -> {
                   if (value !=  null){
                       list.clear();
                       for (DocumentSnapshot snapshot: value.getDocuments()){
                           if (Objects.equals(snapshot.getString("userId"), auth.getUid())){
                              if (snapshot.getBoolean("paymentStatus")){
                                  list.add(new OrderUpdateModel(
                                          Objects.requireNonNull(snapshot.getString("productName")),
                                          Objects.requireNonNull(snapshot.getString("price")),
                                          Objects.requireNonNull(snapshot.getString("maxPrice")),
                                          Objects.requireNonNull(snapshot.getString("qtyBuy")),
                                          Objects.requireNonNull(snapshot.getString("address")),
                                          Objects.requireNonNull(snapshot.getString("phone")),
                                          Objects.requireNonNull(snapshot.getString("email")),
                                          Objects.requireNonNull(snapshot.getLong("createdAt")),
                                          Objects.requireNonNull(snapshot.getString("paymentId")),
                                          Objects.requireNonNull(snapshot.getBoolean("paymentStatus")),
                                          Objects.requireNonNull(snapshot.getString("vendorId")),
                                          Objects.requireNonNull(snapshot.getString("userId")),
                                          Objects. requireNonNull(snapshot.getString("imageUrl")),
                                          Objects. requireNonNull(snapshot.getString("payableAmt")),
                                          Objects. requireNonNull(snapshot.getString("sellerName")),
                                          Objects. requireNonNull(snapshot.getString("userName")),
                                          Objects. requireNonNull(snapshot.getString("productId")),
                                          snapshot.getId()
                                  ));
                                  Log.d(TAG_HOME, "getOrderPaymentStatus1: "+snapshot.getBoolean("paymentStatus"));
                              }else {
                                  Log.d(TAG_HOME, "getOrderPaymentStatus: "+snapshot.getBoolean("paymentStatus"));
                              }

                           }
                           binding.swipeLayoutOrder.setRefreshing(false);
                           adapter.notifyDataSetChanged();
                       }
                   }
                });
    }
}