package com.ustb.shellbox.shelllife.inneractivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.BaseSetupWizardActivity;
import com.ustb.shellbox.shelllife.databean.EportalCardBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InnerCardInfoActivity extends BaseSetupWizardActivity {
    ArrayList<EportalCardBean> arrayListECB;
    ArrayList<Map<String,Object>> mapArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_card_info);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        arrayListECB=(ArrayList<EportalCardBean>)bundle.getSerializable("cardInfo");
        mapArrayList=new ArrayList<Map<String,Object>>();
        ListView listView=(ListView)findViewById(R.id.lv_innerCIA_waterDetials);
        for(int i=0;i<arrayListECB.size();i++){
            Map<String,Object> item=new HashMap<String,Object>();
            item.put("time",arrayListECB.get(i).getTime());
            item.put("where",arrayListECB.get(i).getWhere());
            item.put("consumer",arrayListECB.get(i).getConsumer());
            item.put("balance",arrayListECB.get(i).getBalance());
            mapArrayList.add(item);
        }
        listView.setAdapter(new SimpleAdapter(InnerCardInfoActivity.this,mapArrayList,R.layout.item_lv_innercard_cardinfo,new String[]{"time","where","consumer","balance"},
                new int[]{R.id.tv_innercard_time,R.id.tv_innercard_where,R.id.tv_innercard_consumer,R.id.tv_innercard_balance}));
    }

    @Override
    public void showNextPage() {

    }

    @Override
    public void showPriviousPage() {
        startActivity(new Intent(InnerCardInfoActivity.this,EportalActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(InnerCardInfoActivity.this,EportalActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
