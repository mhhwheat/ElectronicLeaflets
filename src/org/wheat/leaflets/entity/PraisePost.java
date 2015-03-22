/** 
 * description£º
 * @author wheat
 * date: 2015-3-19  
 * time: ÏÂÎç7:37:17
 */ 
package org.wheat.leaflets.entity;

import com.google.gson.annotations.SerializedName;

/** 
 * description:
 * @author wheat
 * date: 2015-3-19  
 * time: ÏÂÎç7:37:17
 */
public class PraisePost 
{
	@SerializedName("leaflet_id")
	private int leafletId;
	
	@SerializedName("username")
	private String userName;

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
	
	
}
