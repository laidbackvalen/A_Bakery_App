package thebakingbreak.seller.auth;

import static thebakingbreak.seller.helper.Constant.REGIS_SUCCESS;
import static thebakingbreak.seller.helper.Constant.Seller;
import static thebakingbreak.seller.helper.Constant.USER_DB;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Objects;

import thebakingbreak.seller.R;
import thebakingbreak.seller.databinding.ActivityCreateAccountBinding;
import thebakingbreak.seller.helper.Constant;
import thebakingbreak.seller.models.UserModel;

public class CreateAccountActivity extends AppCompatActivity {

    ActivityCreateAccountBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        validations();

        binding.imgBackArrow.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });

        binding.btnSubmit.setOnClickListener(v -> registrationValidation());
    }

    private void registrationValidation() {
        if (binding.nameRegistration.getText().toString().isEmpty()){
            binding.nameRegistration.setError(Constant.EMPTY);
        }else if (binding.storeNameRegistration.getText().toString().isEmpty()){
            binding.storeNameRegistration.setError(Constant.EMPTY);
        }else if (binding.storeLocationRegistration.getText().toString().isEmpty()){
            binding.storeLocationRegistration.setError(Constant.EMPTY);
        }else if (binding.ownerAadhaarRegistration.getText().toString().isEmpty()){
            binding.ownerAadhaarRegistration.setError(Constant.EMPTY);
        }else if (binding.phoneNumberRegistration.getText().toString().isEmpty()){
            binding.phoneNumberRegistration.setError(Constant.EMPTY);
        }else if (binding.emailRegistration.getText().toString().isEmpty()){
            binding.emailRegistration.setError(Constant.EMPTY);
        }else if (binding.passwordRegistration.getText().toString().isEmpty()){
            binding.passwordRegistration.setError(Constant.EMPTY);
        }else {
            sellerRegistration();
        }
    }

    private void validations() {
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
                    binding.nameRegistration.setError(Constant.EMPTY);
                    binding.nameRegistration.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                }else {
                    binding.nameRegistration.setBackgroundResource(R.drawable.edit_bg_drawable);
                }
            }
        });

        binding.storeNameRegistration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    binding.storeNameRegistration.setError(Constant.EMPTY);
                    binding.storeNameRegistration.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                }else {
                    binding.storeNameRegistration.setBackgroundResource(R.drawable.edit_bg_drawable);
                }
            }
        });

        binding.storeLocationRegistration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    binding.storeLocationRegistration.setError(Constant.EMPTY);
                    binding.storeLocationRegistration.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                }else {
                    binding.storeLocationRegistration.setBackgroundResource(R.drawable.edit_bg_drawable);
                }
            }
        });

        binding.ownerAadhaarRegistration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    binding.ownerAadhaarRegistration.setError(Constant.EMPTY);
                    binding.ownerAadhaarRegistration.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                }else {
                    binding.ownerAadhaarRegistration.setBackgroundResource(R.drawable.edit_bg_drawable);
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
                    binding.phoneNumberRegistration.setError(Constant.EMPTY);
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
                if (!s.toString().matches(Constant.emailPattern)){
                    binding.emailRegistration.setError(Constant.EMAIL_VALID);
                    binding.emailRegistration.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                }else {
                    binding.emailRegistration.setBackgroundResource(R.drawable.edit_bg_drawable);
                }
                if (s.toString().isEmpty()){
                    binding.emailRegistration.setError(Constant.EMPTY);
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
                if (s.length() < 8) {
                    binding.passwordRegistration.setError(Constant.PASSWORD_LIMIT);
                    binding.viewPwdRegistration.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                }else {
                    binding.viewPwdRegistration.setBackgroundResource(R.drawable.edit_bg_drawable);
                }
                if (s.toString().isEmpty()){
                    binding.passwordRegistration.setError(Constant.EMPTY);
                    binding.viewPwdRegistration.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
                }else {
                    binding.viewPwdRegistration.setBackgroundResource(R.drawable.edit_bg_drawable);
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

    private void sellerRegistration(){
        auth.createUserWithEmailAndPassword(
                binding.emailRegistration.getText().toString().trim(),
                binding.passwordRegistration.getText().toString().trim()
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                sendVerificationMailAddress(Objects.requireNonNull(task.getResult().getUser()));
            }else {
                Constant.setMessage(CreateAccountActivity.this,"");
            }
        }).addOnFailureListener(e -> Constant.setMessage(CreateAccountActivity.this, e.getMessage()));
    }

    private void sendVerificationMailAddress(FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                sendDataIntoFireStoreDatabase();
            }
        }).addOnFailureListener(e -> Constant.setMessage(CreateAccountActivity.this, e.getMessage()));
    }

    private void sendDataIntoFireStoreDatabase() {
        UserModel model = new UserModel(
                binding.nameRegistration.getText().toString().trim(),
                binding.storeNameRegistration.getText().toString().trim(),
                binding.storeLocationRegistration.getText().toString().trim(),
                binding.ownerAadhaarRegistration.getText().toString().trim(),
                binding.phoneNumberRegistration.getText().toString().trim(),
                binding.emailRegistration.getText().toString().trim(),
                Seller,
                new Date().getTime(),
                new Date().getTime()
        );

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference reference = firestore.collection(USER_DB).document(Objects.requireNonNull(auth.getUid()));

        reference.set(model)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Constant.setMessage(CreateAccountActivity.this, REGIS_SUCCESS);
                        auth.signOut();
                        binding.nameRegistration.setText("");
                        binding.storeNameRegistration.setText("");
                        binding.storeLocationRegistration.setText("");
                        binding.ownerAadhaarRegistration.setText("");
                        binding.phoneNumberRegistration.setText("");
                        binding.emailRegistration.setText("");
                        binding.passwordRegistration.setText("");
                    }else {
                        auth.signOut();
                    }
                })
                .addOnFailureListener(e -> Constant.setMessage(CreateAccountActivity.this, e.getMessage()));
    }

}
