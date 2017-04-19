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
import com.ustb.shellbox.shelllife.activity.MainActivity;
import com.ustb.shellbox.shelllife.alert_act.LinkEducationSystemAct;
import com.ustb.shellbox.shelllife.databean.Constants;
import com.ustb.shellbox.shelllife.databean.EduTeachPlanBean_Special;

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

public class SpecialCourseActivity extends BaseSetupWizardActivity{
    Bundle bundle;
    String bundleString;
    ArrayList<EduTeachPlanBean_Special> listOfEduTeachPSpecial=new ArrayList<EduTeachPlanBean_Special>();
    List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
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
        setContentView(R.layout.activity_special_course);
        Intent intent=getIntent();
        bundle=intent.getExtras();
        bundleString=bundle.getString("uid");
        searchTeachPlan_Special();
    }

    @Override
    public void showNextPage() {

    }

    @Override
    public void showPriviousPage() {
        Intent intent=new Intent(SpecialCourseActivity.this, TeachPlanActivity.class);
        Bundle bundleBack=new Bundle();
        bundleBack.putString("uid",bundle.getString("uid"));
        intent.putExtras(bundleBack);
        startActivity(intent);//无效？
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void initListView(){
        ListView listView=(ListView)findViewById(R.id.lv_spcial_TeachPlan);
        for (int i=0;i<listOfEduTeachPSpecial.size();i++){
            Map<String,Object> listItem=new HashMap<String,Object>();
            listItem.put("Term_spc",listOfEduTeachPSpecial.get(i).getTeachPTerm_spc());
            listItem.put("CourseID_spc",listOfEduTeachPSpecial.get(i).getTeachPCourseID_spc());
            listItem.put("CourseName_spc",listOfEduTeachPSpecial.get(i).getTeachPCourseName_spc());
            listItem.put("CourseTime_spc", listOfEduTeachPSpecial.get(i).getTeachPCourseTime_spc());
            listItem.put("CourseScore_spc",listOfEduTeachPSpecial.get(i).getTeachPCourseScore_spc());
            listItem.put("FnlSc_spc",listOfEduTeachPSpecial.get(i).getTeachPFstSc_spc());
            listItems.add(listItem);
        }
        listView.setAdapter(new SimpleAdapter(SpecialCourseActivity.this,listItems,R.layout.item_listview_spcial_tp,new String[]{
                "Term_spc","CourseID_spc","CourseName_spc","CourseTime_spc","CourseScore_spc","FnlSc_spc"
        },new int[]{R.id.tv_list_spc_tp_term,R.id.tv_list_spc_tp_id,R.id.tv_list_spc_tp_name,R.id.tv_list_spc_tp_time,R.id.tv_list_spc_tp_credit,
        R.id.tv_list_spc_tp_fnlSc}));
    }
    private void searchTeachPlan_Special(){
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
                String html=response.body().string();
                Document document= Jsoup.parse(html);
                String teachPTerm_spc;
                String teachPCourseID_spc;
                String teachPCourseName_spc;
                String teachPCourseTime_spc;
                String teachPCourseScore_spc;
                String teachPFnlSc_spc;
                Elements tbodyArray=document.getElementsByTag("tbody");
                Element tbodyEle_spc=tbodyArray.get(1);
                for (int i=0;i<tbodyEle_spc.children().size();i++){
                    Element trEle_spc=tbodyEle_spc.child(i);
                    teachPTerm_spc=trEle_spc.child(0).text();
                    teachPCourseID_spc=trEle_spc.child(1).text();
                    teachPCourseName_spc=trEle_spc.child(2).text();
                    teachPCourseTime_spc=trEle_spc.child(5).text();
                    teachPCourseScore_spc=trEle_spc.child(6).text();
                    teachPFnlSc_spc=trEle_spc.child(7).text();
                    listOfEduTeachPSpecial.add(new EduTeachPlanBean_Special(teachPTerm_spc,teachPCourseID_spc,teachPCourseName_spc,teachPCourseTime_spc
                            ,teachPCourseScore_spc,teachPFnlSc_spc));
                }
                Message msg=handler.obtainMessage();
                msg.what=Constants.HANDLER_UPDATA_LISTVIEW;
                handler.sendMessage(msg);
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent=new Intent(SpecialCourseActivity.this, TeachPlanActivity.class);
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
