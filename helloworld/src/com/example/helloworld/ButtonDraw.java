package com.example.helloworld;

import com.example.helloworld.TimeLine.CallbackInterface;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;

/*按鈕的抽屜*/
/*存按鈕們的地方*/
/*顯示時間軸、隱藏時間軸、開始錄製*/

public class ButtonDraw extends Fragment {
	
	private boolean ButtonDraw_recordcheck = false;//檢查目前有沒有在錄製的狀態，預設為"沒有"
	private ToggleButton record;//Toggle的錄製按鈕
	
	public interface CallbackInterface{//連接MainActivity，告訴MainActivity現在有沒有在錄製狀態
		public void setRecordCheck(boolean in_recordcheck);
	}
	
	private CallbackInterface mCallback;//是一個用來call setRecordCheck的媒介，作用是用來設定in_recordcheck
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
		
		try{
			mCallback = (CallbackInterface) activity;
		}catch(ClassCastException e){
			throw new ClassCastException (activity.toString()+"must implement ButtonDraw.CallbackInterface!");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
		
		return inflater.inflate(R.layout.buttondraw_layout, container,false);
		
	}
	
	@Override	
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		Button button2 = (Button) getView().findViewById(R.id.button02);
        button2.setOnClickListener(btn2Listener);
        button2.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
        Button button3 = (Button) getView().findViewById(R.id.button03);
        button3.setOnClickListener(btn3Listener);
        button3.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
        record = (ToggleButton) getView().findViewById(R.id.recordbutton);
        record.setOnClickListener(recordListener);
        record.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		getActivity().findViewById(R.id.ButtonDraw).setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		getActivity().findViewById(R.id.ButtonDraw).setVisibility(View.GONE);
	}
	
	

    private OnClickListener btn2Listener = new OnClickListener(){//"顯示時間軸"
    	@Override
    	public void onClick(View v) {
	    	FragmentManager fragmentManager =getFragmentManager();
	    	FragmentTransaction transaction = fragmentManager.beginTransaction();
	    	TimeLine timeline = new TimeLine();
	    	transaction.replace(R.id.time_line,timeline,"Time line");
	    	transaction.commit();
	    	fragmentManager.executePendingTransactions();
    	}
    };
    
    private OnClickListener btn3Listener = new OnClickListener(){//"隱藏時間軸"
    	@Override
    	public void onClick(View v) {
	    	FragmentManager fragmentManager =getFragmentManager();
	    	TimeLine timeline = (TimeLine) fragmentManager.findFragmentByTag("Time line");
	    	if (null != timeline){
	    		FragmentTransaction fragTran = fragmentManager.beginTransaction();
	    		fragTran.remove(timeline);
	    		fragTran.commit();
	    		fragmentManager.executePendingTransactions();
	    		return;
	    	}
    	}
    };
    
    private OnClickListener recordListener = new OnClickListener(){//開始/停止錄製
    	@Override
    	public void onClick(View v) {
    		
    		if(record.isChecked()){
    			ButtonDraw_recordcheck = true;
	    		mCallback.setRecordCheck(ButtonDraw_recordcheck);//透過mCallback來從這裡設定MainActivity裡面recordcheck的值
	    		//record.setTextOn("停止錄製");
	    		
    		}
    		else{
    			ButtonDraw_recordcheck = false;
	    		mCallback.setRecordCheck(ButtonDraw_recordcheck);//透過mCallback來從這裡設定MainActivity裡面recordcheck的值
	    		//record.setTextOff("開始錄製");
    		}
    		
	    	/*if(ButtonDraw_recordcheck == false){
	    		ButtonDraw_recordcheck = true;
	    		mCallback.setRecordCheck(ButtonDraw_recordcheck);
	    		Button record = (Button) getView().findViewById(R.id.recordbutton);
	    		record.setText("停止錄製");
	    	}
	    	else{
	    		ButtonDraw_recordcheck = false;
	    		mCallback.setRecordCheck(ButtonDraw_recordcheck);
	    		Button record = (Button) getView().findViewById(R.id.recordbutton);
	    		record.setText("開始錄製");
	    	}*/
    	}
    };
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
