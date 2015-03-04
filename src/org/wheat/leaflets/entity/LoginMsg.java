/** 
 * description£º
 * @author wheat
 * date: 2015-3-3  
 * time: ÏÂÎç3:22:16
 */ 
package org.wheat.leaflets.entity;

import com.google.gson.annotations.SerializedName;

/** 
 * description:
 * @author wheat
 * date: 2015-3-3  
 * time: ÏÂÎç3:22:16
 */
public class LoginMsg 
{
	@SerializedName("username")
	private String strEmail;
	
	@SerializedName("password")
	private String strPassword;
	
	public void setEmail(String email)
	{
		this.strEmail=email;
	}
	
	public void setPassword(String password)
	{
		this.strPassword=password;
	}
	
	public String getEmail()
	{
		return this.strEmail;
	}
	
	public String getPassword()
	{
		return this.strPassword;
	}
}

