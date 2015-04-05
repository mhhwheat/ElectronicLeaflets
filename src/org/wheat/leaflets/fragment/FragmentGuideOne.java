/** 
 * description£º
 * @author wheat
 * date: 2015-4-4  
 * time: ÏÂÎç3:43:37
 */ 
package org.wheat.leaflets.fragment;

import org.wheat.electronicleaflets.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/** 
 * description:
 * @author wheat
 * date: 2015-4-4  
 * time: ÏÂÎç3:43:37
 */
public class FragmentGuideOne extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =inflater.inflate(R.layout.fragment_guide, null);
		LinearLayout layout=(LinearLayout)view.findViewById(R.id.guidance_linearlayout);
		layout.setBackgroundResource(R.drawable.guidance_new1);
		return view;
	}
	
}
