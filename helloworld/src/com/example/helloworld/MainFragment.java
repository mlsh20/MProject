package com.example.helloworld;

import java.util.Vector;

import com.example.helloworld.TimeLine.CallbackInterface;

import android.app.Activity;
import android.app.Application;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import android.widget.ToggleButton;

/*處理球員&球的觸控、儲存路徑，畫出路徑*/

public class MainFragment extends Fragment{

	
	
	private int Total_time = 5000;// 時間軸的最大值
	private ImageView player1, player2, player3, player4, player5, ball;
	private Player P1 = new Player();
	private Player P2 = new Player();
	private Player P3 = new Player();
	private Player P4 = new Player();
	private Player P5 = new Player();
	private Player B = new Player();

	private int P1_startIndex = 0;
	private int P2_startIndex = 0;
	private int P3_startIndex = 0;
	private int P4_startIndex = 0;
	private int P5_startIndex = 0;
	private int B_startIndex = 0;
	
	private int t=0;

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
	private int c_idx=0;
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
		Log.i("debug", "onCreateView()............");
		return inflater.inflate(R.layout.main_layout, container,false);	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("debug", "MainFragment onActivityCreated!");
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
		        Log.i("debug", "tempBitmap's width = "+Integer.toString(tempBitmap.getWidth()));
		   		Log.i("debug", "tempBitmap's height = "+Integer.toString(tempBitmap.getHeight()));
		        circle.getViewTreeObserver().removeGlobalOnLayoutListener(this);  
		           
		        
		        
		        
		       }   
		  });
		
	}
	
	@Override
    public void onStart() {
        super.onStart();
        Log.i("debug", "onStart()............");
        /**/
    }
	
	@Override
	public void onResume() {
		Log.d("debug", "MainFragment onResume!");
		super.onResume();
		
	}

	@Override
	public void onPause() {
		Log.d("debug", "MainFragment onResume!");
		super.onPause();
	}

	
	
	public void onAttach(Activity activity) {
		Log.d("debug", "MainF onAttach!");
		super.onAttach(activity);

		try {
			mCallback = (CallbackInterface) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + "must implement MainFragment.CallbackInterface!");
		}
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
		Log.d("debug", "Set! "+Integer.toString(input.get(0)));
		
		
		
	}
	
	public void clear_paint(){//清除筆跡
		tempBitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);//初始化tempBitmap，指定大小為螢幕大小(大小同circle)
       	tempCanvas = new Canvas(tempBitmap);//畫透明路徑
   		tempCanvas.drawBitmap(tempBitmap, 0, 0, null);//畫透明路徑
   		tempCanvas.drawCircle(1, 1, 5, transparent_paint);//畫透明路徑
   		circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));//把tempBitmap放進circle裡
   		p.reset();
   		c_idx=0;
		P1_curve_x.clear();
		P1_curve_y.clear();
   		
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
	
	
	
	
	
	public void playButton() {
		if (RunLine.isEmpty()) {
			Log.e("empty!", String.valueOf(RunLine.size()));
		} else {
			/*先把全部player移到按下錄製鍵時的位置*/
			if(P1.getRoadSize()!=0)
				player1.layout(P1.handleGetRoad(1), P1.handleGetRoad(2),P1.handleGetRoad(1) + player1.getWidth(),P1.handleGetRoad(2) + player1.getHeight());
			if(P2.getRoadSize()!=0)
				player2.layout(P2.handleGetRoad(1), P2.handleGetRoad(2),P2.handleGetRoad(1) + player2.getWidth(),P2.handleGetRoad(2) + player2.getHeight());
			if(P3.getRoadSize()!=0)
				player3.layout(P3.handleGetRoad(1), P3.handleGetRoad(2),P3.handleGetRoad(1) + player3.getWidth(),P3.handleGetRoad(2) + player3.getHeight());
			if(P4.getRoadSize()!=0)
				player4.layout(P4.handleGetRoad(1), P4.handleGetRoad(2),P4.handleGetRoad(1) + player4.getWidth(),P4.handleGetRoad(2) + player4.getHeight());
			if(P5.getRoadSize()!=0)
				player5.layout(P5.handleGetRoad(1), P5.handleGetRoad(2),P5.handleGetRoad(1) + player5.getWidth(),P5.handleGetRoad(2) + player5.getHeight());
			if(B.getRoadSize()!=0)
				ball.layout(B.handleGetRoad(1), B.handleGetRoad(2),B.handleGetRoad(1) + ball.getWidth(),B.handleGetRoad(2) + ball.getHeight());
			/****************************/
			
			//////////////////////////////////////// Time
			//////////////////////////////////////// counter///////////////////////////////////////
			new Thread(new Runnable() {// Time counter count on per second
				@Override
				public void run() {
					int time = 0;
					int RunLineSize = RunLine.size();
					while (time < Total_time) {
						try {
							Log.e("time = ", String.valueOf(time));
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
				while (i < RunLineSize) {
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
				while (play_k < in_j - 1) {
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
				break;
			case MotionEvent.ACTION_MOVE:// 移動圖片時
				x = mx - startX;
				y = my - startY;
				int [] location =new int[2];
				v.getLocationOnScreen(location);
				if (recordcheck == true) {
					P1.setRoad(x);
					P1.setRoad(y);
					P1.setRoad_3d((int) event.getRawX());
					P1.setRoad_3d((int) event.getRawY());
					
					P1_curve_x.add(c_idx, x);
					P1_curve_y.add(c_idx, y);
					c_idx++;
					
					if(c_idx==N){
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
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player2_paint);//red
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
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player2_paint);//red
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
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player2_paint);//red
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
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player2_paint);//red
										}
									}
									else{
										for(float tmp_x=P1_curve_x.get(tmp-1);tmp_x>=P1_curve_x.get(tmp);tmp_x=tmp_x-(float)0.1){
											tmp_y=lagrange(P1_curve_x,P1_curve_y,tmp_x);
											tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player2_paint);//red
										}
									}
								}
							}
							else{//==  line 其中兩格一樣的話，就兩格之間畫直線
								
								int tmp_x=P1_curve_x.get(tmp-1);
								if(P1_curve_y.get(tmp-1)>P1_curve_y.get(tmp)){
									for(float tmp_y=P1_curve_y.get(tmp-1);tmp_y>=P1_curve_y.get(tmp);tmp_y=tmp_y-(float)0.1){
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player3_paint);//blue
									}
								}
								else{
									for(float tmp_y=P1_curve_y.get(tmp-1);tmp_y<=P1_curve_y.get(tmp);tmp_y=tmp_y+(float)0.1){
										tempCanvas.drawCircle(tmp_x+ v.getWidth()/2, tmp_y+ v.getHeight()/2, 5, player4_paint);//green
									}
								}
							}
						}
						c_idx=0;
						P1_curve_x.clear();
						P1_curve_y.clear();
						P1_curve_x.add(c_idx, x);
						P1_curve_y.add(c_idx, y);
						c_idx++;
					}
					tempCanvas.drawBitmap(tempBitmap, 0, 0, transparent_paint);
					//tempCanvas.drawCircle(x+ v.getWidth()/2, y+ v.getHeight()/2, 5, player1_paint);
					//tempCanvas.drawPath(p, paint);
					circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
				}
				v.layout(x, y, x + v.getWidth(), y + v.getHeight());
				v.postInvalidate();
				break;
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
						timefrag.setSeekBarId(t);
						t++;
						timefrag.createSeekbar();
					}
					P1_startIndex = startIndexTmp + 1;
				}
				break;
			}
			return true;
		}
	};

	private OnTouchListener player2_Listener = new OnTouchListener() {
		private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
		private int startX, startY; // 原本圖片存在的X,Y軸位置
		private int x, y; // 最終的顯示位置
		
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
					P2.setRoad(0); // split positions
					P2.setRoad_3d(0);
				}
				break;
			case MotionEvent.ACTION_MOVE:// 移動圖片時
				x = mx - startX;
				y = my - startY;
				
				if (recordcheck == true) {
					P2.setRoad(x);
					P2.setRoad(y);
					P2.setRoad_3d((int) event.getRawX());
					P2.setRoad_3d((int) event.getRawY());
					tempCanvas.drawBitmap(tempBitmap, 0, 0, transparent_paint);
					tempCanvas.drawCircle(x+ v.getWidth()/2, y+ v.getHeight()/2, 5, player2_paint);
					circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
				}
				v.layout(x, y, x + v.getWidth(), y + v.getHeight());
				v.postInvalidate();
				break;
			case MotionEvent.ACTION_UP:// 放開圖片時
				/*int[] location=  new int[2];
				v.getLocationOnScreen(location);*/
				if (recordcheck == true) {
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

					}
					P2_startIndex = startIndexTmp + 1;
				}
				break;
			}
			return true;
		}
	};

	private OnTouchListener player3_Listener = new OnTouchListener() {
		private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
		private int startX, startY; // 原本圖片存在的X,Y軸位置
		private int x, y; // 最終的顯示位置

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
				break;
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
					
					tempCanvas.drawBitmap(tempBitmap, 0, 0, transparent_paint);
					tempCanvas.drawCircle(x+ v.getWidth()/2, y+ v.getHeight()/2, 5, player3_paint);
					circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
				}
				
				break;
			case MotionEvent.ACTION_UP:// 放開圖片時
				if (recordcheck == true) {
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

					}
					P3_startIndex = startIndexTmp + 1;
				}
				break;
			}
			return true;
		}
	};

	private OnTouchListener player4_Listener = new OnTouchListener() {
		private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
		private int startX, startY; // 原本圖片存在的X,Y軸位置
		private int x, y; // 最終的顯示位置

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
				break;
			case MotionEvent.ACTION_MOVE:// 移動圖片時
				x = mx - startX;
				y = my - startY;
				if (recordcheck == true) {
					P4.setRoad(x);
					P4.setRoad(y);
					P4.setRoad_3d((int) event.getRawX());
					P4.setRoad_3d((int) event.getRawY());
					
					tempCanvas.drawBitmap(tempBitmap, 0, 0, transparent_paint);
					tempCanvas.drawCircle(x+ v.getWidth()/2, y+ v.getHeight()/2, 5, player4_paint);
					circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
				}
				v.layout(x, y, x + v.getWidth(), y + v.getHeight());
				v.postInvalidate();
				break;
			case MotionEvent.ACTION_UP:// 放開圖片時
				if (recordcheck == true) {
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

					}
					P4_startIndex = startIndexTmp + 1;
				}
				break;
			}
			return true;
		}
	};

	private OnTouchListener player5_Listener = new OnTouchListener() {
		private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
		private int startX, startY; // 原本圖片存在的X,Y軸位置
		private int x, y; // 最終的顯示位置

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
				break;
			case MotionEvent.ACTION_MOVE:// 移動圖片時
				x = mx - startX;
				y = my - startY;
				if (recordcheck == true) {
					P5.setRoad(x);
					P5.setRoad(y);
					P5.setRoad_3d((int) event.getRawX());
					P5.setRoad_3d((int) event.getRawY());
					
					tempCanvas.drawBitmap(tempBitmap, 0, 0, transparent_paint);
					tempCanvas.drawCircle(x+ v.getWidth()/2, y+ v.getHeight()/2, 5, player5_paint);
					circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
				}
				v.layout(x, y, x + v.getWidth(), y + v.getHeight());
				v.postInvalidate();
				break;
			case MotionEvent.ACTION_UP:// 放開圖片時
				if (recordcheck == true) {
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

					}
					P5_startIndex = startIndexTmp + 1;
				}
				break;
			}
			return true;
		}
	};

	private OnTouchListener ball_Listener = new OnTouchListener() {
		private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
		private int startX, startY; // 原本圖片存在的X,Y軸位置
		private int x, y; // 最終的顯示位置

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
					B.setRoad(0); // split positions
					B.setRoad_3d(0);
				}
				break;
			case MotionEvent.ACTION_MOVE:// 移動圖片時
				x = mx - startX;
				y = my - startY;
				if (recordcheck == true) {
					B.setRoad(x);
					B.setRoad(y);
					B.setRoad_3d((int) event.getRawX());
					B.setRoad_3d((int) event.getRawY());

					tempCanvas.drawBitmap(tempBitmap, 0, 0, transparent_paint);
					tempCanvas.drawCircle(x+ v.getWidth()/2, y+ v.getHeight()/2, 5, ball_paint);
					circle.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
				}
				v.layout(x, y, x + v.getWidth(), y + v.getHeight());
				v.postInvalidate();
				break;
			case MotionEvent.ACTION_UP:// 放開圖片時
				if (recordcheck == true) {
					int startIndexTmp = B_startIndex;
					while (B_startIndex != -1) {
						RunBag tmp = new RunBag();
						tmp.setStartTime(seekBarCallbackStartTime);
						tmp.setHandler("B_Handle");
						tmp.setRoadStart(B_startIndex + 1);
						B_startIndex += 2;
						B_startIndex = B.getRoad(0, B_startIndex);
						if (B_startIndex == -1) {
							startIndexTmp = B.getLastRoad();
							tmp.setRoadEnd(startIndexTmp);
						} else {
							tmp.setRoadEnd(B_startIndex);
							startIndexTmp = B_startIndex;
						}
						tmp.setDuration(seekBarCallbackDuration);
						RunLine.add(tmp);

					}
					B_startIndex = startIndexTmp + 1;
				}
				break;
			}
			return true;
		}
	};

}
