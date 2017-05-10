package com.allinpayjl.guomao.DAO;

import java.io.IOException;

import com.allinpayjl.guomao.uitl.HttpUitl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
	public int getCount(){
		
		Cursor cursor=db.query(DBHelper.TABLE_NAME, new String[]{"Id","Json","Status","Flag"}, "Status = ?", new String[]{"0"}, null, null,null);
		
		return cursor.getCount();
	}
	
	
	public boolean delete(int id){
		String[] args = {String.valueOf(id)};
		int res =db.delete(DBHelper.TABLE_NAME, "id=?", args);
		if(res !=0){
			return true;
		}else{
			return false;
		}
		
	}
	
    
}
