package hq.king.activity;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import hq.king.app.MyApplication;
import hq.king.broadcast.NetworkChangeReceiver;
import hq.king.client.Client;
import hq.king.client.ClientOutputThread;
import hq.king.db.MessageDB;
import hq.king.entity.RecentChatEntity;
import hq.king.entity.User;
import hq.king.service.GetMsgService;
import hq.king.transport.TranObjectType;
import hq.king.transport.TransportObject;
import hq.king.util.Constants;
import hq.king.util.FileUtil;
import hq.king.util.ImgUtil;
import hq.king.util.MyDate;
import hq.king.util.SharePreferenceUserInfoUtil;
import hq.king.view.ChatMsgEntity;
import hq.king.view.ConstactFragment;
import hq.king.view.DynamicFragment;
import hq.king.view.SlidingLayout;
import hq.king.view.TitleBarView;
import hq.king.view.XiaoxiFatherFragment;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
public  static MainActivity instance=null;
private TitleBarView mTitleBarView;
private ImageButton mXiaoxi,mConstact,mDeynaimic,mSetting;
private View currentButton;
private LinearLayout buttomBarGroup;
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


//以下内容本为MineActivity中的内容
private	RelativeLayout mine_relative,about_me,mine_xiangce_relative;
private	TextView sign_text;
private	ImageView mine_avator;
private int ADAVTOR_CONST=1024;
private int CROP_PHOTO=1023;
private  Uri imageUri;
private Bitmap avatorBitmap = null;
private  Editor edit;
private Button exitButton;
//end
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	//	setContentView(R.layout.main);
		setContentView(R.layout.slide_main);
		
		findView();
		init();
		findLeftView();
		try {
			initLeft();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	protected void  onDestroy() {
		super.onDestroy();
		unregisterReceiver(mNetworkChangeReceiver);
		
	}
	private void findView(){
		slidingLayout=(SlidingLayout)findViewById(R.id.slidingLayout);
		mContent=(RelativeLayout) findViewById(R.id.content);
		mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);
		buttomBarGroup=(LinearLayout) findViewById(R.id.main_bottom);
		mXiaoxi=(ImageButton) findViewById(R.id.buttom_xiaoxi);
		mConstact=(ImageButton) findViewById(R.id.buttom_constact);
		mDeynaimic=(ImageButton) findViewById(R.id.buttom_deynaimic);
		linear_leftBtn=(Button)findViewById(R.id.title_bar_linear_leftBtn);
		
	}
	
	//以下内容本为MineActivity中的内容
	void findLeftView()
	{
		mine_avator=(ImageView) findViewById(R.id.mine_avator);
		mine_xiangce_relative=(RelativeLayout) findViewById(R.id.mine_xiangce_relative);
		about_me=(RelativeLayout) findViewById(R.id.about_me_relative);
		mine_relative=(RelativeLayout) findViewById(R.id.mine_sign_relative);
		sign_text=(TextView) findViewById(R.id.sign_content);
		exitButton=(Button) findViewById(R.id.activity_mine_exit_button);
	}
	void initLeft() throws IOException
	{
		share=getSharedPreferences("htq",MODE_WORLD_READABLE);
		if(share.getBoolean("isQQLogin",false)==true&&share.getBoolean("isAvator", false)==false)
		{
			Bitmap avator=ImgUtil.getQQAvator();
			mine_avator.setImageBitmap(avator);
			
		}
		else if(share.getBoolean("isAvator", false)==false)
		{
			//Bitmap avator=ImgUtil.getDefaultAvator();
			mine_avator.setBackgroundResource(R.drawable.mine_avator);
			
		}
		else {
			Bitmap avator=ImgUtil.getAvator();
			mine_avator.setImageBitmap(avator);
		}
		mine_avator.setOnClickListener(mineAvatorOnClickListener);
		about_me.setOnClickListener(aboutMeOnClickListener);
		mine_relative.setOnClickListener(mineRelaviveOnClickListener);
		mine_xiangce_relative.setOnClickListener(mineXiangCeRelaviveOnClickListener);
		sign_text.setText(FileUtil.readSignFromFile());
		exitButton.setOnClickListener(exitOnClickListener);
	}
	//end
	private void initQQInfo()
	{
		share=getSharedPreferences("htq",MODE_WORLD_READABLE);
		String QQnick=share.getString("QQnick","获取QQ昵称失败！");
		Toast.makeText(getApplicationContext(), QQnick, Toast.LENGTH_SHORT).show();
		Bitmap avator=ImgUtil.getQQAvator();
		mTitleBarView.setUserImgLeft(avator);
	}
	private void init(){
		
	//	slidingLayout.setScrollEvent(mContent);
		
		
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
			mTitleBarView.setDefaultImgLeft(R.drawable.mine_avator);
			
		}
		else
		{
			Bitmap avator=ImgUtil.getAvator();
			mTitleBarView.setUserImgLeft(avator);
		}
		
		 mTitleBarView.getTitleLeft().setOnClickListener(new View.OnClickListener() {
		    	
		    	public void onClick(View v) {
		    		
		    		if(slidingLayout.isLeftLayoutVisible()) {
		    			
                           slidingLayout.scrollToRightLayout();
		    	    }else{
		    	         slidingLayout.scrollToLeftLayout();
		    	 	}
		    	//	Intent intent=new Intent(MainActivity.this,MineActivity.class);
				//	startActivity(intent);
		    		
		    	}
		       });
	}

   
	//以下内容本为MineActivity中的内容
