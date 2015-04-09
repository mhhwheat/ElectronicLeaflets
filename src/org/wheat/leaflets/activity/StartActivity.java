/** 
 * description：
 * @author wheat
 * date: 2015-4-4  
 * time: 下午3:13:58
 */ 
package org.wheat.leaflets.activity;

import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.basic.ExitApplication;
import org.wheat.leaflets.data.UserLoginPreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

/** 
 * description:
 * @author wheat
 * date: 2015-4-4  
 * time: 下午3:13:58
 */
public class StartActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_layout);
		
		new Thread() {
			public void run() {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				handler.sendEmptyMessage(0);
			}

		}.start();
		ExitApplication.getInstance().addActivity(StartActivity.this);
	}
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (UserLoginPreference.getInstance(getApplicationContext()).isFirstRun()) {
				Intent intent = new Intent(StartActivity.this,
						GuideActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(StartActivity.this,
						MainInterfaceActivity.class);
				startActivity(intent);
			}
			finish();
		};
	};
	
	//拦截返回键消息
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

		}
		return true;
	}
}
