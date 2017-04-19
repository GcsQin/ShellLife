 package com.ustb.shellbox.shelllife.inneractivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.BaseSetupWizardActivity;
import com.ustb.shellbox.shelllife.alert_act.LinkEportalAct;
import com.ustb.shellbox.shelllife.databean.Constants;
import com.ustb.shellbox.shelllife.databean.EportalCardBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

 public class EportalActivity extends BaseSetupWizardActivity {
     TextView tvTitle;
     TextView tvInfos;
     TextView tvTime;
     TextView tvIP;
     TextView tvMoney;
     Spinner spinner;
     String spinnerData;
     ArrayList<EportalCardBean> listOfEporatalCardInfo=new ArrayList<EportalCardBean>();
     ArrayList<String> itemsList=new ArrayList<>();
     Handler handler=new Handler(){
         @Override
         public void handleMessage(Message msg) {
             super.handleMessage(msg);
             switch (msg.what){
                 case Constants.HANDELER_GET_EPORTAL_INFO_FAIL:
                     Toast.makeText(EportalActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                     break;
                 case Constants.HANDELER_SHOW_EPORTAL_INFO:
                     String[] arr=(String[])msg.obj;
                     tvInfos.setText(arr[0]);
                     tvTime.setText(arr[1]);
                     tvIP.setText(arr[2]);
                     tvMoney.setText(arr[3]);
                     break;
                 case Constants.HANDELER_SHOW_THIS_MONTH_DETIALS:
                     enterInfoOfCard();
                     break;
             }
         }
     };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eportal);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initData();
        searchEportalInfo();
    }

     @Override
     public void showNextPage() {

     }

     @Override
     public void showPriviousPage() {
         startActivity(new Intent(EportalActivity.this, LinkEportalAct.class));
         overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
         finish();
     }

     private void initData(){
         itemsList.add("当月流水");
         itemsList.add("当月历史流水");
         itemsList.add("上月历史流水");
         tvInfos=(TextView)findViewById(R.id.tv_eportal_info);
         tvTime=(TextView)findViewById(R.id.tv_eportal_lastlogintime);
         tvIP=(TextView)findViewById(R.id.tv_eportal_lastloginIP);
         tvMoney=(TextView)findViewById(R.id.tv_eportal_money);
         spinner=(Spinner)findViewById(R.id.spinner_choice_eportal);
         ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(EportalActivity.this,R.layout.support_simple_spinner_dropdown_item,itemsList);
         arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
         spinner.setAdapter(arrayAdapter);
         spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0:
                            spinnerData="day";
                            break;
                        case 1:
                            spinnerData="mon";
                            break;
                        case 2:
                            spinnerData="lastMon";
                            break;
                    }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });
     }
     private void searchEportalInfo(){
         Request request=new Request.Builder().url(Constants.URL_SEARCH_EPORTAL_INFO).build();
         LinkEportalAct.okHttpClient_eportal.newCall(request).enqueue(new Callback() {
             @Override
             public void onFailure(Call call, IOException e) {
                 Message message=handler.obtainMessage();
                 message.obj="获取用户信息失败"+e.toString();
                 message.what=Constants.HANDELER_GET_EPORTAL_INFO_FAIL;
                 handler.sendMessage(message);
             }
             @Override
             public void onResponse(Call call, Response response) throws IOException {
                 String responseData=response.body().string();
                 Document document= Jsoup.parse(responseData);
                 Element table=document.getElementsByTag("table").get(2);
                 Element table_Content=table.getElementsByTag("table").get(2);
                 Elements tds=table_Content.getElementsByAttributeValue("align","left");
                 Log.e("tds",tds.first().text()+"");
                 String userInfo=tds.get(1).ownText();
                 String loginInfos=tds.get(2).ownText();
                 String[] loginInfo=loginInfos.split("：");
                 String IP=loginInfo[2].trim();
                 String time=loginInfo[1].split("I")[0].toString().trim();
//                 校园卡余额的获取;
                 Element divs=document.getElementsByAttributeValueContaining("style","padding-top:5px;").get(0);
                 String  money=divs.child(0).text();
                 Log.e("money",money);
                 Message message=handler.obtainMessage();
                 String[] arr={userInfo,time,IP,money};
                 message.obj=arr;
                 message.what=Constants.HANDELER_SHOW_EPORTAL_INFO;
                 handler.sendMessage(message);
             }
         });
     }
     public void searchCardInfo(View view){
         Request request=new Request.Builder().url(Constants.URL_SEARCH_EPORTAL_INFO+Constants.URL_SEARCH_CARD_INFO_THISMONTH+spinnerData+"#anchorf8441").build();
         LinkEportalAct.okHttpClient_eportal.newCall(request).enqueue(new Callback() {
             @Override
             public void onFailure(Call call, IOException e) {
                 Message message=handler.obtainMessage();
                 message.obj="查询校园卡流水失败"+e.toString();
                 message.what=Constants.HANDELER_GET_EPORTAL_INFO_FAIL;
                 handler.sendMessage(message);
             }
             @Override
             public void onResponse(Call call, Response response) throws IOException {
                 String responseData=response.body().string();
                 Document document=Jsoup.parse(responseData);
//                 if(responseData.contains("一卡通结算后")){
                     Element table=document.getElementsByAttributeValueContaining("style","border-collapse:collapse").first();
                     Elements trs=table.getElementsByTag("tr");
                     for(int i=0;i<trs.size();i++){
                         String time= trs.get(i).child(0).text();
                         String where=trs.get(i).child(1).text();
                         String consumer=trs.get(i).child(2).text();
                         String balance=trs.get(i).child(3).text();
                         Log.e("tag",time+where+"!!"+consumer+balance);
                         listOfEporatalCardInfo.add(new EportalCardBean(time,where,consumer,balance));
                     }
                     handler.sendEmptyMessage(Constants.HANDELER_SHOW_THIS_MONTH_DETIALS);

//                 }
             }
         });
     }
     private void enterInfoOfCard(){
         Intent intent=new Intent(EportalActivity.this,InnerCardInfoActivity.class);
         Bundle bundle=new Bundle();
         bundle.putSerializable("cardInfo",listOfEporatalCardInfo);
         intent.putExtras(bundle);
         startActivity(intent);
         overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
         finish();
     }
     @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
         if(keyCode==KeyEvent.KEYCODE_BACK){
             startActivity(new Intent(EportalActivity.this, LinkEportalAct.class));
             overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
             finish();
         }
         return super.onKeyDown(keyCode, event);
     }
 }
