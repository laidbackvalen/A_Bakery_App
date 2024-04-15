package thebakingbreak.seller.preferences;

import static thebakingbreak.seller.helper.Constant.pwd;
import static thebakingbreak.seller.helper.Constant.userFilePreferences;
import static thebakingbreak.seller.helper.Constant.email;

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


    public HashMap<String, String> getInfoData(){
        HashMap<String, String> info = new HashMap<>();
        info.put(email,preferences.getString(email,null));
        info.put(pwd,preferences.getString(pwd,null));
        return info;
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }
}
