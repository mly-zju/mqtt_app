package com.example.mqtt_app;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class MyView extends View implements Runnable { 

	private static int XPoint = 100;
	private static int YPoint = 520;
	
	private static int XLength = 760;
	private static int MaxDataSize;
	private static int XScale;
	
	private static int YLength = 480;
	private static float YScale;
	private static int YLabelScale = (int) (YLength/4);
	private static int YMax;
	private static int YMin;
	
	private Paint paint=null;
    
//    private WindowManager wm=(WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
//	private int myWidth=wm.getDefaultDisplay().getWidth();
//	private int scale=myWidth/XLength;
                        
    private static ArrayList<Float> data = new ArrayList<Float>(); 
                        
    private static String[] YLabel = new String[5]; 
    private static String YName;
    private static String deviceName;
    
    private static boolean isWeek;
    private static int timeNow;
    
    //private String[] week={"周日","周一","周二","周三","周四","周五","周六"};
    private String[] week={"周二","周三","周四","周五","周六","周日","周一"};
    
    private String[] hour={"01", "02", "03", "04", "05", "06",
    		"07", "08", "09", "10", "11", "12", "13", "14", "15",
    		"16", "17", "18", "19", "20", "21", "22", "23", "00"
    };
                        
    public MyView(Context context) { 
        super(context); 
        paint=new Paint();
//        for (int i = 0; i < YLabel.length; i++) { 
//            YLabel[i] = (i + 1) + "M/s"; 
//        } 
                        
        new Thread(this).start();
    } 
                        
    public MyView(Context context, AttributeSet attrs) { 
        super(context, attrs); 
        paint=new Paint();
//        for (int i = 0; i < YLabel.length; i++) { 
//            YLabel[i] = (i + 1) + "M/s"; 
//        } 
  
        new Thread(this).start();
    } 
    
    public MyView(Context context, ArrayList<Float> d, String deviceName, 
    		String deviceScaleY, String deviceScaleX, String currentTime) { 
        super(context); 
        data=d;
        YName=deviceScaleY;
        this.deviceName=deviceName;
        
        paint=new Paint();
        //MaxDataSize=d.size();
        if(deviceScaleX.equals("day")){
        	MaxDataSize=7;
        	isWeek=true;
        }else{
        	MaxDataSize=24;
        	isWeek=false;
        }
        Log.i("test",deviceScaleX);
        Log.i("test",MaxDataSize+"");
        timeNow=Integer.parseInt(currentTime);
        
    	XScale=XLength/(MaxDataSize-1);
    	
    	Log.i("myview","first");
    	Log.i("myview",XScale+"");
    	//int len=data.size();
    	float tmpMax,tmpMin;
    	tmpMax=tmpMin=data.get(24-MaxDataSize);
    	for(int i=24-MaxDataSize+1;i<24;i++){
    		if(data.get(i)>tmpMax){
    			tmpMax=data.get(i);
    		}
    		if(data.get(i)<tmpMin){
    			tmpMin=data.get(i);
    		}
    	}
    	Log.i("myview","second");
    	if(tmpMax>=10){
    		YMax=((int) (tmpMax/10)+1)*10;
        	YMin=((int) (tmpMin/10))*10;
        	Log.i("myview",YMax+" -YMax");
        	Log.i("myview",YMin+" -YMin");
        	if(YMax==YMin){
        		if(YMin>10){
        			YMin=YMin-10;
        		}else{
        			YMin=0;
        		}
        	}
    	}else{
    		YMax=(int)(tmpMax+1);
    		YMin=(int)(tmpMin);
    		if(YMax==YMin){
    			YMin=YMin-1;
    		}
    	}
    	YScale=((float)YLength)/(YMax-YMin);
    	Log.i("myview",YLength+" -YLength");
    	Log.i("myview",YMax-YMin+" -max-min");
    	Log.i("myview",YScale+" -YScale");
    	Log.i("myview","new!");
    	float gap=(float)(YMax-YMin)/4;
    	Log.i("myview",gap+" -gap");
    	for(int i=0;i<5;i++){
    		YLabel[i]=(YMin+gap*i)+" "+YName;
    	}
        

        
        new Thread(this).start();
    } 
    
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        // TODO Auto-generated method stub  
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
        setMeasuredDimension(XLength+150, YLength+100);  
    }  
                        
    @Override 
    protected void onDraw(Canvas canvas) { 
        super.onDraw(canvas); 
        //Paint paint = new Paint(); 
        paint.setStyle(Paint.Style.STROKE); 
        paint.setAntiAlias(true); // 去锯齿 
        paint.setColor(Color.BLUE); 
                        
        // 画Y轴 
        canvas.drawLine(XPoint, YPoint - YLength - 20, XPoint, YPoint, paint); 
                        
        // Y轴箭头 
        canvas.drawLine(XPoint, YPoint - YLength - 20, XPoint - 3, YPoint - YLength 
                + 6 -20, paint); // 箭头 
        canvas.drawLine(XPoint, YPoint - YLength - 20, XPoint + 3, YPoint - YLength 
                + 6 - 20, paint); 
                        
        // 添加刻度和文字 
        for (int i = 0; i<5; i++) { 
        	
            canvas.drawLine(XPoint, YPoint - i * YLabelScale, XPoint + 5, YPoint - i 
                    * YLabelScale, paint); // 刻度 
                        
            canvas.drawText(YLabel[i], XPoint - 90, YPoint - i * YLabelScale, paint);// 文字 
        } 
        // 画X轴 
        canvas.drawLine(XPoint, YPoint, XPoint + XLength+20, YPoint, paint); 
        canvas.drawLine(XPoint+XLength+20, YPoint, XPoint+XLength+20-6, YPoint-3, paint);
        canvas.drawLine(XPoint+XLength+20, YPoint, XPoint+XLength+20-6, YPoint+3, paint);
        
        int begin=24-MaxDataSize;
        for(int i=begin; i<24;i++){
        	canvas.drawLine(XPoint+(i-begin)*XScale, YPoint, XPoint+(i-begin)*XScale, YPoint+5, paint);
        	if(isWeek){
        		int index=(timeNow+i-begin)%7;
        		canvas.drawText(week[index], XPoint+(i-begin)*XScale-5, YPoint+20, paint);
        		//canvas.drawText(index+"", XPoint+(i-begin)*XScale-5, YPoint+20, paint);
        	}else{
        		int index=(timeNow+i-begin)%24;
        		canvas.drawText(hour[index], XPoint+(i-begin)*XScale-5, YPoint+20, paint);
        	}
        }
                        
        // 绘折线 
        /*
         * if(data.size() > 1){ for(int i=1; i<data.size(); i++){
         * canvas.drawLine(XPoint + (i-1) * XScale, YPoint - data.get(i-1) *
         * YScale, XPoint + i * XScale, YPoint - data.get(i) * YScale, paint); }
         * }
         */ 
        //paint.setStyle(Paint.Style.FILL); 
        if (data.size() > 1) { 
            Path path = new Path(); 
            path.moveTo(XPoint, YPoint); 
            begin=24-MaxDataSize;
            for (int i = begin; i < 24; i++) { 
                path.lineTo(XPoint + (i-begin) * XScale, YPoint -(data.get(i)-YMin) * YScale); 
            } 
            //path.lineTo(XPoint + (data.size() - 1) * XScale, YPoint); 
            canvas.drawPath(path, paint); 
        } 
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		 while (true) { 
           try { 
               Thread.sleep(1000); 
           } catch (InterruptedException e) { 
               e.printStackTrace(); 
           } 
//           if (data.size() >= MaxDataSize) { 
//               data.remove(0); 
//           } 
//           data.add(new Random().nextInt(4) + 1); 
           postInvalidate();  
       } 
	} 
}
