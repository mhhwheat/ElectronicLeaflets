/** 
 * description：
 * @author wheat
 * date: 2015-3-17  
 * time: 下午1:45:59
 */ 
package org.wheat.leaflets.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.data.UserLoginPreference;
import org.wheat.leaflets.entity.CommentGetFields;
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
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/** 
 * description:显示用户评论的activiyt
 * @author wheat
 * date: 2015-3-17  
 * time: 下午1:45:59
 */
public class CommentContentActivity extends Activity implements OnScrollListener
{
	private final int PAGE_LENGTH=20;
	private UserLoginPreference preference;
	
	private int leafletId;
	private String userName;
	private int newCommentCount=0;//新增评论的条数
	private int commentCount;

	private ListView mListView;
	private List<ReturnData<CommentGetFields>> mListData;
	private CommentContentListAdapter adapter;
	private ImageLoader mImageLoader;
	
	private LayoutInflater mInflater;
	
	private TextView tvCommentContentBack;
	private TextView tvCommentContentComment;
	
	private boolean isLoadingMore=false;//防止重复开启异步加载线程
	private View mFooterView;
	private TextView tvFooterText;
	private ProgressBar pbFooterLoading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
		setContentView(R.layout.activity_comment_content);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_comment_content_title);
		preference=UserLoginPreference.getInstance(getApplicationContext());
		
		mListData=new ArrayList<ReturnData<CommentGetFields>>();
		
		tvCommentContentBack=(TextView)findViewById(R.id.comment_content_back);
		tvCommentContentComment=(TextView)findViewById(R.id.comment_content_comment);
		
		mInflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageLoader=ImageLoader.getInstance(this);
		
		getInfoFromLastActivity();
		
		mListView=(ListView)findViewById(R.id.comment_content_list_view);
		adapter=new CommentContentListAdapter();
		mListView.setAdapter(adapter);
		
		mFooterView=mInflater.inflate(R.layout.refresh_list_footer, null);
		pbFooterLoading=(ProgressBar)mFooterView.findViewById(R.id.refresh_list_footer_progressbar);
		tvFooterText=(TextView)mFooterView.findViewById(R.id.refresh_list_footer_text);
		
		mListView.addFooterView(mFooterView);
		mListView.setOnScrollListener(this);
		
		initialListener();
		
		new UpdateDataTask(userName,leafletId).execute();
		
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==0&&resultCode==RESULT_OK)
		{
			newCommentCount++;
			
			Bundle bundle=data.getExtras();
			String content=bundle.getString("comment_content");
			if(content!=null&&!content.equals(""))
			{
				ReturnData<CommentGetFields> returnData=new ReturnData<CommentGetFields>();
				CommentGetFields comment=new CommentGetFields();
				comment.setCommentContent(content);
				comment.setLeafletID(leafletId);
				comment.setCommentTime(bundle.getString("comment_time"));
				comment.setUserNickName(userName);
				comment.setUserAvatar(preference.getUserAvatar());
				
				returnData.setPrimaryKey(leafletId);
				returnData.setDataFields(comment);
				synchronized (mListData)
				{
					mListData.add(returnData);
					adapter.notifyDataSetChanged();
				}
			}
		}
	}



	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if((firstVisibleItem+visibleItemCount-1)>=totalItemCount&&!isLoadingMore)
		{
			isLoadingMore=true;
			new LoadMoreTask(totalItemCount+1, totalItemCount+PAGE_LENGTH, userName).execute();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
	
	private void initialListener()
	{
		tvCommentContentBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.putExtra("new_comment_count", commentCount+newCommentCount);
				intent.putExtra("leaflet_id", leafletId);
				setResult(RESULT_OK, intent);
				CommentContentActivity.this.finish();
			}
		});
		tvCommentContentComment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(CommentContentActivity.this,CommentActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("user_name", userName);
				bundle.putInt("leaflet_id", leafletId);
				intent.putExtras(bundle);
				startActivityForResult(intent, 0);
			}
		});
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
			holder.tvCommentTime.setText(getDifferenceFromDate(comment.getDataFields().getCommentTime()));
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
	
	//从启动该activity的activity传过来的intent对象中获取userName和leafletId
	private void getInfoFromLastActivity()
	{
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		userName=bundle.getString("user_name");
		leafletId=bundle.getInt("leaflet_id");
		commentCount=bundle.getInt("comment_count");

		Log.d("CommentContentActivity", "username="+userName);
	}
	
	/**
	 * 计算当前时间和参数时间的时间差,返回XX秒钟前,XX分钟前,XX小时前,yyyy-MM-dd HH:mm
	 * @param date
	 * @return
	 */
	private String getDifferenceFromDate(Date date)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
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
}
