/** 
 * description：
 * @author wheat
 * date: 2015-3-20  
 * time: 下午7:14:12
 */ 
package org.wheat.leaflets.activity;

import java.util.ArrayList;
import java.util.List;

import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.basic.DateTools;
import org.wheat.leaflets.data.UserLoginPreference;
import org.wheat.leaflets.entity.CommentGetFields;
import org.wheat.leaflets.entity.LeafletsFields;
import org.wheat.leaflets.entity.PhotoParameters;
import org.wheat.leaflets.entity.ReturnData;
import org.wheat.leaflets.entity.json.CommentGetJson;
import org.wheat.leaflets.loader.HttpLoaderMethods;
import org.wheat.leaflets.loader.ImageLoader;
import org.wheat.leaflets.widget.CircleImageView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
	
	private ReturnData<LeafletsFields> mLeaflet;
	private int leafletId;
	private String userName;
	private int newCommentCount=0;//新增评论的条数
	private int commentCount;
	
	private ListView mListView;
	private List<ReturnData<CommentGetFields>> mListData;
	private CommentContentListAdapter adapter;
	private ImageLoader mImageLoader;
	
	private View mHeaderView;
	
	private boolean isLoadingMore=false;//防止重复开启异步加载线程
	private View mFooterView;
	private TextView tvFooterText;
	private ProgressBar pbFooterLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_leaflet_detail);
		mInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			
			mImageLoader.addTask(new PhotoParameters(comment.getDataFields().getUserAvatar(), holder.ivUserAvatar.getWidth(), holder.ivUserAvatar.getWidth()*holder.ivUserAvatar.getHeight(),"user_portrait"), holder.ivUserAvatar);
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
			
			if(result==null)
				onLoadComplete(true);
			else
				onLoadComplete(false);
			super.onPostExecute(result);
		}	
	}
	
	private class LoadMoreTask extends AsyncTask<Void, Void, CommentGetJson>
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
		protected CommentGetJson doInBackground(Void... params) {
			CommentGetJson json=null;
			try
			{
				json=HttpLoaderMethods.getCommentContent(userName,offsetStart, offsetEnd);
			}catch(Throwable e)
			{
				e.printStackTrace();
			}
			return json;
		}

		@Override
		protected void onPostExecute(CommentGetJson result) {
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
	
	
}
