package android.demo.base;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.JsonParseException;
import android.demo.R;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhuhf
 * @version [AndroidLibrary, 2018-03-16]
 */
public class NetworkError {
    
    public static String errorMsg(Context context, Object ex) {
        String errmsg = null;
        if (ex instanceof ConnectException) {
            if (!isNetworkAvailable(context)) {
                errmsg = context.getString(R.string.common_request_no_network);
            } else {
                errmsg = context.getString(R.string.common_request_failure);
            }
        } else if (ex instanceof TimeoutException || ex instanceof SocketTimeoutException) {
            errmsg = context.getString(R.string.common_request_failure);
        } else if (ex instanceof JsonParseException) {
            errmsg = context.getString(R.string.common_request_parse_error);
        } else {
            errmsg = context.getString(R.string.common_request_failure);
        }
        return errmsg;
    }
    
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
