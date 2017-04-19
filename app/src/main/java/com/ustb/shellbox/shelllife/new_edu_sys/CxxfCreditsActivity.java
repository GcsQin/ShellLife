package com.ustb.shellbox.shelllife.new_edu_sys;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.BaseSetupWizardActivity;
import com.ustb.shellbox.shelllife.iactivity.INewEduSysActivity;
import com.ustb.shellbox.shelllife.alert_act.LinkEducationSystemAct;
import com.ustb.shellbox.shelllife.databean.Constants;
import com.ustb.shellbox.shelllife.databean.EduCxxfInfoBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CxxfCreditsActivity extends BaseSetupWizardActivity {
    Bundle bundle;
    ArrayList<EduCxxfInfoBean> listOfEduCxxf=new ArrayList<EduCxxfInfoBean>();
    List <Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.HANDLER_UPDATA_LISTVIEW:
                    initListView();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cxxf_credits);
        Intent intent=getIntent();
        bundle=intent.getExtras();
        searcheInnovationCredit();
    }

    @Override
    public void showNextPage() {

    }

    @Override
    public void showPriviousPage() {
        Intent intent=new Intent(CxxfCreditsActivity.this, INewEduSysActivity.class);
        Bundle bundleBack=new Bundle();
        bundleBack.putString("uid",bundle.getString("uid"));
        intent.putExtras(bundleBack);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void initListView(){
        ListView listView=(ListView)findViewById(R.id.lv_cxxf);
        for (int i=0;i<listOfEduCxxf.size();i++){
            Map<String,Object> listItem=new HashMap<String,Object>();
            listItem.put("cxxfTerm",listOfEduCxxf.get(i).getCxxfTem());
            listItem.put("cxxfType",listOfEduCxxf.get(i).getStuCxxfType());
            listItem.put("cxxfProject",listOfEduCxxf.get(i).getCxxfProjectName());
            listItem.put("cxxfScore",listOfEduCxxf.get(i).getScoreCxxf());
            listItem.put("cxxfSignTime",listOfEduCxxf.get(i).getCxxfSignInTime());
            listItems.add(listItem);
        }
        listView.setAdapter(new SimpleAdapter(this, listItems, R.layout.item_listview_cxxf,
                new String[]{"cxxfTerm", "cxxfType", "cxxfProject", "cxxfScore", "cxxfSignTime"}, new int[]{
                R.id.tv_listcxxf_term, R.id.tv_listcxxf_type, R.id.tv_listcxxf_name, R.id.tv_listcxxf_score, R.id.tv_listcxxf_signtime
        }));

    }
    public void searcheInnovationCredit() {
        FormBody.Builder builder=new FormBody.Builder();
        builder.add("uid", bundle.getString("uid"));
        RequestBody requestBody=builder.build();
        Request request = new Request.Builder().url(Constants.URL_InnovationCredit)
                .post(requestBody)
                .build();
        LinkEducationSystemAct.okHttpClient_edu.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = response.body().string();
                if(html.contains("如果您看到本条信息，是因为您现在还没登录或已经掉线，请重新打开网站并登录"))
                {

                }else {};
                Log.e("Cxxf", html);

                String cxxfTerm;
                String cxxfType;
                String cxxfProjectName;
                String cxxfScore;
                String cxxfSignInTime;
                Document document = Jsoup.parse(html);
                String titleEle = document.title();//html的标题内容
                Elements tbodyArray = document.getElementsByTag("tbody");//tbody标签数组
                Log.e("Cxxf",tbodyArray.size()+"");
                Element tbodyEle = tbodyArray.get(0);//我们只有一个tbody标签,通过get(0)即可tbodyArray从获取我们需要的tbody标签
                for (int i = 0; i < tbodyEle.children().size(); i++) {
                    Element trEle = tbodyEle.child(i);//获取第几个tr
                    cxxfTerm=setValueForCxxf(trEle.child(0).text());
                    cxxfType=setValueForCxxf(trEle.child(1).text());
                    cxxfProjectName=setValueForCxxf(trEle.child(2).text());
                    cxxfScore=setValueForCxxf(trEle.child(3).text());
                    cxxfSignInTime=setValueForCxxf(trEle.child(4).text());
                    listOfEduCxxf.add(new EduCxxfInfoBean(cxxfTerm, cxxfType, cxxfProjectName, cxxfScore, cxxfSignInTime));
                }
                Message message=handler.obtainMessage();
                message.what=Constants.HANDLER_UPDATA_LISTVIEW;
                handler.sendMessage(message);
            }
        });
    }
    private String setValueForCxxf(String str){
        String string=str.trim();
        if (string != null) {
            return str;
        } else {
           return "  ";
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(CxxfCreditsActivity.this, INewEduSysActivity.class);
            Bundle bundleBack=new Bundle();
            bundleBack.putString("uid",bundle.getString("uid"));
            intent.putExtras(bundleBack);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
