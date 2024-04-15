package thebakingbreak.seller.fragments;

import static thebakingbreak.seller.helper.Constant.ORDER_DB;
import static thebakingbreak.seller.helper.Constant.TAG_ORDER;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import thebakingbreak.seller.adapters.OrderAdapter;
import thebakingbreak.seller.databinding.FragmentOrdersBinding;
import thebakingbreak.seller.helper.Constant;
import thebakingbreak.seller.models.OrderModel;


public class OrdersFragment extends Fragment {

    FragmentOrdersBinding binding;

    List<OrderModel> list = new ArrayList<>();
    FirebaseAuth auth;
    OrderAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrdersBinding.inflate(inflater,container,false);
        auth = FirebaseAuth.getInstance();

        binding.swipeRefreshLayoutOrders.setOnRefreshListener(this::getDataOrder);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        binding.RecyclerViewOrders.setLayoutManager(manager);
        adapter = new OrderAdapter(getContext(),list);
        binding.RecyclerViewOrders.setAdapter(adapter);
        getDataOrder();
    }

    private void getDataOrder() {
        binding.swipeRefreshLayoutOrders.setRefreshing(true);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(ORDER_DB)
                .addSnapshotListener((value, error) -> {
                    if (value != null){
                        list.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()){
                            if (snapshot.getString("vendorId").equals(auth.getUid())){
                                if (snapshot.getBoolean("paymentStatus")){
                                    list.add(new OrderModel(
                                            snapshot.getString("productName"),
                                            snapshot.getString("price"),
                                            snapshot.getString("maxPrice"),
                                            snapshot.getString("qtyBuy"),
                                            snapshot.getString("address"),
                                            snapshot.getString("phone"),
                                            snapshot.getString("email"),
                                            snapshot.getLong("createdAt"),
                                            snapshot.getString("paymentId"),
                                            snapshot.getBoolean("paymentStatus"),
                                            snapshot.getString("vendorId"),
                                            snapshot.getString("userId"),
                                            snapshot.getString("imageUrl"),
                                            snapshot.getString("payableAmt"),
                                            snapshot.getString("sellerName"),
                                            snapshot.getString("userName"),
                                            snapshot.getId()
                                    ));
                                }
                            }else {
                                Log.d(TAG_ORDER, "getDataOrder: "+snapshot.getString("vendorId"));
                            }
                            binding.swipeRefreshLayoutOrders.setRefreshing(false);
                            adapter.notifyDataSetChanged();
                        }
                    }else {
                        Constant.setMessage(getContext(), Objects.requireNonNull(error).getMessage());
                    }
                });
    }
}