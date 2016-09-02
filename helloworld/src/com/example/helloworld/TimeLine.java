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
	
	public void createSeekbar(){
		LinearLayout linearLayout1=(LinearLayout) getView().findViewById(R.id.LinearLayout_of_playertimeline);
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mySeekBar tmp = new mySeekBar(getActivity());
		
		LayoutParams lp = new LayoutParams(200,50);
		
		lp.topMargin=55;
		tmp.setLayoutParams(lp);
		tmp.setId(SeekBarId);
		tmp.setOnSeekBarChangeListener(mySeekBarOnChange);
		/*View view = inflater.inflate(R.layout.playertimeline_layout , null, true); //讀取的page2.
		view.setId(SeekBarId);*/
		linearLayout1.addView(tmp); //加入畫面上
		Log.d("debug", "SeekBarId = "+Integer.toString(SeekBarId));
		Log.d("debug", "New View's id = "+Integer.toString(tmp.getId()));
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
