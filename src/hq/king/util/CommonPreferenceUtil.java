package hq.king.util;

import android.content.Context;
import android.content.SharedPreferences;



public class CommonPreferenceUtil  {

	
	public static void setBoolean(Context context,String name,boolean bool)
	{
		SharedPreferences sp = context.getSharedPreferences("htq_qq", context.MODE_WORLD_READABLE);
		SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(name, bool);
        editor.commit();
	}

	public static void setString(Context context,String name,String value)
	{
		SharedPreferences sp = context.getSharedPreferences("htq_qq", context.MODE_WORLD_READABLE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(name, value);
		editor.commit();
		
	}


	
}
