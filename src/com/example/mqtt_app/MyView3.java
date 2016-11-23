package com.example.mqtt_app;

import java.util.ArrayList;
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

public class MyView3 extends View implements Runnable { 

	private static int XPoint = 60;
	private static int YPoint = 520;
	
	private static int XLength = 760;
	private static int MaxDataSize;
	private static int XScale;
	
	private static int YLength = 480;
	private static float YScale;
	private static int YLabelScale = (int) (YLength/5);
	private static int YMax;
	private static int YMin;
	
	private Paint paint=null;
    
//    private WindowManager wm=(WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
//	private int myWidth=wm.getDefaultDisplay().getWidth();
//	private int scale=myWidth/XLength;
                        
    private static ArrayList<Float> data = new ArrayList<Float>(); 
    private int tt=0;
                        
    private static String[] YLabel = new String[5]; 
                        
    public MyView3(Context context) { 
        super(context); 
        paint=new Paint();
//        for (int i = 0; i < YLabel.length; i++) { 
//            YLabel[i] = (i + 1) + "M/s"; 
//        } 
                        
        new Thread(this).start();
    } 
                        
    public MyView3(Context context, AttributeSet attrs) { 
        super(context, attrs); 
        paint=new Paint();
//        for (int i = 0; i < YLabel.length; i++) { 
//            YLabel[i] = (i + 1) + "M/s"; 
//        } 
  
        new Thread(this).start();
    } 
    
//    public MyView(Context context, String deviceName, ArrayList<Float> d) { 
//        super(context); 
//        paint=new Paint();
//        for (int i = 0; i < YLabel.length; i++) { 
//            YLabel[i] = (i + 1) + "M/s"; 
//        } 
//
//        
//        new Thread(this).start();
//    } 
    
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        // TODO Auto-generated method stub  
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
        setMeasuredDimension(XLength+100, YLength+100);  
    }  
    
    public void setData(ArrayList<Float> d){
    	MaxDataSize=7;
    	XScale=XLength/MaxDataSize;
    	data=d;
    	int len=data.size();
    	float tmpMax,tmpMin;
    	tmpMax=tmpMin=data.get(0);
    	for(int i=1;i<len;i++){
    		if(data.get(i)>tmpMax){
    			tmpMax=data.get(i);
    		}
    		if(data.get(i)<tmpMin){
    			tmpMin=data.get(i);
    		}
    	}
    	YMax=((int) (tmpMax/10)+1)*10;
    	YMin=((int) (tmpMin/10))*10;
    	if(YMax==YMin&&YMin>10){
    		YMin=YMax-10;
    	}else{
    		YMin=0;
    	}
    	YScale=YLength/(YMax/YMin);
    	float gap=(YMax-YMin)/4;
    	for(int i=0;i<5;i++){
    		YLabel[i]=(YMin+gap*i)+" ";
    	}
    }
                        
    @Override 
    protected void onDraw(Canvas canvas) { 
        super.onDraw(canvas); 
        //Paint paint = new Paint(); 
        paint.setStyle(Paint.Style.STROKE); 
        paint.setAntiAlias(true); // 去锯齿 
        paint.setColor(Color.BLUE); 
                        
        // 画Y轴 
        canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paint); 
                        
        // Y轴箭头 
        canvas.drawLine(XPoint, YPoint - YLength, XPoint - 3, YPoint - YLength 
                + 6, paint); // 箭头 
        canvas.drawLine(XPoint, YPoint - YLength, XPoint + 3, YPoint - YLength 
                + 6, paint); 
                        
        // 添加刻度和文字 
        for (int i = 0; i * YScale < YLength; i++) { 
            canvas.drawLine(XPoint, YPoint - i * YLabelScale, XPoint + 5, YPoint - i 
                    * YLabelScale, paint); // 刻度 
                        
            canvas.drawText(YLabel[i], XPoint - 50, YPoint - i * YLabelScale, paint);// 文字 
        } 
        // 画X轴 
        canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint); 
                        
        // 绘折线 
        /*
         * if(data.size() > 1){ for(int i=1; i<data.size(); i++){
         * canvas.drawLine(XPoint + (i-1) * XScale, YPoint - data.get(i-1) *
         * YScale, XPoint + i * XScale, YPoint - data.get(i) * YScale, paint); }
         * }
         */ 
        paint.setStyle(Paint.Style.FILL); 
        if (data.size() > 1) { 
            Path path = new Path(); 
            path.moveTo(XPoint, YPoint); 
            for (int i = 0; i < data.size(); i++) { 
                path.lineTo(XPoint + i * XScale, YPoint - data.get(i) * YScale); 
            } 
            path.lineTo(XPoint + (data.size() - 1) * XScale, YPoint); 
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
