/** 
 * description£º
 * @author wheat
 * date: 2015-3-5  
 * time: ÏÂÎç1:50:47
 */ 
package org.wheat.leaflets.entity;

import com.google.gson.annotations.SerializedName;

/** 
 * description:
 * @author wheat
 * date: 2015-3-5  
 * time: ÏÂÎç1:50:47
 */
public class UserName 
{
	@SerializedName("username")
	private String strUserName;
	
	public void setUserName(String username)
	{
		this.strUserName=username;
	}
	
	public String getUserName()
	{
		return this.strUserName;
	}
}
