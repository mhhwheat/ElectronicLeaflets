package org.wheat.leaflets.entity.json;

import java.io.Serializable;

import org.wheat.leaflets.entity.ErrMsg;
import org.wheat.leaflets.httptools.JsonTools;

import com.google.gson.annotations.SerializedName;
/**
 * @author wheat
 * @description �����������ն�Ҫ����һ������
 * ��Ϊ���¼��������
 * 1.��������ʧ�ܣ�����http�Դ���״̬��
 * 2.�������ӳɹ�
 * 	A: �������ݳɹ���������Ӧ���󣨶����ϴ����ݵ�����ҲҪ����һ���ɹ��Ķ���ReturnMessage�����óɹ����룩
 *  B�� ��������ʧ�ܣ�������Ӧ����ͬʱ���ô���������Ϣ�������ϴ����ݵ�����ҲҪ����һ��ʧ�ܵĶ���ReturnMessage������ʧ�ܴ��룩
 * @Date 14-9-14
 * @Time ����21:29
 * @extend ֻ������û�м̳����jsonBase�ӿ�
 */
public class JsonBaseImpl <T> implements JsonBase<T>,Serializable
{

	  	@SerializedName("c")
	  	private int mCode = -1;

	  	@SerializedName("d")
	  	private T mData;

	  	@SerializedName("err")
	  	private ErrMsg mErrMsg;

	  	@Override
	  	public int getCode() {
	        return mCode;
	    }

	    @Override
	    public void setCode(int code) {
	        mCode = code;
	    }

	    @Override
	    public T getData() {
	        return mData;
	    }

	    @Override
	    public void setData(T data) {
	        mData = data;
	    }

	    public ErrMsg getErrMsg() {
	        return mErrMsg;
	    }

	    public void setErrMsg(ErrMsg errMsg) {
	        mErrMsg = errMsg;
	    }

	    @Override
	    public String getMsg() {
	        if (mErrMsg != null) {
	            return mErrMsg.getMsg();
	        }
	        return "";
	    }

	    @Override
	    public void setMsg(String msg) {
	        if (mErrMsg != null) {
	            mErrMsg.setMsg(msg);
	        }
	    }

	    @Override
	    public String toCacheString() 
	    {
	    	return JsonTools.toJson(this);
	    }
}