/** 
 * description：
 * @author wheat
 * date: 2015-3-5  
 * time: 下午1:09:59
 */ 
package org.wheat.leaflets.activity;

import java.util.List;

import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.entity.Leaflets;
import org.wheat.leaflets.loader.ImageLoader;
import org.wheat.leaflets.widget.CircleImageView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * description:
 * @author wheat
 * date: 2015-3-5  
 * time: 下午1:09:59
 */
public class FragmentNeighbor extends Fragment
{
	private final int PAGE_LENGTH=10;//每次请求数据页里面包含的最多数据项
	private PullToRefreshListView mPullToRefreshListView;
	private List<Leaflets> mListData;//保存listview数据项的数组
	private ImageLoader mImageLoader;//加载图片的对象
	private LayoutInflater mInflater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater=inflater;
		
		View view=inflater.inflate(R.layout.fragment_neighbor, container,false);
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	
	private class NeighborListAdapter extends BaseAdapter
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
				convertView=mInflater.inflate(R.layout.fragment_neighbor_list_item, null);
				holder.ivSellerAvatar=(CircleImageView)convertView.findViewById(R.id.neighbor_item_seller_avatar);
				holder.tvSellerName=(TextView)convertView.findViewById(R.id.neighbor_item_seller_name);
				holder.tvPublishTime=(TextView)convertView.findViewById(R.id.neighbor_item_publish_time);
				holder.tvLeafletType=(TextView)convertView.findViewById(R.id.neighbor_item_leaflet_type);
				holder.ivLeafletBrief=(ImageView)convertView.findViewById(R.id.neighbor_item_leaflet_brief);
				holder.ivPraiseButton=(ImageView)convertView.findViewById(R.id.neighbor_item_praise_button);
				holder.tvPraiseTimes=(TextView)convertView.findViewById(R.id.neighbor_item_praise_times);
				holder.ivCommentButton=(ImageView)convertView.findViewById(R.id.neighbor_item_comment_button);
				holder.tvCommentTimes=(TextView)convertView.findViewById(R.id.neighbor_item_comment_times);
			}
			return null;
		}
		
		private final class ViewHolder
		{
			public CircleImageView ivSellerAvatar;
			public TextView tvSellerName;
			public TextView tvPublishTime;
			public TextView tvLeafletType;
			public ImageView ivLeafletBrief;
			public ImageView ivPraiseButton;
			public TextView  tvPraiseTimes;
			public ImageView ivCommentButton;
			public TextView  tvCommentTimes;
			
		}
		
	}
	
}
