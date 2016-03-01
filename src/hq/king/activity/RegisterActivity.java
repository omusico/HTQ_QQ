package hq.king.activity;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import hq.king.activity.R.id;
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
import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity {
	private EditText mAccount,mPassword,mPassWordSure,mNick;
	private Button mBack,mComplete;
	private MyApplication application;
	private Handler mHandler;
	private SharePreferenceUserInfoUtil util;
	private  ProgressDialog pdDialog;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
	    findView();
		init();
		
	}
public void findView()
{
	mAccount=(EditText) findViewById(R.id.register_account);
	mNick=(EditText) findViewById(R.id.register_nick);
	mPassword=(EditText) findViewById(R.id.register_password);
	mPassWordSure=(EditText) findViewById(R.id.register_password_sure);
	mBack=(Button) findViewById(R.id.register_back);
	mComplete=(Button) findViewById(R.id.register_complete);
}
public void init()
{
	
	mBack.setOnClickListener(backOnClickListener);
    mComplete.setOnClickListener(completeOnClickListener);
  
}
private OnClickListener backOnClickListener=new OnClickListener() {
	
	public void onClick(View v) {
		finish();
		// TODO Auto-generated method stub
		
	}
};
private OnClickListener completeOnClickListener=new OnClickListener() {
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		final String account=mAccount.getText().toString();
		final String password=mPassword.getText().toString();
		final String nick=mNick.getText().toString();
		String passwordSure=mPassWordSure.getText().toString();
		if(!password.equals(passwordSure))
		{
			Toast.makeText(RegisterActivity.this, "两次密码输入不一致",Toast.LENGTH_SHORT).show();
		}
		else {
			 register(Integer.parseInt(account), password, nick);
			 
			/*
			DataBaseHelper dbHelper=new DataBaseHelper(getApplicationContext(), "user.db", null, 1);
			SQLiteDatabase db=dbHelper.getWritableDatabase();
			ContentValues contentValues=new ContentValues();
			contentValues.put("id", account);
			contentValues.put("password", password);
		//	String INSERT="insert into User(id,password) values(account,password)";
		//	db.execSQL(INSERT);
			db.insert("userInfo", null, contentValues);*/
			
		}
		
	}
};

private void register(int id,String password,String nick)
{
	    pdDialog=SystemUtil.createWaitDialog(this,"正在注册，请稍后！");
	    pdDialog.show();
	    
 		application=(MyApplication) getApplication();
 		Client client=application.getClient();
	    ClientOutputThread out=client.getClientOutputThread();
		TransportObject<User> tranMsg=new TransportObject<User>(TranObjectType.REGISTER);
		User user=new User();
	    user.setId(id);
	    user.setPassword(password);
	    user.setEmail(String.valueOf(id));
	    user.setName(nick);
	    tranMsg.setObject(user);
	    if(out!=null)
	    {
	      out.setMsg(tranMsg);
	    }else {
	    	Toast.makeText(getApplicationContext(), "服务器端连接暂时出错，请稍后重试或返回登陆界面用管理员账号登陆！",0).show();
		}
	    
}
@Override
protected void getMessage(TransportObject msg) {
	// TODO Auto-generated method stub
	if(msg.getType()==TranObjectType.REGISTER)
	{
		User u = (User) msg.getObject();
		int id = u.getId();
		if (id > 0) { 	
			pdDialog.dismiss();
			Toast.makeText(RegisterActivity.this, "账号注册成功！",Toast.LENGTH_SHORT).show();
			finish();
		}
		else {
			Toast.makeText(getApplicationContext(), "注册失败，请稍后重试！",0).show();
		}
		
	}
	
}
}
