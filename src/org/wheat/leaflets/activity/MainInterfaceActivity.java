/** 
 * description：
 * @author wheat
 * date: 2015-3-9  
 * time: 下午8:06:32
 */ 
package org.wheat.leaflets.activity;


import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.activity.FragmentSlidingMenu.OnSlidingMenuItemClickListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

/** 
 * description:
 * @author wheat
 * date: 2015-3-9  
 * time: 下午8:06:32
 */
public class MainInterfaceActivity extends FragmentActivity implements OnSlidingMenuItemClickListener
{
	private LayoutInflater mInflater;
	
	private PopupWindow popupMenu;
	
	private ImageView ivMinePageButton;//跳转到我的页面的按钮
	
	private Button btSortingType;//筛选按钮
	
	private SlidingMenu menu;//侧滑菜单
	
	private Fragment mCurrentFragment;
	private Fragment mNeighborFragment;
	private Fragment mFollowFragment;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
		setContentView(R.layout.activity_main_interface);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_main_interface_title);
		mInflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		initPopupMenu();
		btSortingType=(Button)findViewById(R.id.btMain_interface_title_sorting_type);
		ivMinePageButton=(ImageView)findViewById(R.id.ivMain_interface_title_me);
		
		btSortingType.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popupMenu.showAsDropDown(v,-40,40);
				popupMenu.showAtLocation(v, Gravity.BOTTOM, 0, 0);
			}
		});
		
		initialSlidingMenu();
		
		mNeighborFragment=new FragmentNeighbor();
		mFollowFragment=new FragmentFollow();
		switchFragment(null, mNeighborFragment, R.id.replacing_fragment);
		mCurrentFragment=mNeighborFragment;
	}
	
	
	
	private void initialSlidingMenu()
	{
		menu=new SlidingMenu(this);
		menu.setShadowWidth(0);
		menu.setBehindOffset(200);
		menu.setFadeDegree(0.35f);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.main_interface_sliding_menu);
		FragmentSlidingMenu slidingFragment=new FragmentSlidingMenu();
		slidingFragment.setOnSlidingMenuItemClickListener(this);
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.menu_frame, slidingFragment).commit();
	}
	
	
	
	
	private void initPopupMenu()
	{
		View menuView=mInflater.inflate(R.layout.main_interface_popup_menu, null,false);
		popupMenu=new PopupWindow(menuView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		
		// 设置允许在外点击消失
		popupMenu.setOutsideTouchable(true);
		// 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
		popupMenu.setFocusable(true);
		
		//如果需要PopupWindow响应返回键，那么必须给PopupWindow设置一个背景才行
		ColorDrawable dw = new ColorDrawable(0X00000000);
		popupMenu.setBackgroundDrawable(dw);	
	}
	
	
	/**
	 * 切换Fragemtn,替换布局为R.id.replacing_fragment
	 * @param from	切换前的Fragment
	 * @param to	切换后的Fragment
	 * @param sourceId 替换的布局id
	 */
	public void switchFragment(Fragment from,Fragment to,int sourceId)
	{
		FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
		if(!to.isAdded())
		{
			if(from!=null)
			{
				transaction.hide(from).add(sourceId, to).commit();
			}
			else
			{
				transaction.add(sourceId, to).commit();
			}
		}
		else
		{
			if(from!=null)
			{
				transaction.hide(from).show(to).commit();
			}
			else
			{
				transaction.show(to).commit();
			}
		}
	}



	@Override
	public void onItemClick(int item) {
		switch(item)
		{
		case 1:
			switchFragment(mCurrentFragment, mNeighborFragment, R.id.replacing_fragment);
			mCurrentFragment=mNeighborFragment;
			menu.toggle();
			break;
		case 0:
			switchFragment(mCurrentFragment, mFollowFragment, R.id.replacing_fragment);
			mCurrentFragment=mFollowFragment;
			menu.toggle();
			break;
		}
	}
	
}
