/** 
 * description£º
 * @author wheat
 * date: 2015-4-4  
 * time: ÏÂÎç3:33:47
 */ 
package org.wheat.leaflets.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/** 
 * description:
 * @author wheat
 * date: 2015-4-4  
 * time: ÏÂÎç3:33:47
 */
public class GuideViewPagerAdapter extends FragmentPagerAdapter
{
	private List<Fragment> fragmentList;
	
	public GuideViewPagerAdapter(FragmentManager fm,List<Fragment> list) {
		super(fm);
		this.fragmentList=list;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragmentList.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragmentList.size();
	}

}
