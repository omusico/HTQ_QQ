package hq.king.util;



import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.WindowManager;

public class SystemUtil {

	 public static Dialog getProgressDialog(Context mContext) {
	                ProgressDialog dialog = new ProgressDialog(mContext);
	                dialog.setMessage("正在登录中...");
	                dialog.setIndeterminate(true);
	                return dialog;
	                
	    }
public static int getScreenWidth(Context context)
{
	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	return wm.getDefaultDisplay().getWidth();

}

public static ProgressDialog createWaitDialog(Context context,String msg)
{
	ProgressDialog dialog = new ProgressDialog(context);
	dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	dialog.setCanceledOnTouchOutside(false);
	dialog.setMessage(msg);
	return dialog;
	
}
public static void ToastDialog(Context context, String title, String msg) {
	new AlertDialog.Builder(context).setTitle(title).setMessage(msg)
			.setPositiveButton("确定", null).create().show();
}
public static boolean isNetWorkConnect(Context context)
{
	ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
	if(networkInfo==null||!networkInfo.isAvailable())
    	return false;
	else {
		return true;
	}
}

public static boolean isNumeric(String str){ 
    Pattern pattern = Pattern.compile("[0-9]*");
    return (pattern.matcher(str).matches());
    }
}
