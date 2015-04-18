/** 
 * description£º
 * @author wheat
 * date: 2015-4-10  
 * time: ÏÂÎç8:23:30
 */ 
package org.wheat.leaflets.activity;

import java.io.File;
import java.util.Date;

import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.loader.HttpUploadMethods;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

/** 
 * description:
 * @author wheat
 * date: 2015-4-10  
 * time: ÏÂÎç8:23:30
 */
public class TestUploadPhotoActivity extends Activity
{
	
	private Button bt;
	private String photoPath;
	private File photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_test_upload_photo);
	    bt=(Button)findViewById(R.id.upload);
	    photoPath=Environment.getExternalStorageDirectory()+"/DCIM/Camera/Ó²ÅÌ¼ì²â1.png";
	    photo=new File(photoPath);
	    
	    bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new UploadPhotoTask(photo, "abc@qq.com", "primary").execute();
			}
		});
	}
	
	private class UploadPhotoTask extends AsyncTask<Void, Void, Integer>
	{
		private File photo;
		private String photoName;
		private String photoType;

		public UploadPhotoTask(File photo,String photoName,String photoType)
		{
			Date date=new Date();
			this.photo=photo;
			this.photoName=photoName+date.getTime();
			this.photoType=photoType;
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			int returnCode=-1;
			try
			{
				returnCode=HttpUploadMethods.uploadPhoto(photo, photoName, photoType);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return returnCode;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
		
		
	}
	
}
