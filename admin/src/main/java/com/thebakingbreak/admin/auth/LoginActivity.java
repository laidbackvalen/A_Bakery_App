package com.thebakingbreak.admin.auth;

import static com.thebakingbreak.admin.helper.Constant.ADMIN;
import static com.thebakingbreak.admin.helper.Constant.USER_DB;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.thebakingbreak.admin.R;
import com.thebakingbreak.admin.activities.MainActivity;
import com.thebakingbreak.admin.databinding.ActivityLoginBinding;
import com.thebakingbreak.admin.helper.Constant;
import com.thebakingbreak.admin.preferences.UserPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        validations();
        auth = FirebaseAuth.getInstance();
        binding.btnLogin.setOnClickListener(v -> signInValidation());

        binding.forgotPwd.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ForgotActivity.class)));

    }

    private void validations() {
        binding.emailAddressLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    binding.emailAddressLogin.setError(Constant.EMPTY);
                    binding.emailAddressLogin.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                }else {
                    binding.emailAddressLogin.setBackgroundResource(R.drawable.edit_bg_drawable);
                }

                if (!s.toString().matches(Constant.emailPattern)){
                    binding.emailAddressLogin.setError(Constant.EMAIL_VALID);
                    binding.emailAddressLogin.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                }else {
                    binding.emailAddressLogin.setBackgroundResource(R.drawable.edit_bg_drawable);
                }
            }
        });

        binding.passwordLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 8) {
                    binding.passwordLogin.setError(Constant.PASSWORD_LIMIT);
                    binding.viewPwd.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                }else {
                    binding.viewPwd.setBackgroundResource(R.drawable.edit_bg_drawable);
                }

                if (s.toString().isEmpty()){
                    binding.passwordLogin.setError(Constant.EMPTY);
                    binding.viewPwd.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                }else {
                    binding.viewPwd.setBackgroundResource(R.drawable.edit_bg_drawable);
                }
            }
        });
    }

    private void signInValidation() {
        if (binding.emailAddressLogin.getText().toString().isEmpty()){
            binding.emailAddressLogin.setError(Constant.EMPTY);
        }else if (!binding.emailAddressLogin.getText().toString().matches(Constant.emailPattern)){
            binding.emailAddressLogin.setError(Constant.EMAIL_VALID);
        }else if (binding.passwordLogin.getText().toString().isEmpty()){
            binding.passwordLogin.setError(Constant.EMPTY);
        }else {
            signIn();
        }
    }

    private void signIn() {
        auth.signInWithEmailAndPassword(
                        binding.emailAddressLogin.getText().toString().trim(),
                        binding.passwordLogin.getText().toString().trim())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                        firestore.collection(USER_DB)
                                .addSnapshotListener((value, error) -> {
                                    if (value != null){
                                        for (DocumentSnapshot snapshot : value.getDocuments()){
                                            if (Objects.equals(snapshot.getString("email"), binding.emailAddressLogin.getText().toString().trim())){
                                                if (Objects.equals(snapshot.getString("userType"),ADMIN)){
                                                    new UserPreferences(LoginActivity.this)
                                                            .setUserInfo(
                                                                    binding.emailAddressLogin.getText().toString().trim(),
                                                                    binding.passwordLogin.getText().toString().trim()
                                                            );
                                                    binding.emailAddressLogin.setText("");
                                                    binding.passwordLogin.setText("");
                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                    finish();
                                                    break;
                                                }else {
                                                    Constant.setMessage(LoginActivity.this,"You are not admin");
                                                    auth.signOut();
                                                }
                                            }
                                        }
                                    }else {
                                        Constant.setMessage(LoginActivity.this,binding.emailAddressLogin.getText().toString().trim()+" check your user name and password..!");
                                        auth.signOut();
                                    }
                                });
                    }else {
                        Constant.setMessage(LoginActivity.this,"You are not admin");
                        auth.signOut();
                    }
                })
                .addOnFailureListener(e -> Constant.setMessage(LoginActivity.this, e.getMessage()));
    }

    public void showPassword(View view) {
        if(view.getId()==R.id.imgVisible){
            if(binding.passwordLogin.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                binding.imgVisible.setImageResource(R.drawable.ic_visibility_off);
                //Show Password
                binding.passwordLogin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else{
                binding.imgVisible.setImageResource(R.drawable.ic_visibility);
                //Hide Password
                binding.passwordLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }
}