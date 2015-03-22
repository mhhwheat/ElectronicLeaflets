/** 
 * description：
 * @author wheat
 * date: 2015-3-17  
 * time: 下午4:18:54
 */ 
package org.wheat.leaflets.activity;

import java.io.UnsupportedEncodingException;

import org.wheat.electronicleaflets.R;
import org.wheat.leaflets.basic.UTCtoLocal;
import org.wheat.leaflets.entity.CommentPost;
import org.wheat.leaflets.entity.ConstantValue;
import org.wheat.leaflets.entity.json.CommentPostJson;
import org.wheat.leaflets.loader.HttpUploadMethods;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/** 
 * description:用户编辑评论页面
 * @author wheat
 * date: 2015-3-17  
 * time: 下午4:18:54
 */
public class CommentActivity extends Activity
{

	private final int TEXT_LENGTH=200;//输入内容限制的字数
	private EditText etCommentContent;
	private TextView tvValidNumber;
	
	private TextView tvTitleCancel;//取消评论的按钮
	private TextView tvTitleComment;//发表评论的按钮
	
	private String userName;
	private int leafletId;
	
	private CommentPost comment=null;
	private View commentViewSoft;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
		setContentView(R.layout.activity_comment);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.activity_comment_title);
		
		getInfoFromLastActivity();
		
		commentViewSoft=findViewById(R.id.comment_view_soft);
		etCommentContent=(EditText)findViewById(R.id.comment_edit_content);
		tvValidNumber=(TextView)findViewById(R.id.comment_valid_number);
		
		tvTitleCancel=(TextView)findViewById(R.id.comment_title_cancel);
		tvTitleComment=(TextView)findViewById(R.id.comment_title_comment);
		
		initialWidgetListener();
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			this.finish();
			overridePendingTransition(R.anim.pop_bottom_in, R.anim.pop_bottom_out);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void initialWidgetListener()
	{
		etCommentContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				tvValidNumber.setText(String.valueOf(TEXT_LENGTH-s.length()));
			}
		});

		tvTitleCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CommentActivity.this.finish();
				overridePendingTransition(R.anim.pop_bottom_in, R.anim.pop_bottom_out);
			}
		});
		
		tvTitleComment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!etCommentContent.getText().toString().equals(""))
				{
					comment=new CommentPost();
					comment.setCommentContent(etCommentContent.getText().toString());
					comment.setCommentTime(UTCtoLocal.localDate2UTC());
					comment.setLeafletId(leafletId);
					comment.setUserName(userName);
					
					CommentPostJson json=new CommentPostJson();
					json.setData(comment);
					
					new PostCommentTask(json).execute();
				}
			}
		});
		
		commentViewSoft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
	}
	
	//从启动该activity的activity传过来的intent对象中获取userName和leafletId
	private void getInfoFromLastActivity()
	{
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		userName=bundle.getString("user_name");
		leafletId=bundle.getInt("leaflet_id");
	}
	
	private class PostCommentTask extends AsyncTask<Void, Void, Integer>
	{
		private CommentPostJson comment;
		
		public PostCommentTask(CommentPostJson comment)
		{
			this.comment=comment;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			CommentPostJson json=null;
			try {
				json=HttpUploadMethods.postCommentPost(comment);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(json==null)
				return 0;
			else
				return json.getCode();
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result==ConstantValue.operateSuccess)
			{
				Intent intent=new Intent();
				Bundle bundle=new Bundle();
				try {
					bundle.putString("comment_content", new String(etCommentContent.getText().toString().getBytes(),"UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				bundle.putString("comment_time", comment.getData().getCommentTime());
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				CommentActivity.this.finish();
			}
			else if(result==ConstantValue.commentRepeat)
			{
				Toast.makeText(CommentActivity.this, "重复评论", Toast.LENGTH_LONG).show();
			}
			super.onPostExecute(result);
		}
		
		
		
	}
	
}
