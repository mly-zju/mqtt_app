package com.example.mqtt_app;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.mqtt_app.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
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
	private EditText et;
	private ListView listview;
	private MyAdapter myAdapter;
	private ArrayList<HashMap<String,String>> listitem=new ArrayList<HashMap<String,String>>();
	private String demoData="[{deviceName:\"testDemo\",deviceIp:\"192.168.0.2\",deviceMac:" +
			"\"xx:xx:xx:xx:xx:xx\",topic:\"test\",scale:\"temp/day\"}," +
			"{deviceName:\"testDemo2\",deviceIp:\"192.168.0.3\",deviceMac:" +
			"\"xx:xx:xx:xx:xx:xx\",topic:\"test2\",scale:\"temp/day\"}]"; //这个是fake数据
	
	private ArrayList<Float> singleItem=new ArrayList<Float>();
	private String singleDeviceName;
	private String singleDeviceY;
	
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
				myAdapter=new MyAdapter(mContext, listitem);
				listview.setAdapter(myAdapter);
			}else if(msg.what==0x124){
				Log.i("test",singleItem.get(0).toString());
				Intent intent=new Intent();
				int len=singleItem.size();
				intent.putExtra("len", len);
				for(int i=0;i<len;i++){
					intent.putExtra(i+"", singleItem.get(i));
				}
				intent.putExtra("deviceName", singleDeviceName);
				intent.putExtra("deviceScaleY", singleDeviceY);
				intent.setClass(mContext, DataActivity.class);
				startActivity(intent);
			}
		}
	};
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {    
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);
        btn=(Button) findViewById(R.id.btn);
        et=(EditText) findViewById(R.id.et);
        listview=(ListView) findViewById(R.id.lv);
        mContext=this;
        //mqttInit();
        bindEvent();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1000&&resultCode==1001){
			int index=Integer.parseInt(data.getStringExtra("deviceId"));
			HashMap<String,String> tmp=new HashMap<String,String>();
			tmp.put("deviceName",data.getStringExtra("deviceName"));
			tmp.put("deviceIp",data.getStringExtra("deviceIp"));
			tmp.put("deviceMac",data.getStringExtra("deviceMac"));
			tmp.put("topic",data.getStringExtra("deviceTopic"));
			tmp.put("scale",data.getStringExtra("deviceScale"));
			tmp.put("deviceId", ""+index);
			tmp.put("currentTime",listitem.get(index).get("currentTime"));
			tmp.put("deviceManufac", listitem.get(index).get("deviceManufac"));
			tmp.put("qos",data.getStringExtra("qos"));
			Log.i("test",tmp.toString());
			listitem.set(index,tmp);
			myAdapter.notifyDataSetChanged();
			new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					String data;
					try {
						data = reverseData();
						client.publish("change_data", new MqttMessage(data.getBytes()));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}

				private String reverseData() throws JSONException {
					// TODO Auto-generated method stub
					JSONArray jsonArray=new JSONArray();
					for(int i=0;i<listitem.size();i++){
						HashMap<String,String> tmpObj=listitem.get(i);
						JSONObject jb=new JSONObject();
						for(String key:tmpObj.keySet()){
							String tmp=tmpObj.get(key);
							if(key.equals("currentTime")||key.equals("deviceManufac")||
									key.equals("deviceId")){
								jb.put(key, tmp);
							}else{
								int begin=tmp.indexOf(':');
								if(tmp.length()>begin+1){
									jb.put(key, tmp.substring(begin+2));
								}else{
									jb.put(key, "");
								}
							}
						}
						jsonArray.put(jb);
					}
					return jsonArray.toString();
				}
				
			}).start();
		}
	}

	private void bindEvent() {
		class BtnClickListener implements View.OnClickListener{
			@Override
			public void onClick(View v) {
				if(v.getId()==R.id.btn){
					try {
						//mqtt publish
						String ip=et.getText().toString();
						url="tcp://"+ip+":1883";
						mqttInit();
					} catch (Exception e) {
						Log.i("test",e.toString());
						e.printStackTrace();
					}
				}
			}
		}
		btn.setOnClickListener(new BtnClickListener());
	}
	
	public void mqttInit(){
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
						client.subscribe("post_single_data");
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
//										Log.i("test5","message arrived!");
										payloadString=message.toString();
//										Log.i("test5",payloadString);
										parseData(payloadString);
										myHandler.sendEmptyMessage(0x123);
									}else if(topicName.equals("post_single_data")){
										payloadString=message.toString();
										Log.i("test!",payloadString);
										parseSingleData(payloadString);
										myHandler.sendEmptyMessage(0x124);
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
				data.put("deviceId", Integer.toString(i));
				data.put("currentTime", obj.getString("currentTime"));
				data.put("deviceManufac", obj.getString("deviceManufac"));
				data.put("qos", "qos优先级: "+obj.getString("qos"));
				listitem.add(data);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void parseSingleData(String payload){
		try{
			JSONArray jsonArray=(JSONArray) new JSONArray(payload);
			singleItem.clear();
			for(int i=1;i<jsonArray.length();i++){
//				int tmp=((Integer) jsonArray.get(i)).intValue();
//				singleItem.add((float) tmp);
				String tmp=jsonArray.getString(i);
				singleItem.add(Float.parseFloat(tmp));
			}
		}catch(Exception e){
			Log.i("err",e.toString());
			e.printStackTrace();
		}
	}
	
	public void pullSingleData(final String id){
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					singleDeviceName=listitem.get(Integer.parseInt(id)).get("deviceName");
					String tmp=listitem.get(Integer.parseInt(id)).get("scale");
					int begin=tmp.indexOf(':')+1;
			        int end=tmp.indexOf('/',begin);
			        if(end-begin>1){
			        	singleDeviceY=tmp.substring(begin+1,end);
			        }else{
			        	singleDeviceY="";
			        }
					
					client.publish("pull_single_data",new MqttMessage(id.getBytes()));
				} catch (MqttPersistenceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MqttException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();
	}
	
}