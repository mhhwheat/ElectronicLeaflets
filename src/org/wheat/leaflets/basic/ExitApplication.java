/** 
 * description：
 * @author wheat
 * date: 2015-4-9  
 * time: 下午12:47:32
 */ 
package org.wheat.leaflets.basic;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

/** 
 * description:
 * @author wheat
 * date: 2015-4-9  
 * time: 下午12:47:32
 */
public class ExitApplication extends Application
{
	private List<WeakReference<Activity>> activityList = new LinkedList<WeakReference<Activity>>(); 
	
	private static ExitApplication instance; 
	
	private ExitApplication() { } // 单例模式中获取唯一的ExitApplication实例 
	
	public static ExitApplication getInstance() 
	{ 
		if (null == instance) 
		{ 
			instance = new ExitApplication(); 
		} 
		return instance; 
	} 
	
	// 添加Activity到容器中 
	public void addActivity(Activity activity) 
	{ 
		activityList.add(new WeakReference<Activity>(activity)); 
	} 
	
	// 遍历所有Activity并finish 
	public void exit() 
	{ 
		for (WeakReference<Activity> activity : activityList) 
		{ 
			if(activity.get()!=null)
			{
				activity.get().finish(); 
			}
		} 
		System.exit(0); 
	}
}
