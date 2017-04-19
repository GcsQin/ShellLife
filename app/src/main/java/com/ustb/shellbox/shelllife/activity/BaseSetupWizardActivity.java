package com.ustb.shellbox.shelllife.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.ustb.shellbox.shelllife.R;
/*
* 引导页：Splash页面展现完毕后,判断用户是否进入过引导页,如果没有进入过引导页,则进入引导页,否则进入主界面。
* 性能优化方便：引导页大部分都带有手势滑动的功能,所以写一个基类Acitivity,实现手势滑动功能。
* */

public abstract class BaseSetupWizardActivity extends Activity {
    private GestureDetector gestureDetector;
    public SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_setup_wizard);
        sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);//用于记录是否进入过引导页的sp
        gestureDetector=new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e1.getRawX()-e2.getRawX()>150){
                    showNextPage();
                    return true;//阻止事件传播
                }else if(e2.getRawX()-e1.getRawX()>150){
                    showPriviousPage();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }
    public abstract void showNextPage();
    public abstract void showPriviousPage();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);//委托手势识别器处理滑动事件
        return super.onTouchEvent(event);
    }
}
