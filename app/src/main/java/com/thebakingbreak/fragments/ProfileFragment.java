package com.thebakingbreak.fragments;

import static com.thebakingbreak.helper.Constant.ADDRESS_DB;
import static com.thebakingbreak.helper.Constant.TAG_HOME;
import static com.thebakingbreak.helper.Constant.USER_DB;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thebakingbreak.R;
import com.thebakingbreak.databinding.FragmentProfileBinding;
import com.thebakingbreak.helper.Constant;
import com.thebakingbreak.models.AddressModel;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    FirebaseAuth auth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        auth = FirebaseAuth.getInstance();
        getUser();
        getAddress();

        return binding.getRoot();
    }

    private void openUpdateDialog(String username, String phoneNumber, String ads, String city,
                                  String state, String country, String zipCode) {
        View view = getLayoutInflater().inflate(R.layout.dialog_address_layout,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        EditText eName = view.findViewById(R.id.eName);
        EditText eAddressLine = view.findViewById(R.id.eAddressLine);
        EditText eAddressBuilding = view.findViewById(R.id.eAddressBuilding);
        EditText eAddressCity = view.findViewById(R.id.eAddressCity);
        EditText eAddressZipcode = view.findViewById(R.id.eAddressZipcode);
        EditText eAddressState = view.findViewById(R.id.eAddressState);
        EditText eAddressCountry = view.findViewById(R.id.eAddressCountry);
        EditText eAddressPhoneNumber = view.findViewById(R.id.eAddressPhoneNumber);
        TextView text_save_address = view.findViewById(R.id.text_save_address);
        TextView text_cancel_address = view.findViewById(R.id.text_cancel_address);

        eName.setText(username);
        eAddressLine.setText(ads);
        eAddressCity.setText(city);
        eAddressZipcode.setText(zipCode);
        eAddressState.setText(state);
        eAddressCountry.setText(country);
        eAddressPhoneNumber.setText(phoneNumber);

        text_cancel_address.setOnClickListener(v -> {
            dialog.dismiss();
            dialog.cancel();
        });

        text_save_address.setOnClickListener(v -> {
            if (eName.getText().toString().isEmpty()){
                eName.setError("Empty");
            }else if (eAddressLine.getText().toString().isEmpty()){
                eAddressLine.setError("Empty");
            }else if (eAddressCity.getText().toString().isEmpty()){
                eAddressCity.setError("Empty");
            }else if (eAddressZipcode.getText().toString().isEmpty()){
                eAddressZipcode.setError("Empty");
            }else if (eAddressState.getText().toString().isEmpty()){
                eAddressState.setError("Empty");
            }else if (eAddressCountry.getText().toString().isEmpty()){
                eAddressCountry.setError("Empty");
            }else if (eAddressPhoneNumber.getText().toString().isEmpty()){
                eAddressPhoneNumber.setError("Empty");
            }else {
                saveUpdateAddress(
                        eName.getText().toString().trim(),
                        eAddressLine.getText().toString().trim(),
                        eAddressBuilding.getText().toString().trim(),
                        eAddressCity.getText().toString().trim(),
                        eAddressZipcode.getText().toString().trim(),
                        eAddressState.getText().toString().trim(),
                        eAddressCountry.getText().toString().trim(),
                        eAddressPhoneNumber.getText().toString().trim(),
                        dialog
                );
            }
        });

        dialog.show();

    }

    private void saveUpdateAddress(String name, String addressOne, String addressTwo, String city,
                                   String zipcode, String state, String country, String phone, AlertDialog dialog) {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        HashMap<String, Object> map = new HashMap<>();
        map.put("name",name);
        map.put("address",addressOne+","+addressTwo);
        map.put("city",city);
        map.put("pincode",zipcode);
        map.put("state",state);
        map.put("country",country);
        map.put("phone",phone);
        map.put("uid",auth.getUid());
        map.put("active",true);
        firestore.collection(ADDRESS_DB)
                .document(auth.getUid())
                .update(map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Constant.setMessage(requireContext(),"address updated successfully..!");
                        dialog.cancel();
                        dialog.dismiss();
                        getAddress();
                    }
                }).addOnFailureListener(e -> {
                    Constant.setMessage(requireContext(), e.getMessage());
                    dialog.cancel();
                    dialog.dismiss();
                });
    }

    private void getAddress() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(ADDRESS_DB)
                .document(auth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        String fullAddress = task.getResult().getString("address") + ",\n"+
                                task.getResult().getString("city")+","+
                                task.getResult().getString("state")+","+
                                task.getResult().getString("country")+" ("+
                                task.getResult().getString("pincode")+").";
                        String name = task.getResult().getString("name");
                        String address = task.getResult().getString("address");
                        String city = task.getResult().getString("city");
                        String state = task.getResult().getString("state");
                        String country = task.getResult().getString("country");
                        String pincode = task.getResult().getString("pincode");
                        String phone = task.getResult().getString("phone");

                        textViewSetValue(fullAddress,name,address,city,state,country,pincode,phone);
                    }
                }).addOnFailureListener(e -> Log.d(TAG_HOME, "getAddress: "+e.getMessage()));
    }

    private void textViewSetValue(String fullAddress, String name, String address, String city, String state, String country, String pincode, String phone) {
        if (name == null){
            Log.d(TAG_HOME, "textViewSetValue: Empty "+ name);
            binding.btnAddress.setText("Add address");
            binding.btnAddress.setOnClickListener(v -> openDialog());
        }else {
            binding.btnAddress.setText("Update address");
            binding.textViewAddressName.setText(name);
            binding.textViewFullAddress.setText(fullAddress);
            binding.textViewAddressPhone.setText(phone);
            binding.btnAddress.setOnClickListener(v -> openUpdateDialog(name,phone,address,city,state,country,pincode));
            Log.d(TAG_HOME, "textViewSetValue: full "+name);
        }
    }

    private void openDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_address_layout,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        EditText eName = view.findViewById(R.id.eName);
        EditText eAddressLine = view.findViewById(R.id.eAddressLine);
        EditText eAddressBuilding = view.findViewById(R.id.eAddressBuilding);
        EditText eAddressCity = view.findViewById(R.id.eAddressCity);
        EditText eAddressZipcode = view.findViewById(R.id.eAddressZipcode);
        EditText eAddressState = view.findViewById(R.id.eAddressState);
        EditText eAddressCountry = view.findViewById(R.id.eAddressCountry);
        EditText eAddressPhoneNumber = view.findViewById(R.id.eAddressPhoneNumber);
        TextView text_save_address = view.findViewById(R.id.text_save_address);
        TextView text_cancel_address = view.findViewById(R.id.text_cancel_address);

        text_cancel_address.setOnClickListener(v -> {
            dialog.dismiss();
            dialog.cancel();
        });

        text_save_address.setOnClickListener(v -> {
            if (eName.getText().toString().isEmpty()){
                eName.setError("Empty");
            }else if (eAddressLine.getText().toString().isEmpty()){
                eAddressLine.setError("Empty");
            }else if (eAddressCity.getText().toString().isEmpty()){
                eAddressCity.setError("Empty");
            }else if (eAddressZipcode.getText().toString().isEmpty()){
                eAddressZipcode.setError("Empty");
            }else if (eAddressState.getText().toString().isEmpty()){
                eAddressState.setError("Empty");
            }else if (eAddressCountry.getText().toString().isEmpty()){
                eAddressCountry.setError("Empty");
            }else if (eAddressPhoneNumber.getText().toString().isEmpty()){
                eAddressPhoneNumber.setError("Empty");
            }else {
                saveAddress(
                        eName.getText().toString().trim(),
                        eAddressLine.getText().toString().trim(),
                        eAddressBuilding.getText().toString().trim(),
                        eAddressCity.getText().toString().trim(),
                        eAddressZipcode.getText().toString().trim(),
                        eAddressState.getText().toString().trim(),
                        eAddressCountry.getText().toString().trim(),
                        eAddressPhoneNumber.getText().toString().trim(),
                        dialog
                );
            }
        });

        dialog.show();
    }

    private void saveAddress(String name, String addressOne, String addressTwo, String city,
                             String zipcode, String state, String country, String phone, AlertDialog dialog) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        AddressModel model = new AddressModel(
                name,
                addressOne + ", "+addressTwo,
                city,
                zipcode,
                state,
                country,
                phone,
                Objects.requireNonNull(auth.getUid()),
                new Date().getTime(),
                false
        );
        firestore.collection(ADDRESS_DB)
                .document(auth.getUid())
                .set(model)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Constant.setMessage(requireContext(),"address added successfully..!");
                        dialog.cancel();
                        dialog.dismiss();
                        getAddress();
                    }
                })
                .addOnFailureListener(e -> {
                    Constant.setMessage(requireContext(), e.getMessage());
                    dialog.cancel();
                    dialog.dismiss();
                });
    }

    private void getUser(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(USER_DB)
                .document(Objects.requireNonNull(auth.getUid()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        String name = task.getResult().getString("name");
                        String email = task.getResult().getString("email");
                        String phone = task.getResult().getString("phoneNumber");
                        binding.textViewNameValue.setText(name);
                        binding.textViewEmailValue.setText(email);
                        binding.textViewPhoneValue.setText(phone);
                    }
                });
    }

}