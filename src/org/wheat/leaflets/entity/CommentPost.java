/** 
 * description£º
 * @author wheat
 * date: 2015-3-18  
 * time: ÏÂÎç6:40:50
 */ 
package org.wheat.leaflets.entity;

import com.google.gson.annotations.SerializedName;

/** 
 * description:
 * @author wheat
 * date: 2015-3-18  
 * time: ÏÂÎç6:40:50
 */
public class CommentPost 
{
	@SerializedName("leaflet_id")
	private int leafletId;
	
	@SerializedName("comment_user")
	private String userName;
	
	@SerializedName("comment_time")
	private String commentTime;
	
	@SerializedName("comment_content")
	private String commentContent;

	public int getLeafletId() {
		return leafletId;
	}

	public void setLeafletId(int leafletId) {
		this.leafletId = leafletId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCommentTime() {
		return commentTime;
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
