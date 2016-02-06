package hq.king.view;


import hq.king.activity.ChatActivity;
import hq.king.activity.CurrentOnLineActivity;
import hq.king.activity.R;
import hq.king.entity.User;
import android.R.integer;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;

public class ConstactFragment extends Fragment {
	private Context mContext;
	private View mBaseView;
	private TitleBarView mTitleBarView;
	private RelativeLayout groupRelative;
	private int REQUEST_CHAT=0;
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			mContext=getActivity();
			mBaseView=inflater.inflate(R.layout.fragment_constact, null);
			initTitleBar();
			return mBaseView;
		}
		void initTitleBar()
		{
			mTitleBarView=(TitleBarView) ((Activity) mContext).findViewById(R.id.title_bar);
			
			mTitleBarView.setBtnRight(R.string.title_bar_right_btn);
			mTitleBarView.setBtnRightDrawable(null);
			mTitleBarView.setCommonTitle(View.VISIBLE,View.GONE ,View.VISIBLE, View.VISIBLE);
			mTitleBarView.setTitleText(R.string.constact);
			groupRelative=(RelativeLayout) mBaseView.findViewById(R.id.fragment_contact_group_relative);
			groupRelative.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(mContext,CurrentOnLineActivity.class);
					startActivity(intent);
				}
			});
			Button rightBtn=(Button) ((Activity) mContext).findViewById(R.id.title_bar_rightBtn);
			rightBtn.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			
			Toast.makeText(mContext, "测试", Toast.LENGTH_SHORT);
			// TODO Auto-generated method stub
			
		}
	   });
			
	}
		
	
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			 ExpandableListAdapter adapter=new MyAdapter();
			  ExpandableListView ev=(ExpandableListView) getActivity().findViewById(R.id.expandlist);
			  ev.setGroupIndicator(null);
			  ev.setAdapter(adapter);
			ev.setOnChildClickListener(new OnChildClickListener(){

				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					// TODO Auto-generated method stub
					TextView nameView = (TextView) v.findViewById(R.id.contact_list_item_name);
					String friend_name=nameView.getText().toString();
					Intent intent=new Intent(getActivity(),ChatActivity.class);
					Bundle bundle=new Bundle();
					User user=new User();
					user.setId(10*groupPosition+childPosition);
					user.setName(friend_name);
					intent.putExtra("user",user);
					startActivityForResult(intent,REQUEST_CHAT);
					return true;
				}
				
				
			}
			);
		}
		
		
		
		
		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			if(requestCode==0&&resultCode==1)
			{
				ImageButton mXiaoxi=(ImageButton) getActivity().findViewById(R.id.buttom_xiaoxi);
				mXiaoxi.performClick();
			}
		}




		/*将适配器定义为内部类*/
		class MyAdapter implements ExpandableListAdapter
		{
			
			String [] group=new String[]{"我的偶像","同事","同学"};
			String[][] child=new String[][]{{"赵丽颖","李彦宏"},{"王婷","张雅","马化腾","李彦宏"},{"章泽天","赵丽颖","王婷"}};
	        String [][] signString=new String[][]{{"我家颖宝最可爱！","向偶像学习！"},{"我曾经喜欢过你！","","羞涩的小马哥","度厂第一美男子"},{"奶茶姐姐","萌萌哒的小骨",""}};
			public boolean areAllItemsEnabled() {
				// TODO Auto-generated method stub
				return false;
			}

			public Object getChild(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return child[groupPosition][childPosition];
			}

			public long getChildId(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return childPosition;
			}

			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				
				GroupHolder holder = null;
				if (convertView == null) {
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.fragment_constact_child, null);
					holder = new GroupHolder();
					holder.nameView = (TextView) convertView
							.findViewById(R.id.contact_list_item_name);
					holder.feelView = (TextView) convertView
							.findViewById(R.id.cpntact_list_item_state);
					holder.iconView = (ImageView) convertView.findViewById(R.id.icon);
					convertView.setTag(holder);
				} else {
					holder = (GroupHolder) convertView.getTag();
				}
				
				holder.iconView.setImageResource(R.drawable.head);
				holder.nameView.setText(getChild(groupPosition, childPosition)
						.toString());
				holder.feelView.setText(signString[groupPosition][childPosition]);
				return convertView;
			}

			public int getChildrenCount(int groupPosition) {
				// TODO Auto-generated method stub
				return child[groupPosition].length;
			}

			public long getCombinedChildId(long groupId, long childId) {
				// TODO Auto-generated method stub
				return 0;
			}

			public long getCombinedGroupId(long groupId) {
				// TODO Auto-generated method stub
				return 0;
			}

			public Object getGroup(int groupPosition) {
				// TODO Auto-generated method stub
				return group[groupPosition];
			}

			public int getGroupCount() {
				// TODO Auto-generated method stub
				return group.length;
			}

			public long getGroupId(int groupPosition) {
				// TODO Auto-generated method stub
				return  groupPosition ;
			}

			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				
				
				ChildHolder holder = null;
				if (convertView == null) {
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.fragment_constact_group, null);
					holder = new ChildHolder();
					holder.nameView = (TextView) convertView.findViewById(R.id.group_name);
					holder.onLineView = (TextView) convertView
							.findViewById(R.id.online_count);
					holder.iconView = (ImageView) convertView
							.findViewById(R.id.group_indicator);
					convertView.setTag(holder);
				} else {
					holder = (ChildHolder) convertView.getTag();
				}
				holder.nameView.setText(group[groupPosition]);
				holder.onLineView.setText(getChildrenCount(groupPosition) + "/"
						+ getChildrenCount(groupPosition));
				if (isExpanded) {
					holder.iconView.setImageResource(R.drawable.qb_down);
				} else {
					holder.iconView.setImageResource(R.drawable.qb_right);
				}
				return convertView;
			
			}

			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return true;
			}

			public boolean isChildSelectable(int groupPosition, int childPosition) {
				// TODO Auto-generated method stub
				return true;
			}

			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}

			public void onGroupCollapsed(int groupPosition) {
				// TODO Auto-generated method stub
				
			}

			public void onGroupExpanded(int groupPosition) {
				// TODO Auto-generated method stub
				
			}

			public void registerDataSetObserver(DataSetObserver observer) {
				// TODO Auto-generated method stub
				
			}

			public void unregisterDataSetObserver(DataSetObserver observer) {
				// TODO Auto-generated method stub
				
			}
		}
		class ChildHolder {
			TextView nameView;
			TextView onLineView;
			ImageView iconView;
		}

		class GroupHolder {
			TextView nameView;
			TextView feelView;
			ImageView iconView;
		}
}
