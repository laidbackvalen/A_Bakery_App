
package com.thebakingbreak;

import static com.thebakingbreak.helper.Constant.email;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.thebakingbreak.Preferences.UserPreferences;
import com.thebakingbreak.activities.MainActivity;
import com.thebakingbreak.auth.LoginActivity;

import java.util.HashMap;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        HashMap<String,String> info = new UserPreferences(SplashActivity.this).getInfoData();

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