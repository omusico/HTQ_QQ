package hq.king.activity;

import java.io.IOException;

import hq.king.util.FileUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditSignActivity extends Activity {

	Button finish,back;
	EditText sign_edit;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_sign);
		findView();
		init();
	}
	void findView()
	{
		finish=(Button) findViewById(R.id.edit_sign_finish);
		back=(Button) findViewById(R.id.edit_sign_back);
		sign_edit=(EditText) findViewById(R.id.sign_editText);
	}
	void init()
	{
		back.setOnClickListener(backOnClickListener);
		finish.setOnClickListener(finishOnClickListener);
		
	}
	private OnClickListener backOnClickListener=new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
			
		}
	};
	private OnClickListener finishOnClickListener=new OnClickListener() {
		
		public void onClick(View v) {
			String str=sign_edit.getText().toString();
		//	Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
			Intent intent=getIntent();
			Bundle bundle=new Bundle();
			try {
				FileUtil.writeSignToFile(str);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bundle.putString("Ç©Ãû", str);
			intent.putExtras(bundle);
			setResult(7, intent);
			finish();
			// TODO Auto-generated method stub
			
		}
	};
}
