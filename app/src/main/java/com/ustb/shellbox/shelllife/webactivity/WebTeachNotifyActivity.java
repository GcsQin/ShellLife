package com.ustb.shellbox.shelllife.webactivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.MainActivity;

public class WebTeachNotifyActivity extends AppCompatActivity {
    WebView webView;
    class WebDownUtils implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri=Uri.parse(url);
            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String href=bundle.getString("notifyUrl");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_teach_notify);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        webView=(WebView)findViewById(R.id.web_teach_notify);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setDownloadListener(new WebDownUtils());
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//
        webView.getSettings().setAllowFileAccess(true);//允许访问文件
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//优先使用缓存
//        webView.getSettings()
        //屏幕适配
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//适应内容大小
        webView.getSettings().setSupportZoom(true);//允许缩放
        webView.getSettings().setBuiltInZoomControls(true);//设置缩放按钮
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.loadUrl("http://teach.ustb.edu.cn/"+href);
    }
    public void refresh(View view){
        webView.reload();
    }
    public void backHome(View view){
        startActivity(new Intent(WebTeachNotifyActivity.this,MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }
    public void preWebView(View view){
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            startActivity(new Intent(WebTeachNotifyActivity.this,MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            webView.destroy();
            finish();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if (webView.canGoBack()){
                return true;
            }else {
                startActivity(new Intent(WebTeachNotifyActivity.this,MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
