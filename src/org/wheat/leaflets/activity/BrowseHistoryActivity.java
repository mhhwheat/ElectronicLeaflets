/** 
 * description：
 * @author wheat
 * date: 2015-4-9  
 * time: 下午9:27:26
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
import org.wheat.leaflets.entity.BrowseHistoryFields;
import org.wheat.leaflets.entity.PhotoParameters;
import org.wheat.leaflets.entity.ReturnData;
import org.wheat.leaflets.entity.json.BrowseHistoryJson;
import org.wheat.leaflets.loader.HttpLoaderMethods;
import org.wheat.leaflets.loader.ImageLoader;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

/** 
 * description:
 * @author wheat
 * date: 2015-4-9  
 * time: 下午9:27:26
 */
public class BrowseHistoryActivity extends Activity
{
	private ImageView ivTitleBack;
	private UserLoginPreference preference;
	
	private ListView mListView;
	private List<ReturnData<BrowseHistoryFields>> mListData;
	private ImageLoader mImageLoader;//加载图片的对象
	private LayoutInflater mInflater;
	private BrowseHistoryListAdapter adapter;
	
	private DisplayMetrics metric;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
		setContentView(R.layout.activity_browse_history);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_browse_history_title);
		
		//获取设备信息
		metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		
		preference=UserLoginPreference.getInstance(getApplicationContext());
		
		ivTitleBack=(ImageView)findViewById(R.id.browse_history_title_back_img);
		mListView=(ListView)findViewById(R.id.browse_history_listview);
		mListData=new ArrayList<ReturnData<BrowseHistoryFields>>();
		mImageLoader=ImageLoader.getInstance(getApplicationContext());
		mInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		adapter=new BrowseHistoryListAdapter();
		
		mListView.setAdapter(adapter);
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
	
	private class BrowseHistoryListAdapter extends BaseAdapter
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
			final ReturnData<BrowseHistoryFields> listItem=mListData.get(position);
			ViewHolder holder=null;
			
			if(convertView==null)
			{
				holder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.activity_browse_listview_item, null);
				holder.ivLeafletBrief=(ImageView)convertView.findViewById(R.id.browse_history_brief_leaflet);
				holder.tvSellerName=(TextView)convertView.findViewById(R.id.browse_history_seller_name);
				holder.tvBrowseTime=(TextView)convertView.findViewById(R.id.browse_history_browse_time);
				
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
			holder.tvBrowseTime.setText(DateTools.getDifferenceFromDate(listItem.getDataFields().getBrowseTime()));
			
			return convertView;
		}
		
		private final class ViewHolder
		{
			public ImageView ivLeafletBrief;
			public TextView tvSellerName;
			public TextView tvBrowseTime;
		}
		
	}
	
	private void initialTitleBackListener()
	{
		ivTitleBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BrowseHistoryActivity.this.finish();
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
				ReturnData<BrowseHistoryFields> data=mListData.get(position);
				Intent intent=new Intent(BrowseHistoryActivity.this,LeafletDetailActivity.class);
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
	}
	
	private class UpdateDataTask extends AsyncTask<Void, Void, BrowseHistoryJson>
	{
		private String userName;
		
		public UpdateDataTask(String userName)
		{
			this.userName=userName;
		}

		@Override
		protected BrowseHistoryJson doInBackground(Void... params) {
			BrowseHistoryJson json=null;
			try
			{
				json=HttpLoaderMethods.getBrowseHistory(userName);
			}catch(Throwable e)
			{
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(BrowseHistoryJson result) {
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
			
			super.onPostExecute(result);
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
