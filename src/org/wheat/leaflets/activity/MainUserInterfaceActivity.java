/** 
 * description：
 * @author wheat
 * date: 2015-3-5  
 * time: 下午1:08:04
 */ 
package org.wheat.leaflets.activity;

import java.util.ArrayList;

import org.wheat.electronicleaflets.R;

import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/** 
 * description:主界面
 * @author wheat
 * date: 2015-3-5  
 * time: 下午1:08:04
 */
public class MainUserInterfaceActivity extends FragmentActivity
{
	private ViewPager mViewPager;
	private ArrayList<Fragment> fragmentList;
	private LinearLayout tabNeighbor,tabFind,tabMine;
	private int currIndex=0;//当前页编号
	
	private TransitionDrawable tabNeighborTransition;
	private TransitionDrawable tabFindTransition;
	private TransitionDrawable tabMineTransition;
	
	private ImageView mNeighborPageImg,mFindPageImg,mMinePageImg;
	private TextView mNeighborPageText,mFindPageText,mMinePageText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
		setContentView(R.layout.activity_main_user_interface);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.fragment_neighbor_title); 
		initViewPageTab();
		InitViewPager();
	}
	
	private void initViewPageTab()
	{
		tabNeighbor=(LinearLayout)findViewById(R.id.tab_neighbor_page);
		tabFind=(LinearLayout)findViewById(R.id.tab_find_page);
		tabMine=(LinearLayout)findViewById(R.id.tab_mine_page);
		
		mNeighborPageImg=(ImageView)tabNeighbor.findViewById(R.id.tab_neighbor_page_img);
		mFindPageImg=(ImageView)tabFind.findViewById(R.id.tab_find_page_img);
		mMinePageImg=(ImageView)tabMine.findViewById(R.id.tab_mine_page_img);

		mNeighborPageText=(TextView)tabNeighbor.findViewById(R.id.tab_neighbor_page_text);
		mFindPageText=(TextView)tabFind.findViewById(R.id.tab_find_page_text);
		mMinePageText=(TextView)tabMine.findViewById(R.id.tab_mine_page_text);
		
		tabNeighborTransition=(TransitionDrawable)mNeighborPageImg.getDrawable();
		tabFindTransition=(TransitionDrawable)mFindPageImg.getDrawable();
		tabMineTransition=(TransitionDrawable)mMinePageImg.getDrawable();
		
		tabNeighbor.setOnClickListener(new TabListener(0));
		tabFind.setOnClickListener(new TabListener(1));
		tabMine.setOnClickListener(new TabListener(2));
	}
	
	public class TabListener implements View.OnClickListener
	{
		private int index=0;
		public TabListener(int index)
		{
			this.index=index;
		}
		
		@Override
		public void onClick(View v) {
			mViewPager.setCurrentItem(index);
		}
	}
	
	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager()
	{
		mViewPager=(ViewPager)findViewById(R.id.main_user_interface_view_pager);
		fragmentList=new ArrayList<Fragment>();
		Fragment tabNeighborFragment=new FragmentNeighbor();
		Fragment tabFindFragment=new FragmentFind();
		Fragment tabMineFragment=new FragmentMine();
		fragmentList.add(tabNeighborFragment);
		fragmentList.add(tabFindFragment);
		fragmentList.add(tabMineFragment);
		
		//给ViewPager设置适配器 
		mViewPager.setAdapter(new MainFragmentPagerAdapter(this.getSupportFragmentManager(), fragmentList));
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new MainPageChangeListener());
		checkTab(0);
	}
	
	public void checkTab(int index)
	{
		switch(index)
		{
		case 0:
			tabNeighborTransition.startTransition(100);
			mNeighborPageText.setTextColor(getResources().getColor(R.color.green));
			mViewPager.setCurrentItem(0);
			break;
		case 1:
			tabFindTransition.startTransition(100);
			mFindPageText.setTextColor(getResources().getColor(R.color.green));
			mViewPager.setCurrentItem(1);
			break;
		case 2:
			tabMineTransition.startTransition(100);
			mMinePageText.setTextColor(getResources().getColor(R.color.green));
			mViewPager.setCurrentItem(2);
			break;
		default:
			break;
		}
	}
	
	public void unCheckTab(int index)
	{
		switch(index)
		{
		case 0:
			tabNeighborTransition.reverseTransition(100);
			mNeighborPageText.setTextColor(getResources().getColor(R.color.black));
			break;
		case 1:
			tabFindTransition.reverseTransition(100);
			mFindPageText.setTextColor(getResources().getColor(R.color.black));
			break;
		case 2:
			tabMineTransition.reverseTransition(100);
			mMinePageText.setTextColor(getResources().getColor(R.color.black));
			break;
		default:
			break;
		}
	}
	
	
	private class MainFragmentPagerAdapter extends FragmentPagerAdapter
	{
		private ArrayList<Fragment> list;
		public MainFragmentPagerAdapter(FragmentManager fm,ArrayList<Fragment> list)
		{
			super(fm);
			this.list=list;
		}
		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}
		@Override
		public int getCount() {
			return list.size();
		}
		
	}
	
	public class MainPageChangeListener implements OnPageChangeListener
	{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			unCheckTab(currIndex);
			checkTab(arg0);
			currIndex=arg0;
		}
		
	}
	
}
