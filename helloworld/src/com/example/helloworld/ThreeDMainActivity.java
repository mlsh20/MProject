package com.example.helloworld;

import java.util.HashMap;
import java.util.Vector;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.PointF;
import min3d.Utils;
import min3d.Shared;
import min3d.core.Object3dContainer;
import min3d.core.RendererActivity;
import min3d.objectPrimitives.Box;
import min3d.objectPrimitives.Sphere;
import min3d.parser.IParser;
import min3d.parser.Parser;
import min3d.vos.Light;
import min3d.vos.TextureVo;

public class ThreeDMainActivity extends RendererActivity {
	private Object3dContainer court, ball;
	private Vector<Object3dContainer> player = new Vector();
	
	Bitmap b;
	
	
	private int Total_time = 5000;
	//camera
	private float _dx;
	private float _dy;
	private float _rot;
	private Player P1;
	private Player P2;
	private Player P3;
	private Player P4;
	private Player P5;
	private Player B;
	private Vector<RunBag> RunLine = new Vector();
	private myRunLine myRunline = new myRunLine();
	float x = 1f, y = 12f, z = 0f;
	
	
	
	protected void checkRunLine(final int in_time , final int in_RunLineSize){
    	new Thread(new Runnable(){
			@Override
	        public void run() {
				int time=in_time;
				int RunLineSize=in_RunLineSize;
				int i=0;
	            while(i<RunLineSize){
	                try{
	                	Message m = new Message();
	                	Bundle b = new Bundle();
	                	b.putInt("what", i);//將play_k打包成msg
	                	b.putInt("time",time);
	                    m.setData(b);
	                    RunLineCheck_Handle.sendMessage(m);
	                    i=i+1;
	                }catch(Throwable t){
	                }
	            }
	        }
	    }).start();
    }
    
    Handler RunLineCheck_Handle = new Handler(){
       	@Override
        public void handleMessage(Message msg) {
       		int sentI = msg.getData().getInt("what");
       		int sentTime = msg.getData().getInt("time");
       		if(RunLine.get(sentI).getStartTime()*1000==sentTime){
        		if(RunLine.get(sentI).getHandler().equals("P1_Handle")){
        			play(RunLine.get(sentI).getRate(),P1_Handle,RunLine.get(sentI).getRoadStart(),RunLine.get(sentI).getRoadEnd());
        		}
        		else if(RunLine.get(sentI).getHandler().equals("P2_Handle")){
        			play(RunLine.get(sentI).getRate(),P2_Handle,RunLine.get(sentI).getRoadStart(),RunLine.get(sentI).getRoadEnd());
        		}
        		else if(RunLine.get(sentI).getHandler().equals("P3_Handle")){
        			play(RunLine.get(sentI).getRate(),P3_Handle,RunLine.get(sentI).getRoadStart(),RunLine.get(sentI).getRoadEnd());
        		}
        		else if(RunLine.get(sentI).getHandler().equals("P4_Handle")){
        			play(RunLine.get(sentI).getRate(),P4_Handle,RunLine.get(sentI).getRoadStart(),RunLine.get(sentI).getRoadEnd());
        		}
        		else if(RunLine.get(sentI).getHandler().equals("P5_Handle")){
        			play(RunLine.get(sentI).getRate(),P5_Handle,RunLine.get(sentI).getRoadStart(),RunLine.get(sentI).getRoadEnd());
        		}
        		else if(RunLine.get(sentI).getHandler().equals("B_Handle")){
        			play(RunLine.get(sentI).getRate(),B_Handle,RunLine.get(sentI).getRoadStart(),RunLine.get(sentI).getRoadEnd());
        		}
    		}
       		}
    };
    
    protected void play(final int speed , final Handler play_handler,final int in_k,final int in_j){
    	new Thread(new Runnable(){
			@Override
	        public void run() {
				int play_k=in_k;
	            while(play_k<in_j-1){
	                try{
	                	Message m = new Message();
	                	Bundle b = new Bundle();
	                	b.putInt("what", play_k);//將play_k打包成msg
	                    m.setData(b);
	                	play_handler.sendMessage(m);
	                    Thread.sleep(speed);
	                    play_k=play_k+2;
	                }catch(Throwable t){
	                }
	            }
	        }
	    }).start();
    }
    
    
    Handler P1_Handle = new Handler(){
       	@Override
        public void handleMessage(Message msg) {
       		int sentInt = msg.getData().getInt("what");
       		Log.e("P1  sentInt=", Integer.toString(sentInt));
       		//Log.e("x   P1.handleGetRoad(", Integer.toString(sentInt)+")="+Integer.toString(P1.handleGetRoad(sentInt)));
       		//Log.e("y   P1.handleGetRoad(", Integer.toString(sentInt+1)+")="+Integer.toString(P1.handleGetRoad(sentInt+1)));
         	//player1.layout(P1.handleGetRoad(sentInt), P1.handleGetRoad(sentInt+1), P1.handleGetRoad(sentInt)+player1.getWidth(), P1.handleGetRoad(sentInt+1)+player1.getHeight());
       		setPlayer(0, (float)(P1.handleGetRoad_3d(sentInt)-410)*2/330, (float)(P1.handleGetRoad_3d(sentInt+1)-640)*3/455);
       	}
    };
    
