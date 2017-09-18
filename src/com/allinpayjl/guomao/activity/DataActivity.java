package com.allinpayjl.guomao.activity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.allinpayjl.guomao.R;
import com.allinpayjl.guomao.DAO.DBManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class DataActivity extends Activity implements OnClickListener {
	 private List<Map<String, Object>> mData;
	 private ListView lv1;
	 private Button uploadBt;
	 private Button clearBt;
     //private List<String> data = new ArrayList<String>();
     @Override
     public void onCreate(Bundle savedInstanceState){
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_data);
         lv1 = (ListView) findViewById(R.id.lv1);
         DBManager dbManager=new DBManager(this);
         mData =dbManager.getData();
         SimpleAdapter adapter = new SimpleAdapter(this,mData,R.layout.vlist,
        		 									new String[]{"amount","date","status"},
        		 									new int[]{R.id.amount,R.id.date,R.id.status});
         lv1.setAdapter(adapter);
         
         uploadBt=(Button) findViewById(R.id.upload);
         clearBt=(Button) findViewById(R.id.clear);
         uploadBt.setOnClickListener(this);
         clearBt.setOnClickListener(this);
        
     }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		final DBManager dbManager=new DBManager(this);
		switch (v.getId()) {
		case R.id.upload:
			Toast.makeText(this, "upload", Toast.LENGTH_LONG).show();
			Thread thread=new Thread(new Runnable()  
	        {  
	            @Override  
	            public void run()  
	            {  
	                try {
						dbManager.select();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	            }  
	        });  
	        thread.start();  
			break;
		case R.id.clear:
			new  AlertDialog.Builder(this)   
			.setTitle("清除已同步数据" )  
			.setMessage("真的要清除么？" )  
			.setPositiveButton("是" , new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dbManager.clear();
				}
			})  
			.setNegativeButton("否" , new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			})  
			.show(); 
			break;

		default:
			break;
		}
	}
	
     
     
      
      
    
}
