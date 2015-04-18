/** 
 * description：
 * @author wheat
 * date: 2015-3-18  
 * time: 下午4:43:05
 */ 
package org.wheat.leaflets.data;


import java.io.IOException;
import java.io.StreamCorruptedException;

import org.wheat.leaflets.basic.SerializableUtil;
import org.wheat.leaflets.entity.SellerPreference;
import org.wheat.leaflets.entity.UserPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/** 
 * description:
 * @author wheat
 * date: 2015-3-18  
 * time: 下午4:43:05
 */
public class UserLoginPreference 
{
	private static UserLoginPreference preference = null;  
    private SharedPreferences sharedPreference;  
    private String packageName;  
    
//    private static final String USER_NAME="userName";
//    private static final String PASSWORD = "password";  //密码  
//    private static final String IS_SAVE_PWD = "isSavePwd"; //是否保留密码 
//    private static final String USER_AVATAR="userAvatar";
    
    public static final int USER_LOGIN=1;
    public static final int SELLER_LOGIN=2;
    public static final int NO_USER_LOGIN=0;
    
    private static final String IS_FIRST_RUN="isFirstRun";//是否是第一次启动程序
    
    private static final String USER="user";
    private static final String SELLER="seller";
    
    //("user","seller","")
    private static final String LOGIN_STATE="login_state";
    
    private UserPreference mUser=null;
    private SellerPreference mSeller=null;
    
    public static synchronized UserLoginPreference getInstance(Context context){  
        if(preference == null)  
            preference = new UserLoginPreference(context);  
        return preference;  
    }  
    
    private  UserLoginPreference(Context context){  
        packageName = context.getPackageName() + "_user_login_preferences";  
        sharedPreference = context.getSharedPreferences(  
                packageName, Context.MODE_PRIVATE);  
    } 
    
//    public void setUserName(String userName)
//    {
//    	Editor editor = sharedPreference.edit(); 
//    	editor.putString(USER_NAME, userName);
//    	editor.commit();
//    }
//    
//    
//    public String getUserName()
//    {
//    	String userName=sharedPreference.getString(USER_NAME, "");
//    	return userName;
//    }
//    
//    public void setPassword(String password)
//    {
//    	Editor editor=sharedPreference.edit();
//    	editor.putString(PASSWORD, password);
//    	editor.commit();
//    }
//    
//    public String getPassword()
//    {
//    	return sharedPreference.getString(PASSWORD, "");
//    }
    
//    public void setIsSavePassword(boolean isSavePassword)
//    {
//    	Editor editor=sharedPreference.edit();
//    	editor.putBoolean(IS_SAVE_PWD, isSavePassword);
//    	editor.commit();
//    }
    
//    public boolean getIsSavePassword()
//    {
//    	return sharedPreference.getBoolean(IS_SAVE_PWD, false);
//    }
    
//    public void setUserAvatar(String userAvatar)
//    {
//    	Editor editor = sharedPreference.edit(); 
//    	editor.putString(USER_AVATAR, userAvatar);
//    	editor.commit();
//    }
//    
//    public String getUserAvatar()
//    {
//    	String avatarPath= sharedPreference.getString(USER_AVATAR, "");
//    	if(avatarPath==null||avatarPath.equals(""))
//    	{
//    		return null;
//    	}
//    	return avatarPath;
//    }
    
    public void setLoginState(int state)
    {
    	Editor editor=sharedPreference.edit();
    	editor.putInt(LOGIN_STATE, state);
    	editor.commit();
    }
    
    public int getLoginState()
    {
    	return sharedPreference.getInt(LOGIN_STATE, NO_USER_LOGIN);
    }
    
    public void firstRun()
    {
    	Editor editor=sharedPreference.edit();
    	editor.putBoolean(IS_FIRST_RUN, false);
    	editor.commit();
    }
    
    public boolean isFirstRun()
    {
    	return sharedPreference.getBoolean(IS_FIRST_RUN, true);
    }
    
    
    public void setUserPreference(UserPreference user)
    {
    	Editor editor=sharedPreference.edit();
    	String str="";
    	try
    	{
    		str=SerializableUtil.obj2Str(user);
    	}catch(IOException e)
    	{
    		e.printStackTrace();
    	}
    	editor.putString(USER, str);
    	editor.commit();
    	this.mUser=user;
    }
    
    public UserPreference getUserPreference()
    {
    	if(mUser==null)
    	{
    		mUser=new UserPreference();
    		String str=sharedPreference.getString(USER, "");
    		try
    		{
    			Object obj=SerializableUtil.str2Obj(str);
    			if(obj!=null)
    			{
    				mUser=(UserPreference)obj;
    			}
    			
    		}catch(StreamCorruptedException e)
    		{
    			e.printStackTrace();
    		}catch(IOException e)
    		{
    			e.printStackTrace();
    		}
    	}
    	return mUser;
    }
    
    
    public void setSellerPreference(SellerPreference seller)
    {
    	Editor editor=sharedPreference.edit();
    	String str="";
    	try
    	{
    		str=SerializableUtil.obj2Str(seller);
    	}catch(IOException e)
    	{
    		e.printStackTrace();
    	}
    	editor.putString(SELLER, str);
    	editor.commit();
    	this.mSeller=seller;
    }
    
    public SellerPreference getSellerPreference()
    {
    	if(mSeller==null)
    	{
    		mSeller=new SellerPreference();
    		String str=sharedPreference.getString(SELLER, "");
    		try
    		{
    			Object obj=SerializableUtil.str2Obj(str);
    			if(obj!=null)
    			{
    				mSeller=(SellerPreference)obj;
    			}
    			
    		}catch(StreamCorruptedException e)
    		{
    			e.printStackTrace();
    		}catch(IOException e)
    		{
    			e.printStackTrace();
    		}
    	}
    	return mSeller;
    }
    
}
