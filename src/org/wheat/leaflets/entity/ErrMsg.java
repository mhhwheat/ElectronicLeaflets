package org.wheat.leaflets.entity;

import com.google.gson.annotations.SerializedName;


/**
 * @author wheat
 * @Date 14-9-14
 * @Time ����21:36
 */
public class ErrMsg 
{
	@SerializedName("msg")
    String mMsg;

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String msg) {
        mMsg = msg;
    }
}
