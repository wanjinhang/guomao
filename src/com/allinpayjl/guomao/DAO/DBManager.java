package com.allinpayjl.guomao.DAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allinpayjl.guomao.uitl.HttpUitl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
	private DBHelper helper;
    private SQLiteDatabase db;
    private Context myContext;
	public DBManager(Context context){
		helper = DBHelper.getHelper(context);
		db = helper.getWritableDatabase();
		myContext =context;
	}
	
	public void insert(String json,int flag) {
		
		ContentValues values = new ContentValues(); 
		values.put("Json", json);
		values.put("Status", 0);
		values.put("flag", flag);
		db.insert(DBHelper.TABLE_NAME, null, values);
		
	}
	public void updateStatus(int id){
		String i = Integer.toString(id); 
		
		ContentValues values = new ContentValues(); 
		values.put("Status", 1);
		db.update(DBHelper.TABLE_NAME, values, "Id=?", new String[]{i});
		
	}
	public void select() throws IOException{
		
		
		Cursor cursor=db.query(DBHelper.TABLE_NAME, new String[]{"Id","Json","Status","Flag"}, "Status = ?", new String[]{"0"}, null, null,null);
		
		while (cursor.moveToNext()) {  
			
			int id = cursor.getInt(0); //获取第一列的值,第一列的索引从0开始  
			String json = cursor.getString(1);//获取第二列的值  
			int flag =cursor.getInt(3);
			HttpUitl httpUitl=new HttpUitl();
			httpUitl.updataRequest(id, json,flag, myContext);
			
	   }
		
		
	}
	public int updataRes() throws IOException{
			
			
			Cursor cursor=db.query(DBHelper.TABLE_NAME, new String[]{"Id","Json","Status","Flag"}, "Status = ?", new String[]{"0"}, null, null,null);
			
			while (cursor.moveToNext()) {  
				
				int id = cursor.getInt(0); //获取第一列的值,第一列的索引从0开始  
				String json = cursor.getString(1);//获取第二列的值  
				int flag =cursor.getInt(3);
				HttpUitl httpUitl=new HttpUitl();
				httpUitl.updataRequest(id, json,flag, myContext);
		   }
			
			Cursor cursor1=db.query(DBHelper.TABLE_NAME, new String[]{"Id","Json","Status","Flag"}, "Status = ?", new String[]{"0"}, null, null,null);
			
			return cursor1.getCount();
			
			
		}
	
	public  List<Map<String, Object>> getData() {
		 
		 Cursor cursor=db.query(DBHelper.TABLE_NAME, new String[]{"Id","Json","Status","Flag"}, null, null, null, null,null);
		 
		 List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		 int i=1;
         Map<String, Object> map ;
			while (cursor.moveToNext()) {  
				
				int id = cursor.getInt(0); //获取第一列的值,第一列的索引从0开始  
				String json = cursor.getString(1);//获取第二列的值 
				int stastus =cursor.getInt(2);
				int flag =cursor.getInt(3);
				JSONObject object=JSON.parseObject(json);
				JSONObject object2 =object.getJSONObject("map");
				StringBuilder  amountTmp= new StringBuilder (object2.getString("AMOUNT"));  
				amountTmp.insert(10, ".");    
				String marStrNew = amountTmp.toString();  
				String amount=marStrNew;
				float aa = Float.parseFloat(amount);
				map = new HashMap<String, Object>();
				map.put("number", ""+i);
				if(flag>0){
					map.put("amount", "￥-"+aa);
				}else{
					map.put("amount", "￥"+aa);
				}
				if(stastus>0){
					map.put("status", "已同步");
				}else{
					map.put("status", "未同步");
				}
				StringBuilder  dateTmp= new StringBuilder (object2.getString("DATE"));  
				dateTmp.insert(2, "-");    
				String date = dateTmp.toString();  
				StringBuilder  timeTmp= new StringBuilder (object2.getString("TIME"));  
				timeTmp.insert(2, ":");
				timeTmp.insert(5, ":");
				
				String time = timeTmp.toString(); 
				map.put("date",date+" "+time);
				list.add(map);
				
		   }

  
        
          
         return list;
     }

	public void clear() {
		String[] args = {String.valueOf(1)};
		db.delete(DBHelper.TABLE_NAME, "status=?", args);
		Log.i("CLEAR","commmmmmmmmmmmm");
		
	}
	
    
}
