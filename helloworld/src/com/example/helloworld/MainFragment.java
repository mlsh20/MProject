package com.example.helloworld;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import com.example.helloworld.TimeLine.CallbackInterface;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/*處理球員&球的觸控、儲存路徑，畫出路徑*/

public class MainFragment extends Fragment{

	private int playing=0;
	
	private int Total_time = 8000;// 時間軸的最大值
	private ImageView player1, player2, player3, player4, player5, ball;
	private Player P1 = new Player();
	private Player P2 = new Player();
	private Player P3 = new Player();
	private Player P4 = new Player();
	private Player P5 = new Player();
	private Player B = new Player();

	private Player P1_recommend = new Player();
	private Player P2_recommend = new Player();
	private Player P3_recommend = new Player();
	private Player P4_recommend = new Player();
	private Player P5_recommend = new Player();
	private Player B_recommend = new Player();
	
	
	
	
	
	private int P1_Initial_Position_x = -1;
	private int P2_Initial_Position_x = -1;
	private int P3_Initial_Position_x = -1;
	private int P4_Initial_Position_x = -1;
	private int P5_Initial_Position_x = -1;
	private int B_Initial_Position_x = -1;
	private int P1_Initial_Position_y = -1;
	private int P2_Initial_Position_y = -1;
	private int P3_Initial_Position_y = -1;
	private int P4_Initial_Position_y = -1;
	private int P5_Initial_Position_y = -1;
	private int B_Initial_Position_y = -1;
	
	private int P1_startIndex = 0;
	private int P2_startIndex = 0;
	private int P3_startIndex = 0;
	private int P4_startIndex = 0;
	private int P5_startIndex = 0;
	private int B_startIndex = 0;
	
	private int seekbar_tmp_id=0;

	/**畫圖變數**/
	private ImageView circle;
	private Path p;
	Bitmap tempBitmap;
	Canvas tempCanvas;
	private Paint player1_paint;
	private Paint player2_paint;
	private Paint player3_paint;
	private Paint player4_paint;
	private Paint player5_paint;
	private Paint ball_paint;
	private Paint transparent_paint;
	private Paint paint;
	/*********/
	/*************************曲線變數************************************/
	private int N = 3;
	private Vector<Integer> P1_curve_x = new Vector();
	private Vector<Integer> P1_curve_y = new Vector();
	private int c1_idx=0;
	private Vector<Integer> P2_curve_x = new Vector();
	private Vector<Integer> P2_curve_y = new Vector();
	private int c2_idx=0;
	private Vector<Integer> P3_curve_x = new Vector();
	private Vector<Integer> P3_curve_y = new Vector();
	private int c3_idx=0;
	private Vector<Integer> P4_curve_x = new Vector();
	private Vector<Integer> P4_curve_y = new Vector();
	private int c4_idx=0;
	private Vector<Integer> P5_curve_x = new Vector();
	private Vector<Integer> P5_curve_y = new Vector();
	private int c5_idx=0;
	private Vector<Integer> Ball_curve_x = new Vector();
	private Vector<Integer> Ball_curve_y = new Vector();
	private int Ball_idx=0;
	/*************************************************************/
	/**錄製變數**/
	private boolean recordcheck = false;
	private int seekBarCallbackStartTime;
	private int seekBarCallbackDuration;
	private int seekBarCallbackId;
	private Vector<RunBag> RunLine = new Vector();
	/*********/
	private Handler handler = new Handler();

	private CallbackInterface mCallback;
	public interface CallbackInterface {
		public void RunLineInfo(Vector<RunBag> in_RunLine);

		public void P1Info(Player in_P1);

		public void P2Info(Player in_P2);

		public void P3Info(Player in_P3);

		public void P4Info(Player in_P4);

		public void P5Info(Player in_P5);

		public void BInfo(Player in_B);
		
		public void pass_seekbar(String player);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Log.i("debug", "onCreateView()............");
		return inflater.inflate(R.layout.main_layout, container,false);	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//Log.d("debug", "MainFragment onActivityCreated!");
		player1 = (ImageView) getView().findViewById(R.id.player1);
		player1.setOnTouchListener(player1_Listener);// 觸控時監聽
		player2 = (ImageView) getView().findViewById(R.id.player2);
		player2.setOnTouchListener(player2_Listener);// 觸控時監聽
		player3 = (ImageView) getView().findViewById(R.id.player3);
		player3.setOnTouchListener(player3_Listener);// 觸控時監聽
		player4 = (ImageView) getView().findViewById(R.id.player4);
		player4.setOnTouchListener(player4_Listener);// 觸控時監聽
		player5 = (ImageView) getView().findViewById(R.id.player5);
		player5.setOnTouchListener(player5_Listener);// 觸控時監聽
		ball = (ImageView) getView().findViewById(R.id.ball);
		ball.setOnTouchListener(ball_Listener);// 觸控時監聽
		
		
		p=new Path();
		
		paint=new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLUE);
		paint.setAlpha(100);
		paint.setStyle(Paint.Style.STROKE);
		
		player1_paint=new Paint();
		player1_paint.setAntiAlias(true); // 設置畫筆的鋸齒效果。 true是去除。
		player1_paint.setColor(Color.BLACK); // 設置顏色
		player1_paint.setAlpha(100);
		
		player2_paint=new Paint();
		player2_paint.setAntiAlias(true);
		player2_paint.setColor(Color.TRANSPARENT);
		player2_paint.setColor(Color.RED);
		player2_paint.setAlpha(100);
		
		player3_paint=new Paint();
		player3_paint.setAntiAlias(true);
		player3_paint.setColor(Color.BLUE);
		player3_paint.setAlpha(100);

		player4_paint=new Paint();
		player4_paint.setAntiAlias(true);
		player4_paint.setColor(Color.GREEN);
		player4_paint.setAlpha(100);
		
		player5_paint=new Paint();
		player5_paint.setAntiAlias(true);
		player5_paint.setColor(Color.CYAN);
		player5_paint.setAlpha(100);
		
		ball_paint=new Paint();
		ball_paint.setAntiAlias(true);
		ball_paint.setColor(Color.TRANSPARENT);
		ball_paint.setColor(Color.MAGENTA);
		ball_paint.setAlpha(100);
		
		transparent_paint=new Paint();
		transparent_paint.setAntiAlias(true); // 設置畫筆的鋸齒效果。 true是去除。
		transparent_paint.setColor(Color.TRANSPARENT); // 設置透明顏色
		
		circle=(ImageView) getActivity().findViewById(R.id.circle);
		
