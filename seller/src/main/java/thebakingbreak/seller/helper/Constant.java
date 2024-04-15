package thebakingbreak.seller.helper;

import android.content.Context;
import android.widget.Toast;

public class Constant {
    public static final String userFilePreferences = "user";

    public static final String TAG_PRODUCT = "productActivity";
    public static final String TAG_ORDER = "orderActivity";
    public static final String email = "email";
    public static final String pwd = "pwd";
    public static final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static final String EMPTY = "Empty";
    public static final String Seller = "seller";
    public static final String USER_DB = "users";
    public static final String PRODUCT_DB = "product";
    public static final String ORDER_DB = "orders";
    public static final String TAG_HOME = "home";
    public static final String TRACKING_DB = "tracking";
    public static final String TOKEN = "token";
    public static final String CATEGORY = "category";
    public static final String PRODUCT_IMAGE = "product";
    public static final String EMAIL_VALID = "Enter a valid email address";
    public static final String PASSWORD_LIMIT = "Required password length is 8";
    public static final String REGIS_SUCCESS = "Registration Successfully\n Please verification mail sent..!";
    public static final String VERIFICATION_MAIL = "verification mail sent..!";

    public static void setMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
