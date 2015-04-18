/** 
 * description：
 * @author wheat
 * date: 2015-3-18  
 * time: 下午6:36:31
 */ 
package org.wheat.leaflets.loader;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.entity.json.CommentPostJson;
import org.wheat.leaflets.entity.json.PraisePostJson;
import org.wheat.leaflets.httptools.HttpConnectTools;
import org.wheat.leaflets.httptools.JsonTools;

/** 
 * description:
 * @author wheat
 * date: 2015-3-18  
 * time: 下午6:36:31
 */
public class HttpUploadMethods 
{
	public static CommentPostJson postCommentPost(CommentPostJson comment) throws Exception
	{
		String json= HttpConnectTools.postJsonReturnJsonString(ConstantValue.HttpRoot+"set_comment", null, comment);
		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), CommentPostJson.class);
	}
	
	public static PraisePostJson postPraisePost(PraisePostJson praise) throws Exception
	{
		String json=HttpConnectTools.postJsonReturnJsonString(ConstantValue.HttpRoot+"set_praise", null, praise);
		if(json!=null)
			return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), PraisePostJson.class);
		return null;
	}
	
	public static int removePraiseRecord(int leaflet_id,String userName) throws Exception
	{
		int returnCode=HttpConnectTools.getReturnCode(ConstantValue.HttpRoot+"remove_praise"+"?leaflet_id="+leaflet_id+"&username="+userName, null, null);
		return returnCode;
	}
	

	/**
	 * 
	 * @param FILENAME   上传到服务器的图片url
	 * @param PhotoName  生成的图片id，用来保存在服务器端
	 * @return  int 自定义成功与否的状态码
	 * @throws Exception
	 */
	
	public static int uploadPhoto(File photo,String PhotoName,String photoType) throws Exception {
		System.out.println("in post photo method");
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

		HttpPost httppost = new HttpPost(ConstantValue.HttpRoot+"upload_image"
						+ "?photo_type=" + photoType+"&photo_name="+PhotoName);

		FileEntity reqEntity = new FileEntity(photo, "binary/octet-stream");

		httppost.setEntity(reqEntity);
		reqEntity.setContentType("binary/octet-stream");
		System.out.println("executing request " + httppost.getRequestLine());
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();

		System.out.println(response.getStatusLine());
		//这里不用自定义的statusCode，因为上传失败的原因很多，只有当系统返回正常信息时才认为上传成功
		if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
			return ConstantValue.operateSuccess;//上传成功
		else 
			return ConstantValue.uploadPhotoFailed;//上传失败
	}
	
	public static int removeFavourite(String userName,int leaflet_id) throws Exception
	{
		int returnCode=HttpConnectTools.getReturnCode(ConstantValue.HttpRoot+"remove_favourite"+"&username="+userName+"&leaflet_id="+leaflet_id, null, null);
		return returnCode;
	}
	
	public static int setFavourite(String userName,int leaflet_id) throws Exception
	{
		int returnCode=HttpConnectTools.getReturnCode(ConstantValue.HttpRoot+"set_favourite"+"&username="+userName+"&leaflet_id="+leaflet_id, null, null);
		return returnCode;
	}
}
