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

import thebakingbreak.seller.adapters.PaymentAdapter;
import thebakingbreak.seller.databinding.FragmentPaymentBinding;
import thebakingbreak.seller.helper.Constant;
import thebakingbreak.seller.models.OrderModel;


public class PaymentFragment extends Fragment {

    FragmentPaymentBinding binding;

    List<OrderModel> list = new ArrayList<>();
    FirebaseAuth auth;

    PaymentAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        binding.textViewOrderCount.setText("0");
        binding.textViewPaymentCount.setText("0");
        binding.swipeRefreshLayoutPayment.setOnRefreshListener(this::getOrder);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setStackFromEnd(true);
        manager.setReverseLayout(true);
        binding.recyclerViewPayment.setLayoutManager(manager);
        adapter = new PaymentAdapter(getContext(),list);
        binding.recyclerViewPayment.setAdapter(adapter);
        getOrder();
    }

    private void getOrder() {
        binding.swipeRefreshLayoutPayment.setRefreshing(true);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(ORDER_DB)
                .addSnapshotListener((value, error) -> {
                    if (value != null){
                        list.clear();
                        int payment = 0;
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
                                    payment = payment + Integer.parseInt(snapshot.getString("payableAmt"));
                                }
                                Log.d(TAG_ORDER, "getDataOrderPayment: >>>>>>>>>>>>>>>>>>"+snapshot.getString("vendorId"));
                            }else {
                                Log.d(TAG_ORDER, "getDataOrderPayment: <<<<<<<<<<<<<<<<<<"+snapshot.getString("vendorId"));
                            }
                            adapter.notifyDataSetChanged();
                            binding.swipeRefreshLayoutPayment.setRefreshing(false);
                            binding.textViewPaymentCount.setText(String.valueOf(payment));
                            binding.textViewOrderCount.setText(String.valueOf(list.size()));
                        }
                    }else {
                        Constant.setMessage(getContext(), Objects.requireNonNull(error).getMessage());
                    }
                });
    }
}