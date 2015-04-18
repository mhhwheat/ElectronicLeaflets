/** 
 * description：
 * @author wheat
 * date: 2015-3-22  
 * time: 下午8:01:46
 */ 
package org.wheat.leaflets.adapter;


import org.wheat.electronicleaflets.R;

import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

/** 
 * description:
 * @author wheat
 * date: 2015-3-22  
 * time: 下午8:01:46
 */
public class LeafletClassListAdapter extends BaseAdapter
{

	private int selectedItemIndex;
	private String[] mListData;
	private LayoutInflater mInflater;
	
	public LeafletClassListAdapter(String[] mListData,LayoutInflater inflater)
	{
		this.mListData=mListData;
		this.mInflater=inflater;
		this.selectedItemIndex=0;
	}
	
	public LeafletClassListAdapter(String[] mListData,LayoutInflater inflater,int index)
	{
		this.mListData=mListData;
		this.mInflater=inflater;
		this.selectedItemIndex=index;
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
		// TODO Auto-generated method stub
		String mListItem=mListData[position];
		convertView=mInflater.inflate(R.layout.leaflet_class_list_item, null);
		TextView view=(TextView)convertView.findViewById(R.id.leaflet_class_list_item_text);
		if(position==selectedItemIndex)
		{
			convertView.setBackgroundColor(0x33000000);
			view.setTextColor(0xFFFFFFE0);
		}
		view.setText(mListItem);
		return convertView;
	}
	
	/**
	 * 设置被选中的item，默认是0
	 * @param index
	 */
	public void setSelectedItemIndex(int index)
	{
		this.selectedItemIndex=index;
	}
	
	public int getSelectedItemIndex()
	{
		return this.selectedItemIndex;
	}
	
}
