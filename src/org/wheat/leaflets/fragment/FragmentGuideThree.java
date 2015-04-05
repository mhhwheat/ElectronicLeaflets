/** 
 * description£º
 * @author wheat
 * date: 2015-4-4  
 * time: ÏÂÎç4:08:13
 */ 
package org.wheat.leaflets.fragment;

import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.activity.MainInterfaceActivity;
import org.wheat.leaflets.data.UserLoginPreference;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/** 
 * description:
 * @author wheat
 * date: 2015-4-4  
 * time: ÏÂÎç4:08:13
 */
public class FragmentGuideThree extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =inflater.inflate(R.layout.fragment_guide03, null);
		ImageView image=(ImageView)view.findViewById(R.id.fragment_guide03_login);
		image.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UserLoginPreference preference=UserLoginPreference.getInstance(getActivity());
				preference.firstRun();
				Intent intent=new Intent(getActivity(),MainInterfaceActivity.class);
				startActivity(intent);
			}
		});
		
		return view;
	}
	
	
}
