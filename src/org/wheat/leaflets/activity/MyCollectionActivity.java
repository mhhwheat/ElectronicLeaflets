/** 
 * description：
 * @author wheat
 * date: 2015-4-14  
 * time: 下午9:39:38
 */ 
package org.wheat.leaflets.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.basic.DateTools;
import org.wheat.leaflets.basic.ExitApplication;
import org.wheat.leaflets.data.UserLoginPreference;
import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.entity.LeafletsFields;
import org.wheat.leaflets.entity.PhotoParameters;
import org.wheat.leaflets.entity.ReturnData;
import org.wheat.leaflets.entity.json.LeafletsJson;
import org.wheat.leaflets.loader.HttpLoaderMethods;
import org.wheat.leaflets.loader.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

/** 
 * description:
 * @author wheat
 * date: 2015-4-14  
 * time: 下午9:39:38
 */
public class MyCollectionActivity extends Activity implements OnScrollListener
{
	
	private final int PAGE_LENGTH=10;//每次请求数据页里面包含的最多数据项
	private UserLoginPreference preference;
	private ImageView ivTitleBack;
	
	private ListView mListView;
	private List<ReturnData<LeafletsFields>> mListData;
	private ImageLoader mImageLoader;//加载图片的对象
	private LayoutInflater mInflater;
	private MyCollectionListAdapter adapter;
	
	private DisplayMetrics metric;
	
	private String testUserName="abc@qq.com";
	
	private boolean isLoadingMore=true;//防止重复开启异步加载线程
	private View mFooterView;
	private TextView tvFooterText;
	private ProgressBar pbFooterLoading;
	
