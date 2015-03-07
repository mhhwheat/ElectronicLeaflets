/** 
 * description£º
 * @author wheat
 * date: 2015-3-4  
 * time: ÏÂÎç7:20:29
 */ 
package org.wheat.leaflets.entity;

import com.google.gson.annotations.SerializedName;

/** 
 * description:
 * @author wheat
 * date: 2015-3-4  
 * time: ÏÂÎç7:20:29
 */
public class RegisterMsg 
{
	@SerializedName("username")
	private String strEmail;
	
	@SerializedName("password")
	private String strPassword;
	
	@SerializedName("nickname")
	private String strNickname;
	
	public void setEmail(String email)
	{
		this.strEmail=email;
	}
	
	public String getEmail()
	{
		return this.strEmail;
	}
	
	public void setPassword(String password)
	{
		this.strPassword=password;
	}
	
	public String getPassword()
	{
		return this.strPassword;
	}
	
	public void setNickname(String nickname)
	{
		this.strNickname=nickname;
	}
	
	public String getNickname()
	{
		return this.strNickname;
	}
	
}
