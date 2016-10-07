package com.example.helloworld;

import java.math.BigDecimal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class mySeekBar extends View {
    private static final String TAG = "mySeekBar";
    private static final int Max_Value = 8;		//seekBar的最大值
    private static final int CLICK_ON_LOW = 1;      //點擊在前滑塊上
    private static final int CLICK_ON_HIGH = 2;     //點擊在後滑塊上
    private static final int CLICK_IN_LOW_AREA = 3;
    private static final int CLICK_IN_HIGH_AREA = 4;
    private static final int CLICK_OUT_AREA = 5;
    private static final int CLICK_INVAILD = 0;
    /*
     * private static final int[] PRESSED_STATE_SET = {
     * android.R.attr.state_focused, android.R.attr.state_pressed,
     * android.R.attr.state_selected, android.R.attr.state_window_focused, };
     */
    private static final int[] STATE_NORMAL = {};
    private static final int[] STATE_PRESSED = {
            android.R.attr.state_pressed, android.R.attr.state_window_focused,
    };
    private Drawable hasScrollBarBg;        //滑動條滑動後背景圖
    private Drawable notScrollBarBg;        //滑動條未滑動背景圖
    private Drawable mThumbLow;         //前滑塊
    private Drawable mThumbHigh;        //後滑塊

    private int mScollBarWidth;     //控件寬度=滑動條寬度+滑塊寬度
    private int mScollBarHeight;    //滑動條高度

    private int mThumbWidth;        //滑塊寬度
    private int mThumbHeight;       //滑塊高度

    private double mOffsetLow = 0;     //前滑塊中心座標
    private double mOffsetHigh = 0;    //後滑塊中心座標
    private int mDistance = 0;      //總刻度是固定距離兩邊各去掉半個滑塊距離

    private int mThumbMarginTop = 30;   //滑塊頂部距離上邊框距離，也就是距離字體頂部距離

    private int mFlag = CLICK_INVAILD;
    private OnSeekBarChangeListener mBarChangeListener;


    private double defaultScreenLow = 0;    //默認前滑塊位置百分比
    private double defaultScreenHigh = 1;  //默認後滑塊位置百分比

    private boolean isEdit = false;     //輸入框是否正在輸入

    public mySeekBar(Context context) {
        this(context, null);
    }

    public mySeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public mySeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        this.setBackgroundColor(Color.BLACK);

        Resources resources = getResources();
        notScrollBarBg = resources.getDrawable(R.drawable.no);
        hasScrollBarBg = resources.getDrawable(R.drawable.yes);
        mThumbLow = resources.getDrawable(R.drawable.myseekbar);
        mThumbHigh = resources.getDrawable(R.drawable.myseekbar);

        mThumbLow.setState(STATE_NORMAL);
        mThumbHigh.setState(STATE_NORMAL);

        mScollBarWidth = hasScrollBarBg.getIntrinsicWidth();
        mScollBarHeight = hasScrollBarBg.getIntrinsicHeight();

        mThumbWidth = 50;/*mThumbLow.getIntrinsicWidth();*///滑塊的大小
        mThumbHeight = 50;//mThumbLow.getIntrinsicHeight();

    }

    //默認執行，計算view的寬高，在onDraw()之前
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
//        int height = measureHeight(heightMeasureSpec);
        mScollBarWidth = width;
        mOffsetHigh = width - mThumbWidth / 2;
        mOffsetLow = mThumbWidth / 2;
        mDistance = width - mThumbWidth;

        mOffsetLow = formatDouble(defaultScreenLow / Max_Value * (mDistance ))+ mThumbWidth / 2;
        mOffsetHigh = formatDouble(defaultScreenHigh / Max_Value * (mDistance)) + mThumbWidth / 2;
        setMeasuredDimension(width, mThumbHeight + mThumbMarginTop+2);
    }


    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST) {
        }
        //fill_parent或者精確值
        else if (specMode == MeasureSpec.EXACTLY) {
        }

        return specSize;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int defaultHeight = Max_Value;
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST) {
        }
        //fill_parent或者精確值
        else if (specMode == MeasureSpec.EXACTLY) {
            defaultHeight = specSize;
        }

        return defaultHeight;
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint text_Paint = new Paint();
        text_Paint.setTextAlign(Paint.Align.CENTER);
        text_Paint.setColor(Color.RED);
        text_Paint.setTextSize(20);

        int aaa = mThumbMarginTop + mThumbHeight / 2 - mScollBarHeight / 2;
        int bbb = aaa + mScollBarHeight;

        //白色，不會動
        notScrollBarBg.setBounds(mThumbWidth / 2, aaa, mScollBarWidth - mThumbWidth / 2, bbb);
        notScrollBarBg.draw(canvas);

        //藍色，中間部分會動
        hasScrollBarBg.setBounds((int)mOffsetLow, aaa, (int)mOffsetHigh, bbb);
        hasScrollBarBg.draw(canvas);

        //前滑塊
        mThumbLow.setBounds((int)(mOffsetLow - mThumbWidth / 2), mThumbMarginTop, (int)(mOffsetLow + mThumbWidth / 2), mThumbHeight + mThumbMarginTop);
        mThumbLow.draw(canvas);

        //後滑塊
        mThumbHigh.setBounds((int)(mOffsetHigh - mThumbWidth / 2), mThumbMarginTop, (int)(mOffsetHigh + mThumbWidth / 2), mThumbHeight + mThumbMarginTop);
        mThumbHigh.draw(canvas);

        double progressLow = formatDouble((mOffsetLow - mThumbWidth / 2) * Max_Value / mDistance);
        double progressHigh = formatDouble((mOffsetHigh - mThumbWidth / 2) * Max_Value / mDistance);
