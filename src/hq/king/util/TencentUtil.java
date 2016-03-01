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

	// 锟斤拷录锟截碉拷锟接匡拷
	private IUiListener listener = new IUiListener() {

		public void onError(UiError arg0) {
			// TODO Auto-generated method stub

		}

		public void onComplete(Object response) {
			// TODO Auto-generated method stub
			// 锟斤拷锟斤拷录锟襟返回碉拷锟斤拷锟斤拷token锟斤拷expires锟斤拷openId锟斤拷锟芥到mTencent锟斤拷锟斤拷锟斤拷
			initOpenidAndToken((JSONObject) response);

			// 锟斤拷锟铰斤拷锟芥，锟斤拷示锟斤拷录锟矫伙拷头锟斤拷锟斤拷浅锟�
			updateUserInfo();
			
			/*锟剿达拷锟斤拷锟斤拷锟届步通锟脚革拷知锟斤拷锟斤拷activity锟斤拷取锟斤拷源锟斤拷桑锟斤拷锟斤拷锟斤拷锟阶拷锟斤拷锟斤拷锟斤拷锟�*/
		/*	Message msg=new Message();
			msg.what=0x123;
			msg.obj="isComplete";
			QQHandler.sendMessage(msg);
		 */
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

	
	//锟斤拷锟斤拷UI锟斤拷锟斤拷锟节革拷锟斤拷系统UI锟斤拷锟斤拷锟捷达拷锟斤拷锟斤拷锟饺★拷锟斤拷冉虾锟绞憋拷锟斤拷锟斤拷圆锟斤拷冒锟阶匡拷觳斤拷锟斤拷疲锟斤拷锟斤拷锟斤拷叱锟斤拷写锟斤拷锟斤拷锟斤拷去锟斤拷锟捷ｏ拷然锟斤拷通锟斤拷handler通知锟斤拷锟竭程革拷锟斤拷UI
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
					  	SharedPreferences share=mActivity.getSharedPreferences("htq",Context.MODE_WORLD_READABLE);
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
				try {
					ImgUtil.saveQQImage(bitmap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		//		mImageView.setImageBitmap(bitmap);
			}
			
			Message msg2=new Message();
			msg2.what=0x123;
			msg2.obj="isComplete";
			QQHandler.sendMessage(msg2);
		
		}	
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (requestCode == Constants.REQUEST_API) {
			if (resultCode == Constants.RESULT_LOGIN) {
				Tencent.handleResultData(data, listener);

			}
		} else if (requestCode == Constants.REQUEST_APPBAR) { // app锟斤拷应锟矫吧碉拷录
			if (resultCode == Constants.RESULT_LOGIN) {
				updateUserInfo();
				//Util.showResultDialog(MainActivity.this,
				//		data.getStringExtra(Constants.LOGIN_INFO), "锟斤拷录锟缴癸拷");
			//	Toast.makeText(getApplicationContext(), "锟斤拷陆锟缴癸拷", Toast.LENGTH_SHORT);
			}
		}
		onActivityResult(requestCode, resultCode, data);
	}

	public static Bitmap getbitmap(String imageUri) {
		// 锟斤拷示锟斤拷锟斤拷锟较碉拷图片

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
