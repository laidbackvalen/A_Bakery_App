package com.thebakingbreak.admin;

import static com.thebakingbreak.admin.helper.Constant.email;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.thebakingbreak.admin.activities.MainActivity;
import com.thebakingbreak.admin.auth.LoginActivity;
import com.thebakingbreak.admin.preferences.UserPreferences;

import java.util.HashMap;

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