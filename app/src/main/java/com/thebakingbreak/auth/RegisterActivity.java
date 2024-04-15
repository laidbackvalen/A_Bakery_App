package com.thebakingbreak.auth;

import static com.thebakingbreak.helper.Constant.CUSTOMER;
import static com.thebakingbreak.helper.Constant.REGIS_SUCCESS;
import static com.thebakingbreak.helper.Constant.USER_DB;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.thebakingbreak.R;
import com.thebakingbreak.databinding.ActivityRegisterBinding;
import com.thebakingbreak.helper.Constant;
import com.thebakingbreak.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        validation();

        binding.btnSubmit.setOnClickListener(v -> registerData());
    }

    private void registerData() {
        if (binding.nameRegistration.getText().toString().isEmpty()){
            binding.nameRegistration.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
        }else if (binding.phoneNumberRegistration.getText().toString().isEmpty()){
            binding.phoneNumberRegistration.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
        }else if (binding.emailRegistration.getText().toString().isEmpty()){
            binding.emailRegistration.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
        }else if (binding.passwordRegistration.getText().toString().isEmpty()){
            binding.passwordRegistration.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
        }else {
            register(
                    binding.emailRegistration.getText().toString().trim(),
                    binding.passwordRegistration.getText().toString().trim()
            );
        }
    }

    private void register(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        sendVerificationMail(task.getResult().getUser());
                    }
                }).addOnFailureListener(e -> Constant.setMessage(RegisterActivity.this,e.getMessage()));
    }

    private void sendVerificationMail(FirebaseUser result) {
        if (result != null){
            result.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    sendDataIntoDb();
                }
            }).addOnFailureListener(e -> Constant.setMessage(RegisterActivity.this,e.getMessage()));
        }
    }

    private void sendDataIntoDb() {
        UserModel model = new UserModel(
                binding.nameRegistration.getText().toString().trim(),
                binding.phoneNumberRegistration.getText().toString().trim(),
                binding.emailRegistration.getText().toString().trim(),
                CUSTOMER,
                new Date().getTime(),
                new Date().getTime()
        );
        FirebaseFirestore.getInstance()
                .collection(USER_DB)
                .document(Objects.requireNonNull(auth.getUid()))
                .set(model)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Constant.setMessage(RegisterActivity.this,REGIS_SUCCESS);
                        auth.signOut();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();
                    }
                }).addOnFailureListener(e -> Constant.setMessage(RegisterActivity.this,e.getMessage()));
    }

    private void validation() {

        binding.nameRegistration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    binding.nameRegistration.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                }else {
                    binding.nameRegistration.setBackgroundResource(R.drawable.edit_bg_drawable);
                }
            }
        });

        binding.phoneNumberRegistration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    binding.phoneNumberRegistration.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                }else {
                    binding.phoneNumberRegistration.setBackgroundResource(R.drawable.edit_bg_drawable);
                }
            }
        });

        binding.emailRegistration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    binding.emailRegistration.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                }

                if (!(s.toString().matches(Constant.emailPattern))) {
                    binding.emailRegistration.setError(Constant.EMAIL_VALID);
                    binding.emailRegistration.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                }else {
                    binding.emailRegistration.setBackgroundResource(R.drawable.edit_bg_drawable);
                }
            }
        });

        binding.passwordRegistration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    binding.passwordRegistration.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                }else {
                    binding.passwordRegistration.setBackgroundResource(R.drawable.edit_bg_drawable);
                }
            }
        });

    }

    public void showPassword(View view) {
        if(view.getId()==R.id.imgVisible){
            if(binding.passwordRegistration.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                binding.imgVisible.setImageResource(R.drawable.ic_visibility_off);
                //Show Password
                binding.passwordRegistration.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else{
                binding.imgVisible.setImageResource(R.drawable.ic_visibility);
                //Hide Password
                binding.passwordRegistration.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }

}