	private boolean mLastItemVisible=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
		setContentView(R.layout.activity_my_collection_layout);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_my_collection_title);
		
		//获取设备信息
		metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		
		preference=UserLoginPreference.getInstance(getApplicationContext());
		
		ivTitleBack=(ImageView)findViewById(R.id.my_collection_title_back_img);
		mListView=(ListView)findViewById(R.id.my_colletion_listview);
		mListData=new ArrayList<ReturnData<LeafletsFields>>();
		mImageLoader=ImageLoader.getInstance(getApplicationContext());
		mInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		adapter=new MyCollectionListAdapter();
		
		mFooterView=mInflater.inflate(R.layout.refresh_list_footer, null);
		pbFooterLoading=(ProgressBar)mFooterView.findViewById(R.id.refresh_list_footer_progressbar);
		tvFooterText=(TextView)mFooterView.findViewById(R.id.refresh_list_footer_text);
		
		mListView.setAdapter(adapter);
		mListView.addFooterView(mFooterView);
		mListView.setOnScrollListener(this);
		
		initialListViewListener();
		initialTitleBackListener();
		
		taskPool=new HashMap<String, ImageView>();
		
		if(preference.getLoginState()==UserLoginPreference.NO_USER_LOGIN)
		{
			Toast.makeText(this, "no user Login!", Toast.LENGTH_SHORT).show();
		}
		else if(preference.getLoginState()==UserLoginPreference.SELLER_LOGIN)
		{
			new UpdateDataTask(preference.getSellerPreference().getSellerEmail()).execute();
		}
		else
		{
			new UpdateDataTask(preference.getUserPreference().getUserEmail()).execute();
		}
		
		ExitApplication.getInstance().addActivity(this);
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
		
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE&&!isLoadingMore&&mLastItemVisible)
		{
			isLoadingMore=true;
			pbFooterLoading.setVisibility(View.VISIBLE);
			tvFooterText.setText(R.string.list_footer_loading);
			new LoadMoreTask(mListData.size()+1,mListData.size()+PAGE_LENGTH,testUserName).execute();
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		mLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 1);
		
	}
	
	private class MyCollectionListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			return mListData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ReturnData<LeafletsFields> listItem=mListData.get(position);
			ViewHolder holder=null;
			
			if(convertView==null)
			{
				holder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.activity_my_colletion_list_item, parent,false);
				holder.ivLeafletBrief=(ImageView)convertView.findViewById(R.id.my_collection_brief_leaflet);
				holder.tvSellerName=(TextView)convertView.findViewById(R.id.my_collection_seller_name);
				holder.tvPublishTime=(TextView)convertView.findViewById(R.id.my_collection_publish_time);
				
				convertView.setTag(holder);
			}
			else
				holder=(ViewHolder)convertView.getTag();
			
			if(mPhotoWidth<=0)
			{
				holder.ivLeafletBrief.getViewTreeObserver().addOnGlobalLayoutListener(new GlobalLayoutLinstener(holder.ivLeafletBrief));
			}
			
			addTaskToPool(new PhotoParameters(listItem.getDataFields().getBriefLeafletPath(), mPhotoWidth, 2*mPhotoWidth*mPhotoWidth, true,mPhotoWidth,"primary"), holder.ivLeafletBrief);
			holder.tvSellerName.setText(listItem.getDataFields().getSellerName());
			holder.tvPublishTime.setText(DateTools.getDifferenceFromDate(listItem.getDataFields().getPublishTime()));
			
			return convertView;
		}
		
		private final class ViewHolder
		{
			public ImageView ivLeafletBrief;
			public TextView tvSellerName;
			public TextView tvPublishTime;
		}
		
	}
	
	private void initialTitleBackListener()
	{
		ivTitleBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyCollectionActivity.this.finish();
			}
		});
	}
	
	private void initialListViewListener()
	{
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if(position>mListData.size())
					return;
				ReturnData<LeafletsFields> data=mListData.get(position);
				Intent intent=new Intent(MyCollectionActivity.this,LeafletDetailActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("leaflet_id", data.getPrimaryKey());
				bundle.putString("publish_time", data.getDataFields().getUTCPublishTime());
				bundle.putString("seller_name", data.getDataFields().getSellerName());
				bundle.putString("start_time", data.getDataFields().getUTCStartTime());
				bundle.putString("leaflet_path", data.getDataFields().getLeafletPath());
				bundle.putString("brief_leaflet_path", data.getDataFields().getBriefLeafletPath());
				bundle.putString("end_time", data.getDataFields().getUTCEndTime());
				bundle.putInt("praise_times", data.getDataFields().getPraiseTimes());
				bundle.putString("leaflet_type", data.getDataFields().getLeafletType());
				bundle.putInt("comment_times", data.getDataFields().getCommentTimes());
				bundle.putString("seller_logo_path", data.getDataFields().getSellerLogoPath());
				bundle.putDouble("lat", data.getDataFields().getLat());
				bundle.putDouble("lng", data.getDataFields().getLng());
				bundle.putDouble("distance", data.getDataFields().getDistance());
				bundle.putInt("is_praise", data.getDataFields().isPraise());
				bundle.putString("seller_address", data.getDataFields().getSellerAddress());
				bundle.putString("leaflet_description", data.getDataFields().getLeafletDescription());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		tvFooterText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isLoadingMore)
				{
					isLoadingMore=true;
					pbFooterLoading.setVisibility(View.VISIBLE);
					tvFooterText.setText(R.string.list_footer_loading);
					new LoadMoreTask(mListData.size()+1,mListData.size()+PAGE_LENGTH,testUserName).execute();
				}
			}
		});
	}
	
	private class UpdateDataTask extends AsyncTask<Void, Void, LeafletsJson>
	{
		private String userName;
		
		public UpdateDataTask(String userName)
		{
			this.userName=userName;
		}

		@Override
		protected LeafletsJson doInBackground(Void... params) {
			LeafletsJson json=null;
			try
			{
				json=HttpLoaderMethods.flushMyCollection("abc@qq.com");
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
			onLoadComplete(true);
			super.onPostExecute(result);
		}	
		
	}
	
	private class LoadMoreTask extends AsyncTask<Void, Void, LeafletsJson>
	{
		private int offsetStart;
		private int offsetEnd;
		private String userName;
		
		public LoadMoreTask(int offsetStart,int offsetEnd,String userName)
		{
			super();
			this.offsetStart=offsetStart;
			this.offsetEnd=offsetEnd;
			this.userName=userName;
		}
		
		@Override
		protected LeafletsJson doInBackground(Void... params) {
			LeafletsJson json=null;
			try
			{
				json=HttpLoaderMethods.getMyCollection("abc@qq.com", mListData.size()+1, mListData.size()+PAGE_LENGTH);
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
				if(result.getData()!=null&&result.getData().size()>0)
				{
					synchronized (mListData) {
						mListData.addAll(result.getData());
						adapter.notifyDataSetChanged();
					}
					onLoadComplete(false);
				}
				else
				{
					onLoadComplete(true);
				}
			}
			else if(result!=null&&result.getCode()==ConstantValue.NO_MORE_DATA)
			{
				Toast.makeText(MyCollectionActivity.this, "没有更多内容", Toast.LENGTH_SHORT).show();
				onLoadComplete(true);
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
