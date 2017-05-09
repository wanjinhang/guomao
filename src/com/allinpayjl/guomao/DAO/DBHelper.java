package com.allinpayjl.guomao.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final int DB_VERSION = 1;
    private static final String DB_NAME = "guomao.db";
    public static final String TABLE_NAME = "xssj";
    
    private static DBHelper instance;
    
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	public static DBHelper getHelper(Context context){  
        if(instance==null)  
            instance=new DBHelper(context);  
        return instance;  
    }  
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		/**
		 * Id     ���� id
		 * Json   json��ʽ��֧�����ز���
		 * Status �Ƿ��������ͬ���ı�ʶ
		 * Flag   ֧�� 0  �˻�/���� 1
		 * 			 
		 */
		String sql = "create table if not exists " + TABLE_NAME + " (Id integer primary key, Json text, Status integer,Flag integer)";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		super.onDowngrade(db, oldVersion, newVersion);
		
	}
	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
	}
	 
	
	
	
}
