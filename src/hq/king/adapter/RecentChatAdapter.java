package hq.king.adapter;

import hq.king.activity.ChatActivity;
import hq.king.activity.R;
import hq.king.app.MyApplication;
import hq.king.entity.RecentChatEntity;
import hq.king.entity.User;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecentChatAdapter extends BaseAdapter {

	private Context context;
	private LinkedList<RecentChatEntity> list;
	private MyApplication application;
	private LayoutInflater inflater;
	public RecentChatAdapter() {
		// TODO Auto-generated constructor stub
	}
	public RecentChatAdapter(Context context, LinkedList<RecentChatEntity> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		application = (MyApplication) context.getApplicationContext();
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void remove(RecentChatEntity entity) {
		list.remove(entity);
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.recent_chat_item, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView
					.findViewById(R.id.recent_userhead);
			holder.name = (TextView) convertView.findViewById(R.id.recent_name);
			holder.date = (TextView) convertView.findViewById(R.id.recent_time);
			holder.msg = (TextView) convertView.findViewById(R.id.recent_msg);
			holder.count = (TextView) convertView
					.findViewById(R.id.recent_new_num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final RecentChatEntity entity = list.get(position);
		holder.icon.setImageResource(R.drawable.mine_avator);
		holder.name.setText(entity.getName());
		holder.name.setTextColor(Color.BLACK);
		holder.date.setText(entity.getTime());
		holder.date.setTextColor(Color.BLACK);
		holder.msg.setText(entity.getMsg());
		holder.msg.setTextColor(Color.BLACK);

		if (entity.getCount() > 0) {
			holder.count.setText(entity.getCount() + "");
			holder.count.setTextColor(Color.BLACK);
		} else {
			Log.i("recentchatAdapter","this is entity.getCount()"+String.valueOf(entity.getCount()));
			holder.count.setVisibility(View.INVISIBLE);// 如果没有消息，就隐藏此view
		}
		// 点击事件
		convertView.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 下面是切换到聊天界面处理
				User user = new User();
				user.setName(entity.getName());
				user.setId(entity.getId());
				user.setImg(entity.getImg());
				Intent intent = new Intent(context, ChatActivity.class);
				intent.putExtra("user", user);
				//这一句一定不能去掉否则会报错，Calling startActivity() from outside of an Activity  context 
				//requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
				//即当不是在一个activity中启动一个activity时必须加上上面那个语句
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				context.startActivity(intent);
				
				application.setRecentNum(0);

			}
		});
		return convertView;
	}
	static class ViewHolder {
		public ImageView icon;
		public TextView name;
		public TextView date;
		public TextView msg;
		public TextView count;
	}

}
