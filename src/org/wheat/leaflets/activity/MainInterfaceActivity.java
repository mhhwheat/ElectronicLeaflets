/** 
 * description：
 * @author wheat
 * date: 2015-3-9  
 * time: 下午8:06:32
 */ 
package org.wheat.leaflets.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.entity.Leaflets;
import org.wheat.leaflets.entity.PhotoParameters;
import org.wheat.leaflets.entity.json.LeafletsJson;
import org.wheat.leaflets.loader.HttpLoaderMethods;
import org.wheat.leaflets.loader.ImageLoader;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

/** 
 * description:
 * @author wheat
 * date: 2015-3-9  
 * time: 下午8:06:32
 */
public class MainInterfaceActivity extends FragmentActivity implements OnScrollListener
{
	
	private final int PAGE_LENGTH=10;//每次请求数据页里面包含的最多数据项
	private PullToRefreshListView mPullToRefreshListView;
	private List<Leaflets> mListData;//保存listview数据项的数组
	private ImageLoader mImageLoader;//加载图片的对象
	private LayoutInflater mInflater;
	private MainInterfaceListAdapter adapter;
	
	private boolean isLoadingMore=false;//防止重复开启异步加载线程
	private View mFooterView;
	private TextView tvFooterText;
	private ProgressBar pbFooterLoading;
	private ListView mActualListView;//PulltoRefreshListView中真正的ListView
	
	private DisplayMetrics metric;
	
	private PopupWindow popupMenu;
	
	private ImageView ivMinePageButton;//跳转到我的页面的按钮
	
	private Button btSortingType;//筛选按钮
	
	private SlidingMenu menu;//侧滑菜单
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
		setContentView(R.layout.activity_main_interface);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_main_interface_title);
		
		
		metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		
		mListData=new ArrayList<Leaflets>();
		mImageLoader=ImageLoader.getInstance(getApplicationContext());
		adapter=new MainInterfaceListAdapter();
		
		new UpdateDataTask("wheat","published").execute();
		
		mInflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		taskPool=new HashMap<String, ImageView>();

		mPullToRefreshListView=(PullToRefreshListView)findViewById(R.id.main_interface_refresh_list_view);
		mActualListView=mPullToRefreshListView.getRefreshableView();
		
		mFooterView=mInflater.inflate(R.layout.refresh_list_footer, null);
		pbFooterLoading=(ProgressBar)mFooterView.findViewById(R.id.refresh_list_footer_progressbar);
		tvFooterText=(TextView)mFooterView.findViewById(R.id.refresh_list_footer_text);

		mPullToRefreshListView.setAdapter(adapter);
		mActualListView.addFooterView(mFooterView);
		
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
		initialListViewListener();
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch(scrollState)
		{
		case OnScrollListener.SCROLL_STATE_FLING:
			mImageLoader.lock();
			break;
		case OnScrollListener.SCROLL_STATE_IDLE:
			mImageLoader.unlock();
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			mImageLoader.lock();
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	
	private class MainInterfaceListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListData.size();
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
			final Leaflets listItem=mListData.get(position);
			ViewHolder holder=null;
			if(convertView==null)
			{
				holder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.activity_main_interface_list_item, null);
				holder.ivSellerAvatar=(ImageView)convertView.findViewById(R.id.main_interface_item_seller_avatar);
				holder.tvSellerName=(TextView)convertView.findViewById(R.id.main_interface_item_seller_name);
				holder.tvPublishTime=(TextView)convertView.findViewById(R.id.main_interface_item_publish_time);
				holder.tvLeafletType=(TextView)convertView.findViewById(R.id.main_interface_item_leaflet_type);
				holder.ivLeafletBrief=(ImageView)convertView.findViewById(R.id.main_interface_item_leaflet_brief);
				holder.tvPraiseTimes=(TextView)convertView.findViewById(R.id.main_interface_item_praise_times);
				holder.tvCommentTimes=(TextView)convertView.findViewById(R.id.main_interface_item_comment_times);
				holder.praiseView=convertView.findViewById(R.id.main_interface_item_praise_area);
				holder.commentView=convertView.findViewById(R.id.main_interface_item_comment_area);
				
				convertView.setTag(holder);
				
			}
			else
				holder=(ViewHolder)convertView.getTag();
			
			if(mPhotoWidth<=0)
			{
				holder.ivLeafletBrief.getViewTreeObserver().addOnGlobalLayoutListener(new GlobalLayoutLinstener(holder.ivLeafletBrief));
			}
			
			addTaskToPool(new PhotoParameters(listItem.getLeafletFields().getSellerLogoPath(), 50, 50*50,"seller_logo"), holder.ivSellerAvatar);
			holder.tvSellerName.setText(listItem.getLeafletFields().getSellerName());
			holder.tvPublishTime.setText(getDifferenceFromDate(listItem.getLeafletFields().getPublishTime()));
			holder.tvLeafletType.setText(listItem.getLeafletFields().getLeafletType());
			addTaskToPool(new PhotoParameters(listItem.getLeafletFields().getBriefLeafletPath(), mPhotoWidth, 2*mPhotoWidth*mPhotoWidth, true,mPhotoWidth,"secondary"), holder.ivLeafletBrief);
			holder.tvPraiseTimes.setText(String.valueOf(listItem.getLeafletFields().getPraiseTimes()));
			holder.tvCommentTimes.setText(String.valueOf(listItem.getLeafletFields().getCommentTimes()));
			holder.praiseView.setTag(listItem);
			holder.commentView.setTag(listItem);
			
			return convertView;
		}
		
		private final class ViewHolder
		{
			public ImageView ivSellerAvatar;
			public TextView tvSellerName;
			public TextView tvPublishTime;
			public TextView tvLeafletType;
			public ImageView ivLeafletBrief;
			public TextView  tvPraiseTimes;
			public TextView  tvCommentTimes;
			public View praiseView;
			public View commentView;
			
		}
		
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
		getSupportFragmentManager().beginTransaction()
		.replace(R.id.menu_frame, new FragmentSlidingMenu()).commit();
	}
	
	
	private void initialListViewListener()
	{
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				Log.d("FragmentNeighbor", "UpdateDataTask will be executed");
				new UpdateDataTask("wheat","published").execute();
			}
		});
		
		mPullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
