package com.example.mqtt_app;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.mqtt_app.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.util.Log;
import android.view.*;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.*;

public class MainActivity extends Activity {
	private Context mContext;
	private Button btn;
	private TextView textview;
	private ListView listview;
	private ArrayList<HashMap<String,String>> listitem=new ArrayList<HashMap<String,String>>();
	private String demoData="[{deviceName:\"testDemo\",deviceIp:\"192.168.0.2\",deviceMac:" +
			"\"xx:xx:xx:xx:xx:xx\",topic:\"test\",scale:\"temp/day\"}," +
			"{deviceName:\"testDemo2\",deviceIp:\"192.168.0.3\",deviceMac:" +
			"\"xx:xx:xx:xx:xx:xx\",topic:\"test2\",scale:\"temp/day\"}]"; //这个是fake数据
	
	private MqttClient client;
	private MqttConnectOptions options;
	private String url="tcp://192.168.56.1:1883";
	private String payloadString;
	
	private Handler myHandler=new Handler(){
		@Override 
		public void handleMessage(Message msg){
			if(msg.what==0x123){
//				SimpleAdapter myAdapter=new SimpleAdapter(mContext,listitem,R.layout.list_item,
//						new String[]{"deviceName","deviceIp","deviceMac","topic","scale"},
//						new int[]{R.id.deviceName,R.id.deviceIp,R.id.deviceMac,R.id.deviceTopic,
//						R.id.deviceScale});
				MyAdapter myAdapter=new MyAdapter(mContext, listitem);
				listview.setAdapter(myAdapter);
			}
		}
	};
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {    
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);
        btn=(Button) findViewById(R.id.btn);
        textview=(TextView) findViewById(R.id.tv);
        listview=(ListView) findViewById(R.id.lv);
        mContext=this;
        mqttInit();
        bindEvent();
	}

	private void bindEvent() {
		class BtnClickListener implements View.OnClickListener{
			@Override
			public void onClick(View v) {
				try {
					Log.i("test","enter click");
					//mqtt publish
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
						client.subscribe("post_data");
						client.setCallback(new MqttCallback(){

							@Override
							public void connectionLost(Throwable arg0) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void deliveryComplete(IMqttDeliveryToken arg0) {
								// TODO Auto-generated method stub
								Log.i("test","delivery completed!");
							}

							@Override
							public void messageArrived(String topicName,
									MqttMessage message) throws Exception {
								// TODO Auto-generated method stub
									if(topicName.equals("post_data")){
										payloadString=message.toString();
										parseData(payloadString);
										myHandler.sendEmptyMessage(0x123);
									}
							}
							
						});
						client.publish("pull_data",new MqttMessage(" ".getBytes()));
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
	
	//将得到的json字符串转为对象，并解析出各个数据，填充到listitem中。
	private void parseData(String payload) {
		try {
			JSONArray jsonArray=(JSONArray) new JSONArray(payload);
			listitem.clear();
			for(int i=0;i<jsonArray.length();i++){
				JSONObject obj=(JSONObject) jsonArray.get(i);
				HashMap<String,String> data=new HashMap<String,String>();
				data.put("deviceName","设备名称: "+obj.getString("deviceName"));
				data.put("deviceIp", "IP地址: "+obj.getString("deviceIp"));
				data.put("deviceMac", "MAC地址: "+obj.getString("deviceMac"));
				data.put("topic", "主题: "+obj.getString("topic"));
				data.put("scale", "坐标单位(y/x): "+obj.getString("scale"));
				listitem.add(data);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}