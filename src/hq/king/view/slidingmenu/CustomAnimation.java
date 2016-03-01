package hq.king.view.slidingmenu;

import hq.king.activity.R;
import hq.king.app.MyApplication;
import hq.king.broadcast.NetworkChangeReceiver;
import hq.king.client.Client;
import hq.king.db.MessageDB;
import hq.king.util.ImgUtil;
import hq.king.view.ConstactFragment;
import hq.king.view.DynamicFragment;
import hq.king.view.SlidingLayout;
import hq.king.view.TitleBarView;
import hq.king.view.XiaoxiFatherFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;

public abstract class CustomAnimation extends SlideBaseActivity {
	
	private CanvasTransformer mTransformer;
	public  static CustomAnimation instance=null;
	private TitleBarView mTitleBarView;
	private ImageButton mXiaoxi,mConstact,mDeynaimic,mSetting;
	private View currentButton;
	private Button linear_leftBtn;
	private SharedPreferences share;
	private IntentFilter mIntentFilter;
	private NetworkChangeReceiver mNetworkChangeReceiver;
	private SlidingLayout slidingLayout;
	private RelativeLayout mContent;
	private Client client;
	private int xiaoxiNum=0;
	private MyApplication myApplication;
	private MessageDB messageDB;
	public CustomAnimation(int titleRes, CanvasTransformer transformer) {
		super(titleRes);
		mTransformer = transformer;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the Above View
		setContentView(R.layout.main);
		//getSupportFragmentManager()
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.main_content, new XiaoxiFatherFragment())
		.commit();
		
		SlidingMenu sm = getSlidingMenu();
	//	setSlidingActionBarEnabled(true);
		sm.setBehindScrollScale(0.0f);
		sm.setBehindCanvasTransformer(mTransformer);
		findView();
		init();
	}
	protected void  onDestroy() {
		super.onDestroy();
		unregisterReceiver(mNetworkChangeReceiver);
		
	}
	private void findView(){
	//	slidingLayout=(SlidingLayout)findViewById(R.id.slidingLayout);
		mContent=(RelativeLayout) findViewById(R.id.content);
		mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);
	//	(LinearLayout) findViewById(R.id.main_bottom);
		mXiaoxi=(ImageButton) findViewById(R.id.buttom_xiaoxi);
		mConstact=(ImageButton) findViewById(R.id.buttom_constact);
		mDeynaimic=(ImageButton) findViewById(R.id.buttom_deynaimic);
		linear_leftBtn=(Button)findViewById(R.id.title_bar_linear_leftBtn);
		
	}
	private void initQQInfo()
	{
		share=getSharedPreferences("htq",MODE_WORLD_READABLE);
		String QQnick=share.getString("QQnick","ªÒ»°QQÍ«≥∆ ß∞‹£°");
		Toast.makeText(getApplicationContext(), QQnick, Toast.LENGTH_SHORT).show();
		Bitmap avator=ImgUtil.getQQAvator();
		mTitleBarView.setUserImgLeft(avator);
	}
	
	private void init(){
		
			instance=this;
			myApplication=(MyApplication) getApplication();
			client=myApplication.getClient();
			messageDB=new MessageDB(this);
			
			
			linear_leftBtn.setEnabled(false);
			
			mIntentFilter=new IntentFilter();
			mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
			mNetworkChangeReceiver=new NetworkChangeReceiver();
			registerReceiver(mNetworkChangeReceiver, mIntentFilter);
			
			
			mXiaoxi.setOnClickListener(xiaoxiOnClickListener);
			mConstact.setOnClickListener(constactOnClickListener);
			mDeynaimic.setOnClickListener(deynaimicOnClickListener);
			mXiaoxi.performClick();
			share=getSharedPreferences("htq",MODE_WORLD_READABLE);
			if(share.getBoolean("isQQLogin",false)==true&&share.getBoolean("isAvator", false)==false)
			{
				initQQInfo();
				
			}
			else if(share.getBoolean("isAvator", false)==false)
			{
				mTitleBarView.setDefaultImgLeft(R.drawable.mine_avatar);
				
			}
			else
			{
				Bitmap avator=ImgUtil.getAvator();
				mTitleBarView.setUserImgLeft(avator);
			}
			
			 mTitleBarView.getTitleLeft().setOnClickListener(new View.OnClickListener() {
			    	
			    	public void onClick(View v) {
			    		toggle();
			    		
			    	}
			       });
		}
	
	private OnClickListener xiaoxiOnClickListener=new OnClickListener() {
		public void onClick(View v) {
			FragmentManager fm=getFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			XiaoxiFatherFragment xiaoxiFatherFragment=new XiaoxiFatherFragment();
			ft.replace(R.id.main_content, xiaoxiFatherFragment,null);
			ft.commit();
			setButton(v);
			
		}
	};
	
	private OnClickListener constactOnClickListener=new OnClickListener() {
		public void onClick(View v) {
			FragmentManager fm=getFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			ConstactFragment constactFatherFragment=new ConstactFragment();
			ft.replace(R.id.main_content, constactFatherFragment,null);
			ft.commit();
			setButton(v);
			
		}
	};
	
	private OnClickListener deynaimicOnClickListener=new OnClickListener() {
		public void onClick(View v) {
			FragmentManager fm=getFragmentManager();
			FragmentTransaction ft=fm.beginTransaction();
			DynamicFragment dynamicFragment=new DynamicFragment();
			ft.replace(R.id.main_content, dynamicFragment,null);
			ft.commit();
			setButton(v);
			
		}
	};
	
	private void setButton(View v){
		if(currentButton!=null&&currentButton.getId()!=v.getId()){
			currentButton.setEnabled(true);
		}
		v.setEnabled(false);
		currentButton=v;
	}

}
