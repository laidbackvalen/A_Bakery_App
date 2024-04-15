package com.thebakingbreak.admin.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;

import com.thebakingbreak.admin.R;
import com.thebakingbreak.admin.databinding.ActivityForgotBinding;
import com.thebakingbreak.admin.helper.Constant;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {

    ActivityForgotBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();

        binding.eEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    binding.eEmailAddress.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                }else{
                    binding.eEmailAddress.setBackgroundResource(R.drawable.edit_bg_drawable);
                }

                if (!s.toString().matches(Constant.emailPattern)){
                    binding.eEmailAddress.setError(Constant.EMAIL_VALID);
                    binding.eEmailAddress.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                }else {
                    binding.eEmailAddress.setBackgroundResource(R.drawable.edit_bg_drawable);
                }
            }
        });
        binding.imgBack.setOnClickListener(v -> startActivity(new Intent(ForgotActivity.this,LoginActivity.class)));
        binding.btnSent.setOnClickListener(v -> {
            if (binding.eEmailAddress.getText().toString().isEmpty()){
                binding.eEmailAddress.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
            }else {
                sent(binding.eEmailAddress.getText().toString().trim());
            }
        });

    }

    private void sent(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Constant.setMessage(ForgotActivity.this,"Sent..!");
                    }
                }).addOnFailureListener(e -> Constant.setMessage(ForgotActivity.this,e.getMessage()));
    }
}