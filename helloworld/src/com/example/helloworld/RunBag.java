package com.example.helloworld;

import java.io.Serializable;

public class RunBag implements Serializable{
	private int start_time;
	private int duration;
	private String handler;
	private int road_start;
	private int road_end;
	private int road_size;
	private int rate;
	
	public RunBag(int in_time , int in_duration,String in_handler , int in_start , int in_end){
		start_time = in_time;
		duration = in_duration;
		handler = in_handler;
		road_start = in_start;
		road_end = in_end;
		rate = duration/(road_size);
	}
	
	public RunBag(){
		
	}
	
	public int getStartTime(){
		return start_time;
	}
	
	public void setStartTime(int in_time){
		start_time = in_time;
	}
	
	public String getHandler(){
		return handler;
	}
	
	public void setHandler(String in_handler){
		handler = in_handler;
	}
	
	public int getRoadStart(){
		return road_start;
	}
	
	public void setRoadStart(int in_start){
		road_start = in_start;
	}
	
	public int getRoadEnd(){
		return road_end;
	}
	
	public void setRoadEnd(int in_end){
		road_end = in_end;
	}
	
	public int getDuration(){
		return duration;
	}
	
	public void setDuration(int in_duration){
		duration = in_duration;
		rate = ((duration*1000)/(road_end - road_start));
	}
	
	public int getRate(){
		return rate;
	}
}
