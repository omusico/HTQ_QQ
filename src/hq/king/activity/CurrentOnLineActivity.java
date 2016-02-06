package hq.king.activity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hq.king.activity.R;
import hq.king.app.MyApplication;
import hq.king.client.Client;
import hq.king.client.ClientInputThread;
import hq.king.client.ClientOutputThread;
import hq.king.client.MessageListener;
import hq.king.entity.User;
import hq.king.transport.TranObjectType;
import hq.king.transport.TransportObject;
import hq.king.util.Constants;
import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class CurrentOnLineActivity extends BaseActivity {

	private Button backBtn;
	private ListView mListView;
	private Adapter mAdapter;
	private List<String> contactList=new ArrayList<String>();
	private ArrayList<Map<String, Object>> adArrayList=new ArrayList<Map<String, Object>>();
	private Client client;
	private MyApplication application;
	private List<User> user=new ArrayList<User>();
	private List<User> readUser=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_online);
		findView();
		try {
			init();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

   private void findView()
   {
	   backBtn=(Button) findViewById(R.id.current_online_title_back);
	   mListView=(ListView) findViewById(R.id.activity_current_online_listview);
	   
	   
   }
   private void init() throws UnknownHostException, IOException
   {
	  // mAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,contactList);
	   mAdapter=new SimpleAdapter(getApplicationContext(), adArrayList,R.layout.current_online_listview_item,new String[]{"avatar","name","id"},new int[]{R.id.activity_current_onlie_listview_item_head,R.id.activity_current_onlie_listview_item_name,R.id.activity_current_onlie_listview_item_id});
	   mListView.setAdapter( (ListAdapter) mAdapter);
	   
	   addUserListFromServer();
	   mListView.setOnItemClickListener(onItemClickListener);
	  //处理ListView的item点击事件有两种方式，一种是像上面这样用setOnItemClickListener的方式，即让ListView来处理
	   //用户的响应，另外一种是自己继承BaseAdapter（或者其它的Adapter），然后再重写adapter中的getView()方法时(该方法会返回listView的item布局视图)
	   //用该视图setOnClickListener来实现，即让adapter来处理用户的响应
	   backBtn.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	});
   }
   private void  addUserListFromServer() throws UnknownHostException, IOException
   {
	  
	 
				application=(MyApplication) getApplication();
				client=application.getClient();
				ClientOutputThread out=client.getClientOutputThread();
				TransportObject toMsg=new TransportObject(TranObjectType.All);
				if(out!=null)
				{
				 out.setMsg(toMsg);
				}
				else {
					Toast.makeText(getApplicationContext(), "服务器端连接暂时出错，请稍后重试！",0).show();
				}
	   }
   private OnItemClickListener onItemClickListener=new OnItemClickListener() {

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		HashMap< String, Object> data=(HashMap<String, Object>) parent.getItemAtPosition(position);
		if(data!=null&&data.size()>0)
		{
			 User user =new User();
			 user.setName((String)data.get("name"));
			 user.setId((Integer) data.get("id"));
			intent.putExtra("user",user);
			intent.setClass(getApplicationContext(), ChatActivity.class);
			startActivity(intent);
			
		}
		
	}
};
@Override
protected void getMessage(TransportObject msg) {
	// TODO Auto-generated method stub
	 Log.i("current", "getMessage() is exec");
	 if(msg!=null &&msg.getType() == TranObjectType.All)
	   {
		 readUser=(ArrayList<User>)msg.getObject();//注意msg本身是transportobject类型
		 //他所携带的数据才是list<User>类型
		 for(int i=0;i<readUser.size();i++)
		 {
			 Map<String, Object> map=new HashMap<String,Object>();
			 map.put("name", readUser.get(i).getName());
			 map.put("id", readUser.get(i).getId());
			 Log.i("current", readUser.get(i).getName());
			 adArrayList.add(map);
			 
		   //contactList.add(readUser.get(i).getName()+"\n"+readUser.get(i).getId());
		   
		 }
		 ((BaseAdapter) mAdapter).notifyDataSetChanged();
		 
	   }
}
}
