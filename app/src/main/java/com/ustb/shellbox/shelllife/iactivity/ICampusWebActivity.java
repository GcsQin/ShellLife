package com.ustb.shellbox.shelllife.iactivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.MainActivity;

public class ICampusWebActivity extends AppCompatActivity {
    private WebView webView;
    private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icampus_web);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        relativeLayout=(RelativeLayout)findViewById(R.id.rl_icampus);
        initWebView();
    }
    private void initWebView(){
//        webView=(WebView)findViewById(R.id.web_link_campus_wifi);
        webView=new WebView(ICampusWebActivity.this);
        relativeLayout.addView(webView,0);
        WebSettings webSettings=webView.getSettings();//获取webView管理器
        webSettings.setJavaScriptEnabled(true);//使WebView支持javaScript语言
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//缓存设置
        /*1.LOAD_NO_CACHE 不要使用缓存，从网络加载。2.LOAD_DEFAULT 缓存使用默认模式。3.LOAD_CACHE_ONLY 不要使用网络，从缓存加载。
          4.在缓存资源可用时使用缓存资源，即使它们已过期。
        * */
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.loadUrl("http://202.204.48.82/1.htm");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(webView.canGoBack()){
                webView.goBack();
                return true;
            }else {
                startActivity(new Intent(ICampusWebActivity.this,MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
//        System.exit(0);
    }
}
