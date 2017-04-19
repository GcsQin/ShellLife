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
import com.ustb.shellbox.shelllife.inneractivity.EportalActivity;
import com.ustb.shellbox.shelllife.store.PersistentCookieStore;

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

public class LinkEportalAct extends BaseSetupWizardActivity {
    EditText et_user;
    EditText et_pass;
    public static OkHttpClient okHttpClient_eportal;
    SharedPreferences sharedPreferences;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.HANDELER_ACCESS_EPORTAL_FAIL:
                    Toast.makeText(LinkEportalAct.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case Constants.HANDELER_ACCESS_EPORTAL_SUCCESS:
                    Toast.makeText(LinkEportalAct.this,"登录信息门户网成功",Toast.LENGTH_SHORT).show();
                    enterEportalActivity();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_eportal);
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
        startActivity(new Intent(LinkEportalAct.this,MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void initData(){
        et_user=(EditText)findViewById(R.id.et_alert_linkeportal_user);
        et_pass=(EditText)findViewById(R.id.et_alert_linkeportal_pass);
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.cookieJar(new EportalCookierManager()).readTimeout(8, TimeUnit.SECONDS).writeTimeout(8,TimeUnit.SECONDS).connectTimeout(8,TimeUnit.SECONDS)
                .build();
        okHttpClient_eportal=builder.build();
        sharedPreferences=getSharedPreferences("AccountInfo",MODE_PRIVATE);
        String user=sharedPreferences.getString("eustbUser","");
        String pass=sharedPreferences.getString("eustbPass","");
        if(!TextUtils.isEmpty(user)&&!TextUtils.isEmpty(pass)){
            et_user.setText(user);
            et_pass.setText(pass);
        }
    }
    public void loginEportal(View view){
        String user=et_user.getText().toString();
        String pass=et_pass.getText().toString();
        if(user.isEmpty()|pass.isEmpty()){
            Toast.makeText(LinkEportalAct.this,"账号或密码不能为空",Toast.LENGTH_LONG).show();
        }else {
            sharedPreferences.edit().putString("eustbUser",user).commit();
            sharedPreferences.edit().putString("eustbPass", pass).commit();
            loginEportalNow(user, pass);
        }
    }
    private void loginEportalNow(String user,String pass){
        FormBody.Builder builder= new FormBody.Builder();
        builder.add("Login.Token1",user).add("Login.Token2",pass).add("goto","http://e.ustb.edu.cn/loginSuccess.portal")
                .add("gotoOnFail", "http://e.ustb.edu.cn/loginFailure.portal");
        RequestBody requestBody=builder.build();
        Request request=new Request.Builder().url(Constants.URL_LOGIN_EPORTAL).post(requestBody).build();
        okHttpClient_eportal.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message=handler.obtainMessage();
                message.what=Constants.HANDELER_ACCESS_EPORTAL_FAIL;
                message.obj="访问信息门户失败"+e.toString();
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                Log.e("responseData666",responseData);
                if(responseData.contains("handleLoginSuccessed()")){
                    handler.sendEmptyMessage(Constants.HANDELER_ACCESS_EPORTAL_SUCCESS);
                }else {
                    Message message=handler.obtainMessage();
                    message.obj="登录失败,请检查账号密码是否正确!";
                    message.what=Constants.HANDELER_ACCESS_EPORTAL_FAIL;
                    handler.sendMessage(message);
                }
            }
        });
    }
    private void enterEportalActivity(){
        startActivity(new Intent(LinkEportalAct.this, EportalActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(LinkEportalAct.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    class EportalCookierManager implements CookieJar {
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
