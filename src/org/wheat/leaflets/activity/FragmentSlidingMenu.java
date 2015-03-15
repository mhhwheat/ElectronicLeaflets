/** 
 * description£º
 * @author wheat
 * date: 2015-3-15  
 * time: ÏÂÎç7:05:48
 */ 
package org.wheat.leaflets.activity;

import org.wheat.electronicleaflets.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/** 
 * description:
 * @author wheat
 * date: 2015-3-15  
 * time: ÏÂÎç7:05:48
 */
public class FragmentSlidingMenu extends Fragment
{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_sliding_menu,null);
		return view;
	}
	
}
