/** 
 * description£º
 * @author wheat
 * date: 2015-4-9  
 * time: ÏÂÎç9:53:58
 */ 
package org.wheat.leaflets.entity;

import java.io.Serializable;

/** 
 * description:
 * @author wheat
 * date: 2015-4-9  
 * time: ÏÂÎç9:53:58
 */
public class UserPreference implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String userEmail;
	private String nickName;
	private String userAvatar;
	
	
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUserAvatar() {
		return userAvatar;
	}
	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}
	
	
}
