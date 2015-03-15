/** 
 * description��
 * @author wheat
 * date: 2015-3-7  
 * time: ����11:33:36
 */ 
package org.wheat.leaflets.loader;

import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.entity.Leaflets;
import org.wheat.leaflets.entity.json.LeafletsJson;
import org.wheat.leaflets.httptools.BitmapTools;
import org.wheat.leaflets.httptools.HttpConnectTools;
import org.wheat.leaflets.httptools.JsonTools;

import android.graphics.Bitmap;
import android.util.Log;

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
	public static Bitmap downLoadBitmap(String photoPath,int minSideLength,int maxNumOfPixels,String photoType) throws Throwable
	{
		/*
		HashMap<String,String> hm=new HashMap<String,String>();
		hm.put("path", bitmapPath);
		byte[] data=encryptParamsToPost(hm);
		*/
		//Bitmap bm=HttpLoader.getPhoto( HttpRoot+"/servlet/DownLoadPhotoServlet",data, null);
		Bitmap bm=BitmapTools.getPhoto(ConstantValue.HttpRoot+"get_image?photo_type="+photoType+"&photo_name="+photoPath, null,minSideLength,maxNumOfPixels);
		
		Log.d("HttpLoaderMethods", ConstantValue.HttpRoot+"get_image?photo_type="+photoType+"&photo_name="+photoPath);
		if(bm!=null)
		{
			Log.d("HttpLoaderMethods", "getPhoto Success!");
		}
		else
		{
			Log.d("HttpLoaderMethods", "getPhoto fail!");
		}
		return bm;
	}
	
	/**
	 * ˢ��NeighborFragment����ʹ�õ�api
	 * @param username
	 * @param order_rule   (oder_rule������ֵ"published":������ʱ������,"start":��������ʼ��ʱ������,"end":����������ʱ������)
	 * @return
	 * @throws Exception
	 */
	public static LeafletsJson flushLeafletData(String username,String orderRule) throws Throwable
	{
		String json=HttpConnectTools.get(ConstantValue.HttpRoot+"flush_leaflet_data?username="+username+"&order_rule="+orderRule, null);
		if(json==null)
			return null;
		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), LeafletsJson.class);
	}
	
	/**
	 * ΪNeighborFragment���ظ�������ʱʹ�õ�Api���������ݱ��е�offset_begin����offset_end������
	 * @param offset_begin
	 * @param offset_end
	 * @param username
	 * @param order_rule   (oder_rule������ֵ"published":������ʱ������,"start":��������ʼ��ʱ������,"end":����������ʱ������)
	 * @return
	 * @throws Throwable
	 */
	public static LeafletsJson getNeighborPage(int offset_begin,int offset_end,String username,String order_rule) throws Throwable
	{
		String json=HttpConnectTools.get(ConstantValue.HttpRoot+"get_leaflet_data?username="+username+"&offset_begin="+offset_begin+"&offset_end="+offset_end+"&order_rule="+order_rule, null);
		if(json==null)
			return null;
		return JsonTools.fromJson(new String(json.getBytes("8859_1"),"UTF-8"), LeafletsJson.class);
	}
	
}
