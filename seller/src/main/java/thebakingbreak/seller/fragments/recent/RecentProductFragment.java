package thebakingbreak.seller.fragments.recent;

import static thebakingbreak.seller.helper.Constant.PRODUCT_DB;

import android.os.Bundle;
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

import thebakingbreak.seller.adapters.RecentProductAdapter;
import thebakingbreak.seller.databinding.FragmentRecentProductBinding;
import thebakingbreak.seller.helper.Constant;
import thebakingbreak.seller.models.ProductUpdatedModel;


public class RecentProductFragment extends Fragment {

    FragmentRecentProductBinding binding;
    RecentProductAdapter adapter;
    List<ProductUpdatedModel> listProduct = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecentProductBinding.inflate(inflater,container,false);
        binding.swipeRefreshLayoutRecentProduct.setOnRefreshListener(this::getData);
        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        manager.setStackFromEnd(true);
        binding.recyclerViewRecentProduct.setLayoutManager(manager);
        adapter = new RecentProductAdapter(requireContext(), listProduct);
        binding.recyclerViewRecentProduct.setAdapter(adapter);
        getData();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void getData() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(PRODUCT_DB)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null) {
                        listProduct.clear();
                        for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            String uid = snapshot.getString("uid");
                            binding.swipeRefreshLayoutRecentProduct.setRefreshing(false);
                            if (uid != null) {
                                if (uid.equals(auth.getUid())) {
                                    listProduct.add(new ProductUpdatedModel(
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
                                    binding.swipeRefreshLayoutRecentProduct.setRefreshing(false);
                                    adapter.notifyDataSetChanged();
                                }else {
                                    binding.swipeRefreshLayoutRecentProduct.setRefreshing(false);
                                }
                                binding.swipeRefreshLayoutRecentProduct.setRefreshing(false);
                            }else {
                                binding.swipeRefreshLayoutRecentProduct.setRefreshing(false);
                            }
                        }
                    }else {
                        binding.swipeRefreshLayoutRecentProduct.setRefreshing(false);
                        Constant.setMessage(getContext(),task.getException().getMessage());
                    }
                }).addOnFailureListener(e -> {
                    binding.swipeRefreshLayoutRecentProduct.setRefreshing(false);
                });
    }
}