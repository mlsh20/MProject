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

/*���s����P*/
/*�s���s�̪��a��*/
/*��ܮɶ��b�B���îɶ��b�B�}�l���s�B�M������*/

public class ButtonDraw extends Fragment {
	
	private boolean ButtonDraw_recordcheck = false;//�ˬd�ثe���S���b���s�����A�A�w�]��"�S��"
	private ToggleButton record;//Toggle�����s���s
	
	private boolean first_open_timeline = true;
	private TimeLine timeline = null;
	
	public interface CallbackInterface{//�s��MainActivity�A�i�DMainActivity�{�b���S���b���s���A
		public void setRecordCheck(boolean in_recordcheck);
		public void setClean();
	}
	
	private CallbackInterface mCallback;//�O�@�ӥΨ�call setRecordCheck���C���A�@�άO�Ψӳ]�win_recordcheck
	
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
	
	

    private OnClickListener btn2Listener = new OnClickListener(){//"��ܮɶ��b"
    	@Override
    	public void onClick(View v) {
	    	
    		/*if(first_open_timeline==true){
    			getActivity().findViewById(R.id.time_line).setVisibility(View.VISIBLE);
	    		FragmentManager fragmentManager =getFragmentManager();
		    	FragmentTransaction transaction = fragmentManager.beginTransaction();
		    	timeline = new TimeLine();
		    	transaction.add(R.id.time_line,timeline,"Time line");
		    	transaction.commit();
		    	fragmentManager.executePendingTransactions();
		    	first_open_timeline=false;
	    	
    		}
    		else{*/
    			getActivity().findViewById(R.id.time_line).setVisibility(View.VISIBLE);
    			FragmentManager fragmentManager =getFragmentManager();
		    	FragmentTransaction transaction = fragmentManager.beginTransaction();
		    	timeline = (TimeLine) fragmentManager.findFragmentById(R.id.time_line);
		    	transaction.show(timeline);
		    	transaction.commit();
		    	fragmentManager.executePendingTransactions();
    		//}
    		
    	}
    };
    
    private OnClickListener btn3Listener = new OnClickListener(){//"���îɶ��b"
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
    
    private OnClickListener recordListener = new OnClickListener(){//�}�l/������s
    	@Override
    	public void onClick(View v) {
    		
    		if(record.isChecked()){
    			ButtonDraw_recordcheck = true;
	    		mCallback.setRecordCheck(ButtonDraw_recordcheck);//�z�LmCallback�ӱq�o�̳]�wMainActivity�̭�recordcheck����
	    		//record.setTextOn("������s");
	    		
    		}
    		else{
    			ButtonDraw_recordcheck = false;
	    		mCallback.setRecordCheck(ButtonDraw_recordcheck);//�z�LmCallback�ӱq�o�̳]�wMainActivity�̭�recordcheck����
	    		//record.setTextOff("�}�l���s");
    		}
    	}
    };
    
    private OnClickListener clearListener = new OnClickListener(){//"�M������"
    	@Override
    	public void onClick(View v) {
	    	mCallback.setClean();
    	}
    };
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
