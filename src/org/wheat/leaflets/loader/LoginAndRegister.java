/** 
 * description£º
 * @author wheat
 * date: 2015-3-3  
 * time: ÏÂÎç5:04:52
 */ 
package org.wheat.leaflets.loader;

import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.entity.LoginMsg;
import org.wheat.leaflets.entity.json.LoginMsgJson;
import org.wheat.leaflets.httptools.HttpConnectTools;
import org.wheat.leaflets.httptools.JsonTools;

import android.util.Log;

/** 
 * description:
 * @author wheat
 * date: 2015-3-3  
 * time: ÏÂÎç5:04:52
 */
public class LoginAndRegister 
{
	public static LoginMsgJson synLogin(String strEmail,String strPassword) throws Throwable
	{
		LoginMsg loginMsg=new LoginMsg();
		loginMsg.setEmail(strEmail);
		loginMsg.setPassword(strPassword);
		
		LoginMsgJson loginMsgjson=new LoginMsgJson();
		loginMsgjson.setData(loginMsg);
		
		Log.d("LoginActivity", JsonTools.toJson(loginMsgjson));
		
		byte[] data=JsonTools.toJson(loginMsgjson).getBytes();
		
		String json=HttpConnectTools.post(ConstantValue.HttpRoot+"login/", data, null);
		Log.d("LoginActivity", json);
		return JsonTools.fromJson(json, LoginMsgJson.class);
	}
	
	public static void login() throws Throwable
	{
		String json=HttpConnectTools.get(ConstantValue.HttpRoot+"login?username=wheat&pwd=lkasldf", null);
		Log.d("LoginActivity", json);
	}
}
