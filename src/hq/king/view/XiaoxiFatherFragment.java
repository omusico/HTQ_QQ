package hq.king.view;

import hq.king.activity.R;
import hq.king.activity.RightTopWindowActivity;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class XiaoxiFatherFragment extends Fragment {

	private Context mContext;
	private View mBaseView, mPopView;
	private TitleBarView mTitleBarView;
	private Button linear_rightBtn,linear_leftBtn;
	private PopupWindow mPopupWindow;
//	private RelativeLayout mCanversLayout;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=getActivity();
		mBaseView=inflater.inflate(R.layout.fragment_xiaoxi_father, null);
		initTitleBar();

		return mBaseView;
	}
	void initTitleBar()
	{
		mTitleBarView=(TitleBarView) ((Activity) mContext).findViewById(R.id.title_bar);
		
		
		mPopView=LayoutInflater.from(mContext).inflate(R.layout.xiaoxi_right_pop_window, null);
		mPopupWindow=new PopupWindow(mPopView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
		
		
        mTitleBarView.setBtnRightText("");
		mTitleBarView.setBtnRightIcon(R.drawable.more);
		mTitleBarView.setCommonTitle(View.VISIBLE,View.VISIBLE ,View.GONE, View.VISIBLE);
	    mTitleBarView.setTitleLeft(R.string.xiaoxi);
	    mTitleBarView.setTitleRight(R.string.tonghua);
	    linear_leftBtn=(Button)mTitleBarView.findViewById(R.id.title_bar_linear_leftBtn);
	    linear_rightBtn=(Button)mTitleBarView.findViewById(R.id.title_bar_linear_rightBtn);
	  
	    
	    linear_leftBtn.setOnClickListener(new View.OnClickListener() {
	    	
	    	public void onClick(View v) {
	    		linear_leftBtn.setEnabled(false);
	    		linear_rightBtn.setEnabled(true);
	    		// TODO Auto-generated method stub
	    		FragmentManager fm=getFragmentManager();
				FragmentTransaction ft=fm.beginTransaction();
				XiaoxiFragment xiaoxiFragment=new XiaoxiFragment();
				ft.replace(R.id.fragment_xiaoxi_father, xiaoxiFragment,null);
				ft.commit();
	    	}
	       });
	    
	    
	    linear_rightBtn.setOnClickListener(new View.OnClickListener() {
	
	public void onClick(View v) {
		linear_rightBtn.setEnabled(false);
		linear_leftBtn.setEnabled(true);
		
		FragmentManager fm=getFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		CallFragment callFragment=new CallFragment();
		ft.replace(R.id.fragment_xiaoxi_father, callFragment,null);
		ft.commit();
		Toast.makeText(mContext, "赶快试试QQ电话吧！", Toast.LENGTH_SHORT).show();;
		// TODO Auto-generated method stub
		
	}
   });
	   
	    linear_leftBtn.performClick();//注意这个语句必须在以上两个监听器下面
	    Button rightBtn=(Button) ((Activity) mContext).findViewById(R.id.title_bar_rightBtn);
		rightBtn.setOnClickListener(new View.OnClickListener() {
	
	public void onClick(View v) {
		
		Intent intent =new Intent(mContext, RightTopWindowActivity.class);
		startActivity(intent);
		
	}
   });
		
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		   // TODO Auto-generated method stub
		   if (mPopupWindow.isShowing()) {
		   mPopupWindow.dismiss();
		   }
		   return true;
		}
}
