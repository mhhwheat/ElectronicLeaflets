/** 
 * description：
 * @author wheat
 * date: 2015-3-3  
 * time: 下午5:04:52
 */ 
package org.wheat.leaflets.loader;

import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.entity.LoginMsg;
import org.wheat.leaflets.entity.RegisterMsg;
import org.wheat.leaflets.entity.RegisterSellerMsg;
import org.wheat.leaflets.entity.UserName;
import org.wheat.leaflets.entity.json.LoginMsgJson;
import org.wheat.leaflets.entity.json.RegisterMsgJson;
import org.wheat.leaflets.entity.json.RegisterSellerMsgJson;
import org.wheat.leaflets.entity.json.UserNameJson;
import org.wheat.leaflets.httptools.HttpConnectTools;
import org.wheat.leaflets.httptools.JsonTools;

import android.util.Log;

/** 
 * description:
 * @author wheat
 * date: 2015-3-3  
 * time: 下午5:04:52
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
		
		String json=HttpConnectTools.post(ConstantValue.HttpRoot+"login", data, null);
		Log.d("LoginAndRegister", json);
		return JsonTools.fromJson(json, LoginMsgJson.class);
	}
	
	public static RegisterMsgJson synRegister(String strEmail,String strPassword,String strNickname) throws Throwable
	{
		RegisterMsg registerMsg=new RegisterMsg();
		registerMsg.setEmail(strEmail);
		registerMsg.setPassword(strPassword);
		registerMsg.setNickname(strNickname);
		
		RegisterMsgJson registerMsgJson=new RegisterMsgJson();
		registerMsgJson.setData(registerMsg);
		
		Log.d("LoginAndRegister", JsonTools.toJson(registerMsgJson));
		
		byte[] data=JsonTools.toJson(registerMsgJson).getBytes();
		
		String json=HttpConnectTools.post(ConstantValue.HttpRoot+"user_register", data, null);
		Log.d("LoginAndRegister", json);
		return JsonTools.fromJson(json, RegisterMsgJson.class);
	}
	
	public static RegisterSellerMsgJson synRegisterSeller(String strEmail,String strPassword,String strSellerName) throws Throwable
	{
		RegisterSellerMsg registerSellerMsg=new RegisterSellerMsg();
		registerSellerMsg.setEmail(strEmail);
		registerSellerMsg.setPassword(strPassword);
		registerSellerMsg.setSellerName(strSellerName);
		
		RegisterSellerMsgJson registerSellerMsgJson=new RegisterSellerMsgJson();
		registerSellerMsgJson.setData(registerSellerMsg);
		
		byte[] data=JsonTools.toJson(registerSellerMsgJson).getBytes();
		String json=HttpConnectTools.post(ConstantValue.HttpRoot+"seller_register", data, null);
		return JsonTools.fromJson(json, RegisterSellerMsgJson.class);
		
	}
	
	/**
	 * 检查输入的用户名是否已经存在
	 * @param strUserName
	 * @return
	 * @throws Throwable
	 */
	public static UserNameJson synCheckRegisterName(String strUserName) throws Throwable
	{
		UserName userName=new UserName();
		userName.setUserName(strUserName);
		
		UserNameJson userNameJson=new UserNameJson();
		userNameJson.setData(userName);
		
		byte[] data=JsonTools.toJson(userNameJson).getBytes();
		String json=HttpConnectTools.post(ConstantValue.HttpRoot+"check_register_name", data, null);
		Log.d("LoginAndRegister", json);
		return JsonTools.fromJson(json, UserNameJson.class);
	}
	
	
}
