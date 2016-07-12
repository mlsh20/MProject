package com.example.helloworld;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class TimeLine extends Fragment {
	
	public interface CallbackInterface{
		public void seekBarStartTime(int progress);
		public void seekBarDuration(int duration);
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
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
		
		return inflater.inflate(R.layout.timeline_layout, container,false);
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
        mSeek2 = (mySeekBar) getView().findViewById(R.id.seekBar_tg2);
        mSeek2.setOnSeekBarChangeListener(mySeekBarOnChange);
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
	
private mySeekBar.OnSeekBarChangeListener mySeekBarOnChange = new mySeekBar.OnSeekBarChangeListener() {
	
	@Override
	public void onProgressChanged(mySeekBar seekBar, double progressLow,
			double progressHigh) {
		// TODO Auto-generated method stub
		int duration;
		duration = (int)progressHigh - (int)progressLow;
		mCallback.seekBarStartTime((int)progressLow);
		mCallback.seekBarDuration(duration);
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
