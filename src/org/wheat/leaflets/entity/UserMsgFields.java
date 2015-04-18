/** 
 * description£º
 * @author wheat
 * date: 2015-4-11  
 * time: ÏÂÎç2:59:23
 */ 
package org.wheat.leaflets.entity;

import com.google.gson.annotations.SerializedName;

/** 
 * description:
 * @author wheat
 * date: 2015-4-11  
 * time: ÏÂÎç2:59:23
 */
public class UserMsgFields 
{
	@SerializedName("username")
	private String userName;
	
	@SerializedName("phone_number")
	private String phoneNumber;
	
	@SerializedName("lat")
	private int lat;
	
	@SerializedName("portrait")
	private String userAvatar;
	
	@SerializedName("lng")
	private int lng;
	
	@SerializedName("nickname")
	private String nickName;
	
	@SerializedName("email")
	private String email;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getLat() {
		return lat;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public int getLng() {
		return lng;
	}

	public void setLng(int lng) {
		this.lng = lng;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
