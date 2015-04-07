/** 
 * description：
 * @author wheat
 * date: 2015-3-20  
 * time: 下午7:14:12
 */ 
package org.wheat.leaflets.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.basic.DateTools;
import org.wheat.leaflets.basic.UTCtoLocal;
import org.wheat.leaflets.data.UserLoginPreference;
import org.wheat.leaflets.entity.CommentGetFields;
import org.wheat.leaflets.entity.CommentPost;
import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.entity.LeafletsFields;
import org.wheat.leaflets.entity.PhotoParameters;
import org.wheat.leaflets.entity.ReturnData;
import org.wheat.leaflets.entity.json.CommentGetJson;
import org.wheat.leaflets.entity.json.CommentPostJson;
import org.wheat.leaflets.loader.HttpLoaderMethods;
import org.wheat.leaflets.loader.HttpUploadMethods;
import org.wheat.leaflets.loader.ImageLoader;
import org.wheat.leaflets.widget.CircleImageView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/** 
 * description:
 * @author wheat
 * date: 2015-3-20  
 * time: 下午7:14:12
 */
public class LeafletDetailActivity extends Activity
{
	private final int PAGE_LENGTH=20;//每次请求数据页里面包含的最多数据项
	private UserLoginPreference preference;
	private LayoutInflater mInflater;
	private DisplayMetrics metric;
	
	private ReturnData<LeafletsFields> mLeaflet;
	private String userName="abc@qq.com";
	private String userNickName="wheat";
	private int newCommentCount=0;//新增评论的条数
	
	private ListView mListView;
	private List<ReturnData<CommentGetFields>> mListData;
	private CommentContentListAdapter adapter;
	private ImageLoader mImageLoader;
	
	private View mHeaderView;
	private TextView tvSellerName;
	private TextView tvSellerAddress;
	private ImageView ivDetailLeaflet;
	private TextView tvLeafletLifeTime;
	private TextView tvLeafletDescription;
	private EditText etLeafletComment;
	private TextView tvCommentButton;
	private TextView tvCommentTimes;
	
//	private boolean isLoadingMore=false;//防止重复开启异步加载线程
//	private View mFooterView;
//	private TextView tvFooterText;
//	private ProgressBar pbFooterLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_leaflet_detail);
		mInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//获取设备信息
		metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		
		preference=UserLoginPreference.getInstance(getApplicationContext());
		mImageLoader=ImageLoader.getInstance(this);
		
		mListView=(ListView)findViewById(R.id.activity_leaflet_detail_refresh_list);
		mListData=new ArrayList<ReturnData<CommentGetFields>>();
		adapter=new CommentContentListAdapter();
		mListView.setAdapter(adapter);
		
		initialHeaderView();
		mListView.addHeaderView(mHeaderView);
		
