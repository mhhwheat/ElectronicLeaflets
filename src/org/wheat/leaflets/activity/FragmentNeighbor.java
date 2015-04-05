/** 
 * description：
 * @author wheat
 * date: 2015-3-5  
 * time: 下午1:09:59
 */ 
package org.wheat.leaflets.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.data.UserLoginPreference;
import org.wheat.leaflets.entity.LeafletsFields;
import org.wheat.leaflets.entity.PhotoParameters;
import org.wheat.leaflets.entity.PraisePost;
import org.wheat.leaflets.entity.ReturnData;
import org.wheat.leaflets.entity.json.LeafletsJson;
import org.wheat.leaflets.entity.json.PraisePostJson;
import org.wheat.leaflets.loader.HttpLoaderMethods;
import org.wheat.leaflets.loader.HttpUploadMethods;
import org.wheat.leaflets.loader.ImageLoader;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/** 
 * description:
 * @author wheat
 * date: 2015-3-5  
 * time: 下午1:09:59
 */
public class FragmentNeighbor extends Fragment implements OnScrollListener
{
	private final int PAGE_LENGTH=10;//每次请求数据页里面包含的最多数据项
	private String userName="wheat";
	
	private PullToRefreshListView mPullToRefreshListView;
	private List<ReturnData<LeafletsFields>> mListData;//保存listview数据项的数组
	private ImageLoader mImageLoader;//加载图片的对象
	private LayoutInflater mInflater;
	private NeighborRefreshListAdapter adapter;
	
	private boolean isLoadingMore=false;//防止重复开启异步加载线程
	private View mFooterView;
	private TextView tvFooterText;
	private ProgressBar pbFooterLoading;
	private ListView mActualListView;//PulltoRefreshListView中真正的ListView
	
	private DisplayMetrics metric;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		userName=UserLoginPreference.getInstance(getActivity().getApplicationContext()).getUserName();
		if(userName.equals("")||userName==null)
		{
			userName="wheat";
		}
		//获取设备信息
		metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		
		mListData=new ArrayList<ReturnData<LeafletsFields>>();
		mImageLoader=ImageLoader.getInstance(getActivity().getApplicationContext());
		adapter=new NeighborRefreshListAdapter();
		
		new UpdateDataTask("wheat","published").execute();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater=inflater;
		
		taskPool=new HashMap<String, ImageView>();
		
		View view=inflater.inflate(R.layout.fragment_neighbor, container,false);
		mPullToRefreshListView=(PullToRefreshListView)view.findViewById(R.id.neighbor_refresh_list_view);
		
		mActualListView=mPullToRefreshListView.getRefreshableView();
		mFooterView=inflater.inflate(R.layout.refresh_list_footer, null);
		pbFooterLoading=(ProgressBar)mFooterView.findViewById(R.id.refresh_list_footer_progressbar);
		tvFooterText=(TextView)mFooterView.findViewById(R.id.refresh_list_footer_text);
		
