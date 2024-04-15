package thebakingbreak.seller.auth;

import static thebakingbreak.seller.helper.Constant.Seller;
import static thebakingbreak.seller.helper.Constant.TOKEN;
import static thebakingbreak.seller.helper.Constant.USER_DB;
import static thebakingbreak.seller.helper.Constant.VERIFICATION_MAIL;
import static thebakingbreak.seller.helper.Constant.email;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Objects;

import thebakingbreak.seller.R;
import thebakingbreak.seller.activities.MainActivity;
import thebakingbreak.seller.databinding.ActivityLoginBinding;
import thebakingbreak.seller.helper.Constant;
import thebakingbreak.seller.preferences.UserPreferences;

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

        binding.textCreateAccount.setOnClickListener(v ->
            startActivity(new Intent(getApplicationContext(), CreateAccountActivity.class))
        );

        binding.forgotPwd.setOnClickListener(v ->
            startActivity(new Intent(getApplicationContext(), ForgotActivity.class))
        );

    }

    private void signInValidation() {
        if (binding.emailAddressLogin.getText().toString().isEmpty()){
            binding.emailAddressLogin.setError(Constant.EMPTY);
        }else if (!(binding.emailAddressLogin.getText().toString().matches(Constant.emailPattern))) {
            binding.emailAddressLogin.setError(Constant.EMAIL_VALID);
            binding.emailAddressLogin.setBackgroundResource(R.drawable.edit_wrong_bg_drawable);
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
                                                if (Objects.equals(snapshot.getString("userType"),Seller)){
                                                    verificationMail(task.getResult());
                                                    break;
                                                }else {
                                                    Constant.setMessage(LoginActivity.this,"You are not seller");
                                                    auth.signOut();
                                                }
                                            }
                                        }
                                    }else {
                                        Constant.setMessage(LoginActivity.this,binding.emailAddressLogin.getText().toString().trim()+" doesn't exists ");
                                        auth.signOut();
                                    }
                                });
                    }else {
                        Constant.setMessage(LoginActivity.this,binding.emailAddressLogin.getText().toString().trim()+" doesn't exists ");
                        auth.signOut();
                    }
                })
                .addOnFailureListener(e -> Constant.setMessage(LoginActivity.this, e.getMessage()));
    }

    private void verificationMail(AuthResult result) {
        FirebaseUser user = result.getUser();
         if (user != null){
             boolean isUser = user.isEmailVerified();
             if (isUser){
                 FirebaseMessaging.getInstance().getToken()
                         .addOnCompleteListener(task -> {
                             if (task.isSuccessful()){
                                 HashMap<String, Object> userMap = new HashMap<>();
                                 String token = task.getResult();
                                 userMap.put(TOKEN, token);
                                 FirebaseFirestore.getInstance().collection(USER_DB)
                                         .document(user.getUid())
                                         .update(userMap)
                                         .addOnCompleteListener(task1 -> {
                                             if (task.isSuccessful()){
                                                 new UserPreferences(LoginActivity.this)
                                                         .setUserInfo(
                                                                 binding.emailAddressLogin.getText().toString().trim(),
                                                                 binding.passwordLogin.getText().toString().trim()
                                                         );
                                                 binding.emailAddressLogin.setText("");
                                                 binding.passwordLogin.setText("");
                                                 startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                 finish();
                                             }
                                         });
                             }
                         });

             }else {
                 Constant.setMessage(LoginActivity.this,"Email is not verified");
                 auth.signOut();
                 dialogBox(user);
             }
         }
    }

    private void dialogBox(FirebaseUser user) {
        View view = getLayoutInflater().inflate(R.layout.dialog_resent_verification_mail_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        TextView btnOk = view.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(v -> user.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                auth.signOut();
                Constant.setMessage(LoginActivity.this, VERIFICATION_MAIL);
            }
        }).addOnFailureListener(e -> Constant.setMessage(LoginActivity.this, e.getMessage())));

        dialog.show();
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
                }
            }
        });

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

    @Override
    protected void onStart() {
        super.onStart();
        HashMap<String,String> info =new UserPreferences(LoginActivity.this).getInfoData();
        if (info.get(email) != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

}