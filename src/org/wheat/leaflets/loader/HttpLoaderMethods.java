/** 
 * description��
 * @author wheat
 * date: 2015-3-7  
 * time: ����11:33:36
 */ 
package org.wheat.leaflets.loader;

import org.wheat.leaflets.entity.ConstantValue;

import android.graphics.Bitmap;

/** 
 * description:
 * @author wheat
 * date: 2015-3-7  
 * time: ����11:33:36
 */
public class HttpLoaderMethods 
{
	/**
	 * 
	 * @param context
	 * @param bitmapPath ͼƬ�ڷ���˵�·��
	 * @return
	 * @throws Throwable
	 */
	public static Bitmap downLoadBitmap(String bitmapPath,int minSideLength,int maxNumOfPixels) throws Throwable
	{
		/*
		HashMap<String,String> hm=new HashMap<String,String>();
		hm.put("path", bitmapPath);
		byte[] data=encryptParamsToPost(hm);
		*/
		//Bitmap bm=HttpLoader.getPhoto( HttpRoot+"/servlet/DownLoadPhotoServlet",data, null);
		Bitmap bm=org.wheat.leaflets.httptools.BitmapTools.getPhoto( ConstantValue.HttpRoot+"/servlet/DownLoadPhotoServlet?name="+bitmapPath, null,minSideLength,maxNumOfPixels);
		return bm;
	}
}
