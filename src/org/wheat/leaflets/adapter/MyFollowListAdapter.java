/** 
 * description£º
 * @author wheat
 * date: 2015-4-1  
 * time: ÏÂÎç8:06:30
 */ 
package org.wheat.leaflets.adapter;

import org.wheat.electronicleaflets.R;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** 
 * description:
 * @author wheat
 * date: 2015-4-1  
 * time: ÏÂÎç8:06:30
 */
public class MyFollowListAdapter extends BaseAdapter
{
	private int mSelectedItemIndex;
	private String[] mListData;
	private LayoutInflater mInflater;
	
	public MyFollowListAdapter(String[] listData,LayoutInflater inflater)
	{
		this.mListData=listData;
		this.mInflater=inflater;
		this.mSelectedItemIndex=0;
	}
	
	public MyFollowListAdapter(String[] listData,LayoutInflater inflater,int seletedItemIndex)
	{
		this.mListData=listData;
		this.mInflater=inflater;
		this.mSelectedItemIndex=seletedItemIndex;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListData.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String mListItem=mListData[position];
		convertView=mInflater.inflate(R.layout.my_follow_list_item, null);
		TextView view=(TextView)convertView.findViewById(R.id.my_follow_list_item_text);
		if(position==mSelectedItemIndex)
		{
			Log.d("test", "test");
			convertView.setBackgroundColor(0x33000000);
			view.setTextColor(0xFFFFFFE0);
		}
		view.setText(mListItem);
		return convertView;
	}

	public int getmSelectedItemIndex() {
		return mSelectedItemIndex;
	}

	public void setmSelectedItemIndex(int mSelectedItemIndex) {
		this.mSelectedItemIndex = mSelectedItemIndex;
	}
	
	

}
