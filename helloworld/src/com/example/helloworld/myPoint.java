package com.example.helloworld;

import android.graphics.Point;

public class myPoint {
	
	public double x;
    public double y;

    public myPoint() {}

    public myPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public myPoint(myPoint src) {
        this.x = src.x;
        this.y = src.y;
    }
}
