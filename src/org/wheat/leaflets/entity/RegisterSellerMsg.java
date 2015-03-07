/** 
 * description£º
 * @author wheat
 * date: 2015-3-4  
 * time: ÏÂÎç11:21:45
 */ 
package org.wheat.leaflets.entity;

import com.google.gson.annotations.SerializedName;

/** 
 * description:
 * @author wheat
 * date: 2015-3-4  
 * time: ÏÂÎç11:21:45
 */
public class RegisterSellerMsg 
{
	@SerializedName("username")
	private String strEmail;
	
	@SerializedName("password")
	private String strPassword;
	
	@SerializedName("seller_name")
	private String strSellerName;
	
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
	
	public void setSellerName(String sellerName)
	{
		this.strSellerName=sellerName;
	}
	
	public String getSellerName()
	{
		return this.strSellerName;
	}
}
