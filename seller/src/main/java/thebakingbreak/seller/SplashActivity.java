package thebakingbreak.seller;

import static thebakingbreak.seller.helper.Constant.email;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.HashMap;

import thebakingbreak.seller.activities.MainActivity;
import thebakingbreak.seller.auth.LoginActivity;
import thebakingbreak.seller.preferences.UserPreferences;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        HashMap<String,String> info = new UserPreferences(this).getInfoData();

        new Handler().postDelayed(()->{
            if (info.get(email) != null){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }else {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        },2000);
    }
}