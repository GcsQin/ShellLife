package com.ustb.shellbox.shelllife.customview;

import android.app.Activity;
import android.view.View;

import com.ustb.shellbox.shelllife.R;

/**
 * Created by 37266 on 2017/3/21.
 */

public class TabBaseView {
    public Activity activity;
    public View rootView;
    public TabBaseView(Activity activity) {
        this.activity = activity;
        initView();
    }
    public void initView(){
        rootView=View.inflate(activity, R.layout.base_tabs_layout,null);
    }
    public void initData(){

    }
}
