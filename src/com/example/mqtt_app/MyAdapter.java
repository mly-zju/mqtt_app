package com.example.mqtt_app;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdapter extends BaseAdapter {
	
	private ArrayList<HashMap<String,String>> listitem;
	private Context mContext;
	
	public MyAdapter(Context context, ArrayList lv){
		this.listitem=lv;
		this.mContext=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listitem.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView=LayoutInflater.from(mContext).inflate(R.layout.list_item,null);
		TextView deviceName=(TextView) convertView.findViewById(R.id.deviceName);
		TextView deviceIp=(TextView) convertView.findViewById(R.id.deviceIp);
		TextView deviceMac=(TextView) convertView.findViewById(R.id.deviceMac);
		TextView deviceTopic=(TextView) convertView.findViewById(R.id.deviceTopic);
		TextView deviceScale=(TextView) convertView.findViewById(R.id.deviceScale);
		deviceName.setText(listitem.get(position).get("deviceName"));
		deviceIp.setText(listitem.get(position).get("deviceIp"));
		deviceMac.setText(listitem.get(position).get("deviceMac"));
		deviceTopic.setText(listitem.get(position).get("topic"));
		deviceScale.setText(listitem.get(position).get("scale"));
		
		Button deviceConfig=(Button) convertView.findViewById(R.id.deviceConfig);
		Button deviceCheck=(Button) convertView.findViewById(R.id.deviceCheck);
		deviceConfig.setOnClickListener(new BtnClickListener(listitem.get(position)));
		deviceCheck.setOnClickListener(new BtnClickListener(listitem.get(position)));
		
		return convertView;
	}
	
	class BtnClickListener implements View.OnClickListener{
		private HashMap<String,String> mContent;
		
		public BtnClickListener(HashMap<String,String> data){
			this.mContent=data;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v.getId()==R.id.deviceConfig){
				Intent intent=new Intent();
				intent.putExtra("deviceName", mContent.get("deviceName"));
				intent.putExtra("deviceIp", mContent.get("deviceIp"));
				intent.putExtra("deviceMac", mContent.get("deviceMac"));
				intent.putExtra("topic", mContent.get("topic"));
				intent.putExtra("scale", mContent.get("scale"));
				intent.putExtra("deviceId", mContent.get("deviceId"));
				intent.setClass(mContext,ConfigActivity.class);
				((Activity) mContext).startActivityForResult(intent,1000);
			}else if(v.getId()==R.id.deviceCheck){
//				Intent intent=new Intent();
//				intent.setClass(mContext, DataActivity.class);
				((MainActivity)mContext).pullSingleData(mContent.get("deviceId"));
//				mContext.startActivity(intent);
			}
		}

	}
	
}
