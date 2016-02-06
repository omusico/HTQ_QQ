package hq.king.activity;


import hq.king.service.GetMsgService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;



public class WelcomeActivity extends Activity {
	private Context mContext;
	private ImageView mImageView;
	private Editor edit;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		mContext = this;
		findView();
		init();
	}
	
	private void findView() {
		mImageView = (ImageView) findViewById(R.id.welcome_img);
	}

	private void init() {
		 final SharedPreferences share=getSharedPreferences("htq",MODE_WORLD_READABLE);
		 edit=share.edit();
		 Intent service = new Intent(this, GetMsgService.class);
		 startService(service);
		// Log.i("Service","service is open");
		 mImageView.postDelayed(new Runnable() {
			public void run() {
				
				  if(share.getInt("flag", 0)==0)
				  {
					Intent intent = new Intent(mContext, LoginActivity.class);
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent(mContext, MainActivity.class);
					startActivity(intent);
					finish();
				}
			}
		},2000);
		
	}

}
