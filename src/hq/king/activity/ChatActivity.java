package hq.king.activity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import hq.king.adapter.ChatMsgViewAdapter;
import hq.king.adapter.ExpressionAdapter;
import hq.king.adapter.ExpressionPagerAdapter;
import hq.king.app.MyApplication;
import hq.king.client.Client;
import hq.king.client.ClientOutputThread;
import hq.king.db.MessageDB;
import hq.king.entity.RecentChatEntity;
import hq.king.entity.User;
import hq.king.transport.TranObjectType;
import hq.king.transport.TransportObject;
import hq.king.util.MyDate;
import hq.king.util.SmileUtils;
import hq.king.view.ChatMsgEntity;
import hq.king.view.ExpandGridView;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChatActivity extends BaseActivity implements OnClickListener{
	private Button chat_title_back,chat_bottom_send,btnMore;
	private TextView chat_title_nick;
	private EditText chat_bottom_edit;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	private List<String> reslist;
	private User user;
	private Client client;
	private MyApplication application;
	private MessageDB messageDB;
	private ClientOutputThread out;
	private LinearLayout emojiIconContainer;
	private LinearLayout btnContainer;
	private ImageView iv_emoticons_normal,iv_emoticons_checked;
	private View more;

    private ViewPager expressionViewpager;
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
			
			 iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
		     iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
		     btnMore = (Button) findViewById(R.id.btn_more);
		     emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);
		     btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
		     more = findViewById(R.id.more);
		     expressionViewpager = (ViewPager) findViewById(R.id.vPager);
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
		 
		 iv_emoticons_normal.setOnClickListener(this);
	     iv_emoticons_checked.setOnClickListener(this);
	     chat_bottom_edit.addTextChangedListener(editTextWatcher);
		
	     initLogin();
		 initData();
				
	   }
	  public void onClick(View view) {
	  
		  int id=view.getId();
		  if (id == R.id.iv_emoticons_normal) { // 点击显示表情框
	            more.setVisibility(View.VISIBLE);
	            iv_emoticons_normal.setVisibility(View.INVISIBLE);
	            iv_emoticons_checked.setVisibility(View.VISIBLE);
	            btnContainer.setVisibility(View.GONE);
	            emojiIconContainer.setVisibility(View.VISIBLE);
	         //   hideKeyboard();
	        } else if (id == R.id.iv_emoticons_checked) { // 点击隐藏表情框
	            iv_emoticons_normal.setVisibility(View.VISIBLE);
	            iv_emoticons_checked.setVisibility(View.INVISIBLE);
	            btnContainer.setVisibility(View.VISIBLE);
	            emojiIconContainer.setVisibility(View.GONE);
	            more.setVisibility(View.GONE);

	        }else if(id==R.id.btn_more)
	        {
	        	 btnContainer.setVisibility(View.VISIBLE);
	        	 more.setVisibility(View.VISIBLE);
	        	
	        }
	  }
	  /**
	     * 显示或隐藏图标按钮页
	     * 
	     * @param view
	     */
	    public void more(View view) {
	        if (more.getVisibility() == View.GONE) {
	            System.out.println("more gone");
	          //  hideKeyboard();
	            more.setVisibility(View.VISIBLE);
	            btnContainer.setVisibility(View.VISIBLE);
	            emojiIconContainer.setVisibility(View.GONE);
	        } else {
	            if (emojiIconContainer.getVisibility() == View.VISIBLE) {
	                emojiIconContainer.setVisibility(View.GONE);
	                btnContainer.setVisibility(View.VISIBLE);
	                iv_emoticons_normal.setVisibility(View.VISIBLE);
	                iv_emoticons_checked.setVisibility(View.INVISIBLE);
	            } else {
	                more.setVisibility(View.GONE);
	            }

	        }

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
		
		 // 表情list
        reslist = getExpressionRes(35);
        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        views.add(gv1);
        views.add(gv2);
        expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
		
	}

	private TextWatcher editTextWatcher=new TextWatcher(){

        public void onTextChanged(CharSequence s, int start, int before,
                int count) {
            if (!TextUtils.isEmpty(s)) {
                btnMore.setVisibility(View.GONE);
                chat_bottom_send.setVisibility(View.VISIBLE);
            } else {
                btnMore.setVisibility(View.VISIBLE);
                chat_bottom_send.setVisibility(View.GONE);
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {
        }

        public void afterTextChanged(Editable s) {

        }
		
	};
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
	
	
	 /**
     * 获取表情的gridview的子view
     * 
     * @param i
     * @return
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<String>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 20);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(20, reslist.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this,
                1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    // 按住说话可见，不让输入表情
                 //   if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

                        if (filename != "delete_expression") { // 不是删除键，显示表情
                            // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                            @SuppressWarnings("rawtypes")
                            Class clz = Class
                                    .forName("hq.king.util.SmileUtils");
                            Field field = clz.getField(filename);
                            chat_bottom_edit.append(SmileUtils.getSmiledText(
                                    ChatActivity.this, (String) field.get(null)));
                        } else { // 删除文字或者表情
                           if (!TextUtils.isEmpty(chat_bottom_edit.getText())) {

                              int selectionStart = chat_bottom_edit
                                        .getSelectionStart();// 获取光标的位置
                                if (selectionStart > 0) {
                                 String body = chat_bottom_edit.getText()
                                            .toString();
                                    String tempStr = body.substring(0,
                                            selectionStart);
                                    int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                    if (i != -1) {
                                        CharSequence cs = tempStr.substring(i,
                                                selectionStart);
                                  if (SmileUtils.containsKey(cs
                                                .toString()))
                                	  chat_bottom_edit.getEditableText()
                                                    .delete(i, selectionStart);
                                        else
                                        	chat_bottom_edit.getEditableText()
                                                    .delete(selectionStart - 1,
                                                            selectionStart);
                                    } else {
                                    		chat_bottom_edit.getEditableText()
                                                .delete(selectionStart - 1,
                                                        selectionStart);
                                    }
                                }
                            }

                        }
                 //   }
                } catch (Exception e) {
                }

            }
        });
        return view;
    }
	 public List<String> getExpressionRes(int getSum) {
	        List<String> reslist = new ArrayList<String>();
	        for (int x = 1; x <= getSum; x++) {
	            String filename = "ee_" + x;

	            reslist.add(filename);

	        }
	        return reslist;

	    }
    	
}
