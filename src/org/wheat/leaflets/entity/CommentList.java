/** 
 * description��
 * @author wheat
 * date: 2014-12-19  
 * time: ����4:54:37
 */ 
package org.wheat.leaflets.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/** 
 * description:
 * @author wheat
 * date: 2014-12-19  
 * time: ����4:54:37
 */
public class CommentList 
{
	@SerializedName("commentList")
	private List<Comment> mCommentList;
	
	public void setCommentList(List<Comment> list)
	{
		this.mCommentList=list;
	}
	
	public List<Comment> getCommentList()
	{
		return this.mCommentList;
	}
}
