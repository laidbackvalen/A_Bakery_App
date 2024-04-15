package com.thebakingbreak.admin.activities.users;

import static com.thebakingbreak.admin.helper.Constant.USER_DB;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thebakingbreak.admin.R;
import com.thebakingbreak.admin.adapters.UserAdapter;
import com.thebakingbreak.admin.databinding.ActivityUsersBinding;
import com.thebakingbreak.admin.helper.UserInfo;
import com.thebakingbreak.admin.models.UserModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class UsersActivity extends AppCompatActivity implements UserInfo {

    ActivityUsersBinding binding;
    List<UserModel> list = new ArrayList<>();
    UserAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.swipeRefreshLayoutUser.setOnRefreshListener(this::getUserData);
        LinearLayoutManager manager = new LinearLayoutManager(UsersActivity.this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        binding.recyclerViewUser.setLayoutManager(manager);
        adapter = new UserAdapter(UsersActivity.this,list, this);
        binding.recyclerViewUser.setAdapter(adapter);
        getUserData();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getUserData() {
        binding.swipeRefreshLayoutUser.setRefreshing(true);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(USER_DB)
                .addSnapshotListener((value, error) -> {
                    list.clear();
                    if (value != null){
                            for (DocumentSnapshot snapshot: value.getDocuments()){
                                Log.d("TAG_DATA", "getUserData: "+snapshot.getId());
                                if (!Objects.equals(snapshot.getString("userType"), "admin")){
                                    list.add(new UserModel(
                                            snapshot.getString("name"),
                                            snapshot.getString("phoneNumber"),
                                            snapshot.getString("email"),
                                            snapshot.getString("userType"),
                                            snapshot.getLong("createdAt"),
                                            snapshot.getLong("updatedAt"),
                                            snapshot.getId()
                                    ));
                                    binding.swipeRefreshLayoutUser.setRefreshing(false);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                });
    }

    @Override
    public void onUserInfo(String userId) {
        View view = getLayoutInflater().inflate(R.layout.customer_layout,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(UsersActivity.this);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        TextView textName = view.findViewById(R.id.textViewNameValue);
        TextView textEmail = view.findViewById(R.id.textViewEmailValue);
        TextView textPhone = view.findViewById(R.id.textViewPhoneValue);
        TextView textAccount = view.findViewById(R.id.textViewAccountCreatingDateValue);
        TextView textLocation = view.findViewById(R.id.textViewLocation);
        TextView textLocationValue = view.findViewById(R.id.textViewLocationValue);
        TextView textAadhaar = view.findViewById(R.id.textViewAadhaar);
        TextView textAadhaarValue = view.findViewById(R.id.textViewAadhaarValue);
        TextView textStore = view.findViewById(R.id.textViewStoreName);
        TextView textStoreValue = view.findViewById(R.id.textViewStoreNameValue);

        FirebaseFirestore
                .getInstance()
                        .collection(USER_DB)
                                .document(userId)
                                        .addSnapshotListener((value, error) -> {
                                           if (value != null){
                                               if (value.getString("userType").equals("seller")){
                                                   textName.setText(value.getString("name"));
                                                   textPhone.setText(value.getString("phoneNumber"));
                                                   textEmail.setText(value.getString("email"));
                                                   textLocation.setVisibility(View.VISIBLE);
                                                   textLocationValue.setVisibility(View.VISIBLE);
                                                   textAadhaar.setVisibility(View.VISIBLE);
                                                   textAadhaarValue.setVisibility(View.VISIBLE);
                                                   textStore.setVisibility(View.VISIBLE);
                                                   textStoreValue.setVisibility(View.VISIBLE);
                                                   textLocationValue.setText(value.getString("location"));
                                                   textAadhaarValue.setText(value.getString("ownerAadhaar"));
                                                   textStoreValue.setText(value.getString("storeName"));
                                                   textAccount.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date(value.getLong("createdAt"))));
                                               }else if (value.getString("userType").equals("customer")){
                                                   textName.setText(value.getString("name"));
                                                   textPhone.setText(value.getString("phoneNumber"));
                                                   textEmail.setText(value.getString("email"));
                                                   textAccount.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date(value.getLong("createdAt"))));
                                               }
                                           }
                                        });

        dialog.show();
    }
}