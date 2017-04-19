package com.ustb.shellbox.shelllife.iactivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ustb.shellbox.shelllife.R;
import com.ustb.shellbox.shelllife.activity.BaseSetupWizardActivity;
import com.ustb.shellbox.shelllife.activity.MainActivity;
import com.ustb.shellbox.shelllife.webactivity.WebCampusOAActivity;
import com.ustb.shellbox.shelllife.webactivity.WebIndexActivity;
import com.ustb.shellbox.shelllife.webactivity.WebJoinPartyActivity;
import com.ustb.shellbox.shelllife.webactivity.WebLibYuyueActivity;
import com.ustb.shellbox.shelllife.webactivity.WebLibraryActivity;
import com.ustb.shellbox.shelllife.webactivity.WebQualityDevelopActivity;
import com.ustb.shellbox.shelllife.webactivity.WebSchoolNewActivity;
import com.ustb.shellbox.shelllife.webactivity.WebTeachActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IOfficialWebListActivity extends BaseSetupWizardActivity {
    List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
    String list_itemName[]=new String[]{"学校官网","北科大入党学习网","校园OA网","校图书馆官网","素质拓展中心","本科教学网","校新闻官网","图书馆座位预约"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_web_list);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        for (int i=0;i<list_itemName.length;i++){
            Map<String,Object> listItem=new HashMap<String,Object>();
            listItem.put("webName",list_itemName[i]);
            listItems.add(listItem);
        }
        ListView listView=(ListView)findViewById(R.id.lv_owl_official);
        listView.setAdapter(new SimpleAdapter(this,listItems,R.layout.item_lv_owl_webdetial,
                new String[]{"webName"},new int[]{R.id.tv_list_item_officialweb}));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(IOfficialWebListActivity.this,WebIndexActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;
                    case 1:
                        startActivity(new Intent(IOfficialWebListActivity.this, WebJoinPartyActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;
                    case 2:
                        startActivity(new Intent(IOfficialWebListActivity.this, WebCampusOAActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;
                    case 3:
                        startActivity(new Intent(IOfficialWebListActivity.this, WebLibraryActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;
                    case 4:
                        startActivity(new Intent(IOfficialWebListActivity.this, WebQualityDevelopActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;
                    case 5:
                        startActivity(new Intent(IOfficialWebListActivity.this, WebTeachActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;
                    case 6:
                        startActivity(new Intent(IOfficialWebListActivity.this,WebSchoolNewActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;
                    case 7:
                        Intent intent=new Intent(IOfficialWebListActivity.this, WebLibYuyueActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putBoolean("fromLibInfo",false);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                        break;
                }
            }
        });
    }

    @Override
    public void showNextPage() {

    }

    @Override
    public void showPriviousPage() {
        startActivity(new Intent(IOfficialWebListActivity.this, MainActivity.class));//无效？
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(IOfficialWebListActivity.this, MainActivity.class));//无效？
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