//            Log.d(TAG, "onDraw-->mOffsetLow: " + mOffsetLow + "  mOffsetHigh: " + mOffsetHigh   + "  progressLow: " + progressLow + "  progressHigh: " + progressHigh);
        canvas.drawText((int) progressLow + "", (int)mOffsetLow - 2 - 2, 15, text_Paint);
        canvas.drawText((int) progressHigh + "", (int)mOffsetHigh - 2, 15, text_Paint);

        if (mBarChangeListener != null) {
            if (!isEdit) {
                mBarChangeListener.onProgressChanged(this, progressLow, progressHigh);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //按下
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (mBarChangeListener != null) {
                mBarChangeListener.onProgressBefore();
                isEdit = false;
            }
            mFlag = getAreaFlag(e);
//            Log.d(TAG, "e.getX: " + e.getX() + "mFlag: " + mFlag);
//            Log.d("ACTION_DOWN", "------------------");
            if (mFlag == CLICK_ON_LOW) {
                mThumbLow.setState(STATE_PRESSED);
            } else if (mFlag == CLICK_ON_HIGH) {
                mThumbHigh.setState(STATE_PRESSED);
            } else if (mFlag == CLICK_IN_LOW_AREA) {
                mThumbLow.setState(STATE_PRESSED);
                //如果點擊0-mThumbWidth/2座標
                if (e.getX() < 0 || e.getX() <= mThumbWidth/2) {
                    mOffsetLow = mThumbWidth/2;
                } else if (e.getX() > mScollBarWidth - mThumbWidth/2) {
//                    mOffsetLow = mDistance - mDuration;
                    mOffsetLow = mThumbWidth/2 + mDistance;
                } else {
                    mOffsetLow = formatDouble(e.getX());
//                    if (mOffsetHigh<= mOffsetLow) {
//                        mOffsetHigh = (mOffsetLow + mDuration <= mDistance) ? (mOffsetLow + mDuration)
//                                : mDistance;
//                        mOffsetLow = mOffsetHigh - mDuration;
//                    }
                }
            } else if (mFlag == CLICK_IN_HIGH_AREA) {
                mThumbHigh.setState(STATE_PRESSED);
//                if (e.getX() < mDuration) {
//                    mOffsetHigh = mDuration;
//                    mOffsetLow = mOffsetHigh - mDuration;
//                } else if (e.getX() >= mScollBarWidth - mThumbWidth/2) {
//                    mOffsetHigh = mDistance + mThumbWidth/2;
                if(e.getX() >= mScollBarWidth - mThumbWidth/2) {
                    mOffsetHigh = mDistance + mThumbWidth/2;
                } else {
                    mOffsetHigh = formatDouble(e.getX());
//                    if (mOffsetHigh <= mOffsetLow) {
//                        mOffsetLow = (mOffsetHigh - mDuration >= 0) ? (mOffsetHigh - mDuration) : 0;
//                        mOffsetHigh = mOffsetLow + mDuration;
//                    }
                }
            }
            //設置進度條
            refresh();

            //移動move
        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
//            Log.d("ACTION_MOVE", "------------------");
            if (mFlag == CLICK_ON_LOW) {
                if (e.getX() < 0 || e.getX() <= mThumbWidth/2) {
                    mOffsetLow = mThumbWidth/2;
                } else if (e.getX() >= mScollBarWidth - mThumbWidth/2) {
                    mOffsetLow = mThumbWidth/2 + mDistance;
                    mOffsetHigh = mOffsetLow;
                } else {
                    mOffsetLow = formatDouble(e.getX());
                    if (mOffsetHigh - mOffsetLow <= 0) {
                        mOffsetHigh = (mOffsetLow <= mDistance+mThumbWidth/2) ? (mOffsetLow) : (mDistance+mThumbWidth/2);
                    }
                }
            } else if (mFlag == CLICK_ON_HIGH) {
                if (e.getX() <  mThumbWidth/2) {
                    mOffsetHigh = mThumbWidth/2;
                    mOffsetLow = mThumbWidth/2;
                } else if (e.getX() > mScollBarWidth - mThumbWidth/2) {
                    mOffsetHigh = mThumbWidth/2 + mDistance;
                } else {
                    mOffsetHigh = formatDouble(e.getX());
                    if (mOffsetHigh - mOffsetLow <= 0) {
                        mOffsetLow = (mOffsetHigh >= mThumbWidth/2) ? (mOffsetHigh) : mThumbWidth/2;
                    }
                }
            }
            //設置進度條
            refresh();
            //抬起
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
//            Log.d("ACTION_UP", "------------------");
            mThumbLow.setState(STATE_NORMAL);
            mThumbHigh.setState(STATE_NORMAL);

            if (mBarChangeListener != null) {
                mBarChangeListener.onProgressAfter();
            }
            //這兩個for loop是用來自動對齊刻度的，註解掉就可以滑動到任意位置
//            for (int i = 0; i < money.length; i++) {
//            	 if(Math.abs(mOffsetLow-i* ((mScollBarWidth-mThumbWidth)/ (money.length-1)))<=(mScollBarWidth-mThumbWidth)/(money.length-1)/2){
//            		 mprogressLow=i;
//                     mOffsetLow =i* ((mScollBarWidth-mThumbWidth)/(money.length-1));
//                     invalidate();
//                     break;
//                }
//			}
//
//            for (int i = 0; i < money.length; i++) {
//            	  if(Math.abs(mOffsetHigh-i* ((mScollBarWidth-mThumbWidth)/(money.length-1) ))<(mScollBarWidth-mThumbWidth)/(money.length-1)/2){
//            		  mprogressHigh=i;
//                	   mOffsetHigh =i* ((mScollBarWidth-mThumbWidth)/(money.length-1));
//                       invalidate();
//                       break;
//                }
//			}
        }
        return true;
    }

    public int getAreaFlag(MotionEvent e) {

        int top = mThumbMarginTop;
        int bottom = mThumbHeight + mThumbMarginTop;
        if (e.getY() >= top && e.getY() <= bottom && e.getX() >= (mOffsetLow - mThumbWidth / 2) && e.getX() <= mOffsetLow + mThumbWidth / 2) {
            return CLICK_ON_LOW;
        } else if (e.getY() >= top && e.getY() <= bottom && e.getX() >= (mOffsetHigh - mThumbWidth / 2) && e.getX() <= (mOffsetHigh + mThumbWidth / 2)) {
            return CLICK_ON_HIGH;
        } else if (e.getY() >= top
                && e.getY() <= bottom
                && ((e.getX() >= 0 && e.getX() < (mOffsetLow - mThumbWidth / 2)) || ((e.getX() > (mOffsetLow + mThumbWidth / 2))
                && e.getX() <= ((double) mOffsetHigh + mOffsetLow) / 2))) {
            return CLICK_IN_LOW_AREA;
        } else if (e.getY() >= top
                && e.getY() <= bottom
                && (((e.getX() > ((double) mOffsetHigh + mOffsetLow) / 2) && e.getX() < (mOffsetHigh - mThumbWidth / 2)) || (e
                .getX() > (mOffsetHigh + mThumbWidth/2) && e.getX() <= mScollBarWidth))) {
            return CLICK_IN_HIGH_AREA;
        } else if (!(e.getX() >= 0 && e.getX() <= mScollBarWidth && e.getY() >= top && e.getY() <= bottom)) {
            return CLICK_OUT_AREA;
        } else {
            return CLICK_INVAILD;
        }
    }

    private void refresh() {
        invalidate();
    }

    public void setProgressLow(double  progressLow) {
        this.defaultScreenLow = progressLow;
        mOffsetLow = formatDouble(progressLow / Max_Value * (mDistance ))+ mThumbWidth / 2;
        isEdit = true;
        refresh();
    }

    public void setProgressHigh(double  progressHigh) {
        this.defaultScreenHigh = progressHigh;
        mOffsetHigh = formatDouble(progressHigh / Max_Value * (mDistance)) + mThumbWidth / 2;
        isEdit = true;
        refresh();
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener mListener) {
        this.mBarChangeListener = mListener;
    }

    //回調函數，在滑動時實時調用，改變輸入框的值
    public interface OnSeekBarChangeListener {
        //滑動前
        public void onProgressBefore();

        //滑動時
        public void onProgressChanged(mySeekBar seekBar, double progressLow,
                                      double progressHigh);

        //滑動後
        public void onProgressAfter();
    }

/*    private int formatInt(double value) {
        BigDecimal bd = new BigDecimal(value);
        BigDecimal bd1 = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
        return bd1.intValue();
    }*/

    public static double formatDouble(double pDouble) {
        BigDecimal bd = new BigDecimal(pDouble);
        BigDecimal bd1 = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        pDouble = bd1.doubleValue();
        return pDouble;
    }

}