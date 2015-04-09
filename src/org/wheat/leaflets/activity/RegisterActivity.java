package org.wheat.leaflets.activity;
import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.basic.ExitApplication;
import org.wheat.leaflets.entity.json.RegisterMsgJson;
import org.wheat.leaflets.entity.json.UserNameJson;
import org.wheat.leaflets.loader.LoginAndRegister;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/** 
 * description:
 * @author wheat
 * date: 2015-3-4  
 * time: 下午4:29:40
 */
public class RegisterActivity extends Activity
{
	
	private EditText etRegisterEmail;
	private EditText etRegisterPassword;
	private EditText etRegisterNickname;
	private Button btRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		etRegisterEmail=(EditText)findViewById(R.id.etRegister_Email);
		etRegisterPassword=(EditText)findViewById(R.id.etRegister_password);
		etRegisterNickname=(EditText)findViewById(R.id.etRegister_nickname);
		btRegister=(Button)findViewById(R.id.btRegister);
		
		btRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new RegisterTask(etRegisterEmail.getText().toString(), etRegisterPassword.getText().toString(),etRegisterNickname.getText().toString()).execute();
			}
		});
		
		etRegisterEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus&&!etRegisterEmail.getText().toString().equals(""))
				{
					new CheckRegisterNameTask(etRegisterEmail.getText().toString()).execute();
				}
			}
		});
		
		ExitApplication.getInstance().addActivity(this);
	}
	
	/**
	 * 
	 * description:当用户名输入框失去焦点是，如果内容合法，向服务器请求查询是否已存在该用户
	 * @author wheat
	 * date: 2015-3-5  
	 * time: 下午2:07:02
	 */
	private class CheckRegisterNameTask extends AsyncTask<Void, Void, UserNameJson>
	{
		private String strUserName;
		public CheckRegisterNameTask(String userName)
		{
			this.strUserName=userName;
		}
		@Override
		protected UserNameJson doInBackground(Void... params) {
			
			
			UserNameJson userNameJson=null;
			try {
				userNameJson=LoginAndRegister.synCheckRegisterName(strUserName);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return userNameJson;
		}

		@Override
		protected void onPostExecute(UserNameJson result) {
			super.onPostExecute(result);
			if(result==null)
			{
				Log.d("LoginActivity", "UserNameJson is null");
			}
			else
			{
				if(result.getCode()==1000)
				{
					Toast toast=Toast.makeText(RegisterActivity.this, "用户名合法", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				if(result.getCode()==1024)
				{
					etRegisterEmail.setText("用户已经存在");
//					Toast toast=Toast.makeText(LoginActivity.this, "用户已经存在", Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER, 0, 0);
//					toast.show();
				}
			}
		}
	}
	
	private class RegisterTask extends AsyncTask<Void, Void, RegisterMsgJson>
	{
		private String strEmail;
		private String strPassword;
		private String strNickname;
		
		public RegisterTask(String email,String password,String nickname)
		{
			this.strEmail=email;
			this.strPassword=password;
			this.strNickname=nickname;
		}

		@Override
		protected RegisterMsgJson doInBackground(Void... params) {
			RegisterMsgJson registerMsgJson=null;
			try {
				registerMsgJson=LoginAndRegister.synRegister(strEmail, strPassword,strNickname);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return registerMsgJson;
		}

		@Override
		protected void onPostExecute(RegisterMsgJson result) {
			super.onPostExecute(result);
			if(result==null)
			{
				Log.d("RegisterActivity", "RegisterMsgJson is null!");
			}
				
			else
			{
				if(result.getCode()==1000)
				{
					Toast toast=Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		}
	}
}
