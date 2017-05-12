package com.allinpayjl.guomao.activity;



import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.allinpay.usdk.core.data.BaseData;
import com.allinpay.usdk.core.data.ResponseData;
import com.allinpayjl.guomao.R;
import com.allinpayjl.guomao.DAO.DBHelper;
import com.allinpayjl.guomao.service.LongRunningService;
import com.allinpayjl.guomao.uitl.HttpUitl;
import com.allinpayjl.guomao.uitl.JsInteration;
import com.allinpayjl.guomao.uitl.Url;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;


@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
	private WebView myWebView;
	private ProgressBar progressBar;
	private Context myContext;
	@Override
	@SuppressLint("JavascriptInterface")
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		Intent intent = new Intent(this, LongRunningService.class);
		startService(intent);
		

	}
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		DBHelper helper = DBHelper.getHelper(myContext);
		SQLiteDatabase db = helper.getWritableDatabase();
		db.close();
	}


	private void init(){
		
		myContext = this;
		myWebView = (WebView) findViewById(R.id.WebView);
		progressBar = (ProgressBar)findViewById(R.id.index_progressBar);
		WebSettings settings = myWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setDomStorageEnabled(true);
		myWebView.addJavascriptInterface(new JsInteration(this,myWebView), "control");
		myWebView.setWebChromeClient(new WebChromeClient());
		myWebView.loadUrl(Url.webIndex);
		myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progressBar.setVisibility(View.VISIBLE);
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
            @SuppressWarnings("deprecation")
			@Override
            public void onReceivedError(WebView view, int errorCode,
                    String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });

        //显示进度
		myWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if(newProgress >= 100){
                    progressBar.setVisibility(View.GONE);
                }
//                super.onProgressChanged(view, newProgress);
            }
            
        });
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && myWebView.canGoBack()) {
			myWebView.goBack();// 返回前一个页面
	             return true;
	         }
	         return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Bundle extras = data.getExtras();
		if (extras == null) {
			Log.e("", "No extras provided");
			return;
		}
		ResponseData respone = (ResponseData) extras.getSerializable(ResponseData.KEY_ERTRAS);
		final String jsonData = JSONObject.toJSONString(respone);
		Log.i("json",jsonData);
		final HttpUitl httpUitl=new HttpUitl();
		
		switch (requestCode) {
		case 0:										//支付完成时

			String responeRejcode = respone.getValue(BaseData.REJCODE);
			int code=0;//交易失败
			if((requestCode==0)&&responeRejcode.equals("00")){
				code = 1;//交易成功
			}
			
			Toast.makeText(this,respone.getValue(BaseData.REJCODE_CN) ,Toast.LENGTH_LONG).show();
			if(code==1){
				new Thread(){

					/* (non-Javadoc)
					 * @see java.lang.Thread#run()
					 */
					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						try {
							httpUitl.myRequest(jsonData,0, myContext);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}.start();
				    
			}
			String  callBack= "javascript:toastHide()";
			myWebView.loadUrl(callBack);
			break;
		case 1:
			Toast.makeText(this,respone.getValue(BaseData.REJCODE_CN) ,Toast.LENGTH_LONG).show();
			break;
		case 2: 						//撤销和退货
			String void_rejcode = respone.getValue(BaseData.REJCODE);
			Toast.makeText(this,respone.getValue(BaseData.REJCODE_CN) ,Toast.LENGTH_LONG).show();
			if(void_rejcode.equals("00")){
				
				new Thread(){

					/* (non-Javadoc)
					 * @see java.lang.Thread#run()
					 */
					@Override
					public void run() {
						// TODO Auto-generated method stub
						super.run();
						try {
							httpUitl.myRequest(jsonData,1, myContext);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}.start();
			}
			String  callBack2= "javascript:toastHide()";
			myWebView.loadUrl(callBack2);
			break;
		default:
			break;
		}
		
	}
	

}

