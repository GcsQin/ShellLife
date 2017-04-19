package com.ustb.shellbox.shelllife.iactivity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.MainActivity;
import com.ustb.shellbox.shelllife.databean.Constants;
import com.ustb.shellbox.shelllife.store.PersistentCookieStore;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

/*
* 起初这个activity是用来模拟网上登录的，但是发现能够用webView加载数据之后 这个activity就暂时废弃了。
* */
public class INetPlayActivity extends AppCompatActivity {
    ListView listView;
    String user;
    String pass;
    Bundle bundle;
    Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_play);
        Intent intent=getIntent();
        bundle=intent.getExtras();
        initData();
    }
    private void initData(){
        user=bundle.getString("user");
        pass=bundle.getString("pass");
    }
    private void getCampusWebBroad(){
        new Thread(new Runnable() {
            @Override
            public void run() {
            }
        }).start();
    }
    private void searchAccountWebInfo(String user, String pass){
        FormBody.Builder builder=new FormBody.Builder();
        builder.add("account",user).add("password",pass).build();
        RequestBody requestBody=builder.build();
        Request request=new Request.Builder().url(Constants.URL_WEB_SELF_SERVICE).post(requestBody).build();
//       okHttpClient_net.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("failure","登陆失败");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                gotoHtmlTest(response.body().string());
//            }
//        });
    }
    public void logout_onekey(View view){

    }
    public void logout_thisDevice(View view){

    }
    //**********************************************************************************************
    @Override
    public void onBackPressed() {
        startActivity(new Intent(INetPlayActivity.this, MainActivity.class));
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
        super.onBackPressed();
    }

    class CookieManager implements CookieJar{
        final PersistentCookieStore cookieStore = new PersistentCookieStore(getApplicationContext());
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookies != null && cookies.size() > 0) {
                for (Cookie item : cookies) {
                    cookieStore.add(url, item);
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url);
            return cookies;
        }
    }
}
