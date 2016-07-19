package com.example.helloworld;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/*���~��������*/
/*�s���U�Ӥ���M��t�d�����ǻ��T���쥿�T������W��*/
/*����B�����ۥѲ��ʡB����3D����*/

public class MainActivity extends ActionBarActivity implements TimeLine.CallbackInterface ,ButtonDraw.CallbackInterface,MainFragment.CallbackInterface{
	
	private MainFragment mainfrag;
	private int seekBarCallbackStartTime;
	private int seekBarCallbackDuration;
	private boolean recordcheck;//�ˬd�O�_���U���s��
	private Vector<RunBag> RunLine;
	private Player P1,P2,P3,P4,P5,B;
	
	public void RunLineInfo(Vector<RunBag> in_RunLine){
		RunLine=in_RunLine;
	}
	public void P1Info(Player in_P1){
		P1=in_P1;
	}
	public void P2Info(Player in_P2){
		P2=in_P2;
	}
	public void P3Info(Player in_P3){
		P3=in_P3;
	}
	public void P4Info(Player in_P4){
		P4=in_P4;
	}
	public void P5Info(Player in_P5){
		P5=in_P5;
	}
	public void BInfo(Player in_B){
		B=in_B;
	}
	
	public void seekBarStartTime(int startTime){//�����qTimeline�ǹL�Ӫ��ɶ�
		seekBarCallbackStartTime = startTime;
		mainfrag.pass_start_time(startTime);
	}
	
	public void seekBarDuration(int duration){//�����qTimeline�ǹL�Ӫ�����ɶ�
		seekBarCallbackDuration = duration;
		mainfrag.pass_duration(duration);
	}
	////////////////////////////Timeline���ɶ�(startTime,duration)�n�ǵ�main_fragment///////////////////////////
	
	public void setRecordCheck(boolean in_recordcheck){//in_recordcheck�O�bButtonDraw�̭��A�z�Linterface���覡�A��ButtonDraw�i�H�I�s�o�̪�function�A�i�ӳ]�wrecordcheck����
		recordcheck = in_recordcheck;
		mainfrag.pass_recordcheck(in_recordcheck);
	}
	///////////////////////////recordcheck�n�ǵ�main_fragment/////////////////////////////////////////////////
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("debug", "MainActivity onCreate!");
        Button button = (Button)findViewById(R.id.button01);
        button.setOnClickListener(btnListener);//�ۥѲ���
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
        Button three_d = (Button)findViewById(R.id.three_d);
        three_d.setOnClickListener(three_d_Listener);//3D����
        three_d.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
        Button play = (Button)findViewById(R.id.playbutton);
        play.setOnClickListener(playListener);//����
        play.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
        mainfrag = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.Main);
        
    }

    private OnClickListener btnListener = new OnClickListener(){//������"�ۥѲ���"
    	@Override
    	public void onClick(View v){
    		Intent intent = new Intent();
    		intent.setClass(MainActivity.this,MultiActivity.class);
    		startActivity(intent);
    		MainActivity.this.finish();
    	}
    };
//////////////////////////////////////////�n�Ǹ�T��3D����//////////////////////////////////////////////////////////////
////////////////////////�I�smainlayout��function�A�A�qmainlayout�Ǹ�Ƶ�3D//////////////////////////////////////////////////////////////
    
    private OnClickListener three_d_Listener = new OnClickListener(){//������"3D����"
    	@Override
    	public void onClick(View v){
    		
    	mainfrag.pass_RunLine_Player_Info();
    	
    	
   		Intent intent = new Intent();
   		intent.setClass(MainActivity.this,ThreeDMainActivity.class);
   		
   		myRunLine myRunline = new myRunLine();
   		Vector <RunBag> test = new Vector();
   		myRunline.setRunLine(RunLine);
   		Log.e("RunLine = ", String.valueOf(RunLine.get(1).getRate()));
			//new�@��Bundle����A�ñN�n�ǻ�����ƶǤJ
			Bundle bundle = new Bundle();
			bundle.putSerializable("p1", P1);
			bundle.putSerializable("p2", P2);
			bundle.putSerializable("p3", P3);
			bundle.putSerializable("p4", P4);
			bundle.putSerializable("p5", P5);
			bundle.putSerializable("ball", B);
			bundle.putSerializable("myRunLine", myRunline);
			//�NBundle����assign��intent
			intent.putExtras(bundle);
   		
			startActivity(intent);
			MainActivity.this.finish();
    	}
    };
////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private OnClickListener playListener = new OnClickListener(){//����
    	@Override
    	public void onClick(View v) {
	////////////////////////Save each road(RunBag) and add into RunLine////////////////////////
    		
    		mainfrag.playButton();
    	}
    };
    

}
