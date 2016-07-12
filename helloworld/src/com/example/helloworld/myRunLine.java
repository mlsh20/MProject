package com.example.helloworld;

import java.io.Serializable;
import java.util.Vector;

public class myRunLine implements Serializable{
	private Vector <RunBag> myRunLine;
	
	public myRunLine(){
		myRunLine = new Vector();
	}
	
	public void setRunLine(Vector<RunBag> input){
		
		int i=0;
		for (i=0;i<input.size();i++){
			myRunLine.add(input.get(i));
		}
		//myRunLine = input;
	}
	
	public Vector <RunBag> getRunLine(){
		return myRunLine;
	}
	
}
