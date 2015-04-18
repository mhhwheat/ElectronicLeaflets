/** 
 * description£º
 * @author wheat
 * date: 2015-4-9  
 * time: ÏÂÎç11:52:55
 */ 
package org.wheat.leaflets.entity;

import java.io.Serializable;

/** 
 * description:
 * @author wheat
 * date: 2015-4-9  
 * time: ÏÂÎç11:52:55
 */
public class SellerPreference implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String sellerEmail;
	private String sellerName;
	private String sellerLogoPaht;
	
	
	
	
	public String getSellerEmail() {
		return sellerEmail;
	}
	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getSellerLogoPaht() {
		return sellerLogoPaht;
	}
	public void setSellerLogoPaht(String sellerLogoPaht) {
		this.sellerLogoPaht = sellerLogoPaht;
	}
	
	
	
}
