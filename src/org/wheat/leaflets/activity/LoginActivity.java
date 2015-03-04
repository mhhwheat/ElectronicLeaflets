package org.wheat.leaflets.activity;


import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.coders.Coder_Md5;
import org.wheat.leaflets.entity.json.LoginMsgJson;
import org.wheat.leaflets.loader.LoginAndRegister;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/** 
 * description:
 * @author wheat
 * date: 2015-3-3  
 * time: ÏÂÎç12:42:41
 */
public class LoginActivity extends Activity
{
	
	private EditText etEmail;
	private EditText etPassword;
	private Button btLogin;
	private TextView tvRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		System.out.println("alsdjfalskdfjasl");
		etEmail=(EditText)findViewById(R.id.login_user_id);
		etPassword=(EditText)findViewById(R.id.login_pwd);
		btLogin=(Button)findViewById(R.id.btLogin);
		tvRegister=(TextView)findViewById(R.id.tvRegister);
		

		btLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("LoginActivity", "button click!");
				new LoginTask(etEmail.getText().toString(), etPassword.getText().toString()).execute();
			}
		});
	}
	
	private class LoginTask extends AsyncTask<Void, Void, LoginMsgJson>
	{
		private String strEmail;
		private String strPassword;
		
		public LoginTask(String email,String password)
		{
			this.strEmail=email;
			this.strPassword=password;
		}

		@Override
		protected LoginMsgJson doInBackground(Void... params) {
			LoginMsgJson loginMsgJson=null;
			try {
				loginMsgJson=LoginAndRegister.synLogin(strEmail, Coder_Md5.md5(strPassword));
//				LoginAndRegister.login();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return loginMsgJson;
		}

		@Override
		protected void onPostExecute(LoginMsgJson result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result==null)
			{
				Log.d("LoginActivity", "LoginMsgJson is null");
			}
			else
			{
				if(result.getCode()==1000)
				{
					Toast toast=Toast.makeText(LoginActivity.this, "µÇÂ¼³É¹¦", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
				if(result.getCode()==1021)
				{
					Toast toast=Toast.makeText(LoginActivity.this, "µÇÂ¼Ê§°Ü", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				}
			}
		}
		
	}
	
	
}
