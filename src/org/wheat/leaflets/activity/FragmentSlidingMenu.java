/** 
 * description£º
 * @author wheat
 * date: 2015-3-15  
 * time: ÏÂÎç7:05:48
 */ 
package org.wheat.leaflets.activity;

import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.basic.ExitApplication;
import org.wheat.leaflets.data.UserLoginPreference;
import org.wheat.leaflets.entity.PhotoParameters;
import org.wheat.leaflets.loader.ImageLoader;
import org.wheat.leaflets.widget.CircleImageView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/** 
 * description:
 * @author wheat
 * date: 2015-3-15  
 * time: ÏÂÎç7:05:48
 */
public class FragmentSlidingMenu extends Fragment
{
//	private OnSlidingMenuItemClickListener listener;
	private UserLoginPreference sharePreference;
	private ImageLoader mImageLoader;
	
	private LayoutInflater mInflater;
	
	private RelativeLayout llUserInfo;
	private CircleImageView civAvatarAndLogo;
	private TextView tvName;
	private TextView tvBrowseHistory;
	private TextView tvMyCollection;
	private TextView tvSetting;
	private TextView tvLogout;
	private TextView tvExit;
	
	private Dialog mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater=inflater;
		sharePreference=UserLoginPreference.getInstance(getActivity().getApplicationContext());
		mImageLoader=ImageLoader.getInstance(getActivity());
		
		View view=inflater.inflate(R.layout.fragment_sliding_menu,null);
		
		llUserInfo=(RelativeLayout)view.findViewById(R.id.sliding_menu_user_info);
		civAvatarAndLogo=(CircleImageView)view.findViewById(R.id.sliding_menu_user_avatar);
		tvName=(TextView)view.findViewById(R.id.sliding_menu_user_nick_name);
		
		tvSetting=(TextView)view.findViewById(R.id.sliding_menu_setting);
		tvBrowseHistory=(TextView)view.findViewById(R.id.sliding_menu_browse_history);
		tvMyCollection=(TextView)view.findViewById(R.id.sliding_menu_my_collection);
		tvLogout=(TextView)view.findViewById(R.id.sliding_menu_logout);
		tvExit=(TextView)view.findViewById(R.id.sliding_menu_exit);
		
		initialMenuItem();
		
		return view;
	}
	
	
	
	@Override
	public void onStart() {
		super.onStart();
		
		if(sharePreference.getLoginState()==UserLoginPreference.USER_LOGIN)
		{
			tvName.setText(sharePreference.getUserPreference().getNickName());
			mImageLoader.addTask(new PhotoParameters(sharePreference.getUserPreference().getUserAvatar(), 60, 60*60, "user_portrait"), civAvatarAndLogo);
		}
		if(sharePreference.getLoginState()==UserLoginPreference.SELLER_LOGIN)
		{
			tvName.setText(sharePreference.getSellerPreference().getSellerName());
			mImageLoader.addTask(new PhotoParameters(sharePreference.getSellerPreference().getSellerLogoPaht(), 60, 60*60, "seller_logo"), civAvatarAndLogo);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		if(resultCode==1)
//		{
//			
//			tvName.setText(sharePreference.getUserPreference().getNickName());
//			mImageLoader.addTask(new PhotoParameters(sharePreference.getUserPreference().getUserAvatar(), 60, 60*60, "user_portrait"), civAvatarAndLogo);
//		}
//		if(resultCode==2)
//		{
//			
//			tvName.setText(sharePreference.getSellerPreference().getSellerName());
//			mImageLoader.addTask(new PhotoParameters(sharePreference.getSellerPreference().getSellerLogoPaht(), 60, 60*60, "seller_logo"), civAvatarAndLogo);
//		}
	}

	private void initialDialog()
	{
		mDialog=new Dialog(getActivity(), R.style.ExitDialog);
//		View layout=mInflater.inflate(R.layout.dialog_exit, null);
		View layout=View.inflate(getActivity(), R.layout.dialog_exit, null);
		Button btConfirm=(Button)layout.findViewById(R.id.dialog_exit_confirm_button);
		Button btCancel=(Button)layout.findViewById(R.id.dialog_exit_cancel_button);
		btCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDialog.cancel();
			}
		});
		btConfirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ExitApplication.getInstance().exit();
			}
		});
		
		mDialog.setContentView(layout);
	}
	
	
	private void initialMenuItem()
	{
		
//		if(sharePreference.getLoginState()==UserLoginPreference.USER_LOGIN)
//		{
//			UserPreference user=sharePreference.getUserPreference();
//			tvName.setText(user.getNickName());
//			mImageLoader.addTask(new PhotoParameters(user.getUserAvatar(), 60, 60*60, "user_portrait"), civAvatarAndLogo);
//			
//		}
//		else if(sharePreference.getLoginState()==UserLoginPreference.SELLER_LOGIN)
//		{
//			SellerPreference seller=sharePreference.getSellerPreference();
//			tvName.setText(seller.getSellerName());
//			mImageLoader.addTask(new PhotoParameters(seller.getSellerLogoPaht(), 60, 60*60, "seller_logo"), civAvatarAndLogo);
//		}
		
		llUserInfo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(sharePreference.getLoginState()==UserLoginPreference.NO_USER_LOGIN)
				{
					Intent intent=new Intent(getActivity(),LoginActivity.class);
					startActivity(intent);
				}
			}
		});

		tvBrowseHistory.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(sharePreference.getLoginState()==UserLoginPreference.NO_USER_LOGIN)
				{
					Intent intent=new Intent(getActivity(),LoginActivity.class);
					startActivity(intent);
				}
				else
				{
					Intent intent=new Intent(getActivity(),BrowseHistoryActivity.class);
					startActivity(intent);
				}
			}
		});
		
		tvMyCollection.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				if(sharePreference.getLoginState()==UserLoginPreference.NO_USER_LOGIN)
				{
					Intent intent=new Intent(getActivity(),LoginActivity.class);
					startActivity(intent);
				}
				else
				{
					Intent intent=new Intent(getActivity(),MyCollectionActivity.class);
					startActivity(intent);
				}
				
			}
		});

		tvLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				switch(sharePreference.getLoginState())
				{
				case UserLoginPreference.NO_USER_LOGIN:
					Toast.makeText(getActivity(), "»¹Ã»µÇÂ¼", Toast.LENGTH_LONG).show();
					break;
				case UserLoginPreference.USER_LOGIN:
					sharePreference.setLoginState(UserLoginPreference.NO_USER_LOGIN);
					civAvatarAndLogo.setImageResource(R.drawable.avatar_default);
					tvName.setText("Î´µÇÂ¼");
					break;
				case UserLoginPreference.SELLER_LOGIN:
					sharePreference.setLoginState(UserLoginPreference.NO_USER_LOGIN);
					civAvatarAndLogo.setImageResource(R.drawable.avatar_default);
					tvName.setText("Î´µÇÂ¼");
					break;
				}
			}
		});

		tvExit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				initialDialog();
				mDialog.show();
			}
		});
	}
	
	
	
}
