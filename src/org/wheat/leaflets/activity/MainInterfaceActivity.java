/** 
 * description：
 * @author wheat
 * date: 2015-3-9  
 * time: 下午8:06:32
 */ 
package org.wheat.leaflets.activity;



import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.basic.ExitApplication;
import org.wheat.leaflets.entity.MyLocation;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

/** 
 * description:
 * @author wheat
 * date: 2015-3-9  
 * time: 下午8:06:32
 */
public class MainInterfaceActivity extends FragmentActivity implements AMapLocationListener
{
	private LayoutInflater mInflater;
	
	private PopupWindow popupMenu;
	
	private ImageView ivMinePageButton;//跳转到我的页面的按钮
	
	
	private Button btSortingType;//筛选按钮0
	
	private SlidingMenu menu;//侧滑菜单
	
	private Fragment mCurrentFragment;
	private Fragment mMainInterfaceFragment;
	private Fragment mFollowFragment;
	
	
	/**
	 * 高德地图定位信息
	 */
	private LocationManagerProxy mLocationManagerProxy;
	private  MyLocation myLocation = new MyLocation();
	
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
		
		mMainInterfaceFragment=new FragmentMainInterface();
		mFollowFragment=new FragmentFollow();
		switchFragment(null, mMainInterfaceFragment, R.id.replacing_fragment);
		mCurrentFragment=mMainInterfaceFragment;
		
		// 开启界面的时候就定位
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.setGpsEnable(false);
		mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 150, this);
		
		ExitApplication.getInstance().addActivity(this);
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



//	@Override
//	public void onItemClick(int item) {
//		switch(item)
//		{
//		case 0:
//			switchFragment(mCurrentFragment, mMainInterfaceFragment, R.id.replacing_fragment);
//			mCurrentFragment=mMainInterfaceFragment;
//			menu.toggle();
//			break;
//		case 1:
//			switchFragment(mCurrentFragment, mFollowFragment, R.id.replacing_fragment);
//			mCurrentFragment=mFollowFragment;
//			menu.toggle();
//			break;
//		}
//	}


	
	//高德地图api接口

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation!=null&&amapLocation.getAMapException().getErrorCode() == 0) {
			// 定位成功回调信息，设置相关消息
			
			this.myLocation.setLat(amapLocation.getLatitude());
			this.myLocation.setLng(amapLocation.getLongitude());
			this.myLocation.setLocationMessage(amapLocation.getAddress());
			Toast.makeText(this, amapLocation.getAddress(),Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(this, "定位失败，请检查你的GPS和网络", Toast.LENGTH_LONG).show();
		}
		
	}
	
	//拦截返回键消息
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {

			}
			return true;
		}
	
//	/** 
//	 * 以下的几个方法用来，让fragment能够监听touch事件 
//	 */  
//	private ArrayList<ActivityOnTouchListener> onTouchListeners = new ArrayList<ActivityOnTouchListener>();  
//
//	@Override  
//	public boolean dispatchTouchEvent(MotionEvent ev) 
//	{  
//		for (ActivityOnTouchListener listener : onTouchListeners) 
//		{  
//			listener.onTouch(ev);  
//		}  
//		return super.dispatchTouchEvent(ev);  
//	}  
//
//	public void registerActivityOnTouchListener(ActivityOnTouchListener ActivityOnTouchListener) {  
//		onTouchListeners.add(ActivityOnTouchListener);  
//	}  
//
//	public void unregisterActivityOnTouchListener(ActivityOnTouchListener ActivityOnTouchListener) {  
//		onTouchListeners.remove(ActivityOnTouchListener);  
//	}  
//
//	public interface ActivityOnTouchListener
//	{
//		public boolean onTouch(MotionEvent ev);  
//	}
//	
}
