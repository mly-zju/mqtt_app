package com.example.mqtt_app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;


public class DataActivity extends Activity{
	private TextView tv;
	private Button btn;
	MyView myview=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		ArrayList<Float> d=new ArrayList<Float>();
		
		Intent intent=getIntent();
		float tmp;
		int len=intent.getIntExtra("len", 0);
		for(int i=0;i<len;i++){
			d.add(intent.getFloatExtra(i+"", 0.0f));
		}
		String deviceName=intent.getStringExtra("deviceName");
		String deviceScaleY=intent.getStringExtra("deviceScaleY");
		myview=new MyView(this,d, deviceName, deviceScaleY);
        //myview.setData(d);  
	
		setContentView(R.layout.activity_data);
		
		tv=(TextView) findViewById(R.id.device_name);
		tv.setText(deviceName);
		
		btn=(Button) findViewById(R.id.data_back);
		
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		
	}

}
