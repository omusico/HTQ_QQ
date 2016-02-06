package hq.king.app;

import hq.king.adapter.RecentChatAdapter;
import hq.king.client.Client;
import hq.king.entity.RecentChatEntity;
import hq.king.util.Constants;

import java.io.IOException;
import java.net.UnknownHostException;


import java.util.LinkedList;

import android.app.Application;

public class MyApplication extends Application {
//当一个安卓程序启动时最新执行的是Application类，然后才是入口Activity
	//因为application在安卓中是全局的，且他的生命周期为整个app运行的生命周期
	//所以，可以考虑把一些多个activity都要用到的全局的东西在application中实例化
	private int MsgNum=0,recentMsgNum=0;
	private RecentChatAdapter mRecentAdapter;
	private LinkedList<RecentChatEntity> mRecentList;
	private Client client;
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		try {
			client=new Client(Constants.IP, Constants.PORT);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mRecentList = new LinkedList<RecentChatEntity>();
		mRecentAdapter = new RecentChatAdapter(getApplicationContext(),
				mRecentList);
		
	}
	public Client getClient()
	{

		return client;
	}
	
	public LinkedList<RecentChatEntity> getmRecentList() {
		return mRecentList;
	}

	public void setmRecentList(LinkedList<RecentChatEntity> mRecentList) {
		this.mRecentList = mRecentList;
	}
	
	public RecentChatAdapter getmRecentAdapter() {
		return mRecentAdapter;
	}

	public void setmRecentAdapter(RecentChatAdapter mRecentAdapter) {
		this.mRecentAdapter = mRecentAdapter;
	}
	public int getRecentNum() {
		return recentMsgNum;
	}

	public void setRecentNum(int recentNum) {
		this.recentMsgNum = recentNum;
	}

}
