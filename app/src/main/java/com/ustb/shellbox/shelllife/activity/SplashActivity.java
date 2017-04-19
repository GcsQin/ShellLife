package com.ustb.shellbox.shelllife.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.ustb.shellbox.shelllife.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
* Splash页面：展示logo,开发者信息
* 功能函数：主要检测版本更新
* */
public class SplashActivity extends Activity {
    private final int SHOW_UPDATA_DIALOG=1;
    private final int ENTER_MAIN_ACTIVITY=2;
    private final int ERROR_URL=3;
    private final int ERROR_IO=4;
    private final int ERROR_JSON=5;
    //
    private int localVersionCode;
    private String localVersionName;
    private int serveVersionCode;
    private String serveVersionName;
    //
    SharedPreferences sharedPreferences;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SHOW_UPDATA_DIALOG :
                    showUpdateDialog();
                    break;
                case  ENTER_MAIN_ACTIVITY :
                    enterMainActivity();
                    break;
                case ERROR_URL:
//                    Toast.makeText(SplashActivity.this, "(url)网络开小差了~", Toast.LENGTH_LONG).show();
                    enterMainActivity();
                    break;
                case ERROR_JSON:
//                    Toast.makeText(SplashActivity.this,"(json)网络开小差了~",Toast.LENGTH_LONG).show();
                    enterMainActivity();
                    break;
                case ERROR_IO:
//                    Toast.makeText(SplashActivity.this,"(IO)网络开小差了~",Toast.LENGTH_LONG).show();
                    enterMainActivity();
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置无标题栏效果
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        //在Splash页面检测版本更新
        checkUpdate();
        sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
    }
    /*
    * 检测版本更新
    * */
    private void checkUpdate(){
        //这个是为了以后版本更新要用的,不过目前感觉缺少一个后台人员。
        final String servePath="202.204.48.66";
        final long startTime=System.currentTimeMillis();
        new Thread(new Runnable() {
            Message msg=Message.obtain();
            HttpURLConnection httpURLConnection;
            @Override
            public void run() {
                try {
                    URL url=new URL(servePath);
                    httpURLConnection=(HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.connect();
                    if(httpURLConnection.getResponseCode()==200){
                        InputStream inputStream=httpURLConnection.getInputStream();
                        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder response=new StringBuilder();
                        String line;
                        while ((line=reader.readLine())!=null){
                            response.append(line);
                        }
                        JSONObject jsonObject=new JSONObject(response.toString());
                        serveVersionCode=jsonObject.getInt("code");
                        serveVersionName=jsonObject.getString("name");
                        if(serveVersionCode>getLocalVersionCode()){//服务器版本号大于本地版本号,则提示更新。
                            msg.what=SHOW_UPDATA_DIALOG;
                            handler.sendEmptyMessage(msg.what);
                        }else {
                            msg.what=ENTER_MAIN_ACTIVITY;
                            handler.sendEmptyMessage(msg.what);
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what=ERROR_URL;;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what=ERROR_JSON;
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what=ERROR_IO;
                } finally {
                    if(httpURLConnection!=null){
                        httpURLConnection.disconnect();
                    }
                    long endTime=System.currentTimeMillis();
                    long useTime=endTime-startTime;
                    if(useTime<5000){
                        try {
                            Thread.sleep(5000-useTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handler.sendEmptyMessage(msg.what);
                }
            }
        }).start();
    }
    /*
    * 获取本地版本号,用于检测版本更新
    * */
    private int getLocalVersionCode(){
        PackageManager packageManager=getPackageManager();
        try {
            PackageInfo packageInfo=packageManager.getPackageInfo(getPackageName(),0);
            localVersionCode=packageInfo.versionCode;
            return localVersionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;//返回-1表示获取失败。
    }
    /*
    * 获取本地版本名称,可以用来展示作者等。
    * */
    private String getLocalVersionName(){
        PackageManager packageManager=getPackageManager();
        try {
            PackageInfo packageInfo=packageManager.getPackageInfo(getPackageName(),0);
            localVersionName=packageInfo.versionName;
            return localVersionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Failure to get VersionName";
    }
    /*
    * 弹出升级对话框
    * */
    private void showUpdateDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(SplashActivity.this);
        final  AlertDialog updateDialg=builder.create();
        View viewDialog=View.inflate(this, R.layout.layout_custom_dialog, null);
        Button buttonUpdate=(Button)viewDialog.findViewById(R.id.splash_btn_update);
        Button buttonCancel=(Button)viewDialog.findViewById(R.id.splash_btn_cancel);
        updateDialg.setView(viewDialog);
        updateDialg.show();
        updateDialg.setCanceledOnTouchOutside(false);//点击外面不能取消
        updateDialg.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterMainActivity();
            }
        });
//        updateDialg.set
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SplashActivity.this, "启动更新", Toast.LENGTH_LONG).show();

                downloadAPK();
                updateDialg.dismiss();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterMainActivity();
                updateDialg.dismiss();
            }
        });
    }
    /*
    * 进入主界面（其实一开始我想弄类成引导页的）
    * */
    public void enterMainActivity(){
        String configed=sharedPreferences.getString("guided","").toString();
        if(configed.equals("guided")){
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }else {
            startActivity(new Intent(SplashActivity.this, WizardFirstActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }
    public void downloadAPK(){

    }
}