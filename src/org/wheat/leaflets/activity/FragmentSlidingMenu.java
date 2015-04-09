/** 
 * description£º
 * @author wheat
 * date: 2015-3-15  
 * time: ÏÂÎç7:05:48
 */ 
package org.wheat.leaflets.activity;

import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.basic.ExitApplication;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/** 
 * description:
 * @author wheat
 * date: 2015-3-15  
 * time: ÏÂÎç7:05:48
 */
public class FragmentSlidingMenu extends Fragment
{
//	private OnSlidingMenuItemClickListener listener;
	
	private LayoutInflater mInflater;
	private TextView tvSetting;
	private RelativeLayout llUserInfo;
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
		
		View view=inflater.inflate(R.layout.fragment_sliding_menu,null);
		tvSetting=(TextView)view.findViewById(R.id.sliding_menu_setting);
		llUserInfo=(RelativeLayout)view.findViewById(R.id.sliding_menu_user_info);
		tvLogout=(TextView)view.findViewById(R.id.sliding_menu_logout);
		tvExit=(TextView)view.findViewById(R.id.sliding_menu_exit);
		
		tvExit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				initialDialog();
				mDialog.show();
			}
		});
		
		
		
		
		return view;
	}
	
//	public interface OnSlidingMenuItemClickListener
//	{
//		public void onItemClick(int item);
//	}
//	
//	public void setOnSlidingMenuItemClickListener(OnSlidingMenuItemClickListener listener)
//	{
//		this.listener=listener;
//	}
	
	private void initialDialog()
	{
		mDialog=new Dialog(getActivity(), R.style.ExitDialog);
		View layout=mInflater.inflate(R.layout.dialog_exit, null);
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
	
}
