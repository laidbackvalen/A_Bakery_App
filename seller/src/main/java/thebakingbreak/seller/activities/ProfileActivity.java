package thebakingbreak.seller.activities;

import static thebakingbreak.seller.helper.Constant.USER_DB;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import thebakingbreak.seller.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        getUser(auth.getUid());
    }
    private void getUser(String uid) {
        FirebaseFirestore.getInstance()
                .collection(USER_DB)
                .document(uid)
                .addSnapshotListener((value, error) -> {
                    binding.textViewNameValue.setText(value.getString("name"));
                    binding.textViewEmailValue.setText(value.getString("email"));
                    binding.textViewPhoneValue.setText(value.getString("phoneNumber"));
                    binding.textViewLocationValue.setText(value.getString("location"));
                    binding.textViewStoreNameValue.setText(value.getString("storeName"));
                    binding.textViewAadhaarValue.setText(value.getString("ownerAadhaar"));
                    binding.textViewAccountCreatingDateValue.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault()).format(new Date(value.getLong("createdAt"))));
                });
    }
}