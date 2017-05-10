package com.allinpayjl.guomao.uitl;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.allinpayjl.guomao.DAO.DBManager;
import com.zhy.http.okhttp.OkHttpUtils;

import android.content.Context;
import android.util.Log;
import okhttp3.Response;

public class HttpUitl {

	public String appConnect(String jsonData,int flag) throws IOException{
		
		 Response response= OkHttpUtils
				    .post()
				    .url(Url.appConnect)
				    .addParams("data", jsonData)
				    .addParams("flag",flag+"")
				    .build().execute();
		 
		 if(response.isSuccessful()){

			 return response.body().string();
		 }else{
			 return "{\"Status\":\"0\",\"msg\":\"httpUrl is mistake!! \"}";
		 }
		 
		 
		
	}
	
	public void myRequest(String data,int flag,Context context) throws IOException{
		DBManager dbManager=new DBManager(context);
		dbManager.insert(data,flag);
		dbManager.select();
		
		
	}
	public void updataRequest(int id,String data,int flag,Context context) throws IOException{
		Log.i("app",data);
		for(int i =5;i>0;i--){
			String resJson =appConnect(data,flag);
			JSONObject jsonObject= JSONObject.parseObject(resJson);
			if(jsonObject !=null ){
				String status=jsonObject.getString("status");
				if(status.equals("1")){
					DBManager dbManager=new DBManager(context);
					dbManager.updateStatus(id);
					break;
				}
			}
			
		}
		
		
	}
}