		/*獲取元件的長與寬，並初始化tempBitmap，接著先放一次的透明路徑在circle上*/
		ViewTreeObserver vto2 = circle.getViewTreeObserver();   
		   vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
		       @Override   
		       public void onGlobalLayout() {  
		    	
		       	tempBitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);//初始化tempBitmap，指定大小為螢幕大小(大小同circle)
		       	tempCanvas = new Canvas(tempBitmap);//畫透明路徑
		   		tempCanvas.drawBitmap(tempBitmap, 0, 0, null);//畫透明路徑
		   		tempCanvas.drawCircle(1, 1, 5, transparent_paint);//畫透明路徑
		   		circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));//把tempBitmap放進circle裡
		        //Log.i("debug", "tempBitmap's width = "+Integer.toString(tempBitmap.getWidth()));
		   		//Log.i("debug", "tempBitmap's height = "+Integer.toString(tempBitmap.getHeight()));
		        circle.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
		           
		        
		        
		        
		       }   
		  });
		
	}
	
	@Override
    public void onStart() {
        super.onStart();
        //Log.i("debug", "onStart()............");
        /**/
    }
	
	@Override
	public void onResume() {
		//Log.d("debug", "MainFragment onResume!");
		super.onResume();
		
	}

	@Override
	public void onPause() {
		//Log.d("debug", "MainFragment onResume!");
		super.onPause();
	}

	public void onAttach(Activity activity) {
		//Log.d("debug", "MainF onAttach!");
		super.onAttach(activity);

		try {
			mCallback = (CallbackInterface) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement MainFragment.CallbackInterface!");
		}
	}

	public void set_playing(int input){
		playing=input;
	}
	
	public void pass_RunLine_Player_Info() {
		mCallback.P1Info(P1);
		mCallback.P2Info(P2);
		mCallback.P3Info(P3);
		mCallback.P4Info(P4);
		mCallback.P5Info(P5);
		mCallback.BInfo(B);
		mCallback.RunLineInfo(RunLine);

	}

	public void pass_start_time(int input) {
		seekBarCallbackStartTime = input;
	}

	public void pass_duration(int input) {
		seekBarCallbackDuration = input;
	}

	public void pass_id(int input){
		seekBarCallbackId = input;
	}
	public void pass_recordcheck(boolean input) {
		recordcheck = input;
	}
	public void set_seekBar_to_RunBag(Vector <Integer> input){
		/**
		 * Vector<Integer> input: 0=Id,1=StartTime,2=Duration
		 * **/
		RunBag tmp = new RunBag();
		tmp=RunLine.get(input.get(0));
		tmp.setStartTime(input.get(1));
		tmp.setDuration(input.get(2));
		RunLine.set(input.get(0), tmp);
		//Log.d("debug", "Set! "+Integer.toString(input.get(0)));
	}
	
	public void clear_paint(){//清除筆跡
		tempBitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);//初始化tempBitmap，指定大小為螢幕大小(大小同circle)
       	tempCanvas = new Canvas(tempBitmap);//畫透明路徑
   		tempCanvas.drawBitmap(tempBitmap, 0, 0, null);//畫透明路徑
   		tempCanvas.drawCircle(1, 1, 5, transparent_paint);//畫透明路徑
   		circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));//把tempBitmap放進circle裡
   		p.reset();
   		c1_idx=0;
		c2_idx=0;
		c3_idx=0;
		c4_idx=0;
		c5_idx=0;
		Ball_idx=0;
		P1_curve_x.clear();
		P1_curve_y.clear();
		P2_curve_x.clear();
		P2_curve_y.clear();
		P3_curve_x.clear();
		P3_curve_y.clear();
		P4_curve_x.clear();
		P4_curve_y.clear();
		P5_curve_x.clear();
		P5_curve_y.clear();
		Ball_curve_x.clear();
		Ball_curve_y.clear();
	}
	
	public void clear_record(){
		P1.clear_all();
		P2.clear_all();
		P3.clear_all();
		P4.clear_all();
		P5.clear_all();
		B.clear_all();
		RunLine.clear();
		P1_startIndex = 0;
		P2_startIndex = 0;
		P3_startIndex = 0;
		P4_startIndex = 0;
		P5_startIndex = 0;
		B_startIndex = 0;
		seekbar_tmp_id=0;
		clear_paint();
		TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
		timefrag.clear_record_layout();
		playing=0;
	}
	
	public int lagrange(Vector<Integer> px,Vector<Integer> py, float x){
		int y=0;
		float tmpy=0;
	    for (int i=0; i<px.size(); ++i)
	    {
	        float a = 1, b = 1;
	        for (int j=0; j<px.size(); ++j)
	        {
	            if (j == i) continue;
	            a *= x     - px.get(j);
	            b *= px.get(i) - px.get(j);
	        }
	        tmpy=(py.get(i) * a / b);
	        y += py.get(i) * a / b;
	    }	 
		return y;
	}
	
	//TODO DTW
	/**************************************************************************/
	public void Do_DTW(){

		Log.d("debug", "DTW = "+Double.toString(DTW(P1.getCmpltRoad(),P1_recommend.getCmpltRoad())));
		Log.d("debug", "DTW = "+Double.toString(DTW(P2.getCmpltRoad(),P2_recommend.getCmpltRoad())));
		Log.d("debug", "DTW = "+Double.toString(DTW(P3.getCmpltRoad(),P3_recommend.getCmpltRoad())));
		Log.d("debug", "DTW = "+Double.toString(DTW(P4.getCmpltRoad(),P4_recommend.getCmpltRoad())));
		Log.d("debug", "DTW = "+Double.toString(DTW(P5.getCmpltRoad(),P5_recommend.getCmpltRoad())));
		Log.d("debug", "DTW = "+Double.toString(DTW(B.getCmpltRoad(),B_recommend.getCmpltRoad())));
	
	}
	
	public double DTW(Vector<Integer> road_a,Vector<Integer> road_b){
		
		Vector<Integer> split_a = new Vector();
		Vector<Integer> split_b = new Vector();
		
		Vector<Double> nor_a=new Vector();
		Vector<Double> nor_b=new Vector();
		
		Vector<myPoint> finish_a = new Vector();
		Vector<myPoint> finish_b = new Vector();
		
		//save pure road without 0
		if(road_a.size()==0){
			Log.d("debug", "road_a size = 0");
			return -1;
		}
		else if(road_b.size()==0){
			Log.d("debug", "road_b size = 0");
			return -1;
		}
		else{
			
			Log.d("debug", "road_a.size() =  "+Integer.toString(road_a.size()));
			Log.d("debug", "road_b.size() =  "+Integer.toString(road_b.size()));
			
			int count_0=0;
			for(int i=0;i<road_a.size();i++){
				if(road_a.get(i)!=0){
					split_a.add(road_a.get(i));
				}
				else{
					count_0++;
					Log.d("debug", "count_0 =  "+Integer.toString(count_0));
				}
			}
			
			count_0=0;
			for(int i=0;i<road_b.size();i++){
				if(road_b.get(i)!=0){
					split_b.add(road_b.get(i));
				}
				else{
					count_0++;
					Log.d("debug", "count_0 =  "+Integer.toString(count_0));
				}
			}
			
			nor_a=normalization(split_a);
			nor_b=normalization(split_b);
			
			for(int i=0;i<nor_a.size();i++){
				myPoint tmp = new myPoint();
				tmp.x=nor_a.get(i);
				tmp.y=nor_a.get(i+1);
				finish_a.add(tmp);
				i=i+1;
			}
			
			for(int i=0;i<nor_b.size();i++){
				myPoint tmp = new myPoint();
				tmp.x=nor_b.get(i);
				tmp.y=nor_b.get(i+1);
				finish_b.add(tmp);
				i=i+1;
			}
			
			Log.d("debug", "finish_a.size() =  "+Integer.toString(finish_a.size()));
			Log.d("debug", "finish_b.size() =  "+Integer.toString(finish_b.size()));
			
			double D[][]=new double[finish_a.size()][finish_b.size()];
			
			for(int i=0;i<finish_a.size();i++){
				for(int j=0;j<finish_b.size();j++){
					if((i-1)<0 || (j-1)<0){
						D[i][j]=Dist(finish_a.get(i).x,finish_a.get(i).y,finish_b.get(j).x,finish_b.get(j).y);
					}
					else{
						D[i][j]=Dist(finish_a.get(i).x,finish_a.get(i).y,finish_b.get(j).x,finish_b.get(j).y)+Math.min(D[i-1][j],Math.min(D[i][j-1],D[i-1][j-1]));
					}
					//Log.d("debug", "D["+Integer.toString(i)+"]["+Integer.toString(j)+"]= "+Double.toString(D[i][j]));
				}
			}
			return D[(finish_a.size())-1][(finish_b.size())-1];
		}
		
	}
	
	/*public double DTW(Vector<Float> road_a,Vector<Float> road_b){
		
		Vector<Float> split_a = new Vector();
		Vector<Float> split_b = new Vector();
		
		Vector<myPoint> finish_a = new Vector();
		Vector<myPoint> finish_b = new Vector();
		
		//save pure road without 0
		if(road_a.size()==0){
			Log.d("debug", "road_a size = 0");
			return -1;
		}
		else if(road_b.size()==0){
			Log.d("debug", "road_b size = 0");
			return -1;
		}
		else{
			
			Log.d("debug", "road_a.size() =  "+Integer.toString(road_a.size()));
			Log.d("debug", "road_b.size() =  "+Integer.toString(road_b.size()));
			
			int count_0=0;
			for(int i=0;i<road_a.size();i++){
				myPoint tmp = new myPoint();
				if(road_a.get(i)!=0){
					tmp.x=road_a.get(i);
					tmp.y=road_a.get(i+1);
					finish_a.add(tmp);
					i=i+1;
				}
				else{
					count_0++;
					Log.d("debug", "count_0 =  "+Integer.toString(count_0));
				}
			}
			
			count_0=0;
			for(int i=0;i<road_b.size();i++){
				myPoint tmp = new myPoint();
				if(road_b.get(i)!=0){
					tmp.x=road_b.get(i);
					tmp.y=road_b.get(i+1);
					finish_b.add(tmp);
					i=i+1;
				}
				else{
					count_0++;
					Log.d("debug", "count_0 =  "+Integer.toString(count_0));
				}
			}
			
			Log.d("debug", "finish_a.size() =  "+Integer.toString(finish_a.size()));
			Log.d("debug", "finish_b.size() =  "+Integer.toString(finish_b.size()));
			
			
			
			
			double D[][]=new double[finish_a.size()/3][finish_a.size()/3];
			
			for(int i=0;i<finish_a.size()/3;i++){
				for(int j=0;j<finish_a.size()/3;j++){
					if((i-1)<0 || (j-1)<0){
						D[i][j]=Dist(finish_a.get(i).x,finish_a.get(i).y,finish_b.get(i).x,finish_b.get(i).y);
					}
					else{
						D[i][j]=Dist(finish_a.get(i).x,finish_a.get(i).y,finish_b.get(i).x,finish_b.get(i).y)+Math.min(D[i-1][j],Math.min(D[i][j-1],D[i-1][j-1]));
					}
					//Log.d("debug", "D["+Integer.toString(i)+"]["+Integer.toString(j)+"]= "+Double.toString(D[i][j]));
				}
			}
			return D[(finish_a.size()/3)-1][(finish_a.size()/3)-1];
		}
		
	}*/
	
	public double Dist(int xi,int yi,int xj,int yj){

		double dist=0;
		int x = Math.abs(xi-xj);
		//Log.d("debug", "abs x =  "+Integer.toString(x));
		int y = Math.abs(yi-yj);
		//Log.d("debug", "abs y =  "+Integer.toString(y));
		
		dist = Math.sqrt((x*x)+(y*y));
		//Log.d("debug", "dist =  "+Double.toString(dist));
		
		return dist;
	}
	
	public double Dist(double xi,double yi,double xj,double yj){

		double dist=0;
		double x = Math.abs(xi-xj);
		//Log.d("debug", "abs x =  "+Integer.toString(x));
		double y = Math.abs(yi-yj);
		//Log.d("debug", "abs y =  "+Integer.toString(y));
		
		dist = Math.sqrt((x*x)+(y*y));
		//Log.d("debug", "dist =  "+Double.toString(dist));
		
		return dist;
	}
	
	public Vector<Double> normalization(Vector<Integer> road){
		int x_sum=0;
		int y_sum=0;
		float x_mean=0;
		float y_mean=0;
		int x_sum_tmp=0;
		int y_sum_tmp=0;
		double x_standard_deviation=0;
		double y_standard_deviation=0;
		
		Vector<Double> n_road=new Vector();
		
		/*x*/
		for(int i=0;i<road.size();i=i+2){
			x_sum+=road.get(i);
		}
		x_mean=x_sum/(road.size()/2);
		
		for(int i=0;i<road.size();i=i+2){
			x_sum_tmp+=(road.get(i)-x_mean)*(road.get(i)-x_mean);
		}
		x_standard_deviation=Math.sqrt(x_sum_tmp/(road.size()/2));
		
		/*y*/
		for(int i=1;i<road.size();i=i+2){
			y_sum+=road.get(i);
		}
		y_mean=y_sum/(road.size()/2);
		
		for(int i=1;i<road.size();i=i+2){
			y_sum_tmp+=(road.get(i)-y_mean)*(road.get(i)-y_mean);
		}
		y_standard_deviation=Math.sqrt(y_sum_tmp/(road.size()/2));
		
		
		/*save new normalized road*/
		for(int i=0;i<road.size();i++){
			n_road.add((road.get(i)-x_mean)/x_standard_deviation);
			i=i+1;
			n_road.add((road.get(i)-y_mean)/y_standard_deviation);
		}
		
		
		
		return n_road;
	}
	
	
	/**************************************************************************/
	
	
