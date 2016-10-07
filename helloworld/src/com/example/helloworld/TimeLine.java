package com.example.helloworld;

import java.util.Vector;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class TimeLine extends Fragment {
	
	public interface CallbackInterface{
		public void seekBarStartTime(int progress);
		public void seekBarDuration(int duration);
		public void seekBarId(int id);
	}
	
	private CallbackInterface mCallback;
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
		
		try{
			mCallback = (CallbackInterface) activity;
		}catch(ClassCastException e){
			throw new ClassCastException (activity.toString()+"must implement TimeLine.CallbackInterface!");
		}
		
	}
	
	private mySeekBar mSeek2;
	
	
	
	private int SeekBarId;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
		
		return inflater.inflate(R.layout.timeline_layout, container,false);
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
        /*mSeek2 = (mySeekBar) getView().findViewById(R.id.seekBar_tg2);
        mSeek2.setOnSeekBarChangeListener(mySeekBarOnChange);*/
	}
	
	@Override
	public void onResume(){
		super.onResume();
		getActivity().findViewById(R.id.time_line).setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		getActivity().findViewById(R.id.time_line).setVisibility(View.GONE);
	}
	
	public void setSeekBarId(int id_in){
		SeekBarId=id_in;
	}
	
	public void clear_record_layout(){
		LinearLayout linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player1timeline);
		linearLayout.removeAllViews();
		linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player2timeline);
		linearLayout.removeAllViews();
		linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player3timeline);
		linearLayout.removeAllViews();
		linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player4timeline);
		linearLayout.removeAllViews();
		linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player5timeline);
		linearLayout.removeAllViews();
		linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_balltimeline);
		linearLayout.removeAllViews();
	}
	
	public void createSeekbar(int player,int id,int progresslow,int duration){
		//LinearLayout linearLayout=null;
		LinearLayout linearLayout=null;
		if(player==1){
			linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player1timeline);
		}
		else if (player==2){
			linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player2timeline);
		}
		else if (player==3){
			linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player3timeline);
		}
		else if (player==4){
			linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player4timeline);
		}
		else if (player==5){
			linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player5timeline);
		}
		else if (player==6){
			linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_balltimeline);
		}
		else {
			Log.d("debug", "Error! player can't found.  Player code = "+Integer.toString(player));
		}
		
		mySeekBar tmp = new mySeekBar(getActivity());
		LayoutParams lp = new LayoutParams(180,50);
		
		/***************load***********************/
		tmp.setProgressLow(progresslow);
		tmp.setProgressHigh(progresslow+duration);
		
		lp.topMargin=55;
		
		tmp.setLayoutParams(lp);
		tmp.setId(id);
		tmp.setOnSeekBarChangeListener(mySeekBarOnChange);
		
		
		linearLayout.addView(tmp); //加入畫面上
		MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
		Vector<Integer> input=new Vector();
		input.add(id);
		input.add(progresslow);
		input.add(duration);
		mainfrag.set_seekBar_to_RunBag(input);
		mCallback.seekBarStartTime(progresslow);
		mCallback.seekBarDuration(id);
		Log.d("debug", "Load!!!!!SeekBarId = "+Integer.toString(id));
		Log.d("debug", "Load!!!!!New View's id = "+Integer.toString(tmp.getId()));
		
		/*****************************************/
		
		
		
	}
	
	public void createSeekbar(int player){
		//LinearLayout linearLayout=null;
		LinearLayout linearLayout=null;
		if(player==1){
			linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player1timeline);
		}
		else if (player==2){
			linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player2timeline);
		}
		else if (player==3){
			linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player3timeline);
		}
		else if (player==4){
			linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player4timeline);
		}
		else if (player==5){
			linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_player5timeline);
		}
		else if (player==6){
			linearLayout=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_balltimeline);
		}
		else {
			Log.d("debug", "Error! player can't found.  Player code = "+Integer.toString(player));
		}
		
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mySeekBar tmp = new mySeekBar(getActivity());
		LayoutParams lp = new LayoutParams(180,50);
		
		/**TODO setProgressHigh可以設定後滑塊的位置**/
		
		
		
		
		/****************normal*******************/
		tmp.setProgressLow(SeekBarId);
		tmp.setProgressHigh(SeekBarId+1);
		
		lp.topMargin=55;
		tmp.setLayoutParams(lp);
		tmp.setId(SeekBarId);
		tmp.setOnSeekBarChangeListener(mySeekBarOnChange);
		
		
		
		linearLayout.addView(tmp); //加入畫面上
		MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
		Vector<Integer> input=new Vector();
		input.add(SeekBarId);
		input.add(SeekBarId);
		input.add(1);
		mainfrag.set_seekBar_to_RunBag(input);
		mCallback.seekBarStartTime(SeekBarId);
		mCallback.seekBarDuration(1);
		Log.d("debug", "SeekBarId = "+Integer.toString(SeekBarId));
		Log.d("debug", "New View's id = "+Integer.toString(tmp.getId()));
		/*************************************/
		
		
	}

	
	
	
	private mySeekBar.OnSeekBarChangeListener mySeekBarOnChange = new mySeekBar.OnSeekBarChangeListener() {
	
	@Override
	public void onProgressChanged(mySeekBar seekBar, double progressLow,
			double progressHigh) {
		// TODO Auto-generated method stub
		int duration;
		duration = (int)progressHigh - (int)progressLow;
		
		MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
		/**
		 * 
		 * Vector<Integer> input: 0=Id,1=StartTime,2=Duration
		 * 
		 * **/
		
		Vector<Integer> input=new Vector();
		input.add(seekBar.getId());
		input.add((int)progressLow);
		input.add(duration);
		mainfrag.set_seekBar_to_RunBag(input);
		mCallback.seekBarStartTime((int)progressLow);
		mCallback.seekBarDuration(duration);
		
		Log.d("debug", "OnChange! "+Integer.toString(input.get(0)));
	}
	
	@Override
	public void onProgressBefore() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onProgressAfter() {
		// TODO Auto-generated method stub
		
	}
};
}
