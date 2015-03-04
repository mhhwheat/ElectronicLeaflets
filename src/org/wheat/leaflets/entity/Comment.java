package org.wheat.leaflets.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.annotations.SerializedName;


public class Comment
{
	//����ID
	@SerializedName("commentId")
	private int commentID;
	
	//��ƬID
	@SerializedName("photoId")
	private int photoID;
	
	//�û��ֻ���
	@SerializedName("userPhoneNumber")
	private String userPhoneNumber;
	
	//�û�ͷ���ַ
	@SerializedName("userAvatar")
	private String userAvatar;
	
	//�û��ǳ�
	@SerializedName("userNickName")
	private String userNickName;
	
	//����ʱ��
	@SerializedName("commentTime")
	private Date commentTime;
	
	//���۵�����
	@SerializedName("commentContent")
	private String commentContent;
	
	public void setCommentID(int commentID)
	{
		this.commentID=commentID;
	}
	
	public int getCommentID()
	{
		return this.commentID;
	}
	
	public void setPhotoID(int photoID)
	{
		this.photoID=photoID;
	}
	
	public int getPhotoID()
	{
		return photoID;
	}
	
	public void setUserPhoneNumber(String userPhoneNumber)
	{
		this.userPhoneNumber=userPhoneNumber;
	}
	
	public String getUserPhoneNumber()
	{
		return this.userPhoneNumber;
	}
	
	public void setUserAvatar(String userAvatar)
	{
		this.userAvatar=userAvatar;
	}
	
	public String getUserAvatar()
	{
		return this.userAvatar;
	}
	
	public void setUserNickName(String userNickName)
	{
		this.userNickName=userNickName;
	}
	
	public String getUserNickName()
	{
		return this.userNickName;
	}
	
	public void setCommentTime(Date commentTime)
	{
		this.commentTime=commentTime;
	}
	
	public void setCommentTime(String date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.commentTime=sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getCommentTimeToString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(this.commentTime);
	}
	
	public Date getCommentTime()
	{
		return this.commentTime;
	}
	
	public void setCommentContent(String commentContent)
	{
		this.commentContent=commentContent;
	}
	
	public String getCommentContent()
	{
		return this.commentContent;
	}
}
