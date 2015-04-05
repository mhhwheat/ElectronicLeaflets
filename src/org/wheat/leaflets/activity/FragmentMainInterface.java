/** 
 * description：
 * @author wheat
 * date: 2015-3-12  
 * time: 下午8:06:56
 */ 
package org.wheat.leaflets.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.adapter.LeafletClassListAdapter;
import org.wheat.leaflets.adapter.MyFollowListAdapter;
import org.wheat.leaflets.adapter.SortingWayListAdapter;
import org.wheat.leaflets.basic.DateTools;
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
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

/** 
 * description:
 * @author wheat
 * date: 2015-3-12  
 * time: 下午8:06:56
 */
public class FragmentMainInterface extends Fragment implements OnScrollListener
{
	private final int PAGE_LENGTH=10;//每次请求数据页里面包含的最多数据项
	private String[] strMyFollow,strLeafletClass,strSortingWay;
	private TextView tvMyFollow,tvLeafletClass,tvSortingWay;
	
	private String userName="wheat";
	
	private PullToRefreshListView mPullToRefreshListView;
	private List<ReturnData<LeafletsFields>> mListData;//保存listview数据项的数组
	private ImageLoader mImageLoader;//加载图片的对象
	private LayoutInflater mInflater;
	private FragmentMainInterfaceListAdapter adapter;
	
	private boolean isLoadingMore=false;//防止重复开启异步加载线程
	private View mFooterView;
	private TextView tvFooterText;
	private ProgressBar pbFooterLoading;
	private ListView mActualListView;//PulltoRefreshListView中真正的ListView
	
	private DisplayMetrics metric;
	
	private PopupWindow pwLeafletClass,pwSortingWay,pwMyFollow;
	
	private ListView lvLeafletClass;
	private ListView lvSortingWay;
	private ListView lvMyFollow;
	

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
		adapter=new FragmentMainInterfaceListAdapter();
		
		new UpdateDataTask("wheat","published").execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater=inflater;
		
		taskPool=new HashMap<String, ImageView>();
		View view=inflater.inflate(R.layout.fragment_main_interface, container,false);
		
		tvMyFollow=(TextView)view.findViewById(R.id.btFragment_main_interface_my_follow);
		tvLeafletClass=(TextView)view.findViewById(R.id.btFragment_main_interface_leaflet_class);
		tvSortingWay=(TextView)view.findViewById(R.id.btFragment_main_interface_sorting_way);
		
		initialPopupWindow();
		
		
		
		mPullToRefreshListView=(PullToRefreshListView)view.findViewById(R.id.fragment_main_interface_refresh_list_view);
		mActualListView=mPullToRefreshListView.getRefreshableView();
		
		mFooterView=mInflater.inflate(R.layout.refresh_list_footer, null);
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
	
	
	
	private class FragmentMainInterfaceListAdapter extends BaseAdapter
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
				convertView=mInflater.inflate(R.layout.fragment_main_interface_list_item, null);
				holder.ivSellerAvatar=(ImageView)convertView.findViewById(R.id.fragment_main_interface_item_seller_avatar);
				holder.tvSellerName=(TextView)convertView.findViewById(R.id.fragment_main_interface_item_seller_name);
				holder.tvPublishTime=(TextView)convertView.findViewById(R.id.fragment_main_interface_item_publish_time);
				holder.tvLeafletType=(TextView)convertView.findViewById(R.id.fragment_main_interface_item_leaflet_type);
				holder.ivLeafletBrief=(ImageView)convertView.findViewById(R.id.fragment_main_interface_item_leaflet_brief);
				holder.tvPraiseTimes=(TextView)convertView.findViewById(R.id.fragment_main_interface_item_praise_times);
				holder.ivPraise=(ImageView)convertView.findViewById(R.id.fragment_main_interface_item_praise_button);
				holder.praiseView=convertView.findViewById(R.id.fragment_main_interface_item_praise_area);
				
				convertView.setTag(holder);
				
				holder.praiseView.setOnClickListener(new PraiseAreaOnClickListenter());
			}
			else
				holder=(ViewHolder)convertView.getTag();
			
			holder.praiseView.setTag(listItem);
			
			if(mPhotoWidth<=0)
			{
				holder.ivLeafletBrief.getViewTreeObserver().addOnGlobalLayoutListener(new GlobalLayoutLinstener(holder.ivLeafletBrief));
			}
			
			addTaskToPool(new PhotoParameters(listItem.getDataFields().getSellerLogoPath(), 50, 50*50,"seller_logo"), holder.ivSellerAvatar);
			holder.tvSellerName.setText(listItem.getDataFields().getSellerName());
			holder.tvPublishTime.setText(DateTools.getDifferenceFromDate(listItem.getDataFields().getPublishTime()));
			holder.tvLeafletType.setText(listItem.getDataFields().getLeafletType());
			addTaskToPool(new PhotoParameters(listItem.getDataFields().getBriefLeafletPath(), mPhotoWidth, 2*mPhotoWidth*mPhotoWidth, true,mPhotoWidth,"secondary"), holder.ivLeafletBrief);
			holder.tvPraiseTimes.setText(String.valueOf(listItem.getDataFields().getPraiseTimes()));
			if(listItem.getDataFields().isPraise()==1)
				holder.ivPraise.setImageResource(R.drawable.praisefull);
			else
				holder.ivPraise.setImageResource(R.drawable.praise);
			
