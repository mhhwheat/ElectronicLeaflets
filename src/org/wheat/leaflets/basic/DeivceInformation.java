/** 
 * description：
 * @author wheat
 * date: 2015-4-6  
 * time: 上午11:46:36
 */ 
package org.wheat.leaflets.basic;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/** 
 * description:
 * @author wheat
 * date: 2015-4-6  
 * time: 上午11:46:36
 */
public class DeivceInformation 
{
	/**
	 * 获取手机设备的mac地址
	 * @param context
	 * @return
	 */
	public static String getLocalMacAddress(Context context) {  
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);  
        WifiInfo info = wifi.getConnectionInfo();  
        return info.getMacAddress();  
    } 
}
