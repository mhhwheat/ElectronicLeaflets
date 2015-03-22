
package org.wheat.leaflets.basic;




import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/** 
 * description:把utc时间戳转为本地时间戳
 * @author wheat
 * date: 2015-3-4  
 * time: 下午5:07:16
 */
public class UTCtoLocal 
{
	/**
	 * 
	 * @param utcTime			源时间
	 * @param utcTimePatten		源时间的格式
	 * @param localTimePatten 	返回的当地时间的格式
	 * @return
	 */
	public static String utc2Local(String utcTime,String utcTimePatten,String localTimePatten)
	{
		SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date gpsUTCDate = null;
		try {
			gpsUTCDate = utcFormater.parse(utcTime);
		} catch (ParseException e) {
		e.printStackTrace();
		}
		SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
		localFormater.setTimeZone(TimeZone.getDefault());
		String localTime = localFormater.format(gpsUTCDate.getTime());
		return localTime;
	}
	
	public static String utc2Local(String utcTime,String localTimePatten)
	{
		String utcTimePatten="yyyy-MM-dd'T'HH:mm:ss'Z'";
		SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date gpsUTCDate = null;
		try {
			gpsUTCDate = utcFormater.parse(utcTime);
		} catch (ParseException e) {
		e.printStackTrace();
		}
		SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
		localFormater.setTimeZone(TimeZone.getDefault());
		String localTime = localFormater.format(gpsUTCDate.getTime());
		return localTime;
	}
	
	/**
	 * 默认的utc时间是iso 8601格式，默认要转为当地时间的格式是yyyy-MM-dd HH:mm:ss
	 * @param utcTime
	 * @return
	 */
	public static String utc2Local(String utcTime)
	{
		String utcTimePatten="yyyy-MM-dd'T'HH:mm:ss'Z'";
		String localTimePatten="yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date gpsUTCDate = null;
		try {
			gpsUTCDate = utcFormater.parse(utcTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
		localFormater.setTimeZone(TimeZone.getDefault());
		String localTime = localFormater.format(gpsUTCDate.getTime());
		return localTime;
		
	}
	
	
	
	public static Date utc2LocalDate(String utcTime)
	{
		String utcTimePatten="yyyy-MM-dd'T'HH:mm:ss'Z'";
		SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date gpsUTCDate = null;
		try {
			gpsUTCDate = utcFormater.parse(utcTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return gpsUTCDate;
	}
	
	public static String localDate2UTC(Date date)
	{
		String utcTimePatten="yyyy-MM-dd'T'HH:mm:ss'Z'";
		SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		return utcFormater.format(date);
	}
	
	public static String localDate2UTC()
	{
		String utcTimePatten="yyyy-MM-dd'T'HH:mm:ss'Z'";
		SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		return utcFormater.format(new Date());
	}
	
}
