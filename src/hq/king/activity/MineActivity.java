package hq.king.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import hq.king.util.FileUtil;
import hq.king.util.ImgUtil;
import hq.king.util.ImgUtil.OnLoadBitmapListener;
import android.R.integer;
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
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MineActivity extends Activity {

private	RelativeLayout mine_relative,about_me,mine_xiangce_relative;
private	TextView sign_text;
private	ImageView mine_avator;
private int ADAVTOR_CONST=1024;
private int CROP_PHOTO=1023;
private  Uri imageUri;
private Bitmap avatorBitmap = null;
private  Editor edit;
private Button exitButton;
private SharedPreferences share;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine);
		findView();
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	void findView()
	{
		mine_avator=(ImageView) findViewById(R.id.mine_avator);
		mine_xiangce_relative=(RelativeLayout) findViewById(R.id.mine_xiangce_relative);
		about_me=(RelativeLayout) findViewById(R.id.about_me_relative);
		mine_relative=(RelativeLayout) findViewById(R.id.mine_sign_relative);
		sign_text=(TextView) findViewById(R.id.sign_content);
		exitButton=(Button) findViewById(R.id.activity_mine_exit_button);
	}
	void init() throws IOException
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
			mine_avator.setBackgroundResource(R.drawable.mine_avatar);
			
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
			new AlertDialog.Builder(MineActivity.this).setTitle("关于我").
			setIcon(R.drawable.mine_avatar).setMessage("     Copyright @2015 htq"+"\n"
					+ "胡天琪，三峡大学计算机系"
					+"\n"+ "          All right reserved").show();
			// TODO Auto-generated method stub
			
		}
	};
	private OnClickListener mineRelaviveOnClickListener=new OnClickListener() {
		
		public void onClick(View v) {
			Intent intent =new Intent(MineActivity.this,EditSignActivity.class);
			startActivityForResult(intent, 0);
			// TODO Auto-generated method stub
			
		}
	};
	
	private OnClickListener exitOnClickListener=new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
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
		}
		//	Intent intent=new Intent("com.android.camera.action.CROP");
		//	intent.setDataAndType(imageUri, "images/*");
			
			//String path=FileUtil.getImgPath();
			//img_Uri=data.getData();
			
			//String imgPath=img_Uri.toString();
		//	Toast.makeText(getApplicationContext(), imgPath, Toast.LENGTH_SHORT).show();
		/*	ImgUtil.getInstance().loadBitmap(path, new OnLoadBitmapListener() {
				public void loadImage(Bitmap bitmap, String path) {
					if (bitmap != null && mine_avator != null) {
						
						
					//	mine_avator.setImageURI(uri);
					}
				}
			});*/
			

		if(requestCode==CROP_PHOTO&&resultCode==RESULT_OK)//当获取的是系统自带的程序的数据时不需要resultCode
		{
			
		}

	};
	
}
