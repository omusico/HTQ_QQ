package hq.king.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;

public class RightTopWindowActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xiaoxi_right_pop_window);
		
		
		
	}
	public boolean onTouchEvent(MotionEvent event) {
		   // TODO Auto-generated method stub
		   finish();
		   return true;
		   
		}

}
