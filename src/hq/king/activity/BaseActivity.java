package hq.king.activity;

import hq.king.transport.TransportObject;
import hq.king.util.Constants;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public abstract class BaseActivity extends Activity {

	
	
	@Override//将子类中都需要用到的对象的实例化放在onCreate()中，因为onCreate只被执行一次（除非该Activity被销毁然后重新创建）
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override//此处我把广播的注册放在onStart()中，放在onCreate()中也可以，具体要看业务逻辑
	//因为此处的广播主要是为了在Activity中接收来自MsgService中的消息，所以一般是当该Activity处于
	//与用户交互时处理getMessage()，所以最好放在onStart()中，因为当该Activity处于前台时会调用onStart()
	//注销的时候一般在对应的方法里，即onCreate()---onDestroy(),onStart()--onStop().
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		IntentFilter intentFilter=new IntentFilter();
		intentFilter.addAction(Constants.ACTION_MSG);
		registerReceiver(MsgReceiver, intentFilter);
	}

	
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		unregisterReceiver(MsgReceiver);
	}



	//广播接收者，用于接受来自MsgService的消息，Activity与service的通信通常采用
	//广播接收者或bindService的方式，若多个Activity要接受同一个Service的消息，则采用广播接收者更好
	//此处采用的就是广播接受者
	BroadcastReceiver MsgReceiver=new BroadcastReceiver()
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			TransportObject msg=(TransportObject)intent.getSerializableExtra(Constants.MSG);
			getMessage(msg);
			
		}};
	protected abstract void getMessage(TransportObject msg);

}