    Handler P2_Handle = new Handler(){
       	@Override
        public void handleMessage(Message msg) {
       		int sentInt = msg.getData().getInt("what");
       		//Log.e("sentInt=", Integer.toString(sentInt));
         	//player2.layout(P2.handleGetRoad(sentInt), P2.handleGetRoad(sentInt+1), P2.handleGetRoad(sentInt)+player2.getWidth(), P2.handleGetRoad(sentInt+1)+player2.getHeight());
       		setPlayer(1, (float)(P2.handleGetRoad_3d(sentInt)-410)*2/330, (float)(P2.handleGetRoad_3d(sentInt+1)-640)*3/455);
       	}
    };

    Handler P3_Handle = new Handler(){
       	@Override
        public void handleMessage(Message msg) {
       		int sentInt = msg.getData().getInt("what");
       		//Log.e("sentInt=", Integer.toString(sentInt));
         	//player3.layout(P3.handleGetRoad(sentInt), P3.handleGetRoad(sentInt+1), P3.handleGetRoad(sentInt)+player3.getWidth(), P3.handleGetRoad(sentInt+1)+player3.getHeight());
       		setPlayer(2, (float)(P3.handleGetRoad_3d(sentInt)-410)*2/330, (float)(P3.handleGetRoad_3d(sentInt+1)-640)*3/455);
       	}
    };
    
    Handler P4_Handle = new Handler(){
       	@Override
        public void handleMessage(Message msg) {
       		int sentInt = msg.getData().getInt("what");
       		//Log.e("sentInt=", Integer.toString(sentInt));
         	//player4.layout(P4.handleGetRoad(sentInt), P4.handleGetRoad(sentInt+1), P4.handleGetRoad(sentInt)+player4.getWidth(), P4.handleGetRoad(sentInt+1)+player4.getHeight());
       		setPlayer(3, (float)(P4.handleGetRoad_3d(sentInt)-410)*2/330, (float)(P4.handleGetRoad_3d(sentInt+1)-640)*3/455);
       	}
    };
    
    Handler P5_Handle = new Handler(){
       	@Override
        public void handleMessage(Message msg) {
       		int sentInt = msg.getData().getInt("what");
       		//Log.e("P5  sentInt=", Integer.toString(sentInt));
       		//Log.e("x   P5.handleGetRoad(", Integer.toString(sentInt)+")="+Integer.toString(P1.handleGetRoad(sentInt)));
       		//Log.e("y   P5.handleGetRoad(", Integer.toString(sentInt+1)+")="+Integer.toString(P1.handleGetRoad(sentInt+1)));
         	//player5.layout(P5.handleGetRoad(sentInt), P5.handleGetRoad(sentInt+1), P5.handleGetRoad(sentInt)+player5.getWidth(), P5.handleGetRoad(sentInt+1)+player5.getHeight());
       		setPlayer(4, (float)(P5.handleGetRoad_3d(sentInt)-410)*2/330, (float)(P5.handleGetRoad_3d(sentInt+1)-640)*3/455);
       	}
    };
    
