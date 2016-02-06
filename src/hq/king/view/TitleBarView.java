package hq.king.view;

import hq.king.activity.R;
import hq.king.activity.R.string;
import hq.king.util.ImgUtil;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleBarView extends RelativeLayout {

	private Context mContext;
	Button rightBtn;
	TextView center_tv;
	LinearLayout title_linearlayout;
	Button linear_leftBtn;
	Button linear_rightBtn;
	ImageView leftImg;
	
	public TitleBarView(Context context) {
		super(context);
		mContext=context;
		findView();
		// TODO Auto-generated constructor stub
	}

	public TitleBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		findView();
	}
	public void findView()
	{
		//当使用此语句的时候意味着布局文件中的视图已与该自定义控件绑定在一起，如果要将此控件识图显示出来
		//可以采用动态的添加addView（）或者在XML文件中像普通控件那样使用该自定义控件
		LayoutInflater.from(mContext).inflate(R.layout.common_title_bar, this);
		
		 leftImg=(ImageView) findViewById(R.id.title_bar_leftImg);
		 rightBtn=(Button)findViewById(R.id.title_bar_rightBtn);
		 center_tv=(TextView) findViewById(R.id.center_tv);
		 title_linearlayout=(LinearLayout) findViewById(R.id.title_bar_linearLayout);
		 linear_leftBtn=(Button) findViewById(R.id.title_bar_linear_leftBtn);
		 linear_rightBtn=(Button) findViewById(R.id.title_bar_linear_rightBtn);
		
		
		
	}
	public void setCommonTitle(int leftImg_vi,int centerLinear_vi,int textView_vi,int rightBtn_vi)
	{
	   
		leftImg.setVisibility(leftImg_vi);
		title_linearlayout.setVisibility(centerLinear_vi);
		center_tv.setVisibility(textView_vi);
		rightBtn.setVisibility(rightBtn_vi);
		
	}
	public void setDefaultImgLeft(int icon){
		Drawable img=mContext.getResources().getDrawable(icon);
		leftImg.setImageDrawable(img);;
	}
	public void setUserImgLeft(Bitmap bitmap)
	{
		leftImg.setImageBitmap(bitmap);
		
	}
	public void setBtnRight(int icon)
	{
		rightBtn.setText(icon);
		
	}
	public void setBtnRightText(String str)
	{
		rightBtn.setText(str);
		
	}
	
	public void setBtnRightIcon(int icon)
	{
		
		Drawable img=mContext.getResources().getDrawable(icon);
		rightBtn.setBackgroundDrawable(img);
		
	}
	public void setBtnRightDrawable(Drawable img)
	{
		
		rightBtn.setBackgroundDrawable(img);
		
	}
	
	public void setTitleLeft(int resId){
		linear_leftBtn.setText(resId);
	}
	
	public void setTitleRight(int resId){
		linear_rightBtn.setText(resId);
	}
	public void setTitleText(int txtRes){
		center_tv.setText(txtRes);
	}
	
	public void setLefImgtOnclickListener(OnClickListener listener){
		leftImg.setOnClickListener(listener);
	}
	
	public void setBtnRightOnclickListener(OnClickListener listener){
		rightBtn.setOnClickListener(listener);
	}
	
	public ImageView getTitleLeft(){
		return leftImg;
	}
	
	public Button getTitleRight(){
		return rightBtn;
	}

	
	
}