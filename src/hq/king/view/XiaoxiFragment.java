package hq.king.view;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.MemoryHandler;

import hq.king.activity.ChatActivity;
import hq.king.activity.R;
import hq.king.adapter.RecentChatAdapter;
import hq.king.app.MyApplication;
import hq.king.client.Client;
import hq.king.client.ClientInputThread;
import hq.king.client.MessageListener;
import hq.king.entity.RecentChatEntity;
import hq.king.entity.User;
import hq.king.transport.TransportObject;
import hq.king.util.Constants;
import hq.king.util.MyDate;
import hq.king.view.MyListView.OnDeleteListener;
import android.R.integer;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class XiaoxiFragment extends Fragment {
private Context mContext;
private View mBaseView;
private RelativeLayout relative_w,relative_l;
private TextView w_textView,l_textView;
private MyListView mRecentListView;
private MyApplication application;
private static int xiaoxiNum=0;
private RecentChatAdapter mRecentAdapter;
private LinkedList<RecentChatEntity> mRecentList;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=getActivity();
		mBaseView=inflater.inflate(R.layout.fragment_xiaoxi, null);
		initRelative();
		return mBaseView;
	}
	
	void initRelative()
	{
//查找Fragment中定义的控件不能使用findViewById(),必须先将fragment的布局
//mBaseView=inflater.inflate解析出来，然后 mBaseView.findViewById().		
		w_textView=(TextView) mBaseView.findViewById(R.id.fragment_xiaoxi_name);
		l_textView=(TextView) mBaseView.findViewById(R.id.fragment_xiaoxi_name_l);
		
		//从这里也可以看出采用application类来管理全局对象的好处，即可以在多个不同的组件之间处理该对象
		//例如，此处listView的设置adapter是在该fragment中处理的，但是listview的刷新是在MainActivity
		//中处理的，此外在ChatActivity中当用户点击send按钮后，返回到xiaoxiFragment界面时，应该能显示用户
		//的信息，即在ChatActivity中也要处理RecentListView的内容，即在多个组件之间要共享信息，所以把RencentAdapter
		//放在Application这个全局单例对象中保存
		application=(MyApplication) getActivity().getApplication();
		mRecentListView=(MyListView) mBaseView.findViewById(R.id.fragment_xiaoxi_recent_chat_listView);
		mRecentListView.setAdapter(application.getmRecentAdapter());
		mRecentListView.setOnDeleteListener(new OnDeleteListener() {
			
			public void onDelete(int index) {
				// TODO Auto-generated method stub
				application.getmRecentList().remove(index);
				application.getmRecentAdapter().notifyDataSetChanged();
			}
		});
		
		
		
		
		relative_w=(RelativeLayout) mBaseView.findViewById(R.id.xiaoxi_relative_w);
		relative_l=(RelativeLayout) mBaseView.findViewById(R.id.xiaoxi_relative_l);
		relative_w.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String friend_name=w_textView.getText().toString();
				Intent intent=new Intent(getActivity(),ChatActivity.class);
				User user=new User();
				user.setName(friend_name);
				intent.putExtra("user",user);
				startActivity(intent);
			}
		});
		relative_l.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String friend_name=l_textView.getText().toString();
				Intent intent=new Intent(getActivity(),ChatActivity.class);
				User user=new User();
				user.setName(friend_name);
				intent.putExtra("user",user);
				startActivity(intent);
				
			}
		});
		
	}
/*	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		application=(MyApplication) getActivity().getApplication();
		mRecentList = new LinkedList<RecentChatEntity>();
		mRecentAdapter = new RecentChatAdapter(getActivity().getApplicationContext(),
				mRecentList);
	//	final ArrayList<Map<String, Object>> adArrayList=new ArrayList<Map<String, Object>>();
	 //   final SimpleAdapter mAdapter=new SimpleAdapter(getActivity().getApplicationContext(), adArrayList,R.layout.current_online_listview_item,new String[]{"avatar","name","id"},new int[]{R.id.activity_current_onlie_listview_item_head,R.id.activity_current_onlie_listview_item_name,R.id.activity_current_onlie_listview_item_id});
		
		mRecentListView=(ListView) mBaseView.findViewById(R.id.fragment_xiaoxi_recent_chat_listView);
	//	mRecentListView.setAdapter(application.getmRecentAdapter());
		mRecentListView.setAdapter(mRecentAdapter);
		//mRecentListView.setAdapter(new SimpleAdapter(mContext,null,R.layout.recent_chat_item,new String[]{},new int[]{}));
		//如果仅仅是要在
		final Handler mHandler=new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(msg.what==0x12)
				{
					//application.getmRecentAdapter().notifyDataSetChanged();
					mRecentAdapter.notifyDataSetChanged();
					//mAdapter.notifyDataSetChanged();
					MediaPlayer.create(getActivity(), R.raw.msg).start();
					Toast.makeText(getActivity(),"亲！recentchat新消息哦 ", 0).show();
				}
				
			}
			
		};
		//最好不要把recentChat的消息处理放在这，因为这样的话会与ChatActivity中的getMessage产生冲突
		//即当用户在chatActivity界面时接受的Message消息会被这个处理消息的线程拦截，即该消息处理会先读取到
		//接收到的消息，具体原因不知，可能是因为这里直接运用线程处理，而没采用广播的形式，所以会快一些
		
	    Client client=application.getClient();
		ClientInputThread cit=client.getClientInputThread();
		if(cit!=null)
		{
			 Log.i("xiaoxiFragmetn","this sentence is in cit!=null");
			cit.setMessageListener(new MessageListener() {
				
				public void getMessage(TransportObject msg) {
					
					if(msg!=null&&msg instanceof TransportObject)
					{
						switch (msg.getType()) {
						case MESSAGE:
				            xiaoxiNum++;
				            Log.i("xiaoxiFragmetn","this is xiaoxiNum"+String.valueOf(xiaoxiNum));
							application.setRecentNum(xiaoxiNum);// 保存到全局变量
							String message = (String) msg.getObject();
						//	ChatMsgEntity entity= new ChatMsgEntity("",MyDate.getDateEN(),
							//		message,-1,true);// 收到的消息
						//	messageDB.saveMsg(msg.getFromUser(), entity);// 保存到数据库
							//Toast.makeText(getActivity(),"亲！新消息哦 " + msg.getFromUser() + ":" + message, 0).show();// 提示用户
							
							//User user = userDB.selectInfo(msg.getFromUser());// 通过id查询对应数据库该好友信息
					
							RecentChatEntity entity = new RecentChatEntity(msg.getFromUser(),
									0,xiaoxiNum, msg.getName(), MyDate.getDate(),message);
							//application.getmRecentAdapter().remove(entity);// 先移除该对象，目的是添加到首部
							//application.getmRecentList().addFirst(entity);// 再添加到首部
						//	mRecentAdapter.remove(entity);
							mRecentList.addFirst(entity);
							
							
							 Map<String, Object> map=new HashMap<String,Object>();
							 map.put("head",R.drawable.mine_avator);
							 map.put("name", msg.getName());
							 map.put("id",msg.getFromUser());
							 adArrayList.add(map);
							Message message2=new Message();
							message2.what=0x12;
							mHandler.sendMessage(message2);
				            break;
						}
						
						
					}
				}
			});
			 
		}
		else {
			Toast.makeText(getActivity(),"亲！网络连接异常", 0).show();
		}
		
	}*/
	
}
