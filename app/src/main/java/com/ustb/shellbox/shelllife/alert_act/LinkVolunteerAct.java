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
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.BaseSetupWizardActivity;
import com.ustb.shellbox.shelllife.activity.MainActivity;
import com.ustb.shellbox.shelllife.databean.Constants;
import com.ustb.shellbox.shelllife.inneractivity.ZhiyuanInfoActivity;
import com.ustb.shellbox.shelllife.store.PersistentCookieStore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
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

public class LinkVolunteerAct extends BaseSetupWizardActivity {
    public static OkHttpClient okHttpClient_vol;
    SharedPreferences sharedPreferences;
    EditText editText_user;
    EditText editText_pass;
    ArrayList<String> arrayList;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.HANDELER_ACCESS_ZHIYUAN_FAIL:
                    Toast.makeText(LinkVolunteerAct.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                break;
                case Constants.HANDELER_ACCESS_ZHIYUAN_FAIL_ACCOUNT_WRONG:
                    Toast.makeText(LinkVolunteerAct.this,"用户名或密码错误!",Toast.LENGTH_LONG).show();
                    break;
                case Constants.HANDELER_ENTER_ZHIYUAN_CENTER:
                    enterZhiyuanCenter();
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_volunteer);
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
        startActivity(new Intent(LinkVolunteerAct.this,MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void initData(){
        arrayList=new ArrayList<>();
        editText_user=(EditText)findViewById(R.id.et_alert_linkvolunteer_user);
        editText_pass=(EditText)findViewById(R.id.et_alert_linkvolunteer_pass);
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.cookieJar(new VolunteerCookierManager()).connectTimeout(10, TimeUnit.SECONDS).readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS);
        okHttpClient_vol=builder.build();
        sharedPreferences=getSharedPreferences("AccountInfo", MODE_PRIVATE);
        String user=sharedPreferences.getString("zyzUser","");
        String pass=sharedPreferences.getString("zyzPass","");
        Log.e("user",user+6);
        Log.e("pass",pass+6);
        if(!TextUtils.isEmpty(user)&&!TextUtils.isEmpty(pass)){
            editText_user.setText(user);
            editText_pass.setText(pass);
        }

    }
    public void loginVolunteer(View view){
        String user=editText_user.getText().toString();
        String pass=editText_pass.getText().toString();
        if(user.isEmpty()|pass.isEmpty()){
            Toast.makeText(LinkVolunteerAct.this,"账号或密码不能为空！",Toast.LENGTH_LONG).show();
        }else {
            sharedPreferences.edit().putString("zyzUser",user).commit();
            sharedPreferences.edit().putString("zyzPass",pass).commit();
            loginVolunteerNow(user, pass);
        }
    }
    private void loginVolunteerNow(String user,String pass){
        FormBody.Builder builder=new FormBody.Builder();
        builder.add("username",user).add("lastUrl","").add("password",pass);
        RequestBody requestBody=builder.build();
        Request request=new Request.Builder().post(requestBody).url(Constants.URL_LOGIN_ZHIYUAN).build();
        okHttpClient_vol.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=handler.obtainMessage();
                message.what=Constants.HANDELER_ACCESS_ZHIYUAN_FAIL;
                message.obj="访问志愿服务网失败"+e.toString();
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    String responseData=response.body().toString();
                    if(responseData.contains("用户名或密码错误")){
                        handler.sendEmptyMessage(Constants.HANDELER_ACCESS_ZHIYUAN_FAIL_ACCOUNT_WRONG);
                    }else {
                        getAccountInfo();
                    }
            }
        });
    }
    private void getAccountInfo(){
        Request request=new Request.Builder().url(Constants.URL_ZHIYUAN_INFO).build();
        okHttpClient_vol.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=handler.obtainMessage();
                message.what=Constants.HANDELER_ACCESS_ZHIYUAN_FAIL;
                message.obj="获取用户信息失败"+e.toString();
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                if(responseData.contains("欢迎您进入北京科技大学志愿服务课程网！")){
                    Document document= Jsoup.parse(responseData);
                    Elements spans=document.getElementsByTag("span");
                    for (int i=0;i<spans.size()-1;i++){
                        arrayList.add(spans.get(i).ownText());
                    }
                    arrayList.add(spans.get(4).text());
                    Message message=handler.obtainMessage();
                    message.what=Constants.HANDELER_ENTER_ZHIYUAN_CENTER;
                    handler.sendMessage(message);
                }
            }
        });
    }
    private void enterZhiyuanCenter(){
        Intent intent=new Intent(LinkVolunteerAct.this, ZhiyuanInfoActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("volInfo",arrayList);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(LinkVolunteerAct.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    class VolunteerCookierManager implements CookieJar {
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
