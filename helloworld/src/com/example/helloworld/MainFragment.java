package com.example.helloworld;

import java.util.Vector;

import com.example.helloworld.TimeLine.CallbackInterface;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

/*處理球員&球的觸控、儲存路徑*/

public class MainFragment extends Fragment {

	
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

	private ImageView circle;
	Bitmap tempBitmap;

	private boolean recordcheck = false;
	private int seekBarCallbackStartTime;
	private int seekBarCallbackDuration;
	private Vector<RunBag> RunLine = new Vector();

	private Handler handler = new Handler();

	public interface CallbackInterface {
		public void RunLineInfo(Vector<RunBag> in_RunLine);

		public void P1Info(Player in_P1);

		public void P2Info(Player in_P2);

		public void P3Info(Player in_P3);

		public void P4Info(Player in_P4);

		public void P5Info(Player in_P5);

		public void BInfo(Player in_B);

	}

	private CallbackInterface mCallback;

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.main_layout, container, false);
		Log.d("debug", "MainFragment onCreateView!");
		//Log.e("MainFragment onCreateView!", String.valueOf(recordcheck));
		return rootView;
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

		circle=(ImageView) getView().findViewById(R.id.circle);
		
		
		
		
	}

	public void pass_start_time(int input) {
		seekBarCallbackStartTime = input;
	}

	public void pass_duration(int input) {
		seekBarCallbackDuration = input;
	}

	public void pass_recordcheck(boolean input) {
		recordcheck = input;
		if (recordcheck==true){
			tempBitmap = Bitmap.createBitmap(circle.getWidth(),circle.getHeight(),Bitmap.Config.ARGB_8888);
		}
		/*int[] location =new int[2];
		player1.getLocationOnScreen(location);*/
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

	public void playButton() {
		if (RunLine.isEmpty()) {
			Log.e("empty!", String.valueOf(RunLine.size()));
		} else {
			/*先把全部player移到按下錄製鍵時的位置*/
			
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
		private Paint paint = new Paint();

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TouchEvent(P1,paint,mx,my,startX,startY,v,event,"P1");
			
			Canvas tempCanvas = null;
			paint = new Paint(); // 創建畫筆
			paint.setAntiAlias(true); // 設置畫筆的鋸齒效果。 true是去除。
			paint.setColor(Color.BLUE); // 設置顏色
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
				if (recordcheck == true) {
					P1.setRoad(x);
					P1.setRoad(y);
					P1.setRoad_3d((int) event.getRawX());
					P1.setRoad_3d((int) event.getRawY());
					tempCanvas = new Canvas(tempBitmap);
					tempCanvas.drawBitmap(tempBitmap, 0, 0, null);
					tempCanvas.drawCircle(x+ v.getWidth()/2, y+ v.getHeight()/2, 5, paint);
					ImageView myImageView = (ImageView) getView().findViewById(R.id.circle);
					myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
				}
				v.layout(x, y, x + v.getWidth(), y + v.getHeight());
				v.postInvalidate();
				break;
			case MotionEvent.ACTION_UP:// 放開圖片時
				if (recordcheck == true) {
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
		private Paint paint = new Paint();
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TouchEvent(P1,paint,mx,my,startX,startY,v,event,"P1");
			
			Canvas tempCanvas = null;
			paint.setAntiAlias(true); // 設置畫筆的鋸齒效果。 true是去除。
			paint.setColor(Color.RED); // 設置顏色
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
					tempCanvas = new Canvas(tempBitmap);
					P2.setRoad(x);
					P2.setRoad(y);
					P2.setRoad_3d((int) event.getRawX());
					P2.setRoad_3d((int) event.getRawY());

					tempCanvas.drawBitmap(tempBitmap, 0, 0, null);
					tempCanvas.drawCircle(x+ v.getWidth()/2, y+ v.getHeight()/2, 5, paint);
					ImageView myImageView = (ImageView) getView().findViewById(R.id.circle);
					myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
				}
				v.layout(x, y, x + v.getWidth(), y + v.getHeight());
				v.postInvalidate();
				break;
			case MotionEvent.ACTION_UP:// 放開圖片時
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
		private Paint paint = new Paint();

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TouchEvent(P1,paint,mx,my,startX,startY,v,event,"P1");
			
			Canvas tempCanvas = null;
			paint.setAntiAlias(true); // 設置畫筆的鋸齒效果。 true是去除。
			paint.setColor(Color.BLUE); // 設置顏色
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
				if (recordcheck == true) {
					P3.setRoad(x);
					P3.setRoad(y);
					P3.setRoad_3d((int) event.getRawX());
					P3.setRoad_3d((int) event.getRawY());
					tempCanvas = new Canvas(tempBitmap);
					tempCanvas.drawBitmap(tempBitmap, 0, 0, null);
					tempCanvas.drawCircle(x+ v.getWidth()/2, y+ v.getHeight()/2, 5, paint);
					ImageView myImageView = (ImageView) getView().findViewById(R.id.circle);
					myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
				}
				v.layout(x, y, x + v.getWidth(), y + v.getHeight());
				v.postInvalidate();
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
		private Paint paint = new Paint();

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TouchEvent(P1,paint,mx,my,startX,startY,v,event,"P1");
			
			Canvas tempCanvas = null;
			paint.setAntiAlias(true); // 設置畫筆的鋸齒效果。 true是去除。
			paint.setColor(Color.GREEN); // 設置顏色
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
					
					tempCanvas = new Canvas(tempBitmap);
					tempCanvas.drawBitmap(tempBitmap, 0, 0, null);
					tempCanvas.drawCircle(x+ v.getWidth()/2, y+ v.getHeight()/2, 5, paint);
					ImageView myImageView = (ImageView) getView().findViewById(R.id.circle);
					myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
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
		private Paint paint = new Paint();

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TouchEvent(P1,paint,mx,my,startX,startY,v,event,"P1");
			
			Canvas tempCanvas = null;
			paint.setAntiAlias(true); // 設置畫筆的鋸齒效果。 true是去除。
			paint.setColor(Color.CYAN); // 設置顏色
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
					
					tempCanvas = new Canvas(tempBitmap);
					tempCanvas.drawBitmap(tempBitmap, 0, 0, null);
					tempCanvas.drawCircle(x+ v.getWidth()/2, y+ v.getHeight()/2, 5, paint);
					ImageView myImageView = (ImageView) getView().findViewById(R.id.circle);
					myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
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
		private Paint paint = new Paint();

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TouchEvent(P1,paint,mx,my,startX,startY,v,event,"P1");
			
			Canvas tempCanvas = null;
			paint.setAntiAlias(true); // 設置畫筆的鋸齒效果。 true是去除。
			paint.setColor(Color.MAGENTA); // 設置顏色
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

					tempCanvas = new Canvas(tempBitmap);
					tempCanvas.drawBitmap(tempBitmap, 0, 0, null);
					tempCanvas.drawCircle(x+ v.getWidth()/2, y+ v.getHeight()/2, 5, paint);
					ImageView myImageView = (ImageView) getView().findViewById(R.id.circle);
					myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
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