    Handler B_Handle = new Handler(){
       	@Override
        public void handleMessage(Message msg) {
       		int sentInt = msg.getData().getInt("what");
       		//Log.e("P1  sentInt=", Integer.toString(sentInt));
       		//Log.e("x   P1.handleGetRoad(", Integer.toString(sentInt)+")="+Integer.toString(P1.handleGetRoad(sentInt)));
       		//Log.e("y   P1.handleGetRoad(", Integer.toString(sentInt+1)+")="+Integer.toString(P1.handleGetRoad(sentInt+1)));
         	//ball.layout(B.handleGetRoad(sentInt), B.handleGetRoad(sentInt+1), B.handleGetRoad(sentInt)+ball.getWidth(), B.handleGetRoad(sentInt+1)+ball.getHeight());
       		ball.position().x = (float)(B.handleGetRoad_3d(sentInt)-410)*2/330;
    		ball.position().z = (float)(B.handleGetRoad_3d(sentInt+1)-640)*3/455;
       	}
    };
	
	
	
	
	@Override
	public void initScene() {
		
		//取的intent中的bundle物件
	    Bundle bundleThreeD =this.getIntent().getExtras();

	   
	    /*hashMap = (HashMap<String, Player>) bundleThreeD.getSerializable("HashMap");
	    P1 = hashMap.get("p1");*/
	    
	    P1 = (Player) bundleThreeD.getSerializable("p1");
	    P2 = (Player) bundleThreeD.getSerializable("p2");
	    P3 = (Player) bundleThreeD.getSerializable("p3");
	    P4 = (Player) bundleThreeD.getSerializable("p4");
	    P5 = (Player) bundleThreeD.getSerializable("p5");
	    B = (Player) bundleThreeD.getSerializable("ball");
	    myRunline = (myRunLine) bundleThreeD.getSerializable("myRunLine");
	    RunLine = myRunline.getRunLine();
	    //RunLine = (Vector<RunBag>) bundleThreeD.getSerializable("runline");
	    
	    
	    Log.e("RunLine", String.valueOf(RunLine.get(1).getRate()));
		
	    Light light0 = new Light();
		Light light1 = new Light();
		light0.position.setAll(3,5,-3);
		light1.position.setAll(-3,5,3);
		scene.lights().add(light0);
		scene.lights().add(light1);
		
		b = Utils.makeBitmapFromResourceId(R.drawable.player0);
		Shared.textureManager().addTextureId(b, "player0", false);
		b.recycle();
		TextureVo playerTexture = new TextureVo("player0");
		
		b = Utils.makeBitmapFromResourceId(R.drawable.basketball2);
		Shared.textureManager().addTextureId(b, "ball", false);
		b.recycle();
		TextureVo ballTexture = new TextureVo("ball");

		// player initialize
		int j = -2;
		for (int i = 0; i < 5; ++i, j+=1) {
			player.add(new Box(0.4f, 0.4f, 0.4f));
			player.get(i).position().z = +j;
			player.get(i).position().y = +0.3f;
			scene.addChild(player.get(i));
			player.get(i).textures().add(playerTexture);
		}
		// initial position
		ball = new Sphere(0.1f, 10, 10);
		ball.position().x = +0.3f;
		ball.position().y = +0.3f;
		scene.addChild(ball);
		ball.textures().add(ballTexture);
		
		ball.position().x = 0.3f;
		ball.position().z = 0.3f;
		
		IParser parser = Parser.createParser(Parser.Type.OBJ,
				getResources(), "com.example.helloworld:raw/court2_obj", true);
		parser.parse();

		court = parser.getParsedObject();
		scene.addChild(court);
		
		//texture (court)
		b = Utils.makeBitmapFromResourceId(R.drawable.plane);
		Shared.textureManager().addTextureId(b, "plane", false);
		b.recycle();
		TextureVo planeTexture01 = new TextureVo("plane");
		court.textures().add(planeTexture01);

		scene.camera().position.setAll(x, y, z);
		//play(50 , P1_Handle,P1.getStartIndex()+1,P1.getLastRoad());
		//play(2,P1_Handle,50);
		new Thread(new Runnable(){//Time counter count on per second
			@Override
	        public void run() {
				int time = 0;
				int RunLineSize=RunLine.size();
	            while(time<Total_time){
	                try{
	                	Log.e("time = ", String.valueOf(time));
	                	//do RunLine here!!
	                	//////check each road's start time in RunLine///////
	                	checkRunLine(time,RunLineSize);
	                	Thread.sleep(1000);
	                    time=time+1000;	
	                    
	                }catch(Throwable t){
	                }
	            }
	        }
	    }).start();
		
		
		
		/*ball.position().x = 0.0f;
		ball.position().z = 0.0f;
		setPlayer(0,0,0);
		setPlayer(1,2.0f,0);
		setPlayer(2,0,3.0f);
		setPlayer(3,0,1.0f);
		setPlayer(4,0,0);*/
	}

	
	
	
	@Override
	public void updateScene() {
		if (_dx != 0)
		{
			scene.camera().position.rotateY(-_dx%360);
			_dx = 0;
		}
		
		if (_dy != 0)
		{
			scene.camera().position.rotateZ(_dy%360);
			_dy = 0;
		}
	}
	
	public void setPlayer(int index, float x, float z) {
		player.get(index).position().x = x;
		player.get(index).position().z = z;
	}
	
	PointF start = new PointF();
	float oldDist = 1f;
	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;
	private float previousX, previousY;
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		
		switch (e.getAction() & MotionEvent.ACTION_MASK) { //ACTION_MASK:for multi-touch
			case MotionEvent.ACTION_DOWN:
				start.set(e.getX(), e.getY());
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(e);
				if (oldDist > 5f) {
					mode = ZOOM;
				}
				break;
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == ZOOM) { //zoom
					float newDist = spacing(e);
					if (newDist > 5.0f) {
						float scale = oldDist / newDist;
						float len = scene.camera().frustum.shortSideLength()*scale;
						if (len < 0.1f) len = 0.1f;
						if (len > 50f) len = 50f;
						scene.camera().frustum.shortSideLength(len);
						oldDist = newDist;
					}
				}
				else { // rotate
					_dx = (x - previousX)/180;
					_dy = (y - previousY)/180;
				}
	            break;
		}
		previousX = x;
	    previousY = y;
	    return true;
	}
	
	//*******************Determine the space between the first two fingers
	private float spacing(MotionEvent event) {
	   float x = event.getX(0) - event.getX(1);
	   float y = event.getY(0) - event.getY(1);
	   return (float) Math.sqrt(x * x + y * y);
	}
}
