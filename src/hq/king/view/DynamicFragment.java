package hq.king.view;

import hq.king.activity.HttpActivity;
import hq.king.activity.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DynamicFragment extends Fragment {
private Context mContext;
private View mBaseView;
private TitleBarView mTitleBarView;
private RelativeLayout mDynamic;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=getActivity();
		mBaseView=inflater.inflate(R.layout.fragment_dynamic, null);
		initTitleBar();
		init();
		return mBaseView;
	}
	void initTitleBar()
	{
		mTitleBarView=(TitleBarView) ((Activity) mContext).findViewById(R.id.title_bar);
		
		mTitleBarView.setBtnRight(R.string.more);
		mTitleBarView.setBtnRightDrawable(null);
		
		mTitleBarView.setCommonTitle(View.VISIBLE,View.GONE ,View.VISIBLE, View.VISIBLE);
	    mTitleBarView.setTitleText(R.string.dynamic);
	    mTitleBarView.setTitleRight(R.string.more);
	  
	    Button rightBtn=(Button) ((Activity) mContext).findViewById(R.id.title_bar_rightBtn);
		rightBtn.setOnClickListener(new View.OnClickListener() {
	
	public void onClick(View v) {
		
		Toast.makeText(mContext, "≤‚ ‘", Toast.LENGTH_SHORT).show();
		// TODO Auto-generated method stub
		
	}
   });
		
	}
	private void init()
	{
		mDynamic=(RelativeLayout) mBaseView.findViewById(R.id.fragment_dynamic_one);
		mDynamic.setOnClickListener(mDynamicOnClickListener);
	}
	private android.view.View.OnClickListener mDynamicOnClickListener=new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			Intent intent=new Intent(mContext, HttpActivity.class);
			startActivity(intent);
		}
	};
}
