package com.android.zycojamie.letterdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zckya on 2017/11/5.
 */

public class LetterSideView extends View {
    private Paint mPaint;
    private Paint mPaintLight;
    private int mColorNoTouch;
    private int mColorTouch;
    private int textSize;
    private int itemHeight;
    private String currentLetter;
    private String lastLetter=" ";
    public static String[] mLetters={"A","B","C","D","E","F","G","H","I","J","K","L",
            "M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","#"};
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };

    public LetterSideView(Context context) {
        this(context,null);
    }

    public LetterSideView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LetterSideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.LetterSideView);
        //获取自定义的颜色
        mColorNoTouch =typedArray.getColor(R.styleable.LetterSideView_no_touch_letter_color,Color.BLACK);
        mColorTouch =typedArray.getColor(R.styleable.LetterSideView_touch_letter_color,Color.RED);
        //获取自定义的文本大小
        textSize=typedArray.getDimensionPixelSize(R.styleable.LetterSideView_android_text,(int)sp2px(16));
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        mPaintLight=new Paint();
        mPaintLight.setAntiAlias(true);
        mPaintLight.setTextSize(textSize);
    }

    private float sp2px(int sp) {
       return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取高度
        int height=MeasureSpec.getSize(heightMeasureSpec);
        //计算宽度 左右padding+绘制的字母宽度
        int width=getPaddingLeft()+getPaddingRight()+(int)mPaint.measureText("M");
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        itemHeight=(getHeight()-getPaddingTop()-getPaddingBottom())/mLetters.length;
        for(int i=0;i<mLetters.length;i++){
            //每个字母的中心Y坐标
            int centerY=itemHeight*i+getPaddingTop()+itemHeight/2;
            //基线
            Paint.FontMetricsInt fontMetricsInt=mPaint.getFontMetricsInt();
            int dy=(fontMetricsInt.bottom-fontMetricsInt.top)/2-fontMetricsInt.bottom;
            int baseLine=centerY+dy;
            //将触摸的字母高亮
            if(mLetters[i].equals(currentLetter)){
                mPaintLight.setColor(mColorTouch);
            }else{
                mPaint.setColor(mColorNoTouch);
            }
            //绘制字母，其中getWidth()/2-mPaint.measureText(mLetters[i])/2 宽度1/2-字母1/2 保证了绘制的字母是居中的
            canvas.drawText(mLetters[i],getWidth()/2-mPaint.measureText(mLetters[i])/2,baseLine,mPaint);
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        super.onTouchEvent(event);
        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float currentY=event.getY();
                int currentTouchPosition= (int) ((currentY-getPaddingTop())/itemHeight);
                if(currentTouchPosition<0){
                    currentTouchPosition=0;
                }else if(currentTouchPosition>=mLetters.length){
                    currentTouchPosition=mLetters.length-1;
                }
                currentLetter=mLetters[currentTouchPosition];
                if(listener!=null){
                    listener.call(currentLetter);
                }
                //优化，触摸与上次相同的字母时不重绘
                if(!lastLetter.equals(currentLetter)){
                    invalidate();
                }
                lastLetter=currentLetter;
                break;
            case MotionEvent.ACTION_UP:
                if(onTouchRemoveListener!=null){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onTouchRemoveListener.call();
                        }
                    },500);
                }
                break;


        }
        return true;
    }

    //回调接口
    private OnTouchMoveListener listener;
    public void setOnTouchMoveListener(OnTouchMoveListener listener){
        this.listener=listener;
    }
    interface OnTouchMoveListener{
        void call(CharSequence charSequence);
    }

    private OnTouchRemoveListener onTouchRemoveListener;
    public void setOnTouchRemoveListener(OnTouchRemoveListener onTouchRemoveListener){
        this.onTouchRemoveListener=onTouchRemoveListener;
    }
    interface OnTouchRemoveListener{
        public void call();
    }
}
