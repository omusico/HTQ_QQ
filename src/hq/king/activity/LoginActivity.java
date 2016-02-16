package hq.king.activity;

import java.util.List;

import com.tencent.tauth.Tencent;

import hq.king.app.MyApplication;
import hq.king.client.Client;
import hq.king.client.ClientInputThread;
import hq.king.client.ClientOutputThread;
import hq.king.client.MessageListener;
import hq.king.db.DataBaseHelper;
import hq.king.entity.User;
import hq.king.transport.TranObjectType;
import hq.king.transport.TransportObject;
import hq.king.util.Constants;
import hq.king.util.SharePreferenceUserInfoUtil;
import hq.king.util.SystemUtil;
import hq.king.util.TencentUtil;
import hq.king.view.slidingmenu.CustomZoomAnimation;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
	
	private RelativeLayout rl_user;
	private EditText mAccount,mPassword;
	private Button mLogin,tencentLogin;
	private Button register;
	private SharedPreferences share;
	private  Editor edit;
	private String account, password;
	private String dbPassword;
	private static Tencent mTencent;
	private String mAppid = "222222";
	private Handler mHandler;
	private MyApplication application;
	private SharePreferenceUserInfoUtil util;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findView();
		init();
		
	}
	void findView()
	{
		rl_user=(RelativeLayout) findViewById(R.id.rl_user);
		mLogin=(Button) findViewById(R.id.login);
		tencentLogin=(Button) findViewById(R.id.tencentLogin);
		register=(Button) findViewById(R.id.register);
		mAccount=(EditText) findViewById(R.id.account);
		mPassword=(EditText) findViewById(R.id.password);
		
	}
	void init()
	{
		Animation anim=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.login_anim);
		anim.setFillAfter(true);
		rl_user.startAnimation(anim);
	    mLogin.setOnClickListener(loginOnClickListener);
	    tencentLogin.setOnClickListener(tencentLoginOnClickListener);
		register.setOnClickListener(registerOnClickListener);
		
		mHandler=new Handler()
		{

			 public void handleMessage(Message msg)
			  {
				  
				  if(msg.what==0x123)
				  {
					  edit.putInt("flag", 1);
					  
					  edit.putBoolean("isQQLogin",true);
					  
					  edit.commit();
					  Intent intent=new Intent(LoginActivity.this,CustomZoomAnimation.class);
					  startActivity(intent);
					  finish();
					  
				  }
			  }
		};
	}
	private OnClickListener tencentLoginOnClickListener=new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			share=getSharedPreferences("htq",MODE_WORLD_READABLE);
		    edit=share.edit();
			
			
			mTencent=Tencent.createInstance(mAppid,getApplicationContext());
			new TencentUtil(mTencent,getApplicationContext(),LoginActivity.this,mHandler);
			
			
			
		}
	};
private OnClickListener loginOnClickListener=new OnClickListener() {
		
		public void onClick(View v) {
			
			account=mAccount.getText().toString();
			password=mPassword.getText().toString();
			
			if(account.length()==0||password.length()==0)
			{
				Toast.makeText(LoginActivity.this, "账号或密码不能为空！",Toast.LENGTH_SHORT).show();
			}
			
			else if(SystemUtil.isNumeric(account))
				
				login(Integer.parseInt(account),password);
				
			else if(account.equals("zhy")&&password.equals("hq730"))
			{
			//	Intent intent=new Intent(LoginActivity.this,MainActivity.class);
				Intent intent=new Intent(LoginActivity.this,CustomZoomAnimation.class);
				startActivity(intent);
			    share=getSharedPreferences("htq",MODE_WORLD_READABLE);
			    edit=share.edit();
			    edit.putInt("flag", 1);
			    edit.commit();
			    finish();
			}
			
		/*	DataBaseHelper dbHelper=new DataBaseHelper(getApplicationContext(), "user.db", null, 1);
			SQLiteDatabase db=dbHelper.getReadableDatabase();
			Cursor cursor = db.rawQuery("select * from userInfo where id=? and password=? ",new String[]{account,password});
			*/
			
			
		/*	else if(cursor.moveToFirst())
			{
			 do{
			     dbPassword= cursor.getString(cursor.getColumnIndex("password"));
			     Log.i("password", dbPassword);
			   if(dbPassword.equals(password))
				{
				   
					Intent intent=new Intent(LoginActivity.this,MainActivity.class);
				    startActivity(intent);
				    share=getSharedPreferences("htq",MODE_WORLD_READABLE);
				    edit=share.edit();
				    edit.putInt("flag", 1);
				    edit.commit();
				    finish();
				}
			  }while(cursor.moveToNext());
			 cursor.close();
			}
			
			else {
				Toast.makeText(LoginActivity.this, "账号或密码错误！",Toast.LENGTH_SHORT).show();
			}*/
			
		}
	};
	
	private OnClickListener registerOnClickListener=new OnClickListener() {
		
		public void onClick(View v) {
		//	Toast.makeText(LoginActivity.this, "暂不能注册哦！",Toast.LENGTH_SHORT).show();
			Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
			
		}

	};
	 private void login(final int id,final String password)
	    {
		 		application=(MyApplication) getApplication();
		 		Client client=application.getClient();
	    	    ClientOutputThread out=client.getClientOutputThread();
				TransportObject<User> tranMsg=new TransportObject<User>(TranObjectType.LOGIN);
				User user=new User();
			    user.setId(id);
			    user.setPassword(password);
			    tranMsg.setObject(user);
			    if(out!=null)
			    {
			      out.setMsg(tranMsg);
			    }else {
			    	Toast.makeText(getApplicationContext(), "服务器端连接暂时出错，请稍后重试或用管理员账号登陆！",0).show();
				}
	    }
	@Override
	protected void getMessage(TransportObject msg) {
		// TODO Auto-generated method stub
		
		if(msg.getType()==TranObjectType.LOGIN)
		{
			List<User> list = (List<User>) msg.getObject();
			if (list.size() > 0) {
				//保存用户信息
				util = new SharePreferenceUserInfoUtil(getApplicationContext(),
						Constants.SAVE_USER);
				util.setId(String.valueOf(account));
				util.setPasswd(password);
				util.setEmail(list.get(0).getEmail());
				util.setName(list.get(0).getName());
			    Toast.makeText(getApplicationContext(), "登陆成功！",0).show();
				
			    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
			    startActivity(intent);
			    share=getSharedPreferences("htq",MODE_WORLD_READABLE);
			    edit=share.edit();
			    edit.putInt("flag", 1);
			    edit.commit();
			    finish();
				
			}
			else {
				
				Toast.makeText(getApplicationContext(), "亲，登录名或密码错误哦！",0).show();
			}
			
		}
	}

}
