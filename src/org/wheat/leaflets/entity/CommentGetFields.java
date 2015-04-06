/** 
 * description£º
 * @author wheat
 * date: 2015-3-17  
 * time: ÏÂÎç3:08:44
 */ 
package org.wheat.leaflets.entity;

import java.util.Date;

import org.wheat.leaflets.basic.UTCtoLocal;

import com.google.gson.annotations.SerializedName;

/** 
 * description:
 * @author wheat
 * date: 2015-3-17  
 * time: ÏÂÎç3:08:44
 */
public class CommentGetFields
{
	@SerializedName("leaflet_id")
	private int leafletID;
	
	@SerializedName("user_portrait")
	private String userAvatar;
	
	@SerializedName("comment_user")
	private String userName;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@SerializedName("comment_user_nickname")
	private String userNickName;
	
	@SerializedName("comment_time")
	private String commentTime;
	
	@SerializedName("comment_content")
	private String commentContent;

	public int getLeafletID() {
		return leafletID;
	}

	public void setLeafletID(int leafletID) {
		this.leafletID = leafletID;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	public Date getCommentTime() {
		return UTCtoLocal.utc2LocalDate(this.commentTime);
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	
	
}
