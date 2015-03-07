package org.wheat.leaflets.entity;

public class PhotoParameters 
{
	private String url;
	private int minSideLength;
	private int maxNumOfPixels;
	private boolean mFixWidth;
	private int mImageViewWidth;
	public PhotoParameters(String url,int minSideLength,int maxNumOfPixels)
	{
		this(url,minSideLength,maxNumOfPixels,false,0);
	}
	
	/**
	 * 
	 * @param url				ͼƬ�����ص�ַ
	 * @param minSideLength		ͼƬ��С�ߵ�����
	 * @param maxNumOfPixels	ͼƬ��������
	 * @param fixWidth			�Ƿ񱣳�ͼƬ�ĸ߿���������Ϊtrue��imageViewWidth����С��0�����fixWidthΪfalse��imageViewWidth������Ч
	 * @param imageViewWidth	ͼƬ����ImageView�Ŀ��
	 */
	public PhotoParameters(String url,int minSideLength,int maxNumOfPixels,boolean fixWidth,int imageViewWidth)
	{
		this.url=url;
		this.minSideLength=minSideLength;
		this.maxNumOfPixels=maxNumOfPixels;
		this.mFixWidth=fixWidth;
		this.mImageViewWidth=imageViewWidth;
	}
	
	public String getUrl()
	{
		return this.url;
	}
	
	public void setUrl(String url)
	{
		this.url=url;
	}
	
	public int getMinSideLength()
	{
		return this.minSideLength;
	}
	
	public void setMinSideLength(int minSideLength)
	{
		this.minSideLength=minSideLength;
	}
	
	public int getMaxNumOfPixels()
	{
		return this.maxNumOfPixels;
	}
	
	public void setMaxNumOfPixles(int maxNumOfPixels)
	{
		this.maxNumOfPixels=maxNumOfPixels;
	}
	
	public void setFixWidth(boolean fixWidth)
	{
		this.mFixWidth=fixWidth;
	}
	
	public boolean isFixWidth()
	{
		return this.mFixWidth;
	}
	
	public void setImageViewWidth(int imageViewWidth)
	{
		this.mImageViewWidth=imageViewWidth;
	}
	
	public int getImageViewWidth()
	{
		return this.mImageViewWidth;
	}
}
