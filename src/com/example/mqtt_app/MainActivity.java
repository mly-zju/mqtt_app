package com.example.mqtt_app;

import com.example.mqtt_app.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import android.view.*;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity extends Activity {
	private Button btn;
	private TextView textview;
	private MqttClient client;
	private MqttConnectOptions options;
	private String url="tcp://192.168.56.1:1883";
	private String topic="test";
	private MqttMessage content=new MqttMessage("this is a test".getBytes());
	
	private Handler myHandler=new Handler(){
		@Override 
		public void handleMessage(Message msg){
			if(msg.what==0x123){
				textview.setText("another hello");
			}
		}
	};
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {    
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);
        btn=(Button) findViewById(R.id.btn);
        textview=(TextView) findViewById(R.id.tv);
        mqttInit();
        bindEvent();
	}

	private void bindEvent() {
		class BtnClickListener implements View.OnClickListener{
			@Override
			public void onClick(View v) {
				Log.i("test","enter click");
				try {
					client.publish(topic, content);
				} catch (Exception e) {
					Log.i("test",e.toString());
					e.printStackTrace();
				}
			}
		}
		btn.setOnClickListener(new BtnClickListener());
	}
	
	private void mqttInit(){
		try{
			client=new MqttClient(url,"demo",new MemoryPersistence());
			options=new MqttConnectOptions();
			options.setCleanSession(true);
			new Thread(new Runnable(){
				@Override
				public void run(){
					try {
						client.connect(options);
					} catch (Exception e) {
						Log.i("test",e.toString());
						e.printStackTrace();
					} 
				}
			}).start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	
	
}