		new UpdateDataTask("abc@qq.com", mLeaflet.getPrimaryKey()).execute();
		
	}
	
	/**
	 * 初始化HeaderView
	 */
	private void initialHeaderView()
	{
		mHeaderView=mInflater.inflate(R.layout.activity_leaflet_detail_header, null);
		mLeaflet=getDataFromLastActivity();
		tvSellerName=(TextView)mHeaderView.findViewById(R.id.leaflet_detail_seller_name);
		tvSellerAddress=(TextView)mHeaderView.findViewById(R.id.leaflet_detail_seller_address);
		ivDetailLeaflet=(ImageView)mHeaderView.findViewById(R.id.leaflet_detail_leaflet_img);
		tvLeafletLifeTime=(TextView)mHeaderView.findViewById(R.id.leaflet_detail_activity_time);
		tvLeafletDescription=(TextView)mHeaderView.findViewById(R.id.leaflet_detail_activity_description);
		etLeafletComment=(EditText)mHeaderView.findViewById(R.id.leaflet_detail_activity_edit_comment);
		tvCommentButton=(TextView)mHeaderView.findViewById(R.id.leaflet_detail_activity_comment_button);
		tvCommentTimes=(TextView)mHeaderView.findViewById(R.id.leaflet_detail_activity_comment_times);
		
		tvSellerName.setText(mLeaflet.getDataFields().getSellerName());
		tvSellerAddress.setText(mLeaflet.getDataFields().getSellerAddress());
		tvLeafletLifeTime.setText(DateTools.getStringFromDate(mLeaflet.getDataFields().getStartTime())+"--"+
				DateTools.getStringFromDate(mLeaflet.getDataFields().getEndTime()));
		tvLeafletDescription.setText(mLeaflet.getDataFields().getLeafletDescription());
		tvCommentTimes.setText("("+mLeaflet.getDataFields().getCommentTimes()+")");
		
		etLeafletComment.setTag("false");
		etLeafletComment.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				Log.d("LeafletDetailActivity", "start params"+start);
				Log.d("LeafletDetailActivity", "before params"+before);
				Log.d("LeafletDetailActivity", "count params"+count);
				Log.d("LeafletDetailActivity", s.toString());
				if(((String)etLeafletComment.getTag()).equals("false")&&etLeafletComment.getText().toString().trim().length()>0)
				{
//					tvCommentButton.setClickable(true);
					tvCommentButton.setTextColor(LeafletDetailActivity.this.getResources().getColor(R.color.white));
					tvCommentButton.setBackgroundResource(R.drawable.comment_button_able_background);
					etLeafletComment.setTag("true");
				}
				
				if(((String)etLeafletComment.getTag()).equals("true")&&etLeafletComment.getText().toString().trim().length()==0)
				{
//					tvCommentButton.setClickable(false);
					tvCommentButton.setBackgroundResource(R.drawable.comment_button_unable_background);
					tvCommentButton.setTextColor(LeafletDetailActivity.this.getResources().getColor(R.color.black));
					etLeafletComment.setTag("false");
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		tvCommentButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!etLeafletComment.getText().toString().trim().equals(""))
				{
					CommentPost comment=new CommentPost();
					comment.setCommentContent(etLeafletComment.getText().toString());
					comment.setCommentTime(UTCtoLocal.localDate2UTC());
					comment.setLeafletId(mLeaflet.getPrimaryKey());
					comment.setUserName(userName);
					
					CommentPostJson json=new CommentPostJson();
					json.setData(comment);
					
					tvCommentButton.setBackgroundResource(R.drawable.comment_button_unable_background);
					tvCommentButton.setTextColor(LeafletDetailActivity.this.getResources().getColor(R.color.black));
					etLeafletComment.setTag("false");
					etLeafletComment.setText("");
					
					new PostCommentTask(json).execute();
				}
			}
		});
		
	}
	
	private ReturnData<LeafletsFields> getDataFromLastActivity()
	{
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		ReturnData<LeafletsFields> leaflet=new ReturnData<LeafletsFields>();
		if(bundle!=null)
		{
			leaflet.setPrimaryKey(bundle.getInt("leaflet_id"));
			LeafletsFields data=new LeafletsFields();
			data.setPublishTime(bundle.getString("publish_time"));
			data.setSellerName(bundle.getString("seller_name"));
			data.setStartTime(bundle.getString("start_time"));
			data.setLeafletPath(bundle.getString("leaflet_path"));
			data.setBriefLeafletPath(bundle.getString("brief_leaflet_path"));
			data.setEndTime(bundle.getString("end_time"));
			data.setPraiseTimes(bundle.getInt("praise_times"));
			data.setLeafletType(bundle.getString("leaflet_type"));
			data.setCommentTimes(bundle.getInt("comment_times"));
			data.setSellerLogoPath(bundle.getString("seller_logo_path"));
			data.setLat(bundle.getDouble("lat"));
			data.setLng(bundle.getDouble("lng"));
			data.setDistance(bundle.getDouble("distance"));
			data.setPraise(bundle.getInt("is_praise"));
			data.setSellerAddress(bundle.getString("seller_address"));
			data.setLeafletDescription(bundle.getString("leaflet_description"));
			leaflet.setDataFields(data);
		}
		
		return leaflet;
	}
	
	
	public class CommentContentListAdapter extends BaseAdapter
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
			return mListData.size()-position-1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ReturnData<CommentGetFields>comment=mListData.get(position);
			ViewHolder holder=null;
			if(convertView==null)
			{
				holder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.activity_comment_content_list_item, null);
				holder.ivUserAvatar=(CircleImageView)convertView.findViewById(R.id.comment_content_user_avatar);
				holder.tvUserNickName=(TextView)convertView.findViewById(R.id.comment_content_user_nike_name);
				holder.tvCommentTime=(TextView)convertView.findViewById(R.id.comment_content_comment_time);
				holder.tvCommentContent=(TextView)convertView.findViewById(R.id.comment_content_comment_content);
				convertView.setTag(holder);
			}
			else
				holder=(ViewHolder)convertView.getTag();
			if(comment.getDataFields().getUserAvatar()==null)
			{
				holder.ivUserAvatar.setImageResource(R.drawable.liu);
			}
			else
			{
				mImageLoader.addTask(new PhotoParameters(comment.getDataFields().getUserAvatar(), holder.ivUserAvatar.getWidth(), holder.ivUserAvatar.getWidth()*holder.ivUserAvatar.getHeight(),"user_portrait"), holder.ivUserAvatar);
			}
			holder.tvUserNickName.setText(comment.getDataFields().getUserNickName());
			holder.tvCommentTime.setText(DateTools.getDifferenceFromDate(comment.getDataFields().getCommentTime()));
			holder.tvCommentContent.setText(comment.getDataFields().getCommentContent());
			
			return convertView;
		}
		
		private final class ViewHolder
		{
			public CircleImageView ivUserAvatar;
			public TextView tvUserNickName;
			public TextView tvCommentTime;
			public TextView tvCommentContent;
		}
		
	}
	
	
	private class PostCommentTask extends AsyncTask<Void, Void, Integer>
	{
		private CommentPostJson comment;
		
		public PostCommentTask(CommentPostJson comment)
		{
			this.comment=comment;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			CommentPostJson json=null;
			try {
				json=HttpUploadMethods.postCommentPost(comment);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(json==null)
				return 0;
			else
				return json.getCode();
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result==ConstantValue.operateSuccess)
			{
				ReturnData<CommentGetFields> commentGet=new ReturnData<CommentGetFields>();
				CommentGetFields fields=new CommentGetFields();
				fields.setCommentContent(comment.getData().getCommentContent());
				fields.setCommentTime(comment.getData().getCommentTime());
				fields.setLeafletID(mLeaflet.getPrimaryKey());
				fields.setUserAvatar(preference.getUserAvatar());
				fields.setUserName(userName);
				fields.setUserNickName(userNickName);
				commentGet.setDataFields(fields);
				mListData.add(commentGet);
				adapter.notifyDataSetChanged();
				
			}
			else if(result==ConstantValue.commentRepeat)
			{
				Toast.makeText(LeafletDetailActivity.this, "不能多次评论", Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
	}
	
	private class UpdateDataTask extends AsyncTask<Void, Void, CommentGetJson>
	{
		private int leafletId;
		private String userName;
		
		public UpdateDataTask(String userName,int leafletId)
		{
			this.leafletId=leafletId;
			this.userName=userName;
		}
		@Override
		protected CommentGetJson doInBackground(Void... params) {
			CommentGetJson json=null;
			try {
				json=HttpLoaderMethods.getCommentContent(this.userName,this.leafletId);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(CommentGetJson result) {
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
			
//			if(result==null)
//				onLoadComplete(true);
//			else
//				onLoadComplete(false);
//			super.onPostExecute(result);
		}	
	}
	
//	private class LoadMoreTask extends AsyncTask<Void, Void, CommentGetJson>
//	{
//		private int offsetStart;
//		private int offsetEnd;
//		private String userName;
//		
//		public LoadMoreTask(int offsetStart,int offsetEnd,String userName)
//		{
//			super();
//			this.offsetStart=offsetStart;
//			this.offsetEnd=offsetEnd;
//			this.userName=userName;
//		}
//		
//		@Override
//		protected CommentGetJson doInBackground(Void... params) {
//			CommentGetJson json=null;
//			try
//			{
//				json=HttpLoaderMethods.getCommentContent(userName,offsetStart, offsetEnd);
//			}catch(Throwable e)
//			{
//				e.printStackTrace();
//			}
//			return json;
//		}
//
//		@Override
//		protected void onPostExecute(CommentGetJson result) {
//			if(result!=null&&result.getCode()==1000)
//			{
//				synchronized (mListData) {
//					mListData.addAll(result.getData());
//					adapter.notifyDataSetChanged();
//				}
//				onLoadComplete(false);
//			}
//			else
//				onLoadComplete(true);
//			super.onPostExecute(result);
//		}
//	}
//	
//	/**
//	 * 
//	 * @param wasLoadNothing 加载完成后，是否内容没有增加,true表示内容没有增加,false表示内容增加了
//	 */
//	private void onLoadComplete(boolean wasLoadNothing)
//	{
//		isLoadingMore=false;
//		if(wasLoadNothing)
//		{
//			pbFooterLoading.setVisibility(View.GONE);
//			tvFooterText.setText(R.string.list_footer_no_more);
//		}
//	}
	
	
	
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