//TODO 儲存、載入戰術(按鈕的判斷在ButtonDraw.java裡面)
	/**************************************************************************/
	public void save_dialog(){
    	
    	File dir = getActivity().getBaseContext().getExternalFilesDir(null);
    			
    	//Log.d("debug", "RunLine.RunBagInfo = "+RunLine.get(0).getRunBagInfo());
    	//在該目錄底下開啟或建立檔名為 "test.txt" 的檔案
    	File outFile = new File(dir, "myStrategy.txt");
    	String tmp=new String();
    	//將資料寫入檔案中，若 package name 為 com.myapp
    	//就會產生 /data/data/com.myapp/files/test.txt 檔案
    	
    	
    	//還要存Initial_Position
    	if(P1_Initial_Position_x!=-1){
    		tmp+="P1_Initial_Position\n";
    		tmp+=Integer.toString(P1_Initial_Position_x)+"\n";
    		tmp+=Integer.toString(P1_Initial_Position_y)+"\n";
    		tmp+="---\n";
    	}
    	if(P2_Initial_Position_x!=-1){
    		tmp+="P2_Initial_Position\n";
    		tmp+=Integer.toString(P2_Initial_Position_x)+"\n";
    		tmp+=Integer.toString(P2_Initial_Position_y)+"\n";
    		tmp+="---\n";
    	}
    	if(P3_Initial_Position_x!=-1){
    		tmp+="P3_Initial_Position\n";
    		tmp+=Integer.toString(P3_Initial_Position_x)+"\n";
    		tmp+=Integer.toString(P3_Initial_Position_y)+"\n";
    		tmp+="---\n";
    	}
    	if(P4_Initial_Position_x!=-1){
    		tmp+="P4_Initial_Position\n";
    		tmp+=Integer.toString(P4_Initial_Position_x)+"\n";
    		tmp+=Integer.toString(P4_Initial_Position_y)+"\n";
    		tmp+="---\n";
    	}
    	if(P5_Initial_Position_x!=-1){
    		tmp+="P5_Initial_Position\n";
    		tmp+=Integer.toString(P5_Initial_Position_x)+"\n";
    		tmp+=Integer.toString(P5_Initial_Position_y)+"\n";
    		tmp+="---\n";
    	}
    	if(B_Initial_Position_x!=-1){
    		tmp+="B_Initial_Position\n";
    		tmp+=Integer.toString(B_Initial_Position_x)+"\n";
    		tmp+=Integer.toString(B_Initial_Position_y)+"\n";
    		tmp+="---\n";
    	}
    	
    	/*存road*/
    	if(P1.getRoadSize()!=0){
    		tmp+="P1\n";
    		for(int i = 0;i<P1.getRoadSize();i++){
    			tmp+=Integer.toString(P1.handleGetRoad(i))+"\n";
    		}
    		tmp+="---\n";
    	}
    	
    	if(P2.getRoadSize()!=0){
    		tmp+="P2\n";
    		for(int i = 0;i<P2.getRoadSize();i++){
    			tmp+=Integer.toString(P2.handleGetRoad(i))+"\n";
    		}
    		tmp+="---\n";
    	}
    	if(P3.getRoadSize()!=0){
    		tmp+="P3\n";
    		for(int i = 0;i<P3.getRoadSize();i++){
    			tmp+=Integer.toString(P3.handleGetRoad(i))+"\n";
    		}
    		tmp+="---\n";
    	}
    	if(P4.getRoadSize()!=0){
    		tmp+="P4\n";
    		for(int i = 0;i<P4.getRoadSize();i++){
    			tmp+=Integer.toString(P4.handleGetRoad(i))+"\n";
    		}
    		tmp+="---\n";
    	}
    	if(P5.getRoadSize()!=0){
    		tmp+="P5\n";
    		for(int i = 0;i<P5.getRoadSize();i++){
    			tmp+=Integer.toString(P5.handleGetRoad(i))+"\n";
    		}
    		tmp+="---\n";
    	}
    	if(B.getRoadSize()!=0){
    		tmp+="B\n";
    		for(int i = 0;i<B.getRoadSize();i++){
    			tmp+=Integer.toString(B.handleGetRoad(i))+"\n";
    		}
    		tmp+="---\n";
    	}
    	/*存curve*/
    	if(P1.getCurveSize()!=0){
    		tmp+="P1_curve\n";
    		for(int i = 0;i<P1.getCurveSize();i++){
    			tmp+=Float.toString(P1.getCurve(i))+"\n";
    		}
    		tmp+="---\n";
    	}
    	
    	if(P2.getCurveSize()!=0){
    		tmp+="P2_curve\n";
    		for(int i = 0;i<P2.getCurveSize();i++){
    			tmp+=Float.toString(P2.getCurve(i))+"\n";
    		}
    		tmp+="---\n";
    	}
    	
    	if(P3.getCurveSize()!=0){
    		tmp+="P3_curve\n";
    		for(int i = 0;i<P3.getCurveSize();i++){
    			tmp+=Float.toString(P3.getCurve(i))+"\n";
    		}
    		tmp+="---\n";
    	}
    	if(P4.getCurveSize()!=0){
    		tmp+="P4_curve\n";
    		for(int i = 0;i<P4.getCurveSize();i++){
    			tmp+=Float.toString(P4.getCurve(i))+"\n";
    		}
    		tmp+="---\n";
    	}
    	if(P5.getCurveSize()!=0){
    		tmp+="P5_curve\n";
    		for(int i = 0;i<P5.getCurveSize();i++){
    			tmp+=Float.toString(P5.getCurve(i))+"\n";
    		}
    		tmp+="---\n";
    	}
    	if(B.getCurveSize()!=0){
    		tmp+="B_curve\n";
    		for(int i = 0;i<B.getCurveSize();i++){
    			tmp+=Float.toString(B.getCurve(i))+"\n";
    		}
    		tmp+="---\n";
    	}
    	
    	/**/
    	tmp+="RunBag\n";
    	for(int i = 0 ; i < RunLine.size() ; i++){
    		tmp+=RunLine.get(i).getRunBagInfo()+"\n";
    		tmp+="---\n";
    	}
    	writeToFile(outFile,tmp);
    }
  //writeToFile 方法如下
	public void writeToFile(File fout, String data) {
	    FileOutputStream osw = null;
	    try {
	        osw = new FileOutputStream(fout);
	        osw.write(data.getBytes());
	        osw.flush();
	    } catch (Exception e) {
	        ;
	    } finally {
	        try {
	            osw.close();
	        } catch (Exception e) {
	            ;
	        }
	    }
	}
    
	public void load_dialog(){
		
		final String[] strategies = {"1號","3號","4號","5號","載入自訂戰術"};
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 
		builder.setItems(strategies, new DialogInterface.OnClickListener(){
	         @Override
	         //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
	         public void onClick(DialogInterface dialog, int which) {
	        	//取得內部儲存體擺放檔案的目錄
     			//預設擺放目錄為 /data/data/[package.name]/
     			File dir = getActivity().getBaseContext().getExternalFilesDir(null);
     			//Log.d("debug", "Environment = "+dir.toString());
	        	File inFile = null; 
	        	 switch(which){
	        	 case 0:
	        		 	//開啟或建立該目錄底下檔名為 "test.txt" 的檔案
	        			inFile = new File(dir, "strategy1.txt");
	        		 break;
	        	 case 1:
	        		 
	        		 break;
	        	 case 2:
	        		 
	        		 break;
	        	 case 3:
	        		 
	        		 break;
	        	 case 4:
	        		 inFile = new File(dir, "myStrategy.txt");
	        		 break;
	        	 default:
	        		 //inFile = new File(dir, "strategy1.txt");
	        		 break;
	        	 }
	        	 
	        	//讀取 /data/data/com.myapp/檔名.txt 檔案內容
     			String data = readFromFile(inFile);
     			String [] sData=data.split("\n");
     			for(int i =0;i<sData.length;i++){
     				Log.d("debug", "data["+i+"] ="+sData[i]);
     			
     			//先處理Initial position 
     				if(sData[i].equals("P1_Initial_Position")){
     					P1_Initial_Position_x=Integer.parseInt(sData[i+1]);
     					P1_Initial_Position_y=Integer.parseInt(sData[i+2]);
     					i=i+3;
     					Log.d("debug", "P1 initial saved.");
     				}
     				if(sData[i].equals("P2_Initial_Position")){
     					P2_Initial_Position_x=Integer.parseInt(sData[i+1]);
     					P2_Initial_Position_y=Integer.parseInt(sData[i+2]);
     					i=i+3;
     					Log.d("debug", "P2 initial saved.");
     				}
     				if(sData[i].equals("P3_Initial_Position")){
     					P3_Initial_Position_x=Integer.parseInt(sData[i+1]);
     					P3_Initial_Position_y=Integer.parseInt(sData[i+2]);
     					i=i+3;
     					Log.d("debug", "P3 initial saved.");
     				}
     				if(sData[i].equals("P4_Initial_Position")){
     					P4_Initial_Position_x=Integer.parseInt(sData[i+1]);
     					P4_Initial_Position_y=Integer.parseInt(sData[i+2]);
     					i=i+3;
     					Log.d("debug", "P4 initial saved.");
     				}
     				if(sData[i].equals("P5_Initial_Position")){
     					P5_Initial_Position_x=Integer.parseInt(sData[i+1]);
     					P5_Initial_Position_y=Integer.parseInt(sData[i+2]);
     					i=i+3;
     					Log.d("debug", "P5 initial saved.");
     				}
     				if(sData[i].equals("B_Initial_Position")){
     					B_Initial_Position_x=Integer.parseInt(sData[i+1]);
     					B_Initial_Position_y=Integer.parseInt(sData[i+2]);
     					i=i+3;
     					Log.d("debug", "B initial saved.");
     				}
     				
     				
     				//存Player的road
     				if(sData[i].equals("P1")){
     					i++;
     					while(!sData[i].equals("---")){
     						P1.setRoad(Integer.parseInt(sData[i]));
     						P1.setRoad_3d(Integer.parseInt(sData[i]));
     						
     						P1_recommend.setRoad(Integer.parseInt(sData[i]));
     						
     						i++;
     					}
     					Log.d("debug", "P1 road saved. Size="+Integer.toString(P1.getRoadSize())+"....."+Integer.toString(P1.getRoadSize_3d()));
     				}
     				if(sData[i].equals("P2")){
     					i++;
     					while(!sData[i].equals("---")){
     						P2.setRoad(Integer.parseInt(sData[i]));
     						P2.setRoad_3d(Integer.parseInt(sData[i]));
     						
     						P2_recommend.setRoad(Integer.parseInt(sData[i]));
     						i++;
     					}
     					Log.d("debug", "P2 road saved. Size="+Integer.toString(P2.getRoadSize())+"....."+Integer.toString(P2.getRoadSize_3d()));
     				}
     				if(sData[i].equals("P3")){
     					i++;
     					while(!sData[i].equals("---")){
     						P3.setRoad(Integer.parseInt(sData[i]));
     						P3.setRoad_3d(Integer.parseInt(sData[i]));
     						P3_recommend.setRoad(Integer.parseInt(sData[i]));
     						i++;
     					}
     					Log.d("debug", "P3 road saved. Size="+Integer.toString(P3.getRoadSize())+"....."+Integer.toString(P3.getRoadSize_3d()));
     				}
     				if(sData[i].equals("P4")){
     					i++;
     					while(!sData[i].equals("---")){
     						P4.setRoad(Integer.parseInt(sData[i]));
     						P4.setRoad_3d(Integer.parseInt(sData[i]));
     						P4_recommend.setRoad(Integer.parseInt(sData[i]));
     						i++;
     					}
     					Log.d("debug", "P4 road saved. Size="+Integer.toString(P4.getRoadSize())+"....."+Integer.toString(P4.getRoadSize_3d()));
     				}
     				if(sData[i].equals("P5")){
     					i++;
     					while(!sData[i].equals("---")){
     						P5.setRoad(Integer.parseInt(sData[i]));
     						P5.setRoad_3d(Integer.parseInt(sData[i]));
     						P5_recommend.setRoad(Integer.parseInt(sData[i]));
     						i++;
     					}
     					Log.d("debug", "P5 road saved. Size="+Integer.toString(P5.getRoadSize())+"....."+Integer.toString(P5.getRoadSize_3d()));
     				}
     				if(sData[i].equals("B")){
     					i++;
     					while(!sData[i].equals("---")){
     						B.setRoad(Integer.parseInt(sData[i]));
     						B.setRoad_3d(Integer.parseInt(sData[i]));
     						B_recommend.setRoad(Integer.parseInt(sData[i]));
     						i++;
     					}
     					Log.d("debug", "B road saved. Size="+Integer.toString(B.getRoadSize())+"....."+Integer.toString(B.getRoadSize_3d()));
     				}
     				
     			//存Curve
     				if(sData[i].equals("P1_curve")){
     					i++;
     					while(!sData[i].equals("---")){
     						P1.setCurve(Float.parseFloat(sData[i]));
     						P1_recommend.setCurve(Float.parseFloat(sData[i]));
     						i++;
     					}
     					Log.d("debug", "P1 curve saved. Size="+Integer.toString(P1.getCurveSize())+".....");
     				}
     				if(sData[i].equals("P2_curve")){
     					i++;
     					while(!sData[i].equals("---")){
     						P2.setCurve(Float.parseFloat(sData[i]));
     						P2_recommend.setCurve(Float.parseFloat(sData[i]));
     						i++;
     					}
     					Log.d("debug", "P2 curve saved. Size="+Integer.toString(P1.getCurveSize())+".....");
     				}
     				if(sData[i].equals("P3_curve")){
     					i++;
     					while(!sData[i].equals("---")){
     						P3.setCurve(Float.parseFloat(sData[i]));
     						P3_recommend.setCurve(Float.parseFloat(sData[i]));
     						i++;
     					}
     					Log.d("debug", "P3 curve saved. Size="+Integer.toString(P3.getCurveSize())+".....");
     				}
     				if(sData[i].equals("P4_curve")){
     					i++;
     					while(!sData[i].equals("---")){
     						P4.setCurve(Float.parseFloat(sData[i]));
     						P4_recommend.setCurve(Float.parseFloat(sData[i]));
     						i++;
     					}
     					Log.d("debug", "P4 curve saved. Size="+Integer.toString(P4.getCurveSize())+".....");
     				}
     				if(sData[i].equals("P5_curve")){
     					i++;
     					while(!sData[i].equals("---")){
     						P5.setCurve(Float.parseFloat(sData[i]));
     						P5_recommend.setCurve(Float.parseFloat(sData[i]));
     						i++;
     					}
     					Log.d("debug", "P5 curve saved. Size="+Integer.toString(P5.getCurveSize())+".....");
     				}
     				if(sData[i].equals("B_curve")){
     					i++;
     					while(!sData[i].equals("---")){
     						B.setCurve(Float.parseFloat(sData[i]));
     						B_recommend.setCurve(Float.parseFloat(sData[i]));
     						i++;
     					}
     					Log.d("debug", "B curve saved. Size="+Integer.toString(B.getCurveSize())+".....");
     				}
     			//存RunBag
     				TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
     				
     				if(sData[i].equals("RunBag")){
     					int load_seekbar_tmp_id=0;
     					i++;
     					while(i<sData.length){
     						while(!sData[i].equals("---")){
     							RunBag tmp = new RunBag();
     							tmp.setStartTime(Integer.parseInt(sData[i]));
     							i++;
     							tmp.setHandler(sData[i]);
     							i++;
     							tmp.setRoadStart(Integer.parseInt(sData[i]));
     							i++;
     							tmp.setRoadEnd(Integer.parseInt(sData[i]));
     							i++;
     							tmp.setDuration(Integer.parseInt(sData[i]));
     							i++;
     							RunLine.add(tmp);
     							timefrag.setSeekBarId(load_seekbar_tmp_id);
     							if(tmp.getHandler().equals("P1_Handle")){
	        							timefrag.createSeekbar(1,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
     							}
     							else if(tmp.getHandler().equals("P2_Handle")){
     								timefrag.createSeekbar(2,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
     							}
     							else if(tmp.getHandler().equals("P3_Handle")){
     								timefrag.createSeekbar(3,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
     							}
     							else if(tmp.getHandler().equals("P4_Handle")){
     								timefrag.createSeekbar(4,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
     							}
     							else if(tmp.getHandler().equals("P5_Handle")){
     								timefrag.createSeekbar(5,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
     							}
     							else if(tmp.getHandler().equals("B_Handle")){
     								timefrag.createSeekbar(6,load_seekbar_tmp_id,tmp.getStartTime(),tmp.getDuration());
     							}
     							
     							load_seekbar_tmp_id++;
     							
     							
     							
	        					}
     						i++;
     					}
     					//Log.d("debug", "DTW = "+Double.toString(DTW(P2.getCmpltRoad(),P2.getCmpltRoad())));
     					
     					Log.d("debug", "RunBag saved. Size="+Integer.toString(RunLine.size()));
     					
     				}
     			}
	        	 
	        	 
	          }
	    });
        AlertDialog about_dialog = builder.create();
        about_dialog.show();
		
	}
	
	//readFromFile 方法如下
	private String readFromFile(File fin) {
	    StringBuilder data = new StringBuilder();
	    BufferedReader reader = null;
	    try {
	        reader = new BufferedReader(new FileReader(fin));
	        String line;
	        while ((line = reader.readLine()) != null) {
	            data.append(line);
	            data.append("\n");
	        }
	    } catch (Exception e) {
	        ;
	    } finally {
	        try {
	            reader.close();
	        } catch (Exception e) {
	            ;
	        }
	    }
	    return data.toString();
	}

	/**************************************************************************/
	
	
	public void playButton() {
		if (RunLine.isEmpty()) {
			//Log.e("empty!", String.valueOf(RunLine.size()));
		} else {
			/*先把全部player移到按下錄製鍵時的位置*/
			if(P1_Initial_Position_x!=-1)
				player1.layout(P1_Initial_Position_x, P1_Initial_Position_y,P1_Initial_Position_x + player1.getWidth(),P1_Initial_Position_y + player1.getHeight());
			if(P2_Initial_Position_x!=-1)
				player2.layout(P2_Initial_Position_x, P2_Initial_Position_y,P2_Initial_Position_x + player2.getWidth(),P2_Initial_Position_y + player2.getHeight());
			if(P3_Initial_Position_x!=-1)
				player3.layout(P3_Initial_Position_x, P3_Initial_Position_y,P3_Initial_Position_x + player3.getWidth(),P3_Initial_Position_y + player3.getHeight());
			if(P4_Initial_Position_x!=-1)
				player4.layout(P4_Initial_Position_x, P4_Initial_Position_y,P4_Initial_Position_x + player4.getWidth(),P4_Initial_Position_y + player4.getHeight());
			if(P5_Initial_Position_x!=-1)
				player5.layout(P5_Initial_Position_x, P5_Initial_Position_y,P5_Initial_Position_x + player5.getWidth(),P5_Initial_Position_y + player5.getHeight());
			if(B_Initial_Position_x!=-1)
				ball.layout(B_Initial_Position_x, B_Initial_Position_y,B_Initial_Position_x + ball.getWidth(),B_Initial_Position_y + ball.getHeight());
			/****************************/
			
			//////////////////////////////////////// Time
			//////////////////////////////////////// counter///////////////////////////////////////
			new Thread(new Runnable() {// Time counter count on per second
				@Override
				public void run() {
					int time = 0;
					int RunLineSize = RunLine.size();
					while (time < Total_time && playing==1) {
						try {
							//Log.e("time = ", String.valueOf(time));
							// do RunLine here!!
							////// check each road's start time in
							// RunLine///////
							checkRunLine(time, RunLineSize);
							Thread.sleep(1000);
							time = time + 1000;

						} catch (Throwable t) {
						}
					}
				}
			}).start();
		}
	}

	protected void checkRunLine(final int in_time, final int in_RunLineSize) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int time = in_time;
				int RunLineSize = in_RunLineSize;
				int i = 0;
				while (i < RunLineSize && playing==1) {
					try {
						Message m = new Message();
						Bundle b = new Bundle();
						b.putInt("what", i);// 將play_k打包成msg
						b.putInt("time", time);
						m.setData(b);
						RunLineCheck_Handle.sendMessage(m);
						i = i + 1;
					} catch (Throwable t) {
					}
				}
			}
		}).start();
	}

	protected void play(final int speed, final Handler play_handler, final int in_k, final int in_j) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int play_k = in_k;
				while (play_k < in_j - 1 && playing==1) {
					try {
						Message m = new Message();
						Bundle b = new Bundle();
						b.putInt("what", play_k);// 將play_k打包成msg
						m.setData(b);
						play_handler.sendMessage(m);
						Thread.sleep(speed);
						play_k = play_k + 2;
					} catch (Throwable t) {
					}
				}
			}
		}).start();
	}

	Handler P1_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			// Log.e("P1 sentInt=", Integer.toString(sentInt));
			// Log.e("x P1.handleGetRoad(",
			// Integer.toString(sentInt)+")="+Integer.toString(P1.handleGetRoad(sentInt)));
			// Log.e("y P1.handleGetRoad(",
			// Integer.toString(sentInt+1)+")="+Integer.toString(P1.handleGetRoad(sentInt+1)));
			player1.layout(P1.handleGetRoad(sentInt), P1.handleGetRoad(sentInt + 1),
							P1.handleGetRoad(sentInt) + player1.getWidth(),
							P1.handleGetRoad(sentInt + 1) + player1.getHeight());
		}
	};

	Handler P2_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			player2.layout(P2.handleGetRoad(sentInt), P2.handleGetRoad(sentInt + 1),
							P2.handleGetRoad(sentInt) + player2.getWidth(),
							P2.handleGetRoad(sentInt + 1) + player2.getHeight());
		}
	};

	Handler P3_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			player3.layout(P3.handleGetRoad(sentInt), P3.handleGetRoad(sentInt + 1),
							P3.handleGetRoad(sentInt) + player3.getWidth(),
							P3.handleGetRoad(sentInt + 1) + player3.getHeight());
		}
	};

	Handler P4_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			player4.layout(P4.handleGetRoad(sentInt), P4.handleGetRoad(sentInt + 1),
							P4.handleGetRoad(sentInt) + player4.getWidth(),
							P4.handleGetRoad(sentInt + 1) + player4.getHeight());
		}
	};

	Handler P5_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			player5.layout(P5.handleGetRoad(sentInt), P5.handleGetRoad(sentInt + 1),
							P5.handleGetRoad(sentInt) + player5.getWidth(),
							P5.handleGetRoad(sentInt + 1) + player5.getHeight());
		}
	};

	Handler B_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentInt = msg.getData().getInt("what");
			ball.layout(B.handleGetRoad(sentInt), B.handleGetRoad(sentInt + 1),
					B.handleGetRoad(sentInt) + ball.getWidth(), B.handleGetRoad(sentInt + 1) + ball.getHeight());
		}
	};

	Handler RunLineCheck_Handle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int sentI = msg.getData().getInt("what");
			int sentTime = msg.getData().getInt("time");
			if (RunLine.get(sentI).getStartTime() * 1000 == sentTime) {
				if (RunLine.get(sentI).getHandler().equals("P1_Handle")) {
					play(RunLine.get(sentI).getRate(), P1_Handle, RunLine.get(sentI).getRoadStart(),
							RunLine.get(sentI).getRoadEnd());
				} else if (RunLine.get(sentI).getHandler().equals("P2_Handle")) {
					play(RunLine.get(sentI).getRate(), P2_Handle, RunLine.get(sentI).getRoadStart(),
							RunLine.get(sentI).getRoadEnd());
				} else if (RunLine.get(sentI).getHandler().equals("P3_Handle")) {
					play(RunLine.get(sentI).getRate(), P3_Handle, RunLine.get(sentI).getRoadStart(),
							RunLine.get(sentI).getRoadEnd());
				} else if (RunLine.get(sentI).getHandler().equals("P4_Handle")) {
					play(RunLine.get(sentI).getRate(), P4_Handle, RunLine.get(sentI).getRoadStart(),
							RunLine.get(sentI).getRoadEnd());
				} else if (RunLine.get(sentI).getHandler().equals("P5_Handle")) {
					play(RunLine.get(sentI).getRate(), P5_Handle, RunLine.get(sentI).getRoadStart(),
							RunLine.get(sentI).getRoadEnd());
				} else if (RunLine.get(sentI).getHandler().equals("B_Handle")) {
					play(RunLine.get(sentI).getRate(), B_Handle, RunLine.get(sentI).getRoadStart(),
							RunLine.get(sentI).getRoadEnd());
				}
			}
		}
	};
	
	/**TODO player1**/	


	private OnTouchListener player1_Listener = new OnTouchListener() { 
		private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
		private int startX, startY; // 原本圖片存在的X,Y軸位置
		private int x, y; // 最終的顯示位置
		private float tmp_y;
		
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TouchEvent(P1,paint,mx,my,startX,startY,v,event,"P1");
			mx = (int) (event.getRawX());
			my = (int) (event.getRawY());
			
			switch (event.getAction()) { // 判斷觸控的動作
			case MotionEvent.ACTION_DOWN:// 按下圖片時
				
				startX = (int) event.getX();
				startY = my - v.getTop();
				if (recordcheck == true) {
					P1.setRoad(0); // split positions
					P1.setRoad_3d(0);
				}
				return true;
			case MotionEvent.ACTION_MOVE:// 移動圖片時
				x = mx - startX;
				y = my - startY;
				int [] location =new int[2];
				v.getLocationOnScreen(location);
				if (recordcheck == true) {
					long CurrentTime = System.currentTimeMillis();
					P1.setRoad(x);
					P1.setRoad(y);
					P1.setRoad_3d((int) event.getRawX());
					P1.setRoad_3d((int) event.getRawY());
					
					P1_curve_x.add(c1_idx, x);
					P1_curve_y.add(c1_idx, y);
					c1_idx++;
					
					if(c1_idx==N){
						for(int tmp=1;tmp<N;tmp++){
							if(P1_curve_x.get(tmp-1)<P1_curve_x.get(tmp)){//++
								if(tmp==1 && ( (int) P1_curve_x.get(1)==(int) P1_curve_x.get(2) )){//第二、三格一樣，給lagrange的陣列不能有x一樣的情況，所以只能給lagrange兩格(一、二格)
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P1_curve_x.get(0));
									tmp_curve_x.add(P1_curve_x.get(1));
									tmp_curve_y.add(P1_curve_y.get(0));
									tmp_curve_y.add(P1_curve_y.get(1));
									for(float tmp_x=P1_curve_x.get(tmp-1);tmp_x<=P1_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player1_paint);//black
									}
								}
								else if(tmp==2 && ( (int) P1_curve_x.get(0)==(int) P1_curve_x.get(1) )){//第一、二格一樣
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P1_curve_x.get(1));
									tmp_curve_x.add(P1_curve_x.get(2));
									tmp_curve_y.add(P1_curve_y.get(1));
									tmp_curve_y.add(P1_curve_y.get(2));
									for(float tmp_x=P1_curve_x.get(tmp-1);tmp_x<=P1_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player1_paint);//black
									}
								}
								else{//都不一樣
									if(tmp==1 &&(int) P1_curve_x.get(1)>(int) P1_curve_x.get(2)){//1<2   2>3
										
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P1_curve_x.get(0));
										tmp_curve_x.add(P1_curve_x.get(1));
										tmp_curve_y.add(P1_curve_y.get(0));
										tmp_curve_y.add(P1_curve_y.get(1));
										for(float tmp_x=P1_curve_x.get(tmp-1);tmp_x<=P1_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player1_paint);//black
										}
									}
									else if(tmp==2 && ( (int) P1_curve_x.get(0)>(int) P1_curve_x.get(1) )){//1>2   2<3
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P1_curve_x.get(1));
										tmp_curve_x.add(P1_curve_x.get(2));
										tmp_curve_y.add(P1_curve_y.get(1));
										tmp_curve_y.add(P1_curve_y.get(2));
										for(float tmp_x=P1_curve_x.get(tmp-1);tmp_x<=P1_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player1_paint);//black
										}
									}
									else{
										for(float tmp_x=P1_curve_x.get(tmp-1);tmp_x<=P1_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
											tmp_y=lagrange(P1_curve_x,P1_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player1_paint);//black
										}
									}
								}
							}
							else if(P1_curve_x.get(tmp-1)>P1_curve_x.get(tmp)){//--
								
								if(tmp==1 && ( (int) P1_curve_x.get(1)==(int) P1_curve_x.get(2) )){//第二、三格一樣，給lagrange的陣列不能有x一樣的情況，所以只能給lagrange兩格(一、二格)
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P1_curve_x.get(0));
									tmp_curve_x.add(P1_curve_x.get(1));
									tmp_curve_y.add(P1_curve_y.get(0));
									tmp_curve_y.add(P1_curve_y.get(1));
									for(float tmp_x=P1_curve_x.get(tmp-1);tmp_x>=P1_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player1_paint);//red
									}
								}
								else if(tmp==2 && ( (int) P1_curve_x.get(0)==(int) P1_curve_x.get(1) )){//第一、二格一樣
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P1_curve_x.get(1));
									tmp_curve_x.add(P1_curve_x.get(2));
									tmp_curve_y.add(P1_curve_y.get(1));
									tmp_curve_y.add(P1_curve_y.get(2));
									for(float tmp_x=P1_curve_x.get(tmp-1);tmp_x>=P1_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player1_paint);//red
									}
								}
								else{//都不一樣
									
									if(tmp==1 &&(int) P1_curve_x.get(1)<(int) P1_curve_x.get(2)){//1>2   2<3
										
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P1_curve_x.get(0));
										tmp_curve_x.add(P1_curve_x.get(1));
										tmp_curve_y.add(P1_curve_y.get(0));
										tmp_curve_y.add(P1_curve_y.get(1));
										for(float tmp_x=P1_curve_x.get(tmp-1);tmp_x>=P1_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player1_paint);//red
										}
									}
									else if(tmp==2 && ( (int) P1_curve_x.get(0)<(int) P1_curve_x.get(1) )){//1<2   2>3
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P1_curve_x.get(1));
										tmp_curve_x.add(P1_curve_x.get(2));
										tmp_curve_y.add(P1_curve_y.get(1));
										tmp_curve_y.add(P1_curve_y.get(2));
										for(float tmp_x=P1_curve_x.get(tmp-1);tmp_x>=P1_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player1_paint);//red
										}
									}
									else{
										for(float tmp_x=P1_curve_x.get(tmp-1);tmp_x>=P1_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(P1_curve_x,P1_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player1_paint);//red
										}
									}
								}
							}
							else{//==  line 其中兩格一樣的話，就兩格之間畫直線
								
								int tmp_x=P1_curve_x.get(tmp-1);
								if(P1_curve_y.get(tmp-1)>P1_curve_y.get(tmp)){
									for(float tmp_y=P1_curve_y.get(tmp-1);tmp_y>=P1_curve_y.get(tmp);tmp_y=tmp_y-(float)0.1){
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player1_paint);//blue
									}
								}
								else{
									for(float tmp_y=P1_curve_y.get(tmp-1);tmp_y<=P1_curve_y.get(tmp);tmp_y=tmp_y+(float)0.1){
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player1_paint);//green
									}
								}
							}
						}
						c1_idx=0;
						P1_curve_x.clear();
						P1_curve_y.clear();
						P1_curve_x.add(c1_idx, x);
						P1_curve_y.add(c1_idx, y);
						c1_idx++;
					}
					tempCanvas.drawBitmap(tempBitmap, 0, 0, transparent_paint);
					tempCanvas.drawCircle(x+ v.getWidth()/2, y+ v.getHeight()/2, 5, player1_paint);
					//tempCanvas.drawPath(p, paint);
					circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
				}
				v.layout(x, y, x + v.getWidth(), y + v.getHeight());
				v.postInvalidate();
				return true;
			case MotionEvent.ACTION_UP:// 放開圖片時
				if (recordcheck == true) {
					TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
					int startIndexTmp = P1_startIndex;
					while (P1_startIndex != -1) {
						RunBag tmp = new RunBag();
						tmp.setStartTime(seekBarCallbackStartTime);
						tmp.setHandler("P1_Handle");
						tmp.setRoadStart(P1_startIndex + 1);
						P1_startIndex += 2;
						P1_startIndex = P1.getRoad(0, P1_startIndex);
						if (P1_startIndex == -1) {
							startIndexTmp = P1.getLastRoad();
							tmp.setRoadEnd(startIndexTmp);
						} else {
							tmp.setRoadEnd(P1_startIndex);
							startIndexTmp = P1_startIndex;
						}
						tmp.setDuration(seekBarCallbackDuration);
						RunLine.add(tmp);
						timefrag.setSeekBarId(seekbar_tmp_id);
						seekbar_tmp_id++;
						timefrag.createSeekbar(1);
					}
					P1_startIndex = startIndexTmp + 1;
				}
				else{
					P1_Initial_Position_x=x;
					P1_Initial_Position_y=y;
				}
				
				return true;
			}
			return true;
		}
	};
	/**TODO player2**/

	private OnTouchListener player2_Listener = new OnTouchListener() {

		private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
		private int startX, startY; // 原本圖片存在的X,Y軸位置
		private int x, y; // 最終的顯示位置
		private float tmp_y;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			mx = (int) (event.getRawX());
			my = (int) (event.getRawY());

			switch (event.getAction()) { // 判斷觸控的動作
			case MotionEvent.ACTION_DOWN:// 按下圖片時
				
				startX = (int) event.getX();
				startY = my - v.getTop();
				if (recordcheck == true) {
					P2.setRoad(0); // split positions
					P2.setRoad_3d(0);
				}
				return true;
			case MotionEvent.ACTION_MOVE:// 移動圖片時
				x = mx - startX;
				y = my - startY;
				
				if (recordcheck == true) {
					P2.setRoad(x);
					P2.setRoad(y);
					P2.setRoad_3d((int) event.getRawX());
					P2.setRoad_3d((int) event.getRawY());
					
					P2.setCurve(x);
					P2.setCurve(y);
					

					P2_curve_x.add(c2_idx, x);
					P2_curve_y.add(c2_idx, y);
					c2_idx++;
					
					if(c2_idx==N){
						for(int tmp=1;tmp<N;tmp++){
							if(P2_curve_x.get(tmp-1)<P2_curve_x.get(tmp)){//++
								if(tmp==1 && ( (int) P2_curve_x.get(1)==(int) P2_curve_x.get(2) )){//第二、三格一樣，給lagrange的陣列不能有x一樣的情況，所以只能給lagrange兩格(一、二格)
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P2_curve_x.get(0));
									tmp_curve_x.add(P2_curve_x.get(1));
									tmp_curve_y.add(P2_curve_y.get(0));
									tmp_curve_y.add(P2_curve_y.get(1));
									for(float tmp_x=P2_curve_x.get(tmp-1);tmp_x<=P2_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										P2.setCurve(tmp_x);
										P2.setCurve(tmp_y);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player2_paint);//black
									}
								}
								else if(tmp==2 && ( (int) P2_curve_x.get(0)==(int) P2_curve_x.get(1) )){//第一、二格一樣
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P2_curve_x.get(1));
									tmp_curve_x.add(P2_curve_x.get(2));
									tmp_curve_y.add(P2_curve_y.get(1));
									tmp_curve_y.add(P2_curve_y.get(2));
									for(float tmp_x=P2_curve_x.get(tmp-1);tmp_x<=P2_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										P2.setCurve(tmp_x);
										P2.setCurve(tmp_y);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player2_paint);//black
									}
								}
								else{//都不一樣
									if(tmp==1 &&(int) P2_curve_x.get(1)>(int) P2_curve_x.get(2)){//1<2   2>3
										
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P2_curve_x.get(0));
										tmp_curve_x.add(P2_curve_x.get(1));
										tmp_curve_y.add(P2_curve_y.get(0));
										tmp_curve_y.add(P2_curve_y.get(1));
										for(float tmp_x=P2_curve_x.get(tmp-1);tmp_x<=P2_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											P2.setCurve(tmp_x);
											P2.setCurve(tmp_y);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player2_paint);//black
										}
									}
									else if(tmp==2 && ( (int) P2_curve_x.get(0)>(int) P2_curve_x.get(1) )){//1>2   2<3
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P2_curve_x.get(1));
										tmp_curve_x.add(P2_curve_x.get(2));
										tmp_curve_y.add(P2_curve_y.get(1));
										tmp_curve_y.add(P2_curve_y.get(2));
										for(float tmp_x=P2_curve_x.get(tmp-1);tmp_x<=P2_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											P2.setCurve(tmp_x);
											P2.setCurve(tmp_y);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player2_paint);//black
										}
									}
									else{
										for(float tmp_x=P2_curve_x.get(tmp-1);tmp_x<=P2_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
											tmp_y=lagrange(P2_curve_x,P2_curve_y,tmp_x);
											P2.setCurve(tmp_x);
											P2.setCurve(tmp_y);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player2_paint);//black
										}
									}
								}
							}
							else if(P2_curve_x.get(tmp-1)>P2_curve_x.get(tmp)){//--
								
								if(tmp==1 && ( (int) P2_curve_x.get(1)==(int) P2_curve_x.get(2) )){//第二、三格一樣，給lagrange的陣列不能有x一樣的情況，所以只能給lagrange兩格(一、二格)
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P2_curve_x.get(0));
									tmp_curve_x.add(P2_curve_x.get(1));
									tmp_curve_y.add(P2_curve_y.get(0));
									tmp_curve_y.add(P2_curve_y.get(1));
									for(float tmp_x=P2_curve_x.get(tmp-1);tmp_x>=P2_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										P2.setCurve(tmp_x);
										P2.setCurve(tmp_y);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player2_paint);//red
									}
								}
								else if(tmp==2 && ( (int) P2_curve_x.get(0)==(int) P2_curve_x.get(1) )){//第一、二格一樣
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P2_curve_x.get(1));
									tmp_curve_x.add(P2_curve_x.get(2));
									tmp_curve_y.add(P2_curve_y.get(1));
									tmp_curve_y.add(P2_curve_y.get(2));
									for(float tmp_x=P2_curve_x.get(tmp-1);tmp_x>=P2_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										P2.setCurve(tmp_x);
										P2.setCurve(tmp_y);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player2_paint);//red
									}
								}
								else{//都不一樣
									
									if(tmp==1 &&(int) P2_curve_x.get(1)<(int) P2_curve_x.get(2)){//1>2   2<3
										
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P2_curve_x.get(0));
										tmp_curve_x.add(P2_curve_x.get(1));
										tmp_curve_y.add(P2_curve_y.get(0));
										tmp_curve_y.add(P2_curve_y.get(1));
										for(float tmp_x=P2_curve_x.get(tmp-1);tmp_x>=P2_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											P2.setCurve(tmp_x);
											P2.setCurve(tmp_y);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player2_paint);//red
										}
									}
									else if(tmp==2 && ( (int) P2_curve_x.get(0)<(int) P2_curve_x.get(1) )){//1<2   2>3
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P2_curve_x.get(1));
										tmp_curve_x.add(P2_curve_x.get(2));
										tmp_curve_y.add(P2_curve_y.get(1));
										tmp_curve_y.add(P2_curve_y.get(2));
										for(float tmp_x=P2_curve_x.get(tmp-1);tmp_x>=P2_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											P2.setCurve(tmp_x);
											P2.setCurve(tmp_y);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player2_paint);//red
										}
									}
									else{
										for(float tmp_x=P2_curve_x.get(tmp-1);tmp_x>=P2_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(P2_curve_x,P2_curve_y,tmp_x);
											P2.setCurve(tmp_x);
											P2.setCurve(tmp_y);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player2_paint);//red
										}
									}
								}
							}
							else{//==  line 其中兩格一樣的話，就兩格之間畫直線
								
								int tmp_x=P2_curve_x.get(tmp-1);
								if(P2_curve_y.get(tmp-1)>P2_curve_y.get(tmp)){
									for(float tmp_y=P2_curve_y.get(tmp-1);tmp_y>=P2_curve_y.get(tmp);tmp_y=tmp_y-(float)0.1){
										P2.setCurve(tmp_x);
										P2.setCurve(tmp_y);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player2_paint);//blue
									}
								}
								else{
									for(float tmp_y=P2_curve_y.get(tmp-1);tmp_y<=P2_curve_y.get(tmp);tmp_y=tmp_y+(float)0.1){
										P2.setCurve(tmp_x);
										P2.setCurve(tmp_y);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player2_paint);//green
									}
								}
							}
						}
						c2_idx=0;
						P2_curve_x.clear();
						P2_curve_y.clear();
						P2_curve_x.add(c2_idx, x);
						P2_curve_y.add(c2_idx, y);
						c2_idx++;
					}
					
					
					tempCanvas.drawBitmap(tempBitmap, 0, 0, transparent_paint);
					tempCanvas.drawCircle(x+ v.getWidth()/2, y+ v.getHeight()/2, 5, player2_paint);
					circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
				}
				v.layout(x, y, x + v.getWidth(), y + v.getHeight());
				v.postInvalidate();
				return true;
			case MotionEvent.ACTION_UP:// 放開圖片時
				if (recordcheck == true) {
					TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
					int startIndexTmp = P2_startIndex;
					while (P2_startIndex != -1) {
						RunBag tmp = new RunBag();
						tmp.setStartTime(seekBarCallbackStartTime);
						tmp.setHandler("P2_Handle");
						tmp.setRoadStart(P2_startIndex + 1);
						P2_startIndex += 2;
						P2_startIndex = P2.getRoad(0, P2_startIndex);
						if (P2_startIndex == -1) {
							startIndexTmp = P2.getLastRoad();
							tmp.setRoadEnd(startIndexTmp);
						} else {
							tmp.setRoadEnd(P2_startIndex);
							startIndexTmp = P2_startIndex;
						}
						tmp.setDuration(seekBarCallbackDuration);
						RunLine.add(tmp);
						timefrag.setSeekBarId(seekbar_tmp_id);
						seekbar_tmp_id++;
						timefrag.createSeekbar(2);
					}
					P2_startIndex = startIndexTmp + 1;
				}
				else{
					P2_Initial_Position_x=x;
					P2_Initial_Position_y=y;
				}
				return true;
			}
			return true;
		}
	};
	/**TODO player3**/
	private OnTouchListener player3_Listener = new OnTouchListener() {
		private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
		private int startX, startY; // 原本圖片存在的X,Y軸位置
		private int x, y; // 最終的顯示位置
		private float tmp_y;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TouchEvent(P1,paint,mx,my,startX,startY,v,event,"P1");
			
			
			mx = (int) (event.getRawX());
			my = (int) (event.getRawY());

			switch (event.getAction()) { // 判斷觸控的動作
			case MotionEvent.ACTION_DOWN:// 按下圖片時
				
				startX = (int) event.getX();
				startY = my - v.getTop();
				if (recordcheck == true) {
					P3.setRoad(0); // split positions
					P3.setRoad_3d(0);
				}
				return true;
			case MotionEvent.ACTION_MOVE:// 移動圖片時
				x = mx - startX;
				y = my - startY;
				
				v.layout(x, y, x + v.getWidth(), y + v.getHeight());
				v.postInvalidate();
				if (recordcheck == true) {
					P3.setRoad(x);
					P3.setRoad(y);
					P3.setRoad_3d((int) event.getRawX());
					P3.setRoad_3d((int) event.getRawY());
					
					
					P3_curve_x.add(c3_idx, x);
					P3_curve_y.add(c3_idx, y);
					c3_idx++;
					
					if(c3_idx==N){
						for(int tmp=1;tmp<N;tmp++){
							if(P3_curve_x.get(tmp-1)<P3_curve_x.get(tmp)){//++
								if(tmp==1 && ( (int) P3_curve_x.get(1)==(int) P3_curve_x.get(2) )){//第二、三格一樣，給lagrange的陣列不能有x一樣的情況，所以只能給lagrange兩格(一、二格)
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P3_curve_x.get(0));
									tmp_curve_x.add(P3_curve_x.get(1));
									tmp_curve_y.add(P3_curve_y.get(0));
									tmp_curve_y.add(P3_curve_y.get(1));
									for(float tmp_x=P3_curve_x.get(tmp-1);tmp_x<=P3_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player3_paint);//black
									}
								}
								else if(tmp==2 && ( (int) P3_curve_x.get(0)==(int) P3_curve_x.get(1) )){//第一、二格一樣
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P3_curve_x.get(1));
									tmp_curve_x.add(P3_curve_x.get(2));
									tmp_curve_y.add(P3_curve_y.get(1));
									tmp_curve_y.add(P3_curve_y.get(2));
									for(float tmp_x=P3_curve_x.get(tmp-1);tmp_x<=P3_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player3_paint);//black
									}
								}
								else{//都不一樣
									if(tmp==1 &&(int) P3_curve_x.get(1)>(int) P3_curve_x.get(2)){//1<2   2>3
										
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P3_curve_x.get(0));
										tmp_curve_x.add(P3_curve_x.get(1));
										tmp_curve_y.add(P3_curve_y.get(0));
										tmp_curve_y.add(P3_curve_y.get(1));
										for(float tmp_x=P3_curve_x.get(tmp-1);tmp_x<=P3_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player3_paint);//black
										}
									}
									else if(tmp==2 && ( (int) P3_curve_x.get(0)>(int) P3_curve_x.get(1) )){//1>2   2<3
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P3_curve_x.get(1));
										tmp_curve_x.add(P3_curve_x.get(2));
										tmp_curve_y.add(P3_curve_y.get(1));
										tmp_curve_y.add(P3_curve_y.get(2));
										for(float tmp_x=P3_curve_x.get(tmp-1);tmp_x<=P3_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player3_paint);//black
										}
									}
									else{
										for(float tmp_x=P3_curve_x.get(tmp-1);tmp_x<=P3_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
											tmp_y=lagrange(P3_curve_x,P3_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player3_paint);//black
										}
									}
								}
							}
							else if(P3_curve_x.get(tmp-1)>P3_curve_x.get(tmp)){//--
								
								if(tmp==1 && ( (int) P3_curve_x.get(1)==(int) P3_curve_x.get(2) )){//第二、三格一樣，給lagrange的陣列不能有x一樣的情況，所以只能給lagrange兩格(一、二格)
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P3_curve_x.get(0));
									tmp_curve_x.add(P3_curve_x.get(1));
									tmp_curve_y.add(P3_curve_y.get(0));
									tmp_curve_y.add(P3_curve_y.get(1));
									for(float tmp_x=P3_curve_x.get(tmp-1);tmp_x>=P3_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player3_paint);//red
									}
								}
								else if(tmp==2 && ( (int) P3_curve_x.get(0)==(int) P3_curve_x.get(1) )){//第一、二格一樣
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P3_curve_x.get(1));
									tmp_curve_x.add(P3_curve_x.get(2));
									tmp_curve_y.add(P3_curve_y.get(1));
									tmp_curve_y.add(P3_curve_y.get(2));
									for(float tmp_x=P3_curve_x.get(tmp-1);tmp_x>=P3_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player3_paint);//red
									}
								}
								else{//都不一樣
									
									if(tmp==1 &&(int) P3_curve_x.get(1)<(int) P3_curve_x.get(2)){//1>2   2<3
										
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P3_curve_x.get(0));
										tmp_curve_x.add(P3_curve_x.get(1));
										tmp_curve_y.add(P3_curve_y.get(0));
										tmp_curve_y.add(P3_curve_y.get(1));
										for(float tmp_x=P3_curve_x.get(tmp-1);tmp_x>=P3_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player3_paint);//red
										}
									}
									else if(tmp==2 && ( (int) P3_curve_x.get(0)<(int) P3_curve_x.get(1) )){//1<2   2>3
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P3_curve_x.get(1));
										tmp_curve_x.add(P3_curve_x.get(2));
										tmp_curve_y.add(P3_curve_y.get(1));
										tmp_curve_y.add(P3_curve_y.get(2));
										for(float tmp_x=P3_curve_x.get(tmp-1);tmp_x>=P3_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player3_paint);//red
										}
									}
									else{
										for(float tmp_x=P3_curve_x.get(tmp-1);tmp_x>=P3_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(P3_curve_x,P3_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player3_paint);//red
										}
									}
								}
							}
							else{//==  line 其中兩格一樣的話，就兩格之間畫直線
								
								int tmp_x=P3_curve_x.get(tmp-1);
								if(P3_curve_y.get(tmp-1)>P3_curve_y.get(tmp)){
									for(float tmp_y=P3_curve_y.get(tmp-1);tmp_y>=P3_curve_y.get(tmp);tmp_y=tmp_y-(float)0.1){
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player3_paint);//blue
									}
								}
								else{
									for(float tmp_y=P3_curve_y.get(tmp-1);tmp_y<=P3_curve_y.get(tmp);tmp_y=tmp_y+(float)0.1){
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player3_paint);//green
									}
								}
							}
						}
						c3_idx=0;
						P3_curve_x.clear();
						P3_curve_y.clear();
						P3_curve_x.add(c3_idx, x);
						P3_curve_y.add(c3_idx, y);
						c3_idx++;
					}
					
					
					
					tempCanvas.drawBitmap(tempBitmap, 0, 0, transparent_paint);
					tempCanvas.drawCircle(x+ v.getWidth()/2, y+ v.getHeight()/2, 5, player3_paint);
					circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
				}
				
				return true;
			case MotionEvent.ACTION_UP:// 放開圖片時
				if (recordcheck == true) {
					TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
					int startIndexTmp = P3_startIndex;
					while (P3_startIndex != -1) {
						RunBag tmp = new RunBag();
						tmp.setStartTime(seekBarCallbackStartTime);
						tmp.setHandler("P3_Handle");
						tmp.setRoadStart(P3_startIndex + 1);
						P3_startIndex += 2;
						P3_startIndex = P3.getRoad(0, P3_startIndex);
						if (P3_startIndex == -1) {
							startIndexTmp = P3.getLastRoad();
							tmp.setRoadEnd(startIndexTmp);
						} else {
							tmp.setRoadEnd(P3_startIndex);
							startIndexTmp = P3_startIndex;
						}
						tmp.setDuration(seekBarCallbackDuration);
						RunLine.add(tmp);
						timefrag.setSeekBarId(seekbar_tmp_id);
						seekbar_tmp_id++;
						timefrag.createSeekbar(3);
					}
					P3_startIndex = startIndexTmp + 1;
				}
				else{
					P3_Initial_Position_x=x;
					P3_Initial_Position_y=y;
				}
				return true;
			}
			return true;
		}
	};
	/**TODO player4**/
	private OnTouchListener player4_Listener = new OnTouchListener() {
		private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
		private int startX, startY; // 原本圖片存在的X,Y軸位置
		private int x, y; // 最終的顯示位置
		private float tmp_y;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TouchEvent(P1,paint,mx,my,startX,startY,v,event,"P1");
			
			
			mx = (int) (event.getRawX());
			my = (int) (event.getRawY());

			switch (event.getAction()) { // 判斷觸控的動作
			case MotionEvent.ACTION_DOWN:// 按下圖片時
				
				startX = (int) event.getX();
				startY = my - v.getTop();
				if (recordcheck == true) {
					P4.setRoad(0); // split positions
					P4.setRoad_3d(0);
				}
				return true;
			case MotionEvent.ACTION_MOVE:// 移動圖片時
				x = mx - startX;
				y = my - startY;
				if (recordcheck == true) {
					P4.setRoad(x);
					P4.setRoad(y);
					P4.setRoad_3d((int) event.getRawX());
					P4.setRoad_3d((int) event.getRawY());
					
					
					P4_curve_x.add(c4_idx, x);
					P4_curve_y.add(c4_idx, y);
					c4_idx++;
					
					if(c4_idx==N){
						for(int tmp=1;tmp<N;tmp++){
							if(P4_curve_x.get(tmp-1)<P4_curve_x.get(tmp)){//++
								if(tmp==1 && ( (int) P4_curve_x.get(1)==(int) P4_curve_x.get(2) )){//第二、三格一樣，給lagrange的陣列不能有x一樣的情況，所以只能給lagrange兩格(一、二格)
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P4_curve_x.get(0));
									tmp_curve_x.add(P4_curve_x.get(1));
									tmp_curve_y.add(P4_curve_y.get(0));
									tmp_curve_y.add(P4_curve_y.get(1));
									for(float tmp_x=P4_curve_x.get(tmp-1);tmp_x<=P4_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player4_paint);//black
									}
								}
								else if(tmp==2 && ( (int) P4_curve_x.get(0)==(int) P4_curve_x.get(1) )){//第一、二格一樣
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P4_curve_x.get(1));
									tmp_curve_x.add(P4_curve_x.get(2));
									tmp_curve_y.add(P4_curve_y.get(1));
									tmp_curve_y.add(P4_curve_y.get(2));
									for(float tmp_x=P4_curve_x.get(tmp-1);tmp_x<=P4_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player4_paint);//black
									}
								}
								else{//都不一樣
									if(tmp==1 &&(int) P4_curve_x.get(1)>(int) P4_curve_x.get(2)){//1<2   2>3
										
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P4_curve_x.get(0));
										tmp_curve_x.add(P4_curve_x.get(1));
										tmp_curve_y.add(P4_curve_y.get(0));
										tmp_curve_y.add(P4_curve_y.get(1));
										for(float tmp_x=P4_curve_x.get(tmp-1);tmp_x<=P4_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player4_paint);//black
										}
									}
									else if(tmp==2 && ( (int) P4_curve_x.get(0)>(int) P4_curve_x.get(1) )){//1>2   2<3
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P4_curve_x.get(1));
										tmp_curve_x.add(P4_curve_x.get(2));
										tmp_curve_y.add(P4_curve_y.get(1));
										tmp_curve_y.add(P4_curve_y.get(2));
										for(float tmp_x=P4_curve_x.get(tmp-1);tmp_x<=P4_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player4_paint);//black
										}
									}
									else{
										for(float tmp_x=P4_curve_x.get(tmp-1);tmp_x<=P4_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
											tmp_y=lagrange(P4_curve_x,P4_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player4_paint);//black
										}
									}
								}
							}
							else if(P4_curve_x.get(tmp-1)>P4_curve_x.get(tmp)){//--
								
								if(tmp==1 && ( (int) P4_curve_x.get(1)==(int) P4_curve_x.get(2) )){//第二、三格一樣，給lagrange的陣列不能有x一樣的情況，所以只能給lagrange兩格(一、二格)
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P4_curve_x.get(0));
									tmp_curve_x.add(P4_curve_x.get(1));
									tmp_curve_y.add(P4_curve_y.get(0));
									tmp_curve_y.add(P4_curve_y.get(1));
									for(float tmp_x=P4_curve_x.get(tmp-1);tmp_x>=P4_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player4_paint);//red
									}
								}
								else if(tmp==2 && ( (int) P4_curve_x.get(0)==(int) P4_curve_x.get(1) )){//第一、二格一樣
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P4_curve_x.get(1));
									tmp_curve_x.add(P4_curve_x.get(2));
									tmp_curve_y.add(P4_curve_y.get(1));
									tmp_curve_y.add(P4_curve_y.get(2));
									for(float tmp_x=P4_curve_x.get(tmp-1);tmp_x>=P4_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player4_paint);//red
									}
								}
								else{//都不一樣
									
									if(tmp==1 &&(int) P4_curve_x.get(1)<(int) P4_curve_x.get(2)){//1>2   2<3
										
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P4_curve_x.get(0));
										tmp_curve_x.add(P4_curve_x.get(1));
										tmp_curve_y.add(P4_curve_y.get(0));
										tmp_curve_y.add(P4_curve_y.get(1));
										for(float tmp_x=P4_curve_x.get(tmp-1);tmp_x>=P4_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player4_paint);//red
										}
									}
									else if(tmp==2 && ( (int) P4_curve_x.get(0)<(int) P4_curve_x.get(1) )){//1<2   2>3
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P4_curve_x.get(1));
										tmp_curve_x.add(P4_curve_x.get(2));
										tmp_curve_y.add(P4_curve_y.get(1));
										tmp_curve_y.add(P4_curve_y.get(2));
										for(float tmp_x=P4_curve_x.get(tmp-1);tmp_x>=P4_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player4_paint);//red
										}
									}
									else{
										for(float tmp_x=P4_curve_x.get(tmp-1);tmp_x>=P4_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(P4_curve_x,P4_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player4_paint);//red
										}
									}
								}
							}
							else{//==  line 其中兩格一樣的話，就兩格之間畫直線
								
								int tmp_x=P4_curve_x.get(tmp-1);
								if(P4_curve_y.get(tmp-1)>P4_curve_y.get(tmp)){
									for(float tmp_y=P4_curve_y.get(tmp-1);tmp_y>=P4_curve_y.get(tmp);tmp_y=tmp_y-(float)0.1){
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player4_paint);//blue
									}
								}
								else{
									for(float tmp_y=P4_curve_y.get(tmp-1);tmp_y<=P4_curve_y.get(tmp);tmp_y=tmp_y+(float)0.1){
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player4_paint);//green
									}
								}
							}
						}
						c4_idx=0;
						P4_curve_x.clear();
						P4_curve_y.clear();
						P4_curve_x.add(c4_idx, x);
						P4_curve_y.add(c4_idx, y);
						c4_idx++;
					}
					
					
					
					tempCanvas.drawBitmap(tempBitmap, 0, 0, transparent_paint);
					tempCanvas.drawCircle(x+ v.getWidth()/2, y+ v.getHeight()/2, 5, player4_paint);
					circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
				}
				v.layout(x, y, x + v.getWidth(), y + v.getHeight());
				v.postInvalidate();
				return true;
			case MotionEvent.ACTION_UP:// 放開圖片時
				if (recordcheck == true) {
					TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
					int startIndexTmp = P4_startIndex;
					while (P4_startIndex != -1) {
						RunBag tmp = new RunBag();
						tmp.setStartTime(seekBarCallbackStartTime);
						tmp.setHandler("P4_Handle");
						tmp.setRoadStart(P4_startIndex + 1);
						P4_startIndex += 2;
						P4_startIndex = P4.getRoad(0, P4_startIndex);
						if (P4_startIndex == -1) {
							startIndexTmp = P4.getLastRoad();
							tmp.setRoadEnd(startIndexTmp);
						} else {
							tmp.setRoadEnd(P4_startIndex);
							startIndexTmp = P4_startIndex;
						}
						tmp.setDuration(seekBarCallbackDuration);
						RunLine.add(tmp);
						timefrag.setSeekBarId(seekbar_tmp_id);
						seekbar_tmp_id++;
						timefrag.createSeekbar(4);
					}
					P4_startIndex = startIndexTmp + 1;
				}
				else{
					P4_Initial_Position_x=x;
					P4_Initial_Position_y=y;
				}
				return true;
			}
			return true;
		}
	};
	/**TODO player5**/
	private OnTouchListener player5_Listener = new OnTouchListener() {
		private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
		private int startX, startY; // 原本圖片存在的X,Y軸位置
		private int x, y; // 最終的顯示位置
		private float tmp_y;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TouchEvent(P1,paint,mx,my,startX,startY,v,event,"P1");
			
			mx = (int) (event.getRawX());
			my = (int) (event.getRawY());

			switch (event.getAction()) { // 判斷觸控的動作
			case MotionEvent.ACTION_DOWN:// 按下圖片時
				
				startX = (int) event.getX();
				startY = my - v.getTop();
				if (recordcheck == true) {
					P5.setRoad(0); // split positions
					P5.setRoad_3d(0);
				}
				return true;
			case MotionEvent.ACTION_MOVE:// 移動圖片時
				x = mx - startX;
				y = my - startY;
				if (recordcheck == true) {
					P5.setRoad(x);
					P5.setRoad(y);
					P5.setRoad_3d((int) event.getRawX());
					P5.setRoad_3d((int) event.getRawY());
					
					P5_curve_x.add(c5_idx, x);
					P5_curve_y.add(c5_idx, y);
					c5_idx++;
					
					if(c5_idx==N){
						for(int tmp=1;tmp<N;tmp++){
							if(P5_curve_x.get(tmp-1)<P5_curve_x.get(tmp)){//++
								if(tmp==1 && ( (int) P5_curve_x.get(1)==(int) P5_curve_x.get(2) )){//第二、三格一樣，給lagrange的陣列不能有x一樣的情況，所以只能給lagrange兩格(一、二格)
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P5_curve_x.get(0));
									tmp_curve_x.add(P5_curve_x.get(1));
									tmp_curve_y.add(P5_curve_y.get(0));
									tmp_curve_y.add(P5_curve_y.get(1));
									for(float tmp_x=P5_curve_x.get(tmp-1);tmp_x<=P5_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player5_paint);//black
									}
								}
								else if(tmp==2 && ( (int) P5_curve_x.get(0)==(int) P5_curve_x.get(1) )){//第一、二格一樣
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P5_curve_x.get(1));
									tmp_curve_x.add(P5_curve_x.get(2));
									tmp_curve_y.add(P5_curve_y.get(1));
									tmp_curve_y.add(P5_curve_y.get(2));
									for(float tmp_x=P5_curve_x.get(tmp-1);tmp_x<=P5_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player5_paint);//black
									}
								}
								else{//都不一樣
									if(tmp==1 &&(int) P5_curve_x.get(1)>(int) P5_curve_x.get(2)){//1<2   2>3
										
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P5_curve_x.get(0));
										tmp_curve_x.add(P5_curve_x.get(1));
										tmp_curve_y.add(P5_curve_y.get(0));
										tmp_curve_y.add(P5_curve_y.get(1));
										for(float tmp_x=P5_curve_x.get(tmp-1);tmp_x<=P5_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player5_paint);//black
										}
									}
									else if(tmp==2 && ( (int) P5_curve_x.get(0)>(int) P5_curve_x.get(1) )){//1>2   2<3
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P5_curve_x.get(1));
										tmp_curve_x.add(P5_curve_x.get(2));
										tmp_curve_y.add(P5_curve_y.get(1));
										tmp_curve_y.add(P5_curve_y.get(2));
										for(float tmp_x=P5_curve_x.get(tmp-1);tmp_x<=P5_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player5_paint);//black
										}
									}
									else{
										for(float tmp_x=P5_curve_x.get(tmp-1);tmp_x<=P5_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
											tmp_y=lagrange(P5_curve_x,P5_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player5_paint);//black
										}
									}
								}
							}
							else if(P5_curve_x.get(tmp-1)>P5_curve_x.get(tmp)){//--
								
								if(tmp==1 && ( (int) P5_curve_x.get(1)==(int) P5_curve_x.get(2) )){//第二、三格一樣，給lagrange的陣列不能有x一樣的情況，所以只能給lagrange兩格(一、二格)
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P5_curve_x.get(0));
									tmp_curve_x.add(P5_curve_x.get(1));
									tmp_curve_y.add(P5_curve_y.get(0));
									tmp_curve_y.add(P5_curve_y.get(1));
									for(float tmp_x=P5_curve_x.get(tmp-1);tmp_x>=P5_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player5_paint);//red
									}
								}
								else if(tmp==2 && ( (int) P5_curve_x.get(0)==(int) P5_curve_x.get(1) )){//第一、二格一樣
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(P5_curve_x.get(1));
									tmp_curve_x.add(P5_curve_x.get(2));
									tmp_curve_y.add(P5_curve_y.get(1));
									tmp_curve_y.add(P5_curve_y.get(2));
									for(float tmp_x=P5_curve_x.get(tmp-1);tmp_x>=P5_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player5_paint);//red
									}
								}
								else{//都不一樣
									
									if(tmp==1 &&(int) P5_curve_x.get(1)<(int) P5_curve_x.get(2)){//1>2   2<3
										
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P5_curve_x.get(0));
										tmp_curve_x.add(P5_curve_x.get(1));
										tmp_curve_y.add(P5_curve_y.get(0));
										tmp_curve_y.add(P5_curve_y.get(1));
										for(float tmp_x=P5_curve_x.get(tmp-1);tmp_x>=P5_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player5_paint);//red
										}
									}
									else if(tmp==2 && ( (int) P5_curve_x.get(0)<(int) P5_curve_x.get(1) )){//1<2   2>3
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(P5_curve_x.get(1));
										tmp_curve_x.add(P5_curve_x.get(2));
										tmp_curve_y.add(P5_curve_y.get(1));
										tmp_curve_y.add(P5_curve_y.get(2));
										for(float tmp_x=P5_curve_x.get(tmp-1);tmp_x>=P5_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player5_paint);//red
										}
									}
									else{
										for(float tmp_x=P5_curve_x.get(tmp-1);tmp_x>=P5_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(P5_curve_x,P5_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player5_paint);//red
										}
									}
								}
							}
							else{//==  line 其中兩格一樣的話，就兩格之間畫直線
								
								int tmp_x=P5_curve_x.get(tmp-1);
								if(P5_curve_y.get(tmp-1)>P5_curve_y.get(tmp)){
									for(float tmp_y=P5_curve_y.get(tmp-1);tmp_y>=P5_curve_y.get(tmp);tmp_y=tmp_y-(float)0.1){
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player5_paint);//blue
									}
								}
								else{
									for(float tmp_y=P5_curve_y.get(tmp-1);tmp_y<=P5_curve_y.get(tmp);tmp_y=tmp_y+(float)0.1){
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player5_paint);//green
									}
								}
							}
						}
						c5_idx=0;
						P5_curve_x.clear();
						P5_curve_y.clear();
						P5_curve_x.add(c5_idx, x);
						P5_curve_y.add(c5_idx, y);
						c5_idx++;
					}
					
					tempCanvas.drawBitmap(tempBitmap, 0, 0, transparent_paint);
					tempCanvas.drawCircle(x+ v.getWidth()/2, y+ v.getHeight()/2, 5, player5_paint);
					circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
				}
				v.layout(x, y, x + v.getWidth(), y + v.getHeight());
				v.postInvalidate();
				return true;
			case MotionEvent.ACTION_UP:// 放開圖片時
				if (recordcheck == true) {
					TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
					int startIndexTmp = P5_startIndex;
					while (P5_startIndex != -1) {
						RunBag tmp = new RunBag();
						tmp.setStartTime(seekBarCallbackStartTime);
						tmp.setHandler("P5_Handle");
						tmp.setRoadStart(P5_startIndex + 1);
						P5_startIndex += 2;
						P5_startIndex = P5.getRoad(0, P5_startIndex);
						if (P5_startIndex == -1) {
							startIndexTmp = P5.getLastRoad();
							tmp.setRoadEnd(startIndexTmp);
						} else {
							tmp.setRoadEnd(P5_startIndex);
							startIndexTmp = P5_startIndex;
						}
						tmp.setDuration(seekBarCallbackDuration);
						RunLine.add(tmp);
						timefrag.setSeekBarId(seekbar_tmp_id);
						seekbar_tmp_id++;
						timefrag.createSeekbar(5);
					}
					P5_startIndex = startIndexTmp + 1;
				}
				else{
					P5_Initial_Position_x=x;
					P5_Initial_Position_y=y;
				}
				return true;
			}
			return true;
		}
	};
	/**TODO ball**/
	private OnTouchListener ball_Listener = new OnTouchListener() {
		private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
		private int startX, startY; // 原本圖片存在的X,Y軸位置
		private int x, y; // 最終的顯示位置
		private float tmp_y;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TouchEvent(P1,paint,mx,my,startX,startY,v,event,"P1");
			
			
			mx = (int) (event.getRawX());
			my = (int) (event.getRawY());

			switch (event.getAction()) { // 判斷觸控的動作
			case MotionEvent.ACTION_DOWN:// 按下圖片時
				Log.d("debug", "action down !!!");
				startX = (int) event.getX();
				startY = my - v.getTop();
				if (recordcheck == true) {
					B.setRoad(0); // split positions
					B.setRoad_3d(0);
					
					Log.d("debug", "setRoad(0)!!!!   the position is: "+Integer.toString(B.getRoadSize()));
					
				}
				return true;
			case MotionEvent.ACTION_MOVE:// 移動圖片時
				x = mx - startX;
				y = my - startY;
				if(x==0){
					Log.d("debug", "x=0!!!!!!!");
				}
				if(y==0){
					Log.d("debug", "y=0!!!!!!!");
				}
				if (recordcheck == true) {
					B.setRoad(x);
					Log.d("debug", "x="+Integer.toString(x)+"   position= "+Integer.toString(B.getRoadSize()));
					B.setRoad(y);
					Log.d("debug", "y="+Integer.toString(y)+"   position= "+Integer.toString(B.getRoadSize()));
					
					B.setRoad_3d((int) event.getRawX());
					B.setRoad_3d((int) event.getRawY());

					Ball_curve_x.add(Ball_idx, x);
					Ball_curve_y.add(Ball_idx, y);
					Ball_idx++;
					
					if(Ball_idx==N){
						for(int tmp=1;tmp<N;tmp++){
							if(Ball_curve_x.get(tmp-1)<Ball_curve_x.get(tmp)){//++
								if(tmp==1 && ( (int) Ball_curve_x.get(1)==(int) Ball_curve_x.get(2) )){//第二、三格一樣，給lagrange的陣列不能有x一樣的情況，所以只能給lagrange兩格(一、二格)
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(Ball_curve_x.get(0));
									tmp_curve_x.add(Ball_curve_x.get(1));
									tmp_curve_y.add(Ball_curve_y.get(0));
									tmp_curve_y.add(Ball_curve_y.get(1));
									for(float tmp_x=Ball_curve_x.get(tmp-1);tmp_x<=Ball_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, ball_paint);//black
									}
								}
								else if(tmp==2 && ( (int) Ball_curve_x.get(0)==(int) Ball_curve_x.get(1) )){//第一、二格一樣
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(Ball_curve_x.get(1));
									tmp_curve_x.add(Ball_curve_x.get(2));
									tmp_curve_y.add(Ball_curve_y.get(1));
									tmp_curve_y.add(Ball_curve_y.get(2));
									for(float tmp_x=Ball_curve_x.get(tmp-1);tmp_x<=Ball_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, ball_paint);//black
									}
								}
								else{//都不一樣
									if(tmp==1 &&(int) Ball_curve_x.get(1)>(int) Ball_curve_x.get(2)){//1<2   2>3
										
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(Ball_curve_x.get(0));
										tmp_curve_x.add(Ball_curve_x.get(1));
										tmp_curve_y.add(Ball_curve_y.get(0));
										tmp_curve_y.add(Ball_curve_y.get(1));
										for(float tmp_x=Ball_curve_x.get(tmp-1);tmp_x<=Ball_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, ball_paint);//black
										}
									}
									else if(tmp==2 && ( (int) Ball_curve_x.get(0)>(int) Ball_curve_x.get(1) )){//1>2   2<3
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(Ball_curve_x.get(1));
										tmp_curve_x.add(Ball_curve_x.get(2));
										tmp_curve_y.add(Ball_curve_y.get(1));
										tmp_curve_y.add(Ball_curve_y.get(2));
										for(float tmp_x=Ball_curve_x.get(tmp-1);tmp_x<=Ball_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, ball_paint);//black
										}
									}
									else{
										for(float tmp_x=Ball_curve_x.get(tmp-1);tmp_x<=Ball_curve_x.get(tmp);tmp_x=tmp_x+(float)0.1){
											tmp_y=lagrange(Ball_curve_x,Ball_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, ball_paint);//black
										}
									}
								}
							}
							else if(Ball_curve_x.get(tmp-1)>Ball_curve_x.get(tmp)){//--
								
								if(tmp==1 && ( (int) Ball_curve_x.get(1)==(int) Ball_curve_x.get(2) )){//第二、三格一樣，給lagrange的陣列不能有x一樣的情況，所以只能給lagrange兩格(一、二格)
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(Ball_curve_x.get(0));
									tmp_curve_x.add(Ball_curve_x.get(1));
									tmp_curve_y.add(Ball_curve_y.get(0));
									tmp_curve_y.add(Ball_curve_y.get(1));
									for(float tmp_x=Ball_curve_x.get(tmp-1);tmp_x>=Ball_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, ball_paint);//red
									}
								}
								else if(tmp==2 && ( (int) Ball_curve_x.get(0)==(int) Ball_curve_x.get(1) )){//第一、二格一樣
									Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
									tmp_curve_x.add(Ball_curve_x.get(1));
									tmp_curve_x.add(Ball_curve_x.get(2));
									tmp_curve_y.add(Ball_curve_y.get(1));
									tmp_curve_y.add(Ball_curve_y.get(2));
									for(float tmp_x=Ball_curve_x.get(tmp-1);tmp_x>=Ball_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
										tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, ball_paint);//red
									}
								}
								else{//都不一樣
									
									if(tmp==1 &&(int) Ball_curve_x.get(1)<(int) Ball_curve_x.get(2)){//1>2   2<3
										
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(Ball_curve_x.get(0));
										tmp_curve_x.add(Ball_curve_x.get(1));
										tmp_curve_y.add(Ball_curve_y.get(0));
										tmp_curve_y.add(Ball_curve_y.get(1));
										for(float tmp_x=Ball_curve_x.get(tmp-1);tmp_x>=Ball_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, ball_paint);//red
										}
									}
									else if(tmp==2 && ( (int) Ball_curve_x.get(0)<(int) Ball_curve_x.get(1) )){//1<2   2>3
										Vector<Integer> tmp_curve_x = new Vector(),tmp_curve_y=new Vector();
										tmp_curve_x.add(Ball_curve_x.get(1));
										tmp_curve_x.add(Ball_curve_x.get(2));
										tmp_curve_y.add(Ball_curve_y.get(1));
										tmp_curve_y.add(Ball_curve_y.get(2));
										for(float tmp_x=Ball_curve_x.get(tmp-1);tmp_x>=Ball_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(tmp_curve_x,tmp_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, ball_paint);//red
										}
									}
									else{
										for(float tmp_x=Ball_curve_x.get(tmp-1);tmp_x>=Ball_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(Ball_curve_x,Ball_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, ball_paint);//red
										}
									}
								}
							}
							else{//==  line 其中兩格一樣的話，就兩格之間畫直線
								
								int tmp_x=Ball_curve_x.get(tmp-1);
								if(Ball_curve_y.get(tmp-1)>Ball_curve_y.get(tmp)){
									for(float tmp_y=Ball_curve_y.get(tmp-1);tmp_y>=Ball_curve_y.get(tmp);tmp_y=tmp_y-(float)0.1){
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, ball_paint);//blue
									}
								}
								else{
									for(float tmp_y=Ball_curve_y.get(tmp-1);tmp_y<=Ball_curve_y.get(tmp);tmp_y=tmp_y+(float)0.1){
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, ball_paint);//green
									}
								}
							}
						}
						Ball_idx=0;
						Ball_curve_x.clear();
						Ball_curve_y.clear();
						Ball_curve_x.add(Ball_idx, x);
						Ball_curve_y.add(Ball_idx, y);
						Ball_idx++;
					}
					
					tempCanvas.drawBitmap(tempBitmap, 0, 0, transparent_paint);
					tempCanvas.drawCircle(x+ v.getWidth()/2, y+ v.getHeight()/2, 5, ball_paint);
					circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
				}
				v.layout(x, y, x + v.getWidth(), y + v.getHeight());
				v.postInvalidate();
				return true;
			case MotionEvent.ACTION_UP:// 放開圖片時
				
				if (recordcheck == true) {
					TimeLine timefrag = (TimeLine) getActivity().getFragmentManager().findFragmentById(R.id.time_line);
					int startIndexTmp = B_startIndex;
					
					Log.d("debug", "action up !!!");
					
					
					
					 while (B_startIndex != -1) {
						RunBag tmp = new RunBag();
						tmp.setStartTime(seekBarCallbackStartTime);
						tmp.setHandler("B_Handle");
						tmp.setRoadStart(B_startIndex + 1);
						B_startIndex += 2;
						B_startIndex = B.getRoad(0, B_startIndex);
						if (B_startIndex == -1) {
							startIndexTmp = B.getLastRoad();
							Log.d("debug", "-1 condition    startindextmp =  "+Integer.toString(startIndexTmp));
							tmp.setRoadEnd(startIndexTmp);
						} else {
							tmp.setRoadEnd(B_startIndex);
							Log.d("debug", "else!!!!!!!!!    that road is= "+Integer.toString(B.handleGetRoad(B_startIndex)));
							Log.d("debug", "else!!!!!!!!!    B_startIndex= "+Integer.toString(B_startIndex));
							Log.d("debug", "else!!!!!!!!!    startIndexTmp= "+Integer.toString(startIndexTmp));
							startIndexTmp = B_startIndex;
						}
						tmp.setDuration(seekBarCallbackDuration);
						RunLine.add(tmp);
						timefrag.setSeekBarId(seekbar_tmp_id);
						seekbar_tmp_id++;
						Log.d("debug", "seekbar_tmp_id="+Integer.toString(seekbar_tmp_id));
						timefrag.createSeekbar(6);
					}
					/**TODO 結果是getRoadSize裡面的function有問題，修正之後，問題沒有出現了**/
					B_startIndex = startIndexTmp + 1;
					Log.d("debug", "last   ttttttt   B_startIndex =  "+Integer.toString(B_startIndex));
				}
				else{
					B_Initial_Position_x=x;
					B_Initial_Position_y=y;
				}
				return true;
			}
			return true;
		}
	};

}
