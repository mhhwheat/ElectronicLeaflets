/** 
 * description��
 * @author wheat
 * date: 2015-3-16  
 * time: ����11:59:22
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
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.os.AsyncTask;
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
 * date: 2015-3-16  
 * time: ����11:59:22
 */
public class FragmentFollow extends Fragment implements OnScrollListener
{
	private final int PAGE_LENGTH=10;//ÿ����������ҳ������������������
	private PullToRefreshListView mPullToRefreshListView;
	private List<Leaflets> mListData;//����listview�����������
	private ImageLoader mImageLoader;//����ͼƬ�Ķ���
	private LayoutInflater mInflater;
	private FollowRefreshListAdapter adapter;
	
	private boolean isLoadingMore=false;//��ֹ�ظ������첽�����߳�
	private View mFooterView;
	private TextView tvFooterText;
	private ProgressBar pbFooterLoading;
	private ListView mActualListView;//PulltoRefreshListView��������ListView
	
	private DisplayMetrics metric;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//��ȡ�豸��Ϣ
		metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		
		mListData=new ArrayList<Leaflets>();
		mImageLoader=ImageLoader.getInstance(getActivity().getApplicationContext());
		adapter=new FollowRefreshListAdapter();
		
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
	
	private class FollowRefreshListAdapter extends BaseAdapter
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
				convertView=mInflater.inflate(R.layout.fragment_follow_list_item, null);
				holder.ivSellerAvatar=(ImageView)convertView.findViewById(R.id.follow_item_seller_avatar);
				holder.tvSellerName=(TextView)convertView.findViewById(R.id.follow_item_seller_name);
				holder.tvPublishTime=(TextView)convertView.findViewById(R.id.follow_item_publish_time);
				holder.tvLeafletType=(TextView)convertView.findViewById(R.id.follow_item_leaflet_type);
				holder.ivLeafletBrief=(ImageView)convertView.findViewById(R.id.follow_item_leaflet_brief);
				holder.tvPraiseTimes=(TextView)convertView.findViewById(R.id.follow_item_praise_times);
				holder.tvCommentTimes=(TextView)convertView.findViewById(R.id.follow_item_comment_times);
				holder.praiseView=convertView.findViewById(R.id.follow_item_praise_area);
				holder.commentView=convertView.findViewById(R.id.follow_item_comment_area);
				
				convertView.setTag(holder);
				
			}
			else
				holder=(ViewHolder)convertView.getTag();
			addTaskToPool(new PhotoParameters(listItem.getLeafletFields().getSellerLogoPath(), 50, 50*50,"secondary"), holder.ivSellerAvatar);
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
					new LoadMoreTask(mListData.size(),mListData.size()+PAGE_LENGTH,"wheat","published").execute();
				}
			}
		});
		
		mPullToRefreshListView.setOnScrollListener(this);
	}
	
	
	/**
	 * 
	 * description:ˢ��ListView���ݵ��첽�߳�
	 * @author wheat
	 * date: 2015-3-7  
	 * time: ����7:12:59
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
//				json=HttpLoaderMethods.flushLeafletData("wheat","published");
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
	 * @param wasLoadNothing ������ɺ��Ƿ�����û������,true��ʾ����û������,false��ʾ����������
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
		long between=(now.getTime()-date.getTime())/1000;//��ʱ��תΪ��
		
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
			return hour+new String("Сʱǰ");
		}
		else if(minute>0)
		{
			return minute+new String("����ǰ");
		}
		else
		{
			return second+new String("��ǰ");
		}
	}
	
	
	
	private int mPhotoWidth=0;
	private int mMinSideLength=0;
	private int mMaxNumOfPixles=0;
	//�Ѿ���ȡ����ȷ��ImageWidth
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
	 * ��סʱ�����ܼ���������ͼƬ��ֻ�ܼ��ع̶�ͼƬ
	 */
	public void lockTaskPool()
	{
		this.allowFix=false;
	}
	
	/**
	 * ��������󣬿��Լ���������ͼƬ
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