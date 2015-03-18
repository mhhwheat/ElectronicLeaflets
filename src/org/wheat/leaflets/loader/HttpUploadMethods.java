/** 
 * description£º
 * @author wheat
 * date: 2015-3-18  
 * time: ÏÂÎç6:36:31
 */ 
package org.wheat.leaflets.loader;

import org.wheat.leaflets.entity.CommentPost;
import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.httptools.HttpConnectTools;

/** 
 * description:
 * @author wheat
 * date: 2015-3-18  
 * time: ÏÂÎç6:36:31
 */
public class HttpUploadMethods 
{
	public static int postCommentPost(CommentPost comment) throws Exception
	{
		return HttpConnectTools.postJsonReturnCode(ConstantValue.HttpRoot+"set_comment", comment, null);
	}
	
	
	
}
