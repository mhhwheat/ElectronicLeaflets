/** 
 * description£º
 * @author wheat
 * date: 2015-3-6  
 * time: ÏÂÎç7:26:45
 */ 
package org.wheat.leaflets.entity;

import com.google.gson.annotations.SerializedName;

/** 
 * description:
 * @author wheat
 * date: 2015-3-6  
 * time: ÏÂÎç7:26:45
 */
public class Leaflets 
{
	@SerializedName("pk")
	private int leafletId;
	
	@SerializedName("fields")
	private LeafletsFields leafletsFiedls;
	
	public void setLeafletId(int leafletId)
	{
		this.leafletId=leafletId;
	}
	
	public int getLeafletId()
	{
		return this.leafletId;
	}
	
	public void setLeafletFields(LeafletsFields fields)
	{
		this.leafletsFiedls=fields;
	}
	
	public LeafletsFields getLeafletFields()
	{
		return this.leafletsFiedls;
	}
}
