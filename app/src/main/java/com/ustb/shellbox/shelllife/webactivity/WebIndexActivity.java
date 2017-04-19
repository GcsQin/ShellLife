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
import com.ustb.shellbox.shelllife.iactivity.IOfficialWebListActivity;

/*访问学校官网
* webView
* */
public class WebIndexActivity extends AppCompatActivity {
    class WebDownUtils implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri=Uri.parse(url);
            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        }
    }

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webu_index);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        webView=(WebView)findViewById(R.id.web_index);
        webView.getSettings().setJavaScriptEnabled(true);//如果访问的网页中含有JS，那么WebView必须要设置成支持JS
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//
        webView.getSettings().setAllowFileAccess(true);//允许访问文件
        webView.setDownloadListener(new WebDownUtils());
        //屏幕适配
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//适应内容大小
        webView.getSettings().setSupportZoom(true);//允许缩放
        webView.getSettings().setBuiltInZoomControls(true);//设置缩放按钮
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //根据传入的参数再去加载新的网页
                view.loadUrl(url);
                return true;//返回true表示让WebView可以处理打开新网页的请求，为false调用系统浏览器或第三方浏览器
            }
        });
        //webView加载web资源
        webView.loadUrl("http://www.ustb.edu.cn/index.asp");
        // webView缓存的使用
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//优先使用缓存
//        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);不使用缓存

//        判断页面加载过程
//        webView.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
//                if (newProgress==100){
//                    //网页加载完成
//                }else {
//                    //网页加载中
//                }
//            }
//        });
    }
    //改写物理键返回的逻辑。如果希望浏览器的网页后退而不是退出浏览器，需要WebView覆盖URL加载，
    // 让他自动生成历史访问记录，那样就可以通过前进或后退访问已经访问过的网站
    public void refresh(View view){
        webView.reload();
    }
    public void backHome(View view){
        startActivity(new Intent(WebIndexActivity.this, IOfficialWebListActivity.class));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }
    public void preWebView(View view){
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            startActivity(new Intent(WebIndexActivity.this, IOfficialWebListActivity.class));
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            finish();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if (webView.canGoBack()){
                webView.goBack();
                return true;
            }else {
                startActivity(new Intent(WebIndexActivity.this, IOfficialWebListActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
