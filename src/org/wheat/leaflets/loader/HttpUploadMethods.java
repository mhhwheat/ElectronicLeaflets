/** 
 * description£º
 * @author wheat
 * date: 2015-3-18  
 * time: ÏÂÎç6:36:31
 */ 
package org.wheat.leaflets.loader;

import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.entity.json.CommentPostJson;
import org.wheat.leaflets.entity.json.PraisePostJson;
import org.wheat.leaflets.httptools.HttpConnectTools;
import org.wheat.leaflets.httptools.JsonTools;

/** 
 * description:
 * @author wheat
 * date: 2015-3-18  
 * time: ÏÂÎç6:36:31
 */
public class HttpUploadMethods 
{
	public static CommentPostJson postCommentPost(CommentPostJson comment) throws Exception
	{
		String json= HttpConnectTools.postJsonReturnJsonString(ConstantValue.HttpRoot+"set_comment", null, comment);
		return JsonTools.fromJson(json, CommentPostJson.class);
	}
	
	public static PraisePostJson postPraisePost(PraisePostJson praise) throws Exception
	{
		String json=HttpConnectTools.postJsonReturnJsonString(ConstantValue.HttpRoot+"set_praise", null, praise);
		if(json!=null)
			return JsonTools.fromJson(json, PraisePostJson.class);
		return null;
	}
}
