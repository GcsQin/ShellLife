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
import com.ustb.shellbox.shelllife.activity.MainActivity;
import com.ustb.shellbox.shelllife.iactivity.INewEduSysActivity;
import com.ustb.shellbox.shelllife.databean.Constants;
import com.ustb.shellbox.shelllife.store.PersistentCookieStore;

import org.json.JSONException;
import org.json.JSONObject;

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

public class LinkEducationSystemAct extends BaseSetupWizardActivity{
    SharedPreferences sharedPreferences;
    EditText editText_user;
    EditText editText_pass;
    public static OkHttpClient okHttpClient_edu;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.HANDLER_ENTER_EDUSYS_FAIL:
                    Toast.makeText(LinkEducationSystemAct.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case Constants.HANDLER_GET_INFO_EDUSYS_FAIL:
                    Toast.makeText(LinkEducationSystemAct.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case Constants.HANDLER_ENTER_EDUSYS_SUCCESSFUL:
                    Toast.makeText(LinkEducationSystemAct.this,"登陆成功",Toast.LENGTH_LONG).show();
                    loginEduSysSuc((String)msg.obj);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_education_system);
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
        startActivity(new Intent(this,MainActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void initData(){//初始化本地数据,如果本地已经保存了账号以及密码,则显示在edittext中
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        okHttpClient_edu=builder.cookieJar(new CookieManager()).connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build();
        sharedPreferences=getApplication().getSharedPreferences("AccountInfo",MODE_PRIVATE);
        editText_user=(EditText)findViewById(R.id.et_alert_linkedusys_user);
        editText_pass=(EditText)findViewById(R.id.et_alert_linkedusys_pass);
        String user=sharedPreferences.getString("eduUser", "");
        String pass=sharedPreferences.getString("eduPass", "");
        if(!user.isEmpty()){
            editText_user.setText(user);
        }
        if (!pass.isEmpty()){
            editText_pass.setText(pass);
        }
    }
    public void loginEduSys(View view){//button点击事件
        new Thread(new Runnable() {
            @Override
            public void run() {
                String user=editText_user.getText().toString();
                String pass=editText_pass.getText().toString();
                sharedPreferences.edit().putString("eduUser",user).commit();
                sharedPreferences.edit().putString("eduPass",pass).commit();
                if(TextUtils.isEmpty(user)|TextUtils.isEmpty(pass)){
                    Toast.makeText(LinkEducationSystemAct.this,"账号或密码不能为空!",Toast.LENGTH_SHORT).show();
                }else {
                    loginEduSysNow(user, pass);
                }
            }
            private void loginEduSysNow(String user,String pass){
                //构建POST请求头
                FormBody.Builder builder= new FormBody.Builder();
                builder.add("j_username",user+",undergraduate");
                builder.add("j_password", pass);
                RequestBody requestBody=builder.build();
                //发送POST请求
                Request request=new Request.Builder().url(Constants.URL_LOGIN_SERVER+Constants.URL_ValidateACCOUNT).post(requestBody).build();

                okHttpClient_edu.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Message message=handler.obtainMessage();
                        message.what=Constants.HANDLER_ENTER_NEW_EDU_SYS_FAILURE;
                        message.obj="访问失败!"+e.toString();
                        handler.sendMessage(message);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()){
                            String responseData=response.body().string();
                            Log.e("responsedata",responseData);
                            if(responseData.contains("失败")){
                                Message message=handler.obtainMessage();
                                message.what=Constants.HANDLER_GET_INFO_EDUSYS_FAIL;
                                message.obj=responseData;
                                handler.sendMessage(message);
                            }else {
                                try {
                                    JSONObject jsonObject=new JSONObject(responseData);
                                    if(jsonObject.getBoolean("success")){
                                        Message message=handler.obtainMessage();
                                        message.what=Constants.HANDLER_ENTER_EDUSYS_SUCCESSFUL;
                                        message.obj=jsonObject.getString("uid");
                                        handler.sendMessage(message);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        }).start();

    }
    private void loginEduSysSuc(String uid){//登陆成功后
        Intent intent=new Intent(LinkEducationSystemAct.this,INewEduSysActivity.class);
        Bundle bundle= new Bundle();
        bundle.putString("uid", uid);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
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
