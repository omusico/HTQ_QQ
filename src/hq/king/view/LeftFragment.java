package hq.king.view;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import hq.king.activity.EditSignActivity;
import hq.king.activity.R;
import hq.king.activity.WeatherActivity;
import hq.king.app.MyApplication;
import hq.king.client.Client;
import hq.king.client.ClientOutputThread;
import hq.king.service.GetMsgService;
import hq.king.transport.TranObjectType;
import hq.king.transport.TransportObject;
import hq.king.util.Constants;
import hq.king.util.FileUtil;
import hq.king.util.ImgUtil;
import hq.king.util.SharePreferenceUserInfoUtil;
import hq.king.view.slidingmenu.CustomZoomAnimation;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LeftFragment extends Fragment{
	
	private	RelativeLayout mine_relative_weather,mine_relative,about_me,mine_xiangce_relative;
	private	TextView sign_text;
	private	ImageView mine_avator;
	private int ADAVTOR_CONST=1024;
	private int CROP_PHOTO=1023;
	private  Uri imageUri;
	private Bitmap avatorBitmap = null;
	private  Editor edit;
	private Button exitButton;
	private SharedPreferences share;
	private Client client;
	private MyApplication myApplication;
	private GestureDetector gesture;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
				View view=inflater.inflate(R.layout.activity_mine, null);
				gesture = new GestureDetector(this.getActivity(), new OnGestureListener());
				view.setOnTouchListener(new View.OnTouchListener() {
			   
			           public boolean onTouch(View v, MotionEvent event) {
			                 return gesture.onTouchEvent(event);//返回手势识别触发的事件
			                }
			       });
				return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		findLeftView();
		try {
			initLeft();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void findLeftView()
	{
		mine_avator=(ImageView) getActivity().findViewById(R.id.mine_avator);
		mine_xiangce_relative=(RelativeLayout)getActivity(). findViewById(R.id.mine_xiangce_relative);
		mine_relative_weather=(RelativeLayout)getActivity(). findViewById(R.id.mine_relative_weather);
		about_me=(RelativeLayout) getActivity(). findViewById(R.id.about_me_relative);
		mine_relative=(RelativeLayout) getActivity(). findViewById(R.id.mine_sign_relative);
		sign_text=(TextView) getActivity(). findViewById(R.id.sign_content);
		exitButton=(Button)  getActivity().findViewById(R.id.activity_mine_exit_button);
	}
	void initLeft() throws IOException
	{
		myApplication=(MyApplication)getActivity().getApplication();
		client=myApplication.getClient();
		
		share= getActivity().getSharedPreferences("htq", Activity.MODE_WORLD_READABLE);
		if(share.getBoolean("isQQLogin",false)==true&&share.getBoolean("isAvator", false)==false)
		{
			Bitmap avator=ImgUtil.getQQAvator();
			mine_avator.setImageBitmap(avator);
			
		}
		else if(share.getBoolean("isAvator", false)==false)
		{
			//Bitmap avator=ImgUtil.getDefaultAvator();
			//mine_avator.setBackgroundResource(R.drawable.mine_avatar);
			mine_avator.setImageResource(R.drawable.mine_avatar);//注意此处必须用setImageResource()才能起到circle的效果
			
		}
		else {
			Bitmap avator=ImgUtil.getAvator();
			mine_avator.setImageBitmap(avator);
		}
		mine_avator.setOnClickListener(mineAvatorOnClickListener);
		about_me.setOnClickListener(aboutMeOnClickListener);
		exitButton.setOnClickListener(exitOnClickListener);
		mine_relative.setOnClickListener(mineRelaviveOnClickListener);
		mine_relative_weather.setOnClickListener(mineRelaviveWeatherOnClickListener);
		mine_xiangce_relative.setOnClickListener(mineXiangCeRelaviveOnClickListener);
		sign_text.setText(FileUtil.readSignFromFile());
		
	}
	private class OnGestureListener extends GestureDetector.SimpleOnGestureListener
	{
	    @Override//此方法必须重写且返回真，否则onFling不起效
	    public boolean onDown(MotionEvent e) {
	             return true;
	     }

	    @Override
	     public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	               if((e1.getX()- e2.getX()>120)&&Math.abs(velocityX)>200){
	            	   ((SlidingFragmentActivity) getActivity()).toggle();
	                     return true;
	             }else if((e2.getX()- e1.getX()>120)&&Math.abs(velocityX)>200){
	                 
	                     return true;
	             }
	         return false;
	    }
	}
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
			new AlertDialog.Builder(getActivity()).setTitle("关于我").
			setIcon(R.drawable.mine_avatar).setMessage("     Copyright @2015 htq"+"\n"
					+ "胡天琪，三峡大学计算机系"
					+"\n"+ "          All right reserved").show();
			// TODO Auto-generated method stub
			
		}
	};
private OnClickListener mineRelaviveWeatherOnClickListener=new OnClickListener() {
		
		public void onClick(View v) {
			Intent intent =new Intent( getActivity(),WeatherActivity.class);
			startActivity(intent);
			// TODO Auto-generated method stub
			
		}
	};
	private OnClickListener mineRelaviveOnClickListener=new OnClickListener() {
		
		public void onClick(View v) {
			Intent intent =new Intent( getActivity(),EditSignActivity.class);
			startActivityForResult(intent, 0);
			// TODO Auto-generated method stub
			
		}
	};
	
	private OnClickListener exitOnClickListener=new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent service = new Intent( getActivity().getApplicationContext(), GetMsgService.class);
			 getActivity().stopService(service);
			 getActivity().finish();
			
			System.exit(0);
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
		
		if(requestCode==ADAVTOR_CONST&&resultCode== Activity.RESULT_OK)//当获取的是系统自带的程序的数据时不需要resultCode
		{
			Toast.makeText(getActivity(),"test", Toast.LENGTH_SHORT).show();
			try {
				avatorBitmap=BitmapFactory.decodeStream( getActivity().getContentResolver().openInputStream(imageUri));
			   
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				 ImgUtil.saveAvatorImage(avatorBitmap);
				 share= getActivity().getSharedPreferences("htq", Activity.MODE_WORLD_READABLE);
				 edit=share.edit();
				 edit.putBoolean("isAvator",true);
				 edit.commit();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		mine_avator.setImageBitmap(avatorBitmap);;
		if(client!=null)
		{
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
				SharePreferenceUserInfoUtil util = new SharePreferenceUserInfoUtil( getActivity().getApplicationContext(),
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
		}
		if(requestCode==CROP_PHOTO&&resultCode== Activity.RESULT_OK)//当获取的是系统自带的程序的数据时不需要resultCode
		{
			
		}

	};
	

}
