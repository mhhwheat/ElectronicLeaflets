/** 
 * description：
 * @author wheat
 * date: 2015-3-15  
 * time: 下午7:05:48
 */ 
package org.wheat.leaflets.activity;

import org.wheat.electronicleaflets.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/** 
 * description:
 * @author wheat
 * date: 2015-3-15  
 * time: 下午7:05:48
 */
public class FragmentSlidingMenu extends Fragment
{
	private OnSlidingMenuItemClickListener listener;
	private TextView tvNeighbor;
	private TextView tvFollow;
	private TextView tvSetting;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_sliding_menu,null);
		tvNeighbor=(TextView)view.findViewById(R.id.sliding_menu_neighbor);
		tvFollow=(TextView)view.findViewById(R.id.sliding_menu_my_follow);
		tvSetting=(TextView)view.findViewById(R.id.sliding_menu_setting);
		tvNeighbor.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener!=null)
					listener.onItemClick(0);
			}
		});
		tvFollow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//点击我的关注项
				if(listener!=null)
					listener.onItemClick(1);
			}
		});
		tvSetting.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//点击设置菜单项
				if(listener!=null)
					listener.onItemClick(2);
			}
		});
		
		
		return view;
	}
	
	public interface OnSlidingMenuItemClickListener
	{
		public void onItemClick(int item);
	}
	
	public void setOnSlidingMenuItemClickListener(OnSlidingMenuItemClickListener listener)
	{
		this.listener=listener;
	}
	
}
