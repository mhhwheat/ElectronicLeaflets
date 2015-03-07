/** 
 * description£º
 * @author wheat
 * date: 2015-3-4  
 * time: ÏÂÎç3:22:16
 */ 
package org.wheat.leaflets.coders;

import java.security.MessageDigest;

/** 
 * description:
 * @author wheat
 * date: 2015-3-4  
 * time: ÏÂÎç3:22:16
 */
public class Coder_SHA256 
{
	public static String sha256(String val)
	{
		MessageDigest digest;
		String result="";
		try
		{
			digest=MessageDigest.getInstance("SHA-256");
			byte[] hash=digest.digest(val.getBytes("UTF-8"));
			result=hash.toString();
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
}