//			int w = View.MeasureSpec.makeMeasureSpec(0,
//	                View.MeasureSpec.UNSPECIFIED);
//	        int h = View.MeasureSpec.makeMeasureSpec(0,
//	                View.MeasureSpec.UNSPECIFIED);
//	        holder.ivLeafletBrief.measure(w, h);
//	        System.out.println("image with="+holder.ivLeafletBrief.getMeasuredHeight());
			
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
			public View praiseView;
			
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
	
	private void initialPopupWindow()
	{
		View leafletClassView=mInflater.inflate(R.layout.fragment_main_interface_leaflet_class_pw, null,false);
		View sortingWayView=mInflater.inflate(R.layout.fragment_main_interface_sorting_way_pw, null, false);
		View myFollowView=mInflater.inflate(R.layout.fragment_main_interface_my_follow_pw, null, false);
		
		pwLeafletClass=new PopupWindow(leafletClassView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		pwSortingWay=new PopupWindow(sortingWayView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		pwMyFollow=new PopupWindow(myFollowView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		
		pwLeafletClass.setAnimationStyle(R.style.popwin_anim_style);
		pwSortingWay.setAnimationStyle(R.style.popwin_anim_style);
		pwMyFollow.setAnimationStyle(R.style.popwin_anim_style);
		
		leafletClassView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(pwLeafletClass!=null&&pwLeafletClass.isShowing())
				{
					pwLeafletClass.dismiss();
				}
				return false;
			}
		});
		
		// 设置允许在外点击消失
		pwLeafletClass.setOutsideTouchable(true);
		pwSortingWay.setOutsideTouchable(true);
		pwMyFollow.setOutsideTouchable(true);
		
		// 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
		pwLeafletClass.setFocusable(true);
		pwSortingWay.setFocusable(true);
		pwMyFollow.setFocusable(true);
		
		//如果需要PopupWindow响应返回键，那么必须给PopupWindow设置一个背景才行
		ColorDrawable dw = new ColorDrawable(0X50000000);
		pwLeafletClass.setBackgroundDrawable(dw);
		pwSortingWay.setBackgroundDrawable(dw);
		pwMyFollow.setBackgroundDrawable(dw);
		
		lvLeafletClass=(ListView)leafletClassView.findViewById(R.id.leaflet_class_pw_listview);
		lvMyFollow=(ListView)myFollowView.findViewById(R.id.my_follow_pw_listview);
		lvSortingWay=(ListView)sortingWayView.findViewById(R.id.sorting_way_pw_listview);
		
		Resources res=getResources();
		strLeafletClass=res.getStringArray(R.array.leaflet_class_string_array);
		strMyFollow=res.getStringArray(R.array.my_follow_string_array);
		strSortingWay=res.getStringArray(R.array.sorting_way_string_array);
		
		lvLeafletClass.setAdapter(new LeafletClassListAdapter(strLeafletClass,mInflater));
		lvSortingWay.setAdapter(new SortingWayListAdapter(strSortingWay, mInflater));
		lvMyFollow.setAdapter(new MyFollowListAdapter(strMyFollow,mInflater));
		
		lvLeafletClass.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parrent, View view, int position,
					long id) {
				TextView tv=(TextView)view.findViewById(R.id.leaflet_class_list_item_text);
				tvLeafletClass.setText(tv.getText().toString());
				((LeafletClassListAdapter)lvLeafletClass.getAdapter()).setSelectedItemIndex(position);
				pwLeafletClass.dismiss();
			}
		});
		
		lvSortingWay.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parrent, View view, int position,
					long id) {
				TextView tv=(TextView)view.findViewById(R.id.sorting_way_list_item_text);
				tvSortingWay.setText(tv.getText().toString());
				((SortingWayListAdapter)lvSortingWay.getAdapter()).setmSelectedItemIndex(position);
				pwSortingWay.dismiss();
			}
		});
		
		lvMyFollow.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parrent, View view, int position,
					long id) {
				TextView tv=(TextView)view.findViewById(R.id.my_follow_list_item_text);
				tvMyFollow.setText(tv.getText().toString());
				((MyFollowListAdapter)lvMyFollow.getAdapter()).setmSelectedItemIndex(position);
				pwMyFollow.dismiss();
			}
		});
		
		
		
		tvLeafletClass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(pwLeafletClass.isShowing())
				{
					pwLeafletClass.dismiss();
				}else
				{
					pwLeafletClass.showAsDropDown(v);
				}
			}
		});
		
		tvSortingWay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(pwSortingWay.isShowing())
				{
					pwSortingWay.dismiss();
				}else
				{
					pwSortingWay.showAsDropDown(v);
				}
			}
		});
		
		tvMyFollow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(pwMyFollow.isShowing())
				{
					pwMyFollow.dismiss();
				}else
				{
					pwMyFollow.showAsDropDown(v);
				}
			}
		});
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
	
	
//	private class CommentAreaOnClickListener implements OnClickListener
//	{
//		private ReturnData<LeafletsFields> listItem;
//		@Override
//		public void onClick(View v) {
//			listItem=(ReturnData<LeafletsFields>)v.getTag();
//			
//			Intent intent=new Intent(getActivity(),CommentContentActivity.class);
//			Bundle bundle=new Bundle();
//			bundle.putString("user_name", userName);
//			bundle.putInt("leaflet_id", listItem.getPrimaryKey());
//			bundle.putInt("comment_count", listItem.getDataFields().getCommentTimes());
//			intent.putExtras(bundle);
//			startActivityForResult(intent, 1);
//		}
//		
//	}
	
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
			else
			{
				leaflet.getDataFields().setPraise(0);
				leaflet.getDataFields().setPraiseTimes(leaflet.getDataFields().getPraiseTimes()-1);
				adapter.notifyDataSetChanged();
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						try{
							int returnCode=HttpUploadMethods.removePraiseRecord(leaflet.getPrimaryKey(), userName);
							System.out.println("return code="+returnCode);
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
