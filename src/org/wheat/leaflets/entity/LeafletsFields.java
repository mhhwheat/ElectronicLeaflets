/** 
 * description：
 * @author wheat
 * date: 2015-3-6  
 * time: 下午7:26:14
 */ 
package org.wheat.leaflets.entity;

import com.google.gson.annotations.SerializedName;

/** 
 * description:
 * @author wheat
 * date: 2015-3-6  
 * time: 下午7:26:14
 */
public class LeafletsFields 
{
	@SerializedName("publish_time")
	private String publishTime;
	
	@SerializedName("seller_name")
	private String sellerName;
	
	/**
	 * 传单活动的开始时间
	 */
	@SerializedName("start_time")
	private String startTime;
	
	/**
	 * 详细传单的路径名
	 */
	@SerializedName("s_leaflet_name")
	private String leafletPath;
	
	/**
	 * 简要传单的路径名
	 */
	@SerializedName("p_leaflet_name")
	private String briefLeafletName;
	
	@SerializedName("end_time")
	private String endTime;
	
	@SerializedName("praise")
	private int praiseTimes;
	
	/**
	 * 传单的类型
	 */
	@SerializedName("type")
	private String leafletType;
	
	

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getLeafletPath() {
		return leafletPath;
	}

	public void setLeafletPath(String leafletPath) {
		this.leafletPath = leafletPath;
	}

	public String getBriefLeafletName() {
		return briefLeafletName;
	}

	public void setBriefLeafletName(String briefLeafletName) {
		this.briefLeafletName = briefLeafletName;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getPraiseTimes() {
		return praiseTimes;
	}

	public void setPraiseTimes(int praiseTimes) {
		this.praiseTimes = praiseTimes;
	}

	public String getLeafletType() {
		return leafletType;
	}

	public void setLeafletType(String leafletType) {
		this.leafletType = leafletType;
	}
	
	
	
	
}
