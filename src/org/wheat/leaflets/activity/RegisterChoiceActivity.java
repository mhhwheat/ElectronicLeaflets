
package org.wheat.leaflets.activity;

import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.basic.ExitApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/** 
 * description:
 * @author wheat
 * date: 2015-3-4  
 * time: ÏÂÎç4:37:16
 */
public class RegisterChoiceActivity extends Activity 
{

	private Button btRegisterUser;
	private Button btRegisterSeller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_choice);
		
		btRegisterUser=(Button)findViewById(R.id.btRegister_choice_user);
		btRegisterSeller=(Button)findViewById(R.id.btRegister_choice_seller);
		
		btRegisterUser.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(RegisterChoiceActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
		});
		
		btRegisterSeller.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(RegisterChoiceActivity.this,RegisterSellerActivity.class);
				startActivity(intent);
			}
		});
		
		ExitApplication.getInstance().addActivity(this);
	}
	
}
