package hq.king.view;

import java.util.ArrayList;
import java.util.List;

import hq.king.activity.R;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class CallFragment extends Fragment {

	private Context mContext;
	private View mBaseView;
	private ListView mListView;
	private Adapter mAdapter;
	private List<String> contactList=new ArrayList<String>();
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=getActivity();
		mBaseView=inflater.inflate(R.layout.fragment_call, null);
		init();
		return mBaseView;
	}
	void init()
	{
		mListView=(ListView) mBaseView.findViewById(R.id.fragment_call_constact);
		mAdapter=new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,contactList);
	    mListView.setAdapter((ListAdapter) mAdapter);
	    readContacts();
	}
	private void readContacts()
	{
		Cursor mCursor=null;
		mCursor=mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
		while(mCursor.moveToNext())
		{
			String contactName=mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String contactNumber=mCursor.getString(mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			contactList.add(contactName+"\n"+contactNumber);
			
			
		}
		mCursor.close();
		
	}
}
