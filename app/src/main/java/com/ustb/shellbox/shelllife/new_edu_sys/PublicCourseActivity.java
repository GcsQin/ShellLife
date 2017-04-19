package com.ustb.shellbox.shelllife.new_edu_sys;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.BaseSetupWizardActivity;
import com.ustb.shellbox.shelllife.alert_act.LinkEducationSystemAct;
import com.ustb.shellbox.shelllife.databean.Constants;
import com.ustb.shellbox.shelllife.databean.EduTeachPlanBean_public;

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

public class PublicCourseActivity extends BaseSetupWizardActivity {
    Bundle bundle;
    String bundleString;
    ArrayList<EduTeachPlanBean_public> listOfEduTP_pub=new ArrayList<EduTeachPlanBean_public>();
    List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.HANDLER_UPDATA_LISTVIEW:
                    initListView();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_course);
        Intent intent=getIntent();
        bundle=intent.getExtras();
        bundleString=bundle.getString("uid");
        searchTeachPlan_Public();
    }

    @Override
    public void showNextPage() {

    }

    @Override
    public void showPriviousPage() {
        Intent intent=new Intent(PublicCourseActivity.this, TeachPlanActivity.class);
        Bundle bundleBack=new Bundle();
        bundleBack.putString("uid",bundle.getString("uid"));
        intent.putExtras(bundleBack);
        startActivity(intent);//无效？
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void searchTeachPlan_Public(){
        FormBody.Builder builder=new FormBody.Builder();
        builder.add("uid", bundleString);
        RequestBody requestBody=builder.build();
        Request request=new Request.Builder().post(requestBody).url(Constants.URL_TeachPlan).build();
        LinkEducationSystemAct.okHttpClient_edu.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String html = response.body().string();
                Document document = Jsoup.parse(html);
                String teachPTerm_pub;
                String teachPCourseID_pub;
                String teachPCourseName_pub;
                String teachPCourseType_pub;
                String teachPCourseTime_pub;
                String teachPCourseScore_pub;
                String teachPFstSc_pub;
                Elements tbodyArray = document.getElementsByTag("tbody");
                Element tbodyELe_pub = tbodyArray.get(2);
                for (int i = 0; i < tbodyELe_pub.children().size(); i++) {
                    Element tr_Ele = tbodyELe_pub.child(i);
                    teachPTerm_pub = tr_Ele.child(0).text();
                    teachPCourseID_pub = tr_Ele.child(1).text();
                    teachPCourseName_pub = tr_Ele.child(2).text();
                    teachPCourseType_pub = tr_Ele.child(3).text();
                    teachPCourseTime_pub = tr_Ele.child(4).text();
                    teachPCourseScore_pub = tr_Ele.child(5).text();
                    teachPFstSc_pub = tr_Ele.child(6).text();
                    listOfEduTP_pub.add(new EduTeachPlanBean_public(teachPTerm_pub, teachPCourseID_pub,
                            teachPCourseName_pub, teachPCourseType_pub, teachPCourseTime_pub, teachPCourseScore_pub, teachPFstSc_pub));
                }
                Message message = handler.obtainMessage();
                message.what = Constants.HANDLER_UPDATA_LISTVIEW;
                handler.sendMessage(message);
            }
        });
    }
    private void initListView(){
        ListView listView=(ListView)findViewById(R.id.lv_public_TeachPlan);
        for (int i=0;i<listOfEduTP_pub.size();i++){
            Map<String,Object> listItem=new HashMap<String,Object>();
            listItem.put("Term_pub",listOfEduTP_pub.get(i).getTeachPTerm_pub());
            listItem.put("CourseID_pub",listOfEduTP_pub.get(i).getTeachPCourseID_pub());
            listItem.put("CourseName_pub",listOfEduTP_pub.get(i).getTeachPCourseName_pub());
            listItem.put("CourseTime_pub",listOfEduTP_pub.get(i).getTeachPCourseTime_pub());
            listItem.put("CourseScore_pub",listOfEduTP_pub.get(i).getTeachPCourseScore_pub());
            listItem.put("FstSc_pub",listOfEduTP_pub.get(i).getTeachPFstSc_pub());
            listItems.add(listItem);
        }
        listView.setAdapter(new SimpleAdapter(PublicCourseActivity.this,listItems,R.layout.item_listview_public_tp
        ,new String[]{"Term_pub","CourseID_pub","CourseName_pub","CourseTime_pub","CourseScore_pub","FstSc_pub"},new int[]{
         R.id.tv_list_pub_tp_term,R.id.tv_list_pub_tp_id,R.id.tv_list_pub_tp_name,R.id.tv_list_pub_tp_time,R.id.tv_list_pub_tp_creidt,
                R.id.tv_list_pub_tp_score}));
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(PublicCourseActivity.this, TeachPlanActivity.class);
            Bundle bundleBack=new Bundle();
            bundleBack.putString("uid",bundle.getString("uid"));
            intent.putExtras(bundleBack);
            startActivity(intent);//无效？
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
