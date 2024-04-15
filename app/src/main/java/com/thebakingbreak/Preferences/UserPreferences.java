package com.thebakingbreak.Preferences;

import static com.thebakingbreak.helper.Constant.email;
import static com.thebakingbreak.helper.Constant.phone;
import static com.thebakingbreak.helper.Constant.pwd;
import static com.thebakingbreak.helper.Constant.userFilePreferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class UserPreferences {
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public UserPreferences(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(userFilePreferences, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setUserInfo(String mail, String password){
        editor.putString(email,mail);
        editor.putString(pwd,password);
        editor.commit();
    }

    public void setUserPhone(String phoneNo){
        editor.putString(phone,phoneNo);
        editor.commit();
    }

    public HashMap<String, String> getInfoData(){
        HashMap<String, String> info = new HashMap<>();
        info.put(email,preferences.getString(email,null));
        info.put(pwd,preferences.getString(pwd,null));
        info.put(phone,preferences.getString(phone,null));
        return info;
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }
}
