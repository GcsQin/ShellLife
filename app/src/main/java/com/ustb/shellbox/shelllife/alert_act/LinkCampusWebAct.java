package com.ustb.shellbox.shelllife.alert_act;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.BaseSetupWizardActivity;
import com.ustb.shellbox.shelllife.iactivity.ICampusWebActivity;
import com.ustb.shellbox.shelllife.iactivity.INetPlayActivity;
import com.ustb.shellbox.shelllife.activity.MainActivity;
import com.ustb.shellbox.shelllife.databean.Constants;
import com.ustb.shellbox.shelllife.store.PersistentCookieStore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LinkCampusWebAct extends BaseSetupWizardActivity {
    EditText editText_user;
    EditText editText_pass;
    public static OkHttpClient okHttpClient_web;
    SharedPreferences sharedPreferences;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.HANDLER_ENTER_CAMPUS_WEB_FAIL:
                    Toast.makeText(LinkCampusWebAct.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case Constants.HANDLER_GET_INFO_CAMPUS_WEB_FAIL:
                    Toast.makeText(LinkCampusWebAct.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case Constants.HANDLER_ENTER_CAMPUSWEB_SUCCESSFUL:
                    Toast.makeText(LinkCampusWebAct.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case Constants.HANDLER_GET_INFO_CAMPUS_FAIL_BYERROR:
                    Toast.makeText(LinkCampusWebAct.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_campus_web);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initData();
    }

    @Override
    public void showNextPage() {

    }

    @Override
    public void showPriviousPage() {
        startActivity(new Intent(LinkCampusWebAct.this, MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void initData(){
        editText_user=(EditText)findViewById(R.id.et_alert_linkcampus_user);
        editText_pass=(EditText)findViewById(R.id.et_alert_linkcampus_pass);
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.cookieJar(new WebCookierManager());
        builder.readTimeout(10, TimeUnit.SECONDS).writeTimeout(10,TimeUnit.SECONDS).connectTimeout(10,TimeUnit.SECONDS);
        okHttpClient_web=builder.build();

        sharedPreferences=getSharedPreferences("AccountInfo",MODE_PRIVATE);
        String user=sharedPreferences.getString("netUser","");
        String pass=sharedPreferences.getString("netPass","");
        if(!TextUtils.isEmpty(user)&&!TextUtils.isEmpty(pass)){
            editText_user.setText(user);
            editText_pass.setText(pass);
        }
    }
    public void loginCampusWeb(View view){
        String user=editText_user.getText().toString().trim();
        String pass=editText_pass.getText().toString().trim();
        if(user.isEmpty()|pass.isEmpty()){
            Toast.makeText(LinkCampusWebAct.this,"账号或密码不能为空!",Toast.LENGTH_LONG).show();
        } else {
            sharedPreferences.edit().putString("netUser",user).commit();
            sharedPreferences.edit().putString("netPass",pass).commit();
            loginCampusWebNow(user, pass);
        }
    }
    private void loginCampusWebNow(final String user,final String pass){
//        okHttpClient_web.newBuilder().cookieJar(new WebCookierManager()).readTimeout(10,TimeUnit.SECONDS).connectTimeout(10,TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS).build();
        //链接校园网
        FormBody.Builder builder= new FormBody.Builder();
        builder.add("DDDDD",user).add("upass",pass).add("0MKKey","123456789");
        RequestBody requestBody=builder.build();
        Request request=new Request.Builder().post(requestBody).url(Constants.URL_CAMPUS_WEB).build();
        okHttpClient_web.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=handler.obtainMessage();
                message.what=Constants.HANDLER_ENTER_CAMPUS_WEB_FAIL;
                message.obj="登陆失败"+e.toString();
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                if(responseData.contains("失败")){
                    Message message=handler.obtainMessage();
                    message.what=Constants.HANDLER_GET_INFO_CAMPUS_WEB_FAIL;
                    message.obj="登录失败"+responseData;
                    handler.sendMessage(message);
                }else {
                    Document document= Jsoup.parse(responseData);
                    Element divEle=document.getElementsByAttributeValue("class","d").first();
                    String divContent=divEle.text();

                    if(divContent.contains("You have successfully logged into our system")){
                        Message message=handler.obtainMessage();
                        message.what=Constants.HANDLER_ENTER_CAMPUSWEB_SUCCESSFUL;
                        message.obj="校园网已成功链接!";
                        handler.sendMessage(message);
                        linkCanmpusWebSuc(user,pass);
                    }else {
                        Message message=handler.obtainMessage();
                        message.what=Constants.HANDLER_GET_INFO_CAMPUS_FAIL_BYERROR;
                        message.obj="账号或密码不正确,请重新输入";
                        handler.sendMessage(message);
                    }
                }
            }
        });
    }
    private void linkCanmpusWebSuc(String user,String pass){
//        Bundle bundle=new Bundle();
//        Intent intent = new Intent(LinkCampusWebAct.this, INetPlayActivity.class);
//        bundle.putString("user",user);
//        bundle.putString("pass",pass);
//        intent.putExtras(bundle);
//        startActivity(intent);
        startActivity(new Intent(LinkCampusWebAct.this, ICampusWebActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LinkCampusWebAct.this, MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
        super.onBackPressed();
    }

    class WebCookierManager implements CookieJar{
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
