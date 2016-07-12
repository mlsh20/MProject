package com.example.helloworld;



import java.util.Vector;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable{
	private int[] road;
	private int start_index;
    public Contact(int[] in_road,int in_index) {
    	road = in_road;
    	start_index = in_index;
    }
    
    public Contact(Parcel parcel) {
    	parcel.readIntArray(road);
    	start_index = parcel.readInt();
    }
    
    public static final Parcelable.Creator<Contact> CREATOR = new Creator<Contact>(){
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }
        @Override
        public Contact[] newArray(int size) {
            return null;
        }
    };
    
    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeIntArray(road);
        dest.writeInt(start_index);
    }
    
    public int[] getRoad(){
    	return road;
    }
    
    public int getStartIndex(){
    	return start_index;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
}
