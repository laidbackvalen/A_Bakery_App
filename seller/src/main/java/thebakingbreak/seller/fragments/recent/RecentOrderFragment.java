
package thebakingbreak.seller.fragments.recent;

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

import thebakingbreak.seller.adapters.RecentOrderAdapter;
import thebakingbreak.seller.databinding.FragmentRecentOrderBinding;
import thebakingbreak.seller.helper.Constant;
import thebakingbreak.seller.models.OrderModel;


public class RecentOrderFragment extends Fragment {

    FragmentRecentOrderBinding binding;
    FirebaseAuth auth;
    RecentOrderAdapter adapterOrder;
    List<OrderModel> listOrder = new ArrayList<>();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecentOrderBinding.inflate(inflater,container,false);
        auth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.swipeRefreshLayoutRecentOrder.setOnRefreshListener(this::getDataOrder);
        LinearLayoutManager manager2 = new LinearLayoutManager(getContext());
        binding.recyclerViewRecentOrder.setLayoutManager(manager2);
        adapterOrder = new RecentOrderAdapter(getContext(),listOrder);
        binding.recyclerViewRecentOrder.setAdapter(adapterOrder);
        getDataOrder();
    }

    private void getDataOrder() {
        binding.swipeRefreshLayoutRecentOrder.setRefreshing(true);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(ORDER_DB)
                .addSnapshotListener((value, error) -> {
                    if (value != null){
                        listOrder.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()){
                            if (snapshot.getString("vendorId").equals(auth.getUid())){
                                if (snapshot.getBoolean("paymentStatus")){
                                    listOrder.add(new OrderModel(
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
                                Log.d(TAG_ORDER, "getDataOrder: >>>>>>>>>>>>>>>>>>"+snapshot.getString("vendorId"));
                            }else {
                                Log.d(TAG_ORDER, "getDataOrder: <<<<<<<<<<<<<<<<<<"+snapshot.getString("vendorId"));
                            }
                            binding.swipeRefreshLayoutRecentOrder.setRefreshing(false);
                            adapterOrder.notifyDataSetChanged();
                        }
                    }else {
                        Constant.setMessage(getContext(), Objects.requireNonNull(error).getMessage());
                    }
                });
    }
}