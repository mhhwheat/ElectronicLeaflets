/** 
 * description：
 * @author wheat
 * date: 2015-3-23  
 * time: 下午12:22:02
 */ 
package org.wheat.leaflets.basic;

import java.text.SimpleDateFormat;
import java.util.Date;

/** 
 * description:
 * @author wheat
 * date: 2015-3-23  
 * time: 下午12:22:02
 */
public class DateTools 
{
	public static String getDifferenceFromDate(Date date)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
	
	public static String getStringFromDate(Date date)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(date);
	}
}