//				Toast.makeText(getActivity(), "End of List!", Toast.LENGTH_SHORT).show();
				if(!isLoadingMore)
				{
					isLoadingMore=true;
					pbFooterLoading.setVisibility(View.VISIBLE);
					tvFooterText.setText(R.string.list_footer_loading);
					new LoadMoreTask(mListData.size(),mListData.size()+PAGE_LENGTH,"wheat","published").execute();
				}
			}
		});
		
		mPullToRefreshListView.setOnScrollListener(this);
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
	 * 
	 * description:刷新ListView内容的异步线程
	 * @author wheat
	 * date: 2015-3-7  
	 * time: 下午7:12:59
	 */
	private class UpdateDataTask extends AsyncTask<Void, Void, LeafletsJson>
	{
		private String userName;
		private String sortingType;
		
		public UpdateDataTask(String userName,String sortingType)
		{
			this.userName=userName;
			this.sortingType=sortingType;
		}

		@Override
		protected LeafletsJson doInBackground(Void... params) {
			LeafletsJson json=null;
			try
			{
				json=HttpLoaderMethods.flushLeafletData("wheat","published");
			}catch(Throwable e)
			{
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(LeafletsJson result) {
			if(result!=null&&result.getCode()==1000)
			{
				if(result.getData().size()>0)
				{
					synchronized (mListData) {
						mListData.clear();
						mListData=result.getData();
						adapter.notifyDataSetChanged();
					}
				}
			}
			
			mPullToRefreshListView.onRefreshComplete();
			
			if(result==null)
				onLoadComplete(true);
			else
				onLoadComplete(false);
			
			super.onPostExecute(result);
		}	
		
	}
	
	private class LoadMoreTask extends AsyncTask<Void, Void, LeafletsJson>
	{
		private int offsetStart;
		private int offsetEnd;
		private String userName;
		private String sortingType;
		
		public LoadMoreTask(int offsetStart,int offsetEnd,String userName,String sortingType)
		{
			super();
			this.offsetStart=offsetStart;
			this.offsetEnd=offsetEnd;
			this.sortingType=sortingType;
			this.userName=userName;
		}
		
		@Override
		protected LeafletsJson doInBackground(Void... params) {
			LeafletsJson json=null;
			try
			{
				json=HttpLoaderMethods.getNeighborPage(offsetStart, offsetEnd, userName,sortingType);
			}catch(Throwable e)
			{
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(LeafletsJson result) {
			if(result!=null&&result.getCode()==1000)
			{
				synchronized (mListData) {
					mListData.addAll(result.getData());
					adapter.notifyDataSetChanged();
				}
				onLoadComplete(false);
			}
			else
				onLoadComplete(true);
			super.onPostExecute(result);
		}
		
	}
	
	
	/**
	 * 
	 * @param wasLoadNothing 加载完成后，是否内容没有增加,true表示内容没有增加,false表示内容增加了
	 */
	private void onLoadComplete(boolean wasLoadNothing)
	{
		isLoadingMore=false;
		if(wasLoadNothing)
		{
			pbFooterLoading.setVisibility(View.GONE);
			tvFooterText.setText(R.string.list_footer_no_more);
		}
	}
	
	
	
	@SuppressLint("SimpleDateFormat")
	private String getDifferenceFromDate(Date date)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date now=new Date();
		long between=(now.getTime()-date.getTime())/1000;//把时差转为秒
		
		long day=between/(24*3600);
		long hour=between%(24*3600)/3600;
		long minute=between%3600/60;
		long second=between%60/60;
		
		if(day>0)
		{
			return format.format(date);
		}
		else if(hour>0)
		{
			return hour+new String("小时前");
		}
		else if(minute>0)
		{
			return minute+new String("分钟前");
		}
		else
		{
			return second+new String("秒前");
		}
	}
	
	
	

	private int mPhotoWidth=0;
	private int mMinSideLength=0;
	private int mMaxNumOfPixles=0;
	//已经获取到正确的ImageWidth
	private boolean allowFix=false;
	private Map<String,ImageView> taskPool;
	

	public class GlobalLayoutLinstener implements OnGlobalLayoutListener
	{
		private View view;
		public GlobalLayoutLinstener(View view)
		{
			this.view=view;
		}

		@Override
		public void onGlobalLayout() {
			if(mPhotoWidth<=0&&view.getWidth()>0)
			{
				mPhotoWidth=view.getWidth();
				mMinSideLength=(int)(mPhotoWidth*metric.density);
				mMaxNumOfPixles=2*mMinSideLength*mMinSideLength;
				unLockTaskPool();
				view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
			
		}
		
	}
	
	/**
	 * 锁住时，不能加载自适配图片，只能加载固定图片
	 */
	public void lockTaskPool()
	{
		this.allowFix=false;
	}
	
	/**
	 * 解除锁定后，可以加载自适配图片
	 */
	public void unLockTaskPool()
	{
		if(!allowFix)
		{
			this.allowFix=true;
		}
		doTaskInPool();
	}
	
	public void addTaskToPool(PhotoParameters parameters,ImageView img)
	{
		if(!parameters.isFixWidth())
		{
			mImageLoader.addTask(parameters, img);
		}
		else
		{
			synchronized (taskPool) {
				img.setTag(parameters);
				taskPool.put(Integer.toString(img.hashCode()), img);
			}
			if(allowFix)
			{
				doTaskInPool();
			}	
		}
	}
	
	public void doTaskInPool()
	{
		synchronized (taskPool) {
			Collection<ImageView> con=taskPool.values();
			for(ImageView img:con)
			{
				if(img!=null)
				{
					if(img.getTag()!=null)
					{
						PhotoParameters pp=(PhotoParameters)img.getTag();
						pp.setMinSideLength(mMinSideLength);
						pp.setMaxNumOfPixles(mMaxNumOfPixles);
						pp.setImageViewWidth(mPhotoWidth);
						mImageLoader.addTask(pp, img);
					}
				}
			}
			taskPool.clear();
		}
	}
	
}
