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
public class ReturnData <T>
{
	@SerializedName("pk")
	private int primaryKey;
	
	@SerializedName("fields")
	private T dataFields;

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public T getDataFields() {
		return dataFields;
	}

	public void setDataFields(T dataFields) {
		this.dataFields = dataFields;
	}
	
	
	
}
