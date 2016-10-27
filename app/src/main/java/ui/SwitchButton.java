package ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/10/27.
 */

public class SwitchButton extends View{
    private boolean SwitchState=false;//开关状态
    private float currentX;//记录点击开始位置的x坐标
    private boolean isTouchMode=false;

    /**
     * 用于在xml里使用, 可指定自定义属性
     * @param context
     * @param attrs
     */
    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        //获取配置自定义属性
        String namespace="http://schemas.android.com/apk/res/com.zero.mbutton";
        int switchBackgroundResource=attrs.getAttributeResourceValue(namespace,"switch_background",-1);
        int switchButtonResource=attrs.getAttributeResourceValue(namespace,"switch_button",-1);

        SwitchState=attrs.getAttributeBooleanValue(namespace,"switch_state",false);

        setSwitchButtonBackgroundResource(switchBackgroundResource);
        setSwitchButtonResource(switchButtonResource);

    }

    /**
     * 用于代码创建控件
     * @param context
     */
    public SwitchButton(Context context) {
        super(context);
        init();
    }
    /**
     * 用于在xml里使用, 可指定自定义属性, 如果指定了样式, 则走此构造函数
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint paint;
    private void init() {
        paint=new Paint();
    }

    /**
     * 1、首先测量大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(SwitchButtonBackgroundBitmap.getWidth(),SwitchButtonBackgroundBitmap.getHeight());//保存控件的宽高。
    }

    /**
     * 2、绘制view，在canvas上绘制的所有东西都会显示在界面上
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //1、绘制背景，此处的0,0是根据onMeasure()中设置的开始计算。
        canvas.drawBitmap(SwitchButtonBackgroundBitmap,0,0,paint);

        //2、绘制滑块
        if (isTouchMode){
            // 让滑块向左移动自身一半大小的位置
            float newLeft = currentX - SwitchButtonBitmap.getWidth() / 2.0f;

            int maxLeft = SwitchButtonBackgroundBitmap.getWidth() - SwitchButtonBitmap.getWidth();

            // 限定滑块范围
            if(newLeft < 0){
                newLeft = 0; // 左边范围
            }else if (newLeft > maxLeft) {
                newLeft = maxLeft; // 右边范围
            }

            canvas.drawBitmap(SwitchButtonBitmap, newLeft, 0, paint);
        }else{

            if (SwitchState){
                Log.i("switch",SwitchState+"");
                float newLef=SwitchButtonBackgroundBitmap.getWidth()-SwitchButtonBitmap.getWidth();
                canvas.drawBitmap(SwitchButtonBitmap,newLef,0,paint);

            }else {
                Log.i("switch",SwitchState+"");
                canvas.drawBitmap(SwitchButtonBitmap,0,0,paint);
            }
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i("switch","down:"+event.getX());
                isTouchMode=true;
                currentX=event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("switch","move:"+event.getX());
                isTouchMode=true;
                currentX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                Log.i("switch","up:"+event.getX());
                isTouchMode=false;
                currentX=event.getX();
                float center=SwitchButtonBackgroundBitmap.getWidth()/2.0f;
                boolean state=currentX>center;//为了使滑块不处于中间状态

                // 如果开关状态变化了, 通知界面. 里边开关状态更新了.
                if (state!=SwitchState&&onSwitchStateUpdateListener!=null){
                    // 把最新的boolean, 状态传出去了
                    onSwitchStateUpdateListener.onStateUpdate(state);
                }

                SwitchState=state;
                break;
        }
        invalidate();//重新调用onDraw()方法。

        return true;//True if the event was handled, false otherwise.
    }

    private Bitmap SwitchButtonBitmap;
    private Bitmap SwitchButtonBackgroundBitmap;


    /**
     * 设置背景资源
     * @param switch_background
     */
    public void setSwitchButtonBackgroundResource(int switch_background) {
        SwitchButtonBackgroundBitmap= BitmapFactory.decodeResource(getResources(),switch_background);
    }

    /**
     * 设置开关按钮资源
     * @param slide_button
     */
    public void setSwitchButtonResource(int slide_button) {
        SwitchButtonBitmap= BitmapFactory.decodeResource(getResources(),slide_button);
    }

    /**
     * 设置开关状态
     * @param state
     */
    public void setState(boolean state) {
        SwitchState=state;
    }
    private OnSwitchStateUpdateListener onSwitchStateUpdateListener;
    public void setOnSwitchStateUpdateListener(OnSwitchStateUpdateListener onSwitchStateUpdateListener) {
        this.onSwitchStateUpdateListener=onSwitchStateUpdateListener;
    }


    public interface OnSwitchStateUpdateListener{
        // 状态回调, 把当前状态传出去
        void onStateUpdate(boolean state);
    }
}
