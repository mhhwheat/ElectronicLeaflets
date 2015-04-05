/** 
 * description：
 * @author wheat
 * date: 2015-3-18  
 * time: 下午4:43:05
 */ 
package org.wheat.leaflets.data;


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
    
    private static final String USER_NAME="userName";
    private static final String PASSWORD = "password";  //密码  
    private static final String IS_SAVE_PWD = "isSavePwd"; //是否保留密码 
    private static final String USER_AVATAR="userAvatar";
    private static final String IS_FIRST_RUN="isFirstRun";//是否是第一次启动程序
    
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
    
    public void setUserName(String userName)
    {
    	Editor editor = sharedPreference.edit(); 
    	editor.putString(USER_NAME, userName);
    	editor.commit();
    }
    
    
    public String getUserName()
    {
    	String userName=sharedPreference.getString(USER_NAME, "");
    	return userName;
    }
    
    public void setPassword(String password)
    {
    	Editor editor=sharedPreference.edit();
    	editor.putString(PASSWORD, password);
    	editor.commit();
    }
    
    public String getPassword()
    {
    	return sharedPreference.getString(PASSWORD, "");
    }
    
    public void setIsSavePassword(boolean isSavePassword)
    {
    	Editor editor=sharedPreference.edit();
    	editor.putBoolean(IS_SAVE_PWD, isSavePassword);
    	editor.commit();
    }
    
    public boolean getIsSavePassword()
    {
    	return sharedPreference.getBoolean(IS_SAVE_PWD, false);
    }
    
    public void setUserAvatar(String userAvatar)
    {
    	Editor editor = sharedPreference.edit(); 
    	editor.putString(USER_AVATAR, userAvatar);
    	editor.commit();
    }
    
    public String getUserAvatar()
    {
    	return sharedPreference.getString(USER_AVATAR, "");
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
}
