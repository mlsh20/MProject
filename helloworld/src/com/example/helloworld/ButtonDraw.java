package com.example.helloworld;

import java.io.File;
import java.io.FileOutputStream;

import com.example.helloworld.TimeLine.CallbackInterface;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

/*按鈕的抽屜*/
/*存按鈕們的地方*/
/*顯示時間軸、隱藏時間軸、開始錄製、清除筆跡、清除路徑*/

public class ButtonDraw extends Fragment {
	
	private boolean ButtonDraw_recordcheck = false;//檢查目前有沒有在錄製的狀態，預設為"沒有"
	private ToggleButton record;//Toggle的錄製按鈕
	
	private boolean first_open_timeline = true;
	private TimeLine timeline = null;
	
	public interface CallbackInterface{//連接MainActivity，告訴MainActivity現在有沒有在錄製狀態
		public void setRecordCheck(boolean in_recordcheck);
		public void setClean();
		
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
        
        Button button_clear = (Button) getView().findViewById(R.id.button_clear);
        button_clear.setOnClickListener(clearListener);
        button_clear.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
        
        Button button_load = (Button) getView().findViewById(R.id.button_strategies);
        button_load.setOnClickListener(strategies);
        button_load.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
        
        Button button_DTW = (Button) getView().findViewById(R.id.button_DTW);
        button_DTW.setOnClickListener(DTW);
        button_DTW.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
        
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
    			getActivity().findViewById(R.id.time_line).setVisibility(View.VISIBLE);
    			FragmentManager fragmentManager =getFragmentManager();
		    	FragmentTransaction transaction = fragmentManager.beginTransaction();
		    	timeline = (TimeLine) fragmentManager.findFragmentById(R.id.time_line);
		    	transaction.show(timeline);
		    	transaction.commit();
		    	fragmentManager.executePendingTransactions();
    		
    	}
    };
    
    private OnClickListener btn3Listener = new OnClickListener(){//"隱藏時間軸"
    	@Override
    	public void onClick(View v) {
	    	FragmentManager fragmentManager =getFragmentManager();
	    	timeline = (TimeLine) fragmentManager.findFragmentById(R.id.time_line);
	    	if (null != timeline){
	    		getActivity().findViewById(R.id.time_line).setVisibility(View.GONE);
	    		FragmentTransaction fragTran = fragmentManager.beginTransaction();
	    		fragTran.hide(timeline);
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
    	}
    };
    
    private OnClickListener clearListener = new OnClickListener(){//"清除..."
    	@Override
    	public void onClick(View v) {
    		final String[] strategies = {"清除筆跡","清除路徑"};
    		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    		 
    		builder.setItems(strategies, new DialogInterface.OnClickListener(){
    	         @Override
    	         //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
    	         public void onClick(DialogInterface dialog, int which) {
    	                // TODO Auto-generated method stub
    	        	 	if(which==0){//清除筆跡
    	        	 		mCallback.setClean();
    	        	 	}
    	        	 	else if (which==1){//清除路徑
    	        	 		MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
    	            		mainfrag.clear_paint();
    	            		mainfrag.clear_record();
    	        	 	}
    	          }
    	    });
            AlertDialog about_dialog = builder.create();
            about_dialog.show();
    	}
    };
    
    
    
    private OnClickListener strategies = new OnClickListener(){//"戰術"
    	@Override
    	public void onClick(View v) {
    		final String[] strategies = {"儲存戰術","載入戰術"};
    		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    		 
    		builder.setItems(strategies, new DialogInterface.OnClickListener(){
    	         @Override
    	         //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
    	         public void onClick(DialogInterface dialog, int which) {
    	        	 	MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
    	                // TODO Auto-generated method stub
    	        	 	if(which==0){//儲存戰術
    	        	 		mainfrag.save_dialog();
    	        	 	}
    	        	 	else if (which==1){//載入戰術
    	        	 		mainfrag.load_dialog();
    	        	 	}
    	          }
    	    });
            AlertDialog about_dialog = builder.create();
            about_dialog.show();
    	}
    };
	
    private OnClickListener DTW = new OnClickListener(){//"DTW"
    	@Override
    	public void onClick(View v) {
    		MainFragment mainfrag =(MainFragment) getActivity().getFragmentManager().findFragmentById(R.id.Main);
    		mainfrag.Do_DTW();
    		
    		
    		
    		
    	}
    };
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
