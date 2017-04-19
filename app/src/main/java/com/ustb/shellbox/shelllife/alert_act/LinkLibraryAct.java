package com.ustb.shellbox.shelllife.alert_act;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.BaseSetupWizardActivity;
import com.ustb.shellbox.shelllife.activity.MainActivity;
import com.ustb.shellbox.shelllife.databean.Constants;
import com.ustb.shellbox.shelllife.iactivity.ILibraryActivity;
import com.ustb.shellbox.shelllife.inneractivity.LibraryInfoActivity;
import com.ustb.shellbox.shelllife.store.PersistentCookieStore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
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

public class LinkLibraryAct extends BaseSetupWizardActivity {
    EditText editText_user_lib;
    EditText editText_pass_lib;
    EditText editText_verification;
    SharedPreferences sharedPreferences;
    public static OkHttpClient okHttpClient_libInfo;
    ArrayList<String> infos;
    ArrayList<String> bookInfos;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.HANDLER_GET_VRIFICATION_CODE_LIB:
                    ImageView imageView=(ImageView)findViewById(R.id.img_verification_code);
                    imageView.setImageBitmap((Bitmap)msg.obj);
                    break;
                case Constants.HANDLER_LOGIN_SUCCESSFUL_LIB:
                    EnterLibraryInfoAct();
                    break;
                case Constants.HANDLER_LOGIN_FAILURE_LIB:
                    Toast.makeText(LinkLibraryAct.this,"登录失败!请检查账号密码后重新输入!",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_library);
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
        startActivity(new Intent(LinkLibraryAct.this,ILibraryActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void initData(){
        infos=new ArrayList<>();
        bookInfos=new ArrayList<>();
        sharedPreferences=getSharedPreferences("AccountInfo",MODE_PRIVATE);
        editText_user_lib=(EditText)findViewById(R.id.et_alert_linklib_user);
        editText_pass_lib=(EditText)findViewById(R.id.et_alert_linklib_pass);
        editText_verification=(EditText)findViewById(R.id.et_lib_verification);
        String user=sharedPreferences.getString("libUser", "");
        String pass=sharedPreferences.getString("libPass","");
        if(!user.isEmpty()){
            editText_user_lib.setText(user);
        }
        if (!pass.isEmpty()){
            editText_pass_lib.setText(pass);
        }
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        okHttpClient_libInfo=builder.cookieJar(new LibInfoCookierManager()).connectTimeout(10,TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS).build();
    }
    public void getVerificationCode(View view){
        String user=editText_user_lib.getText().toString();
        String pass=editText_pass_lib.getText().toString();
        final ImageView imageView=(ImageView)findViewById(R.id.img_verification_code);
        imageView.refreshDrawableState();
        /*
        * 接用glide加载验证码图片,然后将验证码上传到服务器 登录不了,但是我直接用okhttp返回的输入流设置成bitmap给imageView添加，
        * 就可以登录的上,GLIDE是不是有缓存机制在里面的，所以导致我从服务器接受的数据有可能是从缓存中取得的？
        * 我的想法是这样的 glide它如果有缓存可能会自动使用缓存的图片，所以它的图片并不是实时的,想要获取实时的图片,
        * 必须先将imgeView里缓存的图片清空才可以获得实时的验证码图片。然后我用okhttp得到的输入流,把流数据放到轮询器里,
        * 而那时候ImageView控件才刚刚创建,所以这个ImageView里的图片一定是实时的。
        * */
        if(TextUtils.isEmpty(user)|TextUtils.isEmpty(pass)){
            Toast.makeText(LinkLibraryAct.this,"账号密码不能为空!",Toast.LENGTH_SHORT).show();
        }else {
            //注释部分和非注释部分都可以使用,但是非注释部分的ImageView一定要在初始化的时候refreshDrawableState();
//            Glide.with(LinkLibraryAct.this).load(Constants.URL_ENTER_LIBRARY_CODE+user).into(imageView);
            Request request=new Request.Builder().url(Constants.URL_ENTER_LIBRARY_CODE+user).build();
            okHttpClient_libInfo.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("img", e.toString());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Message message = handler.obtainMessage();
                    message.what = Constants.HANDLER_GET_VRIFICATION_CODE_LIB;
                    message.obj = bitmap;
                    handler.sendMessage(message);
                }
            });
            editText_verification.setVisibility(View.VISIBLE);
        }
    }
    public void loginLib(View view){
        String user=editText_user_lib.getText().toString();
        String pass=editText_pass_lib.getText().toString();
        String verification_code=editText_verification.getText().toString();
        if(TextUtils.isEmpty(user)|TextUtils.isEmpty(pass)){
            Toast.makeText(LinkLibraryAct.this, "账号或密码不能为空!", Toast.LENGTH_SHORT).show();
        }else if(verification_code.isEmpty()){
            Toast.makeText(LinkLibraryAct.this, "验证码不能为空!", Toast.LENGTH_SHORT).show();
        }else {
            sharedPreferences.edit().putString("libUser",user).commit();
            sharedPreferences.edit().putString("libPass",pass).commit();
            loginLibNow(user, pass, verification_code);
        }
    }
    private void loginLibNow(String user,String pass,String verification){
        FormBody.Builder builder=new FormBody.Builder();
        builder.add("number",user).
                add("passwd",pass).
                add("captcha",verification).
                add("select","cert_no").
                add("returnUrl","");
        RequestBody requestBody=builder.build();
        Request request=new Request.Builder().url(Constants.URL_ENTER_LIBRARY).post(requestBody).build();
        okHttpClient_libInfo.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("lib",e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData=response.body().string();
                if(responseData.contains("验证码")){
                    Message message=handler.obtainMessage();
                    message.what=Constants.HANDLER_LOGIN_FAILURE_LIB;
                    handler.sendMessage(message);
                }else {
                    Document document= Jsoup.parse(responseData);
                    Element body=document.body();
                    Elements div=body.getElementsByTag("div");
                    Element div_extraInfo=div.get(11);
                    String fiveDeadlinebook=div_extraInfo.child(0).text();
                    bookInfos.add(fiveDeadlinebook);
                    String overDeadlinebook=div_extraInfo.child(1).text();
                    bookInfos.add(overDeadlinebook);
                    String preBook=div_extraInfo.child(2).text();
                    bookInfos.add(preBook);
                    String weituoBook=div_extraInfo.child(3).text();
                    bookInfos.add(weituoBook);
                    Element div_userInfo=div.get(12);
                    Elements div_td=div_userInfo.getElementsByTag("TD");
                    for (int i=0;i<div_td.size();i++){
                        infos.add(div_td.get(i).text());
                    }
                    Message message=handler.obtainMessage();
                    message.what=Constants.HANDLER_LOGIN_SUCCESSFUL_LIB;
                    handler.sendMessage(message);
                }
            }
        });
    }
    private void EnterLibraryInfoAct(){
        Intent intent=new Intent(LinkLibraryAct.this, LibraryInfoActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("info",infos);
        bundle.putSerializable("bookInfo",bookInfos);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(LinkLibraryAct.this,MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    class LibInfoCookierManager implements CookieJar {
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
