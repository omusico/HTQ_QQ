package hq.king.activity;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import hq.king.adapter.ChatMsgViewAdapter;
import hq.king.app.MyApplication;
import hq.king.client.Client;
import hq.king.client.ClientOutputThread;
import hq.king.db.MessageDB;
import hq.king.entity.RecentChatEntity;
import hq.king.entity.User;
import hq.king.transport.TranObjectType;
import hq.king.transport.TransportObject;
import hq.king.util.MyDate;
import hq.king.view.ChatMsgEntity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends BaseActivity {
	private Button chat_title_back,chat_bottom_send;
	private TextView chat_title_nick;
	private EditText chat_bottom_edit;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	private User user;
	private Client client;
	private MyApplication application;
	private MessageDB messageDB;
	private ClientOutputThread out;
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_chat);
			application=(MyApplication) getApplicationContext();
			client=application.getClient();
			
			findView();
			try {
				init();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		void findView()
		{
			mListView = (ListView) findViewById(R.id.chat_content_list);
			chat_title_nick=(TextView) findViewById(R.id.chat_title_nick);
			chat_title_back=(Button) findViewById(R.id.chat_title_back);
			chat_bottom_send=(Button) findViewById(R.id.chat_bottom_send);
			chat_bottom_edit=(EditText) findViewById(R.id.chat_bottom_edit);
		}
	void init() throws IOException  
	   {
	
	   	 mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		 mListView.setAdapter(mAdapter);
		
		 chat_title_back.setOnClickListener(chatBackOnclickListener);
		 chat_bottom_send.setOnClickListener(chatSendOnclickListener);
		 user=(User) getIntent().getSerializableExtra("user");
		 chat_title_nick.setText(user.getName());
		 messageDB=new MessageDB(this);
		 initLogin();
		 initData();
				
	   }
	/**
	 * 加载消息历史，从数据库中读出
	 */
	public void initData() {
		List<ChatMsgEntity> list = messageDB.getMsg(user.getId());
		if (list.size() > 0) {
			for (ChatMsgEntity entity : list) {
				//if (entity.getName().equals("")) {
					//entity.setName(user.getName());
				//}
				if (entity.getImg() < 0) {
					entity.setImg(user.getImg());
				}
				mDataArrays.add(entity);
			}
			Collections.reverse(mDataArrays);
		}
	//	mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
	//	mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		mListView.setSelection(mAdapter.getCount() - 1);
		
	}

			private OnClickListener chatBackOnclickListener=new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Intent intent=new Intent(getApplicationContext(), cls)
					setResult(1);
					finish();
					
				}
			};
	
		private OnClickListener chatSendOnclickListener=new OnClickListener() {
				
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String contString = chat_bottom_edit.getText().toString();
				ChatMsgEntity entity = new ChatMsgEntity();
				entity.setDate(getDate());
				entity.setMsgType(false);
		    	entity.setName(user.getName());
		    	entity.setText(contString);
		    	mDataArrays.add(entity);
				mAdapter.notifyDataSetChanged();
				mListView.setSelection(mListView.getCount() - 1);
				
				out=client.getClientOutputThread();
				if (contString.length() > 0)
				{
					if(out==null)
					{
						Toast.makeText(getApplicationContext(), "服务器端连接出错,消息将暂存本地",Toast.LENGTH_SHORT).show();
						
					}
					else {
					    TransportObject<String> tranMsg=new TransportObject<String>(TranObjectType.MESSAGE);
					    tranMsg.setFromUser(user.getId());
					    tranMsg.setToUser(12);
					    tranMsg.setName(user.getName());
					    tranMsg.setObject(contString);
					    out.setMsg(tranMsg);
					}
			   
				   chat_bottom_edit.setText("");
				}
				else {
					Toast.makeText(getApplicationContext(), "内容不能为空",Toast.LENGTH_SHORT).show();
				}
				
				//下面是添加到RecentListView
				RecentChatEntity entity1 = new RecentChatEntity(user.getId(),
						user.getImg(), 0, user.getName(), MyDate.getDate(),
						contString);
				application.getmRecentList().remove(entity1);
				application.getmRecentList().addFirst(entity1);
				application.getmRecentAdapter().notifyDataSetChanged();
			}
			
			};
	 private String getDate() {
	        Calendar c = Calendar.getInstance();

	        String year = String.valueOf(c.get(Calendar.YEAR));
	        String month = String.valueOf(c.get(Calendar.MONTH)+1);
	        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
	        String hour = String.valueOf(c.get(Calendar.HOUR));
	        String mins = String.valueOf(c.get(Calendar.MINUTE));
	        
	        
	        StringBuffer sbBuffer = new StringBuffer();
	        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins); 
	        return sbBuffer.toString();
	    }

    private void initLogin()
    {
    	    out=client.getClientOutputThread();
			TransportObject<User> tranMsg=new TransportObject<User>(TranObjectType.LOGIN);
		    user.setPassword(String.valueOf(user.getId()));
		    tranMsg.setObject(user);
		    if(out!=null)
		    {
		      out.setMsg(tranMsg);
		    }else {
		    	Toast.makeText(getApplicationContext(), "服务器端连接暂时出错，请稍后重试！",0).show();
			}
    }
	@Override
	protected void getMessage(TransportObject msg) {
		// TODO Auto-generated method stub
		switch(msg.getType())
		{
			case MESSAGE :
			{
				ChatMsgEntity entity = new ChatMsgEntity();
				entity.setDate(getDate());
				
				String content= (String) msg.getObject();
					if(msg.getFromUser()==user.getId()/*||msg.getFromUser()==0*/)//如果消息是来自正在聊天的朋友或服务器
					{//user_id是自己的id，代表信息来自自己
				   /* 	entity.setMsgType(false);
				    	entity.setName(current_name);
				// 	String content=(msg.obj.toString()).substring(1);
				    	
				    	entity.setText(content);*/
					}
					else {
						entity.setMsgType(true);
						entity.setName(msg.getName());
					//	entity.setName("一笑奈何");
						entity.setText(content);
						MediaPlayer.create(getApplicationContext(),R.raw.msg).start();
					}
					mDataArrays.add(entity);
					mAdapter.notifyDataSetChanged();
					mListView.setSelection(mListView.getCount() - 1);
					
			}break;
			case LOGIN: {
				//  User user=(User) msg.getObject();
				 // Toast.makeText(getApplicationContext(), user.getName()+"上线了", 0).show();	
			}break;
		}
	}
    	
}
