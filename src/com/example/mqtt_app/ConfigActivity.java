package com.example.mqtt_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ConfigActivity extends Activity {
	
	private EditText configName;
	private EditText configTopic;
	private EditText configScaleX;
	private EditText configScaleY;
	private TextView configIp;
	private TextView configMac;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {    
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_config);
        configName=(EditText) findViewById(R.id.configName);
        configTopic=(EditText) findViewById(R.id.configTopic);
        configScaleX=(EditText) findViewById(R.id.configScaleX);
        configScaleY=(EditText) findViewById(R.id.configScaleY);
        configIp=(TextView) findViewById(R.id.configIp);
        configMac=(TextView) findViewById(R.id.configMac);
        Intent intent=getIntent();
        String tmp;
        tmp=intent.getStringExtra("deviceName");
        configName.setHint(tmp.substring(tmp.indexOf(':')+1));
        tmp=intent.getStringExtra("topic");
        configTopic.setHint(tmp.substring(tmp.indexOf(':')+1));
        tmp=intent.getStringExtra("scale");
        int begin=tmp.indexOf(':')+1;
        int end=tmp.indexOf('/',begin);
        if(end-begin>1){
        	configScaleY.setHint(tmp.substring(begin,end));
        }else{
        	configScaleY.setHint(" ");
        }
        if(tmp.length()-end>1){
        	configScaleX.setHint(tmp.substring(end+1));
        }else{
        	configScaleX.setHint(" ");
        }
        configIp.setText(intent.getStringExtra("deviceIp"));
        configMac.setText(intent.getStringExtra("deviceMac"));
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		Log.i("test","activity2 stoped!");
	}
	
}
