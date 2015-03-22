/** 
 * description：
 * @author wheat
 * date: 2015-3-5  
 * time: 下午1:10:13
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
import org.wheat.leaflets.entity.LeafletsFields;
import org.wheat.leaflets.entity.PhotoParameters;
import org.wheat.leaflets.entity.ReturnData;
import org.wheat.leaflets.loader.ImageLoader;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

/** 
 * description:
 * @author wheat
 * date: 2015-3-5  
 * time: 下午1:10:13
 */
public class FragmentFind extends Fragment implements OnScrollListener
{
	private final int PAGE_LENGTH=10;//每次请求数据页里面包含的最多数据项
	private PullToRefreshListView mPullToRefreshListView;
	private List<ReturnData<LeafletsFields>> mListData;//保存listview数据项的数组
	private ImageLoader mImageLoader;//加载图片的对象
	private LayoutInflater mInflater;
	private FindRefreshListAdapter adapter;
	
	private boolean isLoadingMore=false;//防止重复开启异步加载线程
	private View mFooterView;
	private TextView tvFooterText;
	private ProgressBar pbFooterLoading;
	private ListView mActualListView;//PulltoRefreshListView中真正的ListView
	
	private DisplayMetrics metric;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//获取设备信息
		metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);

		mListData=new ArrayList<ReturnData<LeafletsFields>>();
		mImageLoader=ImageLoader.getInstance(getActivity().getApplicationContext());
		
		adapter=new FindRefreshListAdapter();
		
//		new UpdateDataTask("wheat","published").execute();
	}
	
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater=inflater;

		taskPool=new HashMap<String, ImageView>();

		View view=inflater.inflate(R.layout.fragment_find, container,false);
		mPullToRefreshListView=(PullToRefreshListView)view.findViewById(R.id.find_refresh_list_view);

		mActualListView=mPullToRefreshListView.getRefreshableView();
//		mFooterView=inflater.inflate(R.layout.refresh_list_footer, null);
//		pbFooterLoading=(ProgressBar)mFooterView.findViewById(R.id.refresh_list_footer_progressbar);
//		tvFooterText=(TextView)mFooterView.findViewById(R.id.refresh_list_footer_text);

		mPullToRefreshListView.setAdapter(adapter);
		mActualListView.addFooterView(mFooterView);
		initialListViewListener();


		return view;
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




	private class FindRefreshListAdapter extends BaseAdapter
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
			final ReturnData<LeafletsFields> listItem=mListData.get(position);
			ViewHolder holder=null;
			if(convertView==null)
			{
				holder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.fragment_find_list_item, null);
				holder.ivSellerAvatar=(ImageView)convertView.findViewById(R.id.find_item_seller_avatar);
				holder.tvSellerName=(TextView)convertView.findViewById(R.id.find_item_seller_name);
				holder.tvPublishTime=(TextView)convertView.findViewById(R.id.find_item_publish_time);
				holder.tvLeafletType=(TextView)convertView.findViewById(R.id.find_item_leaflet_type);
				holder.ivLeafletBrief=(ImageView)convertView.findViewById(R.id.find_item_leaflet_brief);
				holder.tvPraiseTimes=(TextView)convertView.findViewById(R.id.find_item_praise_times);
				holder.tvCommentTimes=(TextView)convertView.findViewById(R.id.find_item_comment_times);
				holder.praiseView=convertView.findViewById(R.id.find_item_praise_area);
				holder.commentView=convertView.findViewById(R.id.find_item_comment_area);
				
				convertView.setTag(holder);
				
			}
			else
				holder=(ViewHolder)convertView.getTag();
			addTaskToPool(new PhotoParameters(listItem.getDataFields().getSellerLogoPath(), 50, 50*50,"secondary"), holder.ivSellerAvatar);
			holder.tvSellerName.setText(listItem.getDataFields().getSellerName());
			holder.tvPublishTime.setText(getDifferenceFromDate(listItem.getDataFields().getPublishTime()));
			holder.tvLeafletType.setText(listItem.getDataFields().getLeafletType());
			addTaskToPool(new PhotoParameters(listItem.getDataFields().getBriefLeafletPath(), mPhotoWidth, 2*mPhotoWidth*mPhotoWidth, true,mPhotoWidth,"secondary"), holder.ivLeafletBrief);
			holder.tvPraiseTimes.setText(String.valueOf(listItem.getDataFields().getPraiseTimes()));
			holder.tvCommentTimes.setText(String.valueOf(listItem.getDataFields().getCommentTimes()));
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
	
	private void initialListViewListener()
	{
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				Log.d("FragmentNeighbor", "UpdateDataTask will be executed");
//				new UpdateDataTask("wheat","published").execute();
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
//					new LoadMoreTask(mListData.size(),mListData.size()+PAGE_LENGTH,"wheat","published").execute();
				}
			}
		});
		
		mPullToRefreshListView.setOnScrollListener(this);
	}
	
	
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
