/** 
 * description：
 * @author wheat
 * date: 2015-3-17  
 * time: 下午1:45:59
 */ 
package org.wheat.leaflets.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.entity.CommentGet;
import org.wheat.leaflets.entity.PhotoParameters;
import org.wheat.leaflets.entity.json.CommentGetJson;
import org.wheat.leaflets.loader.ImageLoader;
import org.wheat.leaflets.widget.CircleImageView;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/** 
 * description:显示用户评论的activiyt
 * @author wheat
 * date: 2015-3-17  
 * time: 下午1:45:59
 */
public class CommentContentActivity extends Activity
{

	private ListView mListView;
	private List<CommentGet> mListData;
	private ImageLoader mImageLoader;
	
	private LayoutInflater mInflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
		setContentView(R.layout.activity_main_interface);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_comment_content_title);
		
		mInflater=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageLoader=ImageLoader.getInstance(this);
		mListView=(ListView)findViewById(R.id.comment_content_list_view);
		mListView.setAdapter(new CommentContentListAdapter());
		
		new UpdateDataTask().execute();
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
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final CommentGet comment=mListData.get(position);
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
			
			mImageLoader.addTask(new PhotoParameters(comment.getUserAvatar(), holder.ivUserAvatar.getWidth(), holder.ivUserAvatar.getWidth()*holder.ivUserAvatar.getHeight(),"user_portrait"), holder.ivUserAvatar);
			holder.tvUserNickName.setText(comment.getUserNickName());
			holder.tvCommentTime.setText(getDifferenceFromDate(comment.getCommentTime()));
			holder.tvCommentContent.setText(comment.getCommentContent());
			
			return convertView;
		}
		
		private final class ViewHolder
		{
			public org.wheat.leaflets.widget.CircleImageView ivUserAvatar;
			public TextView tvUserNickName;
			public TextView tvCommentTime;
			public TextView tvCommentContent;
		}
		
	}
	
	
	private class UpdateDataTask extends AsyncTask<Void, Void, CommentGetJson>
	{

		@Override
		protected CommentGetJson doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(CommentGetJson result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
		
		
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
}