		mPullToRefreshListView.setAdapter(adapter);
		mActualListView.addFooterView(mFooterView);
		initialListViewListener();
		
		
		return view;
	}
	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK&&requestCode==1)
		{
			int leafletId=data.getIntExtra("leaflet_id", -1);
			int newCommentCount=data.getIntExtra("new_comment_count", 0);
			
			ReturnData<LeafletsFields> leaflet=findLeafletsByID(mListData, leafletId);
			if(leaflet!=null)
			{
				if(leaflet.getDataFields().getCommentTimes()<newCommentCount)
				{
					leaflet.getDataFields().setCommentTimes(newCommentCount);
					adapter.notifyDataSetChanged();
				}
			}
			
		}
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
	
	
	private class NeighborRefreshListAdapter extends BaseAdapter
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
				convertView=mInflater.inflate(R.layout.fragment_neighbor_list_item, null);
				holder.ivSellerAvatar=(ImageView)convertView.findViewById(R.id.neighbor_item_seller_avatar);
				holder.tvSellerName=(TextView)convertView.findViewById(R.id.neighbor_item_seller_name);
				holder.tvPublishTime=(TextView)convertView.findViewById(R.id.neighbor_item_publish_time);
				holder.tvLeafletType=(TextView)convertView.findViewById(R.id.neighbor_item_leaflet_type);
				holder.ivLeafletBrief=(ImageView)convertView.findViewById(R.id.neighbor_item_leaflet_brief);
				holder.tvPraiseTimes=(TextView)convertView.findViewById(R.id.neighbor_item_praise_times);
				holder.ivPraise=(ImageView)convertView.findViewById(R.id.neighbor_item_praise_button);
				holder.tvCommentTimes=(TextView)convertView.findViewById(R.id.neighbor_item_comment_times);
				holder.praiseView=convertView.findViewById(R.id.neighbor_item_praise_area);
				holder.commentView=convertView.findViewById(R.id.neighbor_item_comment_area);
				
				convertView.setTag(holder);
				
				holder.commentView.setOnClickListener(new CommentAreaOnClickListener());
				holder.praiseView.setOnClickListener(new PraiseAreaOnClickListenter());
				
			}
			else
				holder=(ViewHolder)convertView.getTag();
			
			holder.commentView.setTag(listItem);
			holder.praiseView.setTag(listItem);
			
			if(mPhotoWidth<=0)
			{
				holder.ivLeafletBrief.getViewTreeObserver().addOnGlobalLayoutListener(new GlobalLayoutLinstener(holder.ivLeafletBrief));
			}
			
			addTaskToPool(new PhotoParameters(listItem.getDataFields().getSellerLogoPath(), 50, 50*50,"seller_logo"), holder.ivSellerAvatar);
			holder.tvSellerName.setText(listItem.getDataFields().getSellerName());
			holder.tvPublishTime.setText(getDifferenceFromDate(listItem.getDataFields().getPublishTime()));
			holder.tvLeafletType.setText(listItem.getDataFields().getLeafletType());
			addTaskToPool(new PhotoParameters(listItem.getDataFields().getBriefLeafletPath(), mPhotoWidth, 2*mPhotoWidth*mPhotoWidth, true,mPhotoWidth,"secondary"), holder.ivLeafletBrief);
			holder.tvPraiseTimes.setText(String.valueOf(listItem.getDataFields().getPraiseTimes()));
			if(listItem.getDataFields().isPraise()==1)
				holder.ivPraise.setImageResource(R.drawable.praisefull);
			else
				holder.ivPraise.setImageResource(R.drawable.praise);
			holder.tvCommentTimes.setText(String.valueOf(listItem.getDataFields().getCommentTimes()));
			
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
			public ImageView ivPraise;
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
					new LoadMoreTask(mListData.size()+1,mListData.size()+PAGE_LENGTH,"wheat","published").execute();
				}
			}
		});
		
		mPullToRefreshListView.setOnScrollListener(this);
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
				json=HttpLoaderMethods.flushLeafletData(userName,sortingType);
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
	
	
	private String getDifferenceFromDate(Date date)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date now=new Date();
		long between=(now.getTime()-date.getTime())/1000;//把时差转为秒
		
		long day=between/(24*3600);
		long hour=between%(24*3600)/3600;
		long minute=between%3600/60;
		long second=between%60;
		
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
	
	private ReturnData<LeafletsFields> findLeafletsByID(List<ReturnData<LeafletsFields>> list,int leafletId)
	{
		ReturnData<LeafletsFields> leaflets=null;
		for(Iterator<ReturnData<LeafletsFields>> i=list.iterator();i.hasNext();)
		{
			leaflets=i.next();
			if(leaflets.getPrimaryKey()==leafletId)
				break;
		}
		return leaflets;
	}
	
	private class CommentAreaOnClickListener implements OnClickListener
	{
		private ReturnData<LeafletsFields> listItem;
		@Override
		public void onClick(View v) {
			listItem=(ReturnData<LeafletsFields>)v.getTag();
			
			Intent intent=new Intent(getActivity(),CommentContentActivity.class);
			Bundle bundle=new Bundle();
			bundle.putString("user_name", userName);
			bundle.putInt("leaflet_id", listItem.getPrimaryKey());
			bundle.putInt("comment_count", listItem.getDataFields().getCommentTimes());
			intent.putExtras(bundle);
			startActivityForResult(intent, 1);
		}
		
	}
	
	private class PraiseAreaOnClickListenter implements OnClickListener
	{
		private ReturnData<LeafletsFields> leaflet;

		@Override
		public void onClick(View v) {
			leaflet=(ReturnData<LeafletsFields>)v.getTag();
			if(leaflet.getDataFields().isPraise()==0)
			{
				leaflet.getDataFields().setPraise(1);
				leaflet.getDataFields().setPraiseTimes(leaflet.getDataFields().getPraiseTimes()+1);
				adapter.notifyDataSetChanged();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						PraisePost praisePost=new PraisePost();
						praisePost.setLeafletId(leaflet.getPrimaryKey());
						praisePost.setUserName(userName);
						
						PraisePostJson json=new PraisePostJson();
						json.setData(praisePost);
						try{
							HttpUploadMethods.postPraisePost(json);
						}catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}).start();
			}
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