private OnClickListener mineAvatorOnClickListener=new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			File avator=new File(FileUtil.getImgPath(),"avator.jpg");
			if(avator.exists())
				avator.delete();
			try {
				avator.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			imageUri=Uri.fromFile(avator);//将file对象转换为uri对象。
			Intent intent=new Intent("com.android.camera.action.CROP");
			intent.setAction(Intent.ACTION_PICK);
			intent.setType("image/*");
			intent.putExtra("crop", true);
			intent.putExtra("scale", true);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, ADAVTOR_CONST);
		}
	};
private OnClickListener mineXiangCeRelaviveOnClickListener=new OnClickListener() {
		
		public void onClick(View v) {Intent intent=new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, 3);
			
		}
	};
private OnClickListener aboutMeOnClickListener=new OnClickListener() {
		
		public void onClick(View v) {
			new AlertDialog.Builder(MainActivity.this).setTitle("关于我").
			setIcon(R.drawable.mine_avator).setMessage("     Copyright @2015 htq"+"\n"
					+ "胡天琪，三峡大学计算机系"
					+"\n"+ "          All right reserved").show();
			// TODO Auto-generated method stub
			
		}
	};
	private OnClickListener mineRelaviveOnClickListener=new OnClickListener() {
		
		public void onClick(View v) {
			Intent intent =new Intent(MainActivity.this,EditSignActivity.class);
			startActivityForResult(intent, 0);
			// TODO Auto-generated method stub
			
		}
	};
	
	private OnClickListener exitOnClickListener=new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent service = new Intent(getApplicationContext(), GetMsgService.class);
			stopService(service);
			finish();
			MainActivity.instance.finish();
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==0&&resultCode==7)
		{
			Bundle bundle=data.getExtras();
			String str=bundle.getString("签名");
		//	Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
			sign_text.setText(str);
		}
		if(requestCode==ADAVTOR_CONST&&resultCode==RESULT_OK)//当获取的是系统自带的程序的数据时不需要resultCode
		{
			try {
				avatorBitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
			   
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				
				
				// avator=ImgUtil.createCircleImage(avatorBitmap);
				 ImgUtil.saveAvatorImage(avatorBitmap);
				 share=getSharedPreferences("htq",MODE_WORLD_READABLE);
				 edit=share.edit();
				 edit.putBoolean("isAvator",true);
				 edit.commit();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		mine_avator.setImageBitmap(avatorBitmap);;
		
		ClientOutputThread out=client.getClientOutputThread();
		TransportObject msg=new TransportObject(TranObjectType.AVATAR_POST);
		try {
			File file=new File(ImgUtil.getAvatorPath());
			FileInputStream fis=new FileInputStream(file);
			int len= (int) file.length();
			byte [] buffer=new byte[len];
			while((len=fis.read(buffer, 0,len))!=-1)
			{
			}
			SharePreferenceUserInfoUtil util = new SharePreferenceUserInfoUtil(getApplicationContext(),
					Constants.SAVE_USER);
			Map<String, Object> userInfo=new HashMap<String,Object>();
			userInfo.put("id",util.getId());
			userInfo.put("avatar",buffer);
			msg.setObject(userInfo);
			out.setMsg(msg);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		if(requestCode==CROP_PHOTO&&resultCode==RESULT_OK)//当获取的是系统自带的程序的数据时不需要resultCode
		{
			
		}

	};
//end	
	
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
	public boolean onTouchEvent(MotionEvent event) {
		   // TODO Auto-generated method stub
		slidingLayout.onTouch(slidingLayout, event);
		return true;
	}
	@Override
	protected void getMessage(TransportObject msg) {
		// TODO Auto-generated method stub
		switch (msg.getType()) {
		case MESSAGE:
            xiaoxiNum++;
			myApplication.setRecentNum(xiaoxiNum);// 保存到全局变量
			String message = (String) msg.getObject();
			//将收到的消息包装成chatMsgEntity类型，然后保存到数据库中，用于当用户点击了某一项item时
			//跳转至ChatActivity中时从数据库中读取用户该用户的消息
			ChatMsgEntity entity= new ChatMsgEntity("",MyDate.getDateEN(),
					message,-1,true);// 收到的消息
			messageDB.saveMsg(msg.getFromUser(), entity);// 保存到数据库
			//Toast.makeText(this,"亲！新消息哦 " + msg.getFromUser() + ":" + message, 0).show();// 提示用户
			MediaPlayer.create(this, R.raw.msg).start();
			//User user = userDB.selectInfo(msg.getFromUser());// 通过id查询对应数据库该好友信息
			//将收到的消息添加到recnetadapter中
			RecentChatEntity entity1 = new RecentChatEntity(msg.getFromUser(),
					0,xiaoxiNum, msg.getName(), MyDate.getDate(),message);
			myApplication.getmRecentAdapter().remove(entity1);// 先移除该对象，目的是添加到首部
			myApplication.getmRecentList().addFirst(entity1);// 再添加到首部
			myApplication.getmRecentAdapter().notifyDataSetChanged();
            break;
		}
		
	}
	
	

}
