package hq.king.util;

import hq.king.activity.LoginActivity;
import hq.king.activity.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.sax.StartElementListener;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TencentUtil {
	private static Tencent mTencent;
	private static String mAppid = "222222";
	private static Activity mActivity;
	private Context mContext;
	private UserInfo mInfo;
	private ImageView mImageView;
	private TextView mTextView;
	private SharedPreferences share;
	private Editor edit;
	private Handler QQHandler;
	public TencentUtil(ImageView imageView,TextView textView) {
		// TODO Auto-generated constructor stub
	}
	public TencentUtil(Tencent tencent,Context context,Activity activity,Handler handler)
	{
		mTencent=tencent;
		mContext=context;
		mActivity=activity;
		QQHandler=handler;
		onClickLogin();
		
	}
	public static Tencent getTencentInstance(Context context)
	{
		mTencent = Tencent.createInstance(mAppid, context);
	//	mContext=context;
		return mTencent;
		
	}
	private void onClickLogin() {
		if (!mTencent.isSessionValid()) {
			mTencent.login( mActivity, "all", listener);
			Log.d("SDKQQAgentPref",
					"FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
		} else {
			mTencent.logout(mContext);
			updateUserInfo();
		}
	}

	// 登录回调接口
	private IUiListener listener = new IUiListener() {

		public void onError(UiError arg0) {
			// TODO Auto-generated method stub

		}

		public void onComplete(Object response) {
			// TODO Auto-generated method stub
			// 将登录后返回的数据token，expires，openId保存到mTencent对象中
			initOpenidAndToken((JSONObject) response);

			// 更新界面，显示登录用户头像和昵称
			updateUserInfo();
			
			/*此处采用异步通信告知调用activity获取资源完成，可以跳转到主界面*/
			Message msg=new Message();
			msg.what=0x123;
			msg.obj="isComplete";
			QQHandler.sendMessage(msg);
		 
		}

		public void onCancel() {
			// TODO Auto-generated method stub

		}
	};

	public static void initOpenidAndToken(JSONObject jsonObject) {
		try {
			String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
			String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
			String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
			if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
					&& !TextUtils.isEmpty(openId)) {
				mTencent.setAccessToken(token, expires);
				mTencent.setOpenId(openId);
			}
		} catch (Exception e) {
		}
	}

	
	//更新UI，由于更新系统UI的数据从网络获取，比较耗时，所以采用安卓异步机制，在子线程中从网络后去数据，然后通过handler通知主线程更新UI
	private void updateUserInfo() {
		if (mTencent != null && mTencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {

				public void onError(UiError e) {

				}

				public void onComplete(final Object response) {
					Message msg = new Message();
					msg.obj = response;
					msg.what = 0;
					mHandler.sendMessage(msg);

					new Thread() {

						@Override
						public void run() {
							JSONObject json = (JSONObject) response;
							if (json.has("figureurl")) {
								Bitmap bitmap = null;
								try {
									bitmap = getbitmap(json
											.getString("figureurl_qq_2"));
								ImgUtil.saveQQImage(bitmap);
								} catch (JSONException e) {

								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Message msg = new Message();
								msg.obj = bitmap;
								msg.what = 1;
								mHandler.sendMessage(msg);
							}
						}

					}.start();
				}

				public void onCancel() {

				}
			};
			mInfo = new UserInfo(mContext, mTencent.getQQToken());
			mInfo.getUserInfo(listener);

		} else {
		//	mTextView.setText("");
		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				final JSONObject response = (JSONObject) msg.obj;
				
				if (response.has("nickname")) {
					try {
						String QQnick = response.getString("nickname");
					  	SharedPreferences share=mActivity.getSharedPreferences("htq",mActivity.MODE_WORLD_READABLE);
					  	Editor edit=share.edit();
					 	edit.putString("QQnick", QQnick);
					    edit.commit();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else if (msg.what == 1) {
				Bitmap bitmap = (Bitmap) msg.obj;
		//		mImageView.setImageBitmap(bitmap);
			}
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (requestCode == Constants.REQUEST_API) {
			if (resultCode == Constants.RESULT_LOGIN) {
				Tencent.handleResultData(data, listener);

			}
		} else if (requestCode == Constants.REQUEST_APPBAR) { // app内应用吧登录
			if (resultCode == Constants.RESULT_LOGIN) {
				updateUserInfo();
				//Util.showResultDialog(MainActivity.this,
				//		data.getStringExtra(Constants.LOGIN_INFO), "登录成功");
			//	Toast.makeText(getApplicationContext(), "登陆成功", Toast.LENGTH_SHORT);
			}
		}
		onActivityResult(requestCode, resultCode, data);
	}

	public static Bitmap getbitmap(String imageUri) {
		// 显示网络上的图片

		Bitmap bitmap = null;

		try {

		URL myFileUrl = new URL(imageUri);

		HttpURLConnection conn = (HttpURLConnection) myFileUrl

		.openConnection();
		
		conn.setDoInput(true);
		
		conn.connect();
		
		InputStream is = conn.getInputStream();
		
		bitmap = BitmapFactory.decodeStream(is);
		
		is.close();
		
		//Log.v(TAG, "image download finished." + imageUri);

		} catch (IOException e) {
		
		e.printStackTrace();
		
		return null;

		}
		
		return bitmap;
		
		}
}
