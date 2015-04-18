package org.wheat.leaflets.activity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.basic.DateTools;
import org.wheat.leaflets.basic.ExitApplication;
import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.entity.LeafletsFields;
import org.wheat.leaflets.entity.PhotoParameters;
import org.wheat.leaflets.entity.PraisePost;
import org.wheat.leaflets.entity.ReturnData;
import org.wheat.leaflets.entity.json.LeafletsJson;
import org.wheat.leaflets.entity.json.PraisePostJson;
import org.wheat.leaflets.entity.json.SellerMsgJson;
import org.wheat.leaflets.loader.HttpLoaderMethods;
import org.wheat.leaflets.loader.HttpUploadMethods;
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
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/** 
 * description：
 * @author wheat
 * date: 2015-4-13  
 * time: 下午1:06:41
 */

/** 
 * description:
 * @author wheat
 * date: 2015-4-13  
 * time: 下午1:06:41
 */
public class SellerMessageActivity extends Activity
{
	
	private LayoutInflater mInflater;
	private DisplayMetrics metric;
	
	private String userName="abc@qq.com";
	private String userNickName="wheat";
	private String sellerEmail="youcome@qq.com";
	
	private ListView mListView;
	private SellerMsgJson sellerMsg;
	private List<ReturnData<LeafletsFields>> mListData;
	private SellerMsgListAdapter adapter;
	private ImageLoader mImageLoader;
	
	private ImageView ivTitleBack;
	
	private View mHeaderView;
	private TextView tvSellerName;
	private TextView tvSellerAddress;
	private TextView tvSellerTel;
	private TextView tvSellerEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
		setContentView(R.layout.activity_seller_msg);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_seller_msg_title);
		
		mInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//获取设备信息
		metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mImageLoader=ImageLoader.getInstance(this);
		
		mListView=(ListView)findViewById(R.id.activity_seller_list);
		mListData=new ArrayList<ReturnData<LeafletsFields>>();
		adapter=new SellerMsgListAdapter();
		mListView.setAdapter(adapter);
		
		getSellerEmailFromIntent();
		initialHeaderView();
		mListView.addHeaderView(mHeaderView);
		
		ivTitleBack=(ImageView)findViewById(R.id.seller_msg_title_back_img);
		initialTitleBackListener();
		
		taskPool=new HashMap<String, ImageView>();
		
		new UpdateDataTask(sellerMsg.getData().getUserName()).execute();
		
		ExitApplication.getInstance().addActivity(this);
	}
	
	private void getSellerEmailFromIntent()
	{
		Intent intent=getIntent();
		sellerEmail= intent.getStringExtra("seller_email");
	}
	
	private void initialHeaderView()
	{
		mHeaderView=mInflater.inflate(R.layout.activity_seller_msg_header, mListView,false);
		tvSellerAddress=(TextView)mHeaderView.findViewById(R.id.seller_msg_seller_address);
		tvSellerEmail=(TextView)mHeaderView.findViewById(R.id.seller_msg_mail);
		tvSellerName=(TextView)mHeaderView.findViewById(R.id.seller_msg_seller_name);
		tvSellerTel=(TextView)mHeaderView.findViewById(R.id.seller_msg_tel);
		
		new GetSellerMsgTask().execute();
	}
	
	private void initialTitleBackListener()
	{
		ivTitleBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SellerMessageActivity.this.finish();
			}
		});
	}
	
	private class SellerMsgListAdapter extends BaseAdapter
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
				convertView=mInflater.inflate(R.layout.activity_seller_msg_list_item, parent,false);
				holder.ivSellerAvatar=(ImageView)convertView.findViewById(R.id.seller_msg_item_seller_avatar);
				holder.tvSellerName=(TextView)convertView.findViewById(R.id.seller_msg_item_seller_name);
				holder.tvPublishTime=(TextView)convertView.findViewById(R.id.seller_msg_item_publish_time);
				holder.tvLeafletType=(TextView)convertView.findViewById(R.id.seller_msg_item_leaflet_type);
				holder.ivLeafletBrief=(ImageView)convertView.findViewById(R.id.seller_msg_item_leaflet_brief);
				holder.tvPraiseTimes=(TextView)convertView.findViewById(R.id.seller_msg_item_praise_times);
				holder.ivPraise=(ImageView)convertView.findViewById(R.id.seller_msg_item_praise_button);
				holder.praiseView=convertView.findViewById(R.id.seller_msg_item_praise_area);
				
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
			addTaskToPool(new PhotoParameters(listItem.getDataFields().getBriefLeafletPath(), mPhotoWidth, 2*mPhotoWidth*mPhotoWidth, true,mPhotoWidth,"primary"), holder.ivLeafletBrief);
			holder.tvPraiseTimes.setText(String.valueOf(listItem.getDataFields().getPraiseTimes()));
			if(listItem.getDataFields().isPraise()==1)
				holder.ivPraise.setImageResource(R.drawable.praisefull);
			else
				holder.ivPraise.setImageResource(R.drawable.praise);
			
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
	
	private class GetSellerMsgTask extends AsyncTask<Void, Void, SellerMsgJson>
	{

		@Override
		protected SellerMsgJson doInBackground(Void... params) {
			SellerMsgJson json=null;
			try
			{
				json=HttpLoaderMethods.getSellerData(sellerEmail);
			}catch(Throwable e)
			{
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(SellerMsgJson result) {
			super.onPostExecute(result);
			if(result!=null&&result.getCode()==ConstantValue.operateSuccess)
			{
				sellerMsg=result;
				if(!result.getData().getSellerName().equals(""))
				{
					tvSellerName.setText(result.getData().getSellerName());
				}
				
				if(!result.getData().getPhoneNubmer().equals(""))
				{
					tvSellerTel.setText(result.getData().getPhoneNubmer());
				}
				else
				{
					tvSellerTel.setText("未填写");
				}
				
				if(!result.getData().getEmail().equals(""))
				{
					tvSellerEmail.setText("未填写");
				}
			}
				
		}
		
		
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
				json=HttpLoaderMethods.getSellerleaflets(userName);
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
			
			super.onPostExecute(result);
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
