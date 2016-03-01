package hq.king.activity;

import hq.king.util.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends Activity {
private RequestQueue mQueue;
private TextView temperature,weekTV,dateTV,weatherDesc;
private ImageView weatherIcon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		findView();
		init();
	}
	void findView()
	{
		temperature=(TextView) findViewById(R.id.weather_temperature);
		weekTV=(TextView) findViewById(R.id.weather_week);
		dateTV=(TextView) findViewById(R.id.weather_date);
		weatherDesc=(TextView) findViewById(R.id.weather_desc);
		weatherIcon=(ImageView) findViewById(R.id.weather_image);
	}
	
	void init()
	{

		mQueue= Volley.newRequestQueue(this);
		JsonObjectRequest jsonObjectRequest = new VolleyUtil.CharsetJsonRequest("http://www.weather.com.cn/data/cityinfo/101200101.html", null,
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						if(response.has("weatherinfo"))
						try {
							JSONObject weatherInfo=response.getJSONObject("weatherinfo");
							//String city=weatherInfo.getString("city");
							String temp1=weatherInfo.getString("temp1");
							String weather1=weatherInfo.getString("weather");
						//	dateTV.setText(date);
						//	weekTV.setText(week);
							temperature.setText(temp1);
							weatherDesc.setText(weather1);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}, new Response.ErrorListener() {
					
					public void onErrorResponse(VolleyError error) {
						
						//Log.e("TAG", error.getMessage(), error);
						Toast.makeText(getApplication(),"网络连接出错，请稍后重试",0).show();
		          }

					
				});
		
		mQueue.add(jsonObjectRequest);
	}

}
