/** 
 * description：
 * @author wheat
 * date: 2015-4-10  
 * time: 下午2:49:59
 */ 
package org.wheat.leaflets.entity;

import java.util.Date;

import org.wheat.leaflets.basic.UTCtoLocal;

import com.google.gson.annotations.SerializedName;

/** 
 * description:
 * @author wheat
 * date: 2015-4-10  
 * time: 下午2:49:59
 */
public class BrowseHistoryFields 
{
	@SerializedName("published_time")
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
	private String briefLeafletPath;
	
	@SerializedName("end_time")
	private String endTime;
	
	@SerializedName("praise")
	private int praiseTimes;
	
	/**
	 * 传单的类型
	 */
	@SerializedName("type")
	private String leafletType;
	
	@SerializedName("comment")
	private int commentTimes;
	
	@SerializedName("seller_logo_name")
	private String sellerLogoPath;
	
	/**
	 * 坐标
	 */
	@SerializedName("lat")
	private double lat;
	
	/**
	 * 坐标
	 */
	@SerializedName("lng")
	private double lng;
	
	/**
	 * 该传单的活动地点与用户之间的距离
	 */
	@SerializedName("distance")
	private double distance;
	
	@SerializedName("is_praise")
	private int isPraise;
	
	@SerializedName("seller_address")
	private String sellerAddress;
	
	@SerializedName("leaflet_description")
	private String leafletDescription;
	
	@SerializedName("browse_time")
	private String browseTime;
	

	public Date getPublishTime() {
		return UTCtoLocal.utc2LocalDate(this.publishTime);
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	
	public String getUTCPublishTime()
	{
		return this.publishTime;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public Date getStartTime() {
		return UTCtoLocal.utc2LocalDate(this.startTime);
	}
	
	public String getUTCStartTime()
	{
		return this.startTime;
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

	public String getBriefLeafletPath() {
		return briefLeafletPath;
	}

	public void setBriefLeafletPath(String briefLeafletPath) {
		this.briefLeafletPath = briefLeafletPath;
	}

	public String getUTCEndTime() {
		return endTime;
	}
	
	public Date getEndTime()
	{
		return UTCtoLocal.utc2LocalDate(endTime);
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
	
	public int getCommentTimes() {
		return commentTimes;
	}

	public void setCommentTimes(int commentTimes) {
		this.commentTimes = commentTimes;
	}

	public String getSellerLogoPath() {
		return sellerLogoPath;
	}

	public void setSellerLogoPath(String sellerLogoPath) {
		this.sellerLogoPath = sellerLogoPath;
	}
	
	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public int isPraise() {
		return isPraise;
	}

	public void setPraise(int isPraise) {
		this.isPraise = isPraise;
	}

	public String getSellerAddress() {
		return sellerAddress;
	}

	public void setSellerAddress(String sellerAddress) {
		this.sellerAddress = sellerAddress;
	}

	public String getLeafletDescription() {
		return leafletDescription;
	}

	public void setLeafletDescription(String leafletDescription) {
		this.leafletDescription = leafletDescription;
	}
	
	public void setBrowseTime(String time)
	{
		this.browseTime=time;
	}
	
	public String getUTCBrowseTime()
	{
		return this.browseTime;
	}
	
	public Date getBrowseTime()
	{
		return UTCtoLocal.utc2LocalDate(browseTime);
	}
}
