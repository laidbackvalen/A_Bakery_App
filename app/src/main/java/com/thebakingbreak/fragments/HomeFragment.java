package com.thebakingbreak.fragments;

import static com.thebakingbreak.helper.Constant.CATEGORY;
import static com.thebakingbreak.helper.Constant.PRODUCT_DB;
import static com.thebakingbreak.helper.Constant.TAG_HOME;
import static com.thebakingbreak.helper.Constant.TAG_PRODUCT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.thebakingbreak.Preferences.UserPreferences;
import com.thebakingbreak.R;
import com.thebakingbreak.activities.CartActivity;
import com.thebakingbreak.activities.CategoryWiseActivity;
import com.thebakingbreak.adapters.ProductAdapter;
import com.thebakingbreak.adapters.SlideAdapterView;
import com.thebakingbreak.auth.LoginActivity;
import com.thebakingbreak.databinding.FragmentHomeBinding;
import com.thebakingbreak.helper.Constant;
import com.thebakingbreak.models.ProductModel;
import com.thebakingbreak.models.SlideModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    List<ProductModel> list;
    ArrayList<SlideModel> listSlide;
    List<String> listCategory;
    ProductAdapter adapter;
    FirebaseAuth auth;
    private final Handler slideHandler = new Handler();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        binding.slideViewPager2.setVisibility(View.GONE);

        list = new ArrayList<>();
        listSlide = new ArrayList<>();
        listCategory = new ArrayList<>();

        binding.imgCart.setOnClickListener(v -> startActivity(new Intent(getContext(), CartActivity.class)));
        binding.imgMenu.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(getContext(),binding.imgMenu);
            menu.getMenu().add("Category")
                    .setOnMenuItemClickListener(item -> {
                        categoryDialog();
                        return false;
                    });

            menu.getMenu().add("Logout")
                    .setOnMenuItemClickListener(item -> {
                        new UserPreferences(getContext()).clear();
                        auth.signOut();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        getActivity().finish();
                        return false;
                    });
            menu.show();
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSlider();
        getCategory();
        binding.swipeLayoutProduct.setOnRefreshListener(this::getProduct);
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        binding.recyclerViewProduct.setLayoutManager(manager);
        adapter = new ProductAdapter(getContext(),list);
        binding.recyclerViewProduct.setAdapter(adapter);
        getProduct();
    }

    private void getCategory(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(CATEGORY)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        list.clear();
                        for (DocumentSnapshot snapshot: task.getResult().getDocuments()){
                            Log.d(TAG_HOME, "categoryDialog: "+snapshot.getString(CATEGORY));
                            listCategory.add(snapshot.getString(CATEGORY));
                        }
                    }
                });
    }

    private void categoryDialog(){
        View v = getLayoutInflater().inflate(R.layout.category_list_dialog_layout,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(v);
        AlertDialog dialog = builder.create();
        ListView listView = v.findViewById(R.id.listCategory);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),R.layout.category_layout,R.id.textViewCategory,listCategory);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String category = listCategory.get(position);
            Log.d(TAG_PRODUCT, "onItemClick: "+category);
            startActivity(new Intent(getContext(), CategoryWiseActivity.class)
                    .putExtra("category",category));
            dialog.dismiss();
            dialog.cancel();
        });

        dialog.show();
    }

    private void getSlider() {
        FirebaseFirestore
                .getInstance()
                .collection(PRODUCT_DB)
                .addSnapshotListener(new EventListener<>() {
                                         @Override
                                         public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                             if (value != null) {
                                                 listSlide.clear();
                                                 for (DocumentSnapshot snapshot : value.getDocuments()) {
                                                     if (Boolean.TRUE.equals(snapshot.getBoolean("banner"))) {
                                                         listSlide.add(new SlideModel(snapshot.getString("imageUrl")));
                                                         binding.slideViewPager2.setVisibility(View.VISIBLE);
                                                         binding.slideViewPager2.setAdapter(
                                                                 new SlideAdapterView(getContext(), listSlide, binding.slideViewPager2)
                                                         );
                                                         binding.slideViewPager2.setClipToPadding(false);
                                                         binding.slideViewPager2.setClipChildren(false);
                                                         binding.slideViewPager2.setOffscreenPageLimit(2);
                                                         binding.slideViewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                                                         CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                                                         compositePageTransformer.addTransformer(new MarginPageTransformer(7));
                                                         compositePageTransformer.addTransformer((page, position) -> {
                                                             float r = 1 - Math.abs(position);
                                                             page.setScaleY(0.99f + r * 0.01f);
                                                         });
                                                         binding.slideViewPager2.setPageTransformer(compositePageTransformer);
                                                         binding.slideViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                                             @Override
                                                             public void onPageSelected(int position) {
                                                                 super.onPageSelected(position);
                                                                 slideHandler.removeCallbacks(slideRunnable);
                                                                 slideHandler.postDelayed(slideRunnable, 2000);
                                                             }
                                                         });
                                                     }
                                                 }
                                             } else {
                                                 Constant.setMessage(getContext(), Objects.requireNonNull(error).getMessage());
                                             }
                                         }
                                     });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getProduct(){
        binding.swipeLayoutProduct.setRefreshing(true);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference reference = firestore.collection(PRODUCT_DB);
        list.clear();
        reference.get()
                .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                for (DocumentSnapshot snapshot: task.getResult().getDocuments()){
                                    list.add(new ProductModel(
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
                                            Objects.requireNonNull(snapshot.getLong("createdAt")),
                                            Objects.requireNonNull(snapshot.getLong("updated")),
                                            Objects.requireNonNull(snapshot.getBoolean("banner")),
                                            snapshot.getId(),
                                            snapshot.getString("sellerName"),
                                            snapshot.getString("uidCus")
                                    ));
                                    adapter.notifyDataSetChanged();
                                }
                                binding.swipeLayoutProduct.setRefreshing(false);
                            }

                })
                .addOnFailureListener(e -> Constant.setMessage(getContext(),e.getMessage()));
    }

    private final Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            binding.slideViewPager2.setCurrentItem(binding.slideViewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(slideRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        slideHandler.postDelayed(slideRunnable, 2000);
    }

    @Override
    public void onStart() {
        super.onStart();
        getProduct();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        slideHandler.removeCallbacks(slideRunnable);
    }
}