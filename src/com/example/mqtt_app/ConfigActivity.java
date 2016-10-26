package com.example.mqtt_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
	private Button configYes,configNo;
	private String configId;
	
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
        configYes=(Button) findViewById(R.id.configYes);
        configNo=(Button) findViewById(R.id.configNo);
        displayConfigData();
        bindEvent();
	}
	
	private void bindEvent() {
		// TODO Auto-generated method stub
		configYes.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("deviceName","设备名称: "+getString(configName));
				String tmp=configIp.getText().toString();
				intent.putExtra("deviceIp",tmp);
				tmp=configMac.getText().toString();
				intent.putExtra("deviceMac",tmp);
				intent.putExtra("deviceTopic","主题: "+getString(configTopic));
				intent.putExtra("deviceScale","坐标单位(y/x): "+getString(configScaleY)
						+"/"+getString(configScaleX));
				intent.putExtra("deviceId",configId);
				setResult(1001,intent);
				finish();
			}
			
			String getString(EditText v){
				if(!"".equals(v.getText().toString())){
					return v.getText().toString();
				}else{
					return v.getHint().toString();
				}
			}
			
		});
		
		configNo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
	}

	private void displayConfigData() {
		// TODO Auto-generated method stub
		Intent intent=getIntent();
		String tmp;
        tmp=intent.getStringExtra("deviceName");
        int begin=tmp.indexOf(':');
        if(tmp.length()>begin+1){
        	configName.setHint(tmp.substring(begin+2));
        }else{
        	configName.setHint("");
        }
        tmp=intent.getStringExtra("topic");
        begin=tmp.indexOf(':');
        if(tmp.length()>begin+1){
        	configTopic.setHint(tmp.substring(begin+2));
        }else{
        	configTopic.setHint("");
        }
        tmp=intent.getStringExtra("scale");
        begin=tmp.indexOf(':')+1;
        int end=tmp.indexOf('/',begin);
        if(end-begin>1){
        	configScaleY.setHint(tmp.substring(begin+1,end));
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
        configId=intent.getStringExtra("deviceId");
	}

}
