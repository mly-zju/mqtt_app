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

public class MyView2 extends View implements Runnable { 
//    private int XPoint = 60; 
//    private int YPoint = 260; 
//    private int XScale = 8;
//    private int YScale = 40; 
//    private int XLength = 380; 
//    private int YLength = 240; 
	private int XPoint = 60;
	private int YPoint = 520;
	private int XScale = 16;
	private int YScale = 80;
	private int XLength = 760;
	private int YLength = 480;
	private Paint paint=null;
    
//    private WindowManager wm=(WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
//	private int myWidth=wm.getDefaultDisplay().getWidth();
//	private int scale=myWidth/XLength;
                        
    private int MaxDataSize = XLength / XScale; 
                        
    private static ArrayList<Integer> data = new ArrayList<Integer>(); 
    private int tt=0;
                        
    private String[] YLabel = new String[YLength / YScale]; 
                        
    public MyView2(Context context) { 
        super(context); 
        paint=new Paint();
        for (int i = 0; i < YLabel.length; i++) { 
            YLabel[i] = (i + 1) + "M/s"; 
        } 
                        
        new Thread(this).start();
    } 
                        
    public MyView2(Context context, AttributeSet attrs) { 
        super(context, attrs); 
        paint=new Paint();
        for (int i = 0; i < YLabel.length; i++) { 
            YLabel[i] = (i + 1) + "M/s"; 
        } 
  
        new Thread(this).start();
    } 
    
    public MyView2(Context context, ArrayList<Integer> d) { 
        super(context); 
        data=d;
        paint=new Paint();
        for (int i = 0; i < YLabel.length; i++) { 
            YLabel[i] = (i + 1) + "M/s"; 
        } 

        
        new Thread(this).start();
    } 
    
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        // TODO Auto-generated method stub  
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
        setMeasuredDimension(XLength+100, YLength+100);  
    }  
    
    public void setData(ArrayList<Integer> d){
    	data=d;
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
            canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 5, YPoint - i 
                    * YScale, paint); // 刻度 
                        
            canvas.drawText(YLabel[i], XPoint - 50, YPoint - i * YScale, paint);// 文字 
